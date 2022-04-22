package coms309.s1yn3.backend.service;

import coms309.s1yn3.backend.entity.repository.*;
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

	@Autowired
	GameRepository gameRepository;

	@Autowired
	GameUserRepository gameUserRepository;

	public LobbyRepository getLobbyRepository() {
		return lobbyRepository;
	}

	public PasswordRepository getPasswordRepository() {
		return passwordRepository;
	}

	public UserRepository getUserRepository() {
		return userRepository;
	}

	public GameRepository games() {
		return gameRepository;
	}

	public GameUserRepository gameUserRelations() {
		return gameUserRepository;
	}
}
