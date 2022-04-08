package coms309.s1yn3.backend.controller;

import coms309.s1yn3.backend.entity.User;
import coms309.s1yn3.backend.service.RepositoryProviderService;
import coms309.s1yn3.backend.service.SessionProviderService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractController {
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
	 * @param request The request being sent.
	 * @return The User sending the request.
	 */
	protected User sender(HttpServletRequest request) {
		return (User) request.getAttribute("user");
	}

	/**
	 * @return The Session provider.
	 */
	protected SessionProviderService sessions() {
		return sessionProviderService;
	}
}
