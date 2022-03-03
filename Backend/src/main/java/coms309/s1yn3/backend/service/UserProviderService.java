package coms309.s1yn3.backend.service;

import coms309.s1yn3.backend.entity.User;
import coms309.s1yn3.backend.entity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserProviderService {
	@Autowired
	UserRepository users;

	public User getByUsername(String username) {
		List<User> userList = users.getUsersByUsername(username);
		if (userList.size() <= 0) {
			return null;
		}
		return userList.get(0);
	}

	public User getByEmail(String email) {
		List<User> userList = users.getUsersByEmail(email);
		if (userList.size() <= 0) {
			return null;
		}
		return userList.get(0);
	}

	public User getByUsernameOrEmail(String usernameOrEmail) {
		List<User> userList = users.getUsersByUsernameOrEmail(usernameOrEmail);
		if (userList.size() <= 0) {
			return null;
		}
		return userList.get(0);
	}

	public User getLoginUser(String username, String password) {
		List<User> userList = users.getUsers(username, password);
		if (userList.size() <= 0) {
			return null;
		}
		return userList.get(0);
	}
}
