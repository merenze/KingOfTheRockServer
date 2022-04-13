package coms309.s1yn3.backend.service;

import coms309.s1yn3.backend.entity.repository.LobbyRepository;
import coms309.s1yn3.backend.entity.repository.PasswordRepository;
import coms309.s1yn3.backend.entity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RepositoryProviderService {
	@Autowired
	LobbyRepository lobbyRepository;

	@Autowired
	PasswordRepository passwordRepository;

	@Autowired
	UserRepository userRepository;

	public LobbyRepository getLobbyRepository() {
		return lobbyRepository;
	}

	public PasswordRepository getPasswordRepository() {
		return passwordRepository;
	}

	public UserRepository getUserRepository() {
		return userRepository;
	}
}
