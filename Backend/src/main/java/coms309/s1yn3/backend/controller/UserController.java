package coms309.s1yn3.backend.controller;

import coms309.s1yn3.backend.entity.User;
import coms309.s1yn3.backend.entity.repository.UserRepository;
import coms309.s1yn3.backend.service.UserProviderService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@RestController
public class UserController {
	private static final String EMAIL_PATTERN = "^[0-9a-zA-Z!#$%&'*/=?^_+\\-`\\{|\\}~]+@[0-9a-zA-Z!#$%&'*/=?^_+\\-`\\{|\\}~]+\\.[0-9a-zA-Z!#$%&'*/=?^_+\\-`\\{|\\}~]+(\\.[0-9a-zA-Z!#$%&'*/=?^_+\\-`\\{|\\}~]+)*$";

	@Autowired
	UserRepository users;

	@Autowired
	UserProviderService userProvider;

	@GetMapping("/users")
	public @ResponseBody List<User> index() {
		return users.findAll();
	}

	@GetMapping("/users/{id}")
	public @ResponseBody ResponseEntity show(@PathVariable int id) {
		User user = users.findById(id);
		if (user == null) {
			JSONObject responseBody = new JSONObject();
			responseBody.put("status", HttpStatus.NOT_FOUND);
			return new ResponseEntity(responseBody.toMap(), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(user, HttpStatus.OK);
	}

	@PostMapping("/register")
	public @ResponseBody ResponseEntity create(@RequestBody Map<String, String> requestBody) {
		JSONObject responseBody = new JSONObject();
		// Check for missing username
		if (!requestBody.containsKey("username") || requestBody.get("username").isEmpty()) {
			responseBody.put("username", "Username cannot be empty.");
		} else {
			// Check for duplicate username
			User user = userProvider.getByUsername(requestBody.get("username"));
			if (user != null) {
				responseBody.put("username", "Username is already taken.");
			}
		}
		// Check for missing email
		if (!requestBody.containsKey("email") || requestBody.get("email").isEmpty()) {
			responseBody.put("email", "Email cannot be empty.");
		}
		else {
			// Check for invalid email
			if (!Pattern.matches(EMAIL_PATTERN, requestBody.get("email"))) {
				if (responseBody == null) {
					responseBody = new JSONObject();
				}
				responseBody.put("email", "Invalid email address.");
			} else {
				// Check for duplicate email
				User user = userProvider.getByEmail(requestBody.get("email"));
				if (user != null) {
					responseBody = new JSONObject();
					responseBody.put("email", "Email address is already in use.");
				}
			}
		}
		// Check for missing password
		if (!requestBody.containsKey("password") || requestBody.get("password").isEmpty()) {
			responseBody.put("password", "Password cannot be empty.");
		}
		// User could not be created
		if (!responseBody.isEmpty()) {
			return new ResponseEntity(responseBody.toMap(), HttpStatus.BAD_REQUEST);
		}
		// User could be created
		responseBody.put("status", HttpStatus.OK);
		// User could be created
		users.save(new User(
				requestBody.get("email"),
				requestBody.get("username"),
				"",
				// Default to false where isAdmin is omitted
				requestBody.containsKey("isAdmin") && Boolean.parseBoolean(requestBody.get("isAdmin"))
		));
		return new ResponseEntity(responseBody.toMap(), HttpStatus.OK);
	}

	@PatchMapping("/users/{id}")
	public @ResponseBody ResponseEntity update(@PathVariable int id, @RequestBody User request) {
		User user = users.getById(id);
		if (user == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		user.patch(request);
		users.save(user);
		JSONObject responseBody = new JSONObject();
		responseBody.put("status", HttpStatus.OK);
		return new ResponseEntity(responseBody.toMap(), HttpStatus.OK);
	}

	@DeleteMapping("/users/{id}")
	public @ResponseBody ResponseEntity delete(@PathVariable int id) {
		JSONObject responseBody = new JSONObject();
		if (users.getById(id) == null) {
			responseBody.put("status", HttpStatus.NOT_FOUND);
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		users.deleteById(id);
		responseBody.put("status", HttpStatus.OK);
		return new ResponseEntity(responseBody.toMap(), HttpStatus.OK);
	}

	@PostMapping("/login")
	public @ResponseBody ResponseEntity login(@RequestBody Map<String, String> request) {
		String username = request.get("username");
		String password = request.get("password");
		User user = userProvider.getLoginUser(username, password);
		if (user == null) {
			JSONObject responseBody = new JSONObject();
			responseBody.put("status", HttpStatus.NOT_FOUND);
			return new ResponseEntity(
					responseBody.toMap(),
					HttpStatus.NOT_FOUND
			);
		}
		return new ResponseEntity(
				user,
				HttpStatus.OK
		);
	}

	@GetMapping("/search")
	public @ResponseBody ResponseEntity search(@RequestParam("q") String queryParemeter) {
		return new ResponseEntity(users.searchUsername(queryParemeter), HttpStatus.OK);
	}

}
