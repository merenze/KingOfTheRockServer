package coms309.s1yn3.backend.controller;

import coms309.s1yn3.backend.entity.User;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractController extends AbstractRequestManager {

	/**
	 * @param request The request being sent.
	 * @return The User sending the request.
	 */
	protected User sender(HttpServletRequest request) {
		return (User) request.getAttribute("user");
	}
}
