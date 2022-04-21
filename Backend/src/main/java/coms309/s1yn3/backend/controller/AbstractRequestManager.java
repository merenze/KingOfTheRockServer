package coms309.s1yn3.backend.controller;

import coms309.s1yn3.backend.service.RepositoryProviderService;
import coms309.s1yn3.backend.service.SessionProviderService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractRequestManager {
	@Autowired
	private RepositoryProviderService repositoryProviderService;

	@Autowired
	private SessionProviderService sessionProviderService;

	/**
	 * @return The Repository provider.
	 */
	protected RepositoryProviderService repositories() {
		return repositoryProviderService;
	}

	/**
	 * @return The Session provider.
	 */
	protected SessionProviderService sessions() {
		return sessionProviderService;
	}
}
