package coms309.s1yn3.backend.filter;

import coms309.s1yn3.backend.service.SessionProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Requires request sender to have a valid user session.
 */
public class AuthFilter implements Filter {
	private SessionProviderService sessions;

	public AuthFilter(SessionProviderService sessions) {
		this.sessions = sessions;
	}

	@Override public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
		HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
//		String header = httpRequest.getHeader("auth-token");
		String header = httpRequest.getParameter("auth-token");
		if (header == null || sessions.getUser(header) == null) {
			httpResponse.setStatus(HttpStatus.FORBIDDEN.value());
			return;
		}

		filterChain.doFilter(servletRequest, servletResponse);
	}
}
