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

	public User getLoginUser(String username, String password) {
		List<User> userList = users.getUsers(username, password);
		if (userList.size() <= 0) {
			return null;
		}
		return userList.get(0);
	}
}
