package coms309.s1yn3.backend.service.entityprovider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Contains handles to individual specialized entity providers.
 */
@Service
public class EntityProviderService {
	@Autowired GameProviderService gameProviderService;
	@Autowired LobbyProviderService lobbyProviderService;
	@Autowired StructureProviderService structureProviderService;
	@Autowired MaterialProviderService materialProviderService;

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
}
