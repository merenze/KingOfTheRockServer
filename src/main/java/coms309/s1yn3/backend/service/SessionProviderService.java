package coms309.s1yn3.backend.service;

import coms309.s1yn3.backend.entity.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class SessionProviderService {
	private Map<String, User> sessions = new HashMap<>();

	/**
	 * Store a new session for a user
	 * @param user
	 * @return Auth token for the user's session.
	 */
	public String addSession(User user) {
		removeSession(user);
		String token = UUID.randomUUID().toString();
		sessions.put(token, user);
		return token;
	}

	public void removeSession(User user) {
		for (String token : sessions.keySet()) {
			if (sessions.get(token) == user) {
				sessions.remove(token);
				break;
			}
		}
	}

	public User getUser(String token) {
		return sessions.get(token);
	}
}
