package coms309.s1yn3.backend.controller;

import coms309.s1yn3.backend.entity.User;
import coms309.s1yn3.backend.entity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
	@Autowired
	UserRepository users;

	@GetMapping("/users")
	public @ResponseBody List<User> index() {
		return users.findAll();
	}

	@GetMapping("/users/{id}")
	public @ResponseBody User show(@PathVariable int id) {
		return users.findById(id);
	}

	@PostMapping("/users")
	public @ResponseBody String create(@RequestBody User user) {
		users.save(user);
		// TODO
		return "Saved.";
	}

	@PatchMapping("/users/{id}")
	public @ResponseBody String create(@PathVariable int id, @RequestBody User request) {
		User user = users.getById(id);
		if (user == null) {
			// TODO
			return "Not found";
		}
		user.patch(request);
		users.save(user);
		return "Success";
	}

	@DeleteMapping("/users/{id}")
	public @ResponseBody String delete(@PathVariable int id) {
		if (users.getById(id) == null) {
			// TODO
			return "Not found";
		}
		users.deleteById(id);
		return "Success";
	}
}
