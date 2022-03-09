package coms309.s1yn3.backend.service;

import coms309.s1yn3.backend.entity.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class SessionProviderService {
	private Map<String, User> sessions = new HashMap<>();

	public String addSession(User user) {
		// Remove the user's old session from memory
		for (String token : sessions.keySet()) {
			if (sessions.get(token) == user) {
				sessions.remove(token);
				break;
			}
		}
		// Add the user's new session
		String token = UUID.randomUUID().toString();
		sessions.put(token, user);
		// Return the authentication token
		return token;
	}
}
