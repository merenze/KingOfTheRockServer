package coms309.s1yn3.backend.service;

import coms309.s1yn3.backend.service.entityprovider.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Contains handles to individual specialized entity providers.
 */
@Service
public class EntityProviderProviderService {
	@Autowired GameProviderService gameProviderService;
	@Autowired LobbyProviderService lobbyProviderService;
	@Autowired StructureProviderService structureProviderService;
	@Autowired MaterialProviderService materialProviderService;
	@Autowired GameUserProviderService gameUserProviderService;
	@Autowired GameUserMaterialProviderService gameUserMaterialProviderService;
	@Autowired GameUserStructureProviderService gameUserStructureProviderService;
	@Autowired StructureMaterialProviderService structureMaterialProviderService;

	public GameProviderService getGameProvider() {
		return gameProviderService;
	}

	public LobbyProviderService getLobbyProvider() {
		return lobbyProviderService;
	}

	public StructureProviderService getStructureProvider() {
		return structureProviderService;
	}

	public MaterialProviderService getMaterialProvider() {
		return materialProviderService;
	}

	public GameUserProviderService getGameUserProvider() {
		return gameUserProviderService;
	}

	public GameUserMaterialProviderService getGameUserMaterialProvider() {
		return gameUserMaterialProviderService;
	}

	public GameUserStructureProviderService getGameUserStructureProvider() {
		return gameUserStructureProviderService;
	}

	public StructureMaterialProviderService getStructureMaterialProvider() {
		return structureMaterialProviderService;
	}
}
