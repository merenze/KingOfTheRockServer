package coms309.s1yn3.backend.service;

import coms309.s1yn3.backend.entity.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RepositoryProviderService {
	@Autowired LobbyRepository lobbyRepository;
	@Autowired PasswordRepository passwordRepository;
	@Autowired UserRepository userRepository;
	@Autowired GameRepository gameRepository;
	@Autowired GameUserRepository gameUserRepository;
	@Autowired StructureRepository structureRepository;
	@Autowired MaterialRepository materialRepository;
	@Autowired StructureMaterialRepository structureMaterialRepository;
	@Autowired GameUserMaterialRepository gameUserMaterialRepository;
	@Autowired GameUserStructureRepository gameUserStructureRepository;
	@Autowired MaterialSpawnerRepository materialSpawnerRepository;

	public LobbyRepository getLobbyRepository() {
		return lobbyRepository;
	}

	public PasswordRepository getPasswordRepository() {
		return passwordRepository;
	}

	public UserRepository getUserRepository() {
		return userRepository;
	}

	public GameRepository getGameRepository() {
		return gameRepository;
	}

	public GameUserRepository getGameUserRepository() {
		return gameUserRepository;
	}

	public StructureRepository getStructureRepository() {
		return structureRepository;
	}

	public MaterialRepository getMaterialRepository() {
		return materialRepository;
	}

	public StructureMaterialRepository getStructureMaterialRepository() {
		return structureMaterialRepository;
	}

	public GameUserMaterialRepository getGameUserMaterialRepository() {
		return gameUserMaterialRepository;
	}

	public GameUserStructureRepository getGameUserStructureRepository() {
		return gameUserStructureRepository;
	}

	public MaterialSpawnerRepository getMaterialSpawnerRepository() {
		return materialSpawnerRepository;
	}
}
