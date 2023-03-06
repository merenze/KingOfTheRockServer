package coms309.s1yn3.backend.controller;

import coms309.s1yn3.backend.service.AbstractEntityManagerService;
import coms309.s1yn3.backend.service.SessionProviderService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractRequestManager extends AbstractEntityManagerService {
	@Autowired
	private SessionProviderService sessionProviderService;

	/**
	 * @return The Session provider.
	 */
	protected SessionProviderService sessions() {
		return sessionProviderService;
	}
}
