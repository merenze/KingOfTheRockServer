package coms309.s1yn3.backend.controller.websocket;

import coms309.s1yn3.backend.service.RepositoryProviderService;
import coms309.s1yn3.backend.service.SessionProviderService;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class AbstractWebSocketServer {
	Logger logger = LoggerFactory.logger(AbstractWebSocketServer.class);

	private static RepositoryProviderService repositoryProviderService;
	private static SessionProviderService sessionProviderService;

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

	protected static RepositoryProviderService repositories() {
		return repositoryProviderService;
	}

	protected static SessionProviderService sessions() {
		return sessionProviderService;
	}
}
