package coms309.s1yn3.backend.controller.websocket;

import coms309.s1yn3.backend.service.RepositoryProviderService;
import coms309.s1yn3.backend.service.SessionProviderService;
import coms309.s1yn3.backend.service.entityprovider.LobbyProviderService;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class AbstractWebSocketServer {
	Logger logger = LoggerFactory.logger(AbstractWebSocketServer.class);

	private static RepositoryProviderService repositoryProviderService;
	private static SessionProviderService sessionProviderService;
	private static LobbyProviderService lobbyProviderService;

	@Autowired
	public void setRepositoryProviderService(RepositoryProviderService repositoryProviderService) {
		logger.debug("Injecting RepositoryProviderService");
		AbstractWebSocketServer.repositoryProviderService = repositoryProviderService;
	}

	@Autowired
	public void setSessionProviderService(SessionProviderService sessionProviderService) {
		logger.debug("Injecting SessionProviderService");
		AbstractWebSocketServer.sessionProviderService = sessionProviderService;
	}

	@Autowired
	public void setLobbyProviderService(LobbyProviderService lobbyProviderService) {
		logger.debug("Injecting LobbyProviderService");
		AbstractWebSocketServer.lobbyProviderService = lobbyProviderService;
	}

	protected static RepositoryProviderService repositories() {
		return repositoryProviderService;
	}

	protected static SessionProviderService authSessions() {
		return sessionProviderService;
	}

	protected static LobbyProviderService lobbies() {
		return lobbyProviderService;
	}
}
