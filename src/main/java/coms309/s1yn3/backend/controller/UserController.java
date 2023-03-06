package coms309.s1yn3.backend.controller;

import coms309.s1yn3.backend.entity.Password;
import coms309.s1yn3.backend.entity.User;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Handles User login and registration.
 */
@RestController
public class UserController extends AbstractController {
	private static final String EMAIL_PATTERN = "^[0-9a-zA-Z!#$%&'*/=?^_+\\-`\\{|\\}~]+@[0-9a-zA-Z!#$%&'*/=?^_+\\-`\\{|\\}~]+\\.[0-9a-zA-Z!#$%&'*/=?^_+\\-`\\{|\\}~]+(\\.[0-9a-zA-Z!#$%&'*/=?^_+\\-`\\{|\\}~]+)*$";
	private final Logger logger = LoggerFactory.logger(UserController.class);

	@GetMapping("/users")
	public @ResponseBody List<User> index() {
		return repositories().getUserRepository().findAll();
	}

	@GetMapping("/users/{id}")
	public @ResponseBody ResponseEntity show(@PathVariable int id) {
		User user = repositories().getUserRepository().findById(id);
		if (user == null) {
			JSONObject responseBody = new JSONObject();
			responseBody.put("status", HttpStatus.NOT_FOUND);
			return new ResponseEntity(responseBody.toMap(), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(user, HttpStatus.OK);
	}

	@PatchMapping("/users/{id}")
	public @ResponseBody ResponseEntity update(@PathVariable int id, @RequestBody User request) {
		User user = repositories().getUserRepository().findById(id);
		if (user == null) {
			logger.warnf("User not found for ID %s", id);
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		user.patch(request);
		repositories().getUserRepository().save(user);
		JSONObject responseBody = new JSONObject();
		responseBody.put("status", HttpStatus.OK);
		return new ResponseEntity(responseBody.toMap(), HttpStatus.OK);
	}

	@DeleteMapping("/users/{id}")
	public @ResponseBody ResponseEntity delete(@PathVariable int id) {
		JSONObject responseBody = new JSONObject();
		if (repositories().getUserRepository().findById(id) == null) {
			responseBody.put("status", HttpStatus.NOT_FOUND);
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		repositories().getUserRepository().deleteById(id);
		responseBody.put("status", HttpStatus.OK);
		return new ResponseEntity(responseBody.toMap(), HttpStatus.OK);
	}

	@PostMapping("/register")
	public @ResponseBody ResponseEntity create(@RequestBody Map<String, String> requestBody) {
		JSONObject responseBody = new JSONObject();
		// Parse request values
		String username = requestBody.get("username");
		String email = requestBody.get("email");
		String password = requestBody.get("password");
		boolean isAdmin = requestBody.containsKey("isAdmin") && Boolean.parseBoolean(requestBody.get("isAdmin"));
		// Check for missing username
		if (username == null || username.isEmpty()) {
			responseBody.put("username", "Username cannot be empty.");
		} else {
			// Check for duplicate username
			List<User> users = repositories().getUserRepository().findByUsername(username);
			System.out.printf("Users with username '%s': %d\n", username, users.size());
			if (users.size() > 0) {
				responseBody.put("username", "Username is already taken.");
			}
		}
		// Check for missing email
		if (email == null || email.isEmpty()) {
			responseBody.put("email", "Email cannot be empty.");
		} else {
			// Check for invalid email
			if (!Pattern.matches(EMAIL_PATTERN, email)) {
				responseBody.put("email", "Invalid email address.");
			} else {
				// Check for duplicate email
				List<User> users = repositories().getUserRepository().findByEmail(email);
				System.out.printf("Users with email '%s': %d\n", email, users.size());
				if (users.size() > 0) {
					responseBody = new JSONObject();
					responseBody.put("email", "Email address is already in use.");
				}
			}
		}
		// Check for missing password
		if (password == null || password.isEmpty()) {
			responseBody.put("password", "Password cannot be empty.");
		}
		// User could not be created
		if (!responseBody.isEmpty()) {
			return new ResponseEntity(responseBody.toMap(), HttpStatus.BAD_REQUEST);
		}
		// User could be created
		responseBody.put("status", HttpStatus.OK);
		User user = new User(email, username, isAdmin);
		repositories().getUserRepository().save(user);
		// Save the password
		repositories().getPasswordRepository().save(new Password(user, password));
		return new ResponseEntity(responseBody.toMap(), HttpStatus.OK);
	}

	@PostMapping("/login")
	public @ResponseBody ResponseEntity login(@RequestBody Map<String, String> request) {
		String username = request.get("username");
		String password = request.get("password");
		List<User> users = repositories().getUserRepository().findByUsernameAndPassword(username, password);
		User user = users.size() > 0 ? users.get(0) : null;
		if (user == null) {
			JSONObject responseBody = new JSONObject();
			responseBody.put("status", HttpStatus.NOT_FOUND);
			return new ResponseEntity(
					responseBody.toMap(),
					HttpStatus.NOT_FOUND
			);
		}
		Map<String, String> responseBody = new HashMap<>();
		responseBody.put("auth-token", sessions().addSession(user));
		responseBody.put("isAdmin", Boolean.toString(user.getIsAdmin()));
		logger.infof("User <%s> logged in with token <%s>", user.getUsername(), responseBody.get("auth-token"));
		return new ResponseEntity(
				responseBody,
				HttpStatus.OK
		);
	}

	@GetMapping("/search")
	public @ResponseBody ResponseEntity search(@RequestParam("q") String queryParameter) {
		return new ResponseEntity(repositories().getUserRepository().findByUsernameContaining(queryParameter), HttpStatus.OK);
	}
}
