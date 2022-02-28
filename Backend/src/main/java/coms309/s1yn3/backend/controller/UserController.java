package coms309.s1yn3.backend.controller;

import coms309.s1yn3.backend.entity.User;
import coms309.s1yn3.backend.entity.repository.UserRepository;
import coms309.s1yn3.backend.service.UserProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class UserController {
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
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		else {
			return new ResponseEntity(user, HttpStatus.OK);
		}
	}

	@PostMapping("/register")
	public @ResponseBody ResponseEntity create(@RequestBody User user) {
		users.save(user);
		return new ResponseEntity(HttpStatus.OK);
	}

	@PatchMapping("/users/{id}")
	public @ResponseBody ResponseEntity update(@PathVariable int id, @RequestBody User request) {
		User user = users.getById(id);
		if (user == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		user.patch(request);
		users.save(user);
		return new ResponseEntity(HttpStatus.OK);
	}

	@DeleteMapping("/users/{id}")
	public @ResponseBody ResponseEntity delete(@PathVariable int id) {
		if (users.getById(id) == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		users.deleteById(id);
		return new ResponseEntity(HttpStatus.OK);
	}

	@GetMapping("/login")
	public @ResponseBody ResponseEntity login(@RequestBody Map<String, String> request) {
		String username = request.get("username");
		String password = request.get("password");
		User user = userProvider.getLoginUser(username, password);
		if (user == null) {
			return new ResponseEntity(
					"Username or password incorrect.",
					HttpStatus.NOT_FOUND
			);
		}
		return new ResponseEntity(
				user,
				HttpStatus.OK
		);
	}
}
