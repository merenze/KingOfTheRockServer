package coms309.s1yn3.backend.filter;

import coms309.s1yn3.backend.entity.User;
import coms309.s1yn3.backend.service.SessionProviderService;
import org.springframework.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Requires request sender to have a valid user session.
 */
public class AuthFilter implements Filter {
	private SessionProviderService sessionProvider;

	public AuthFilter(SessionProviderService sessionProvider) {
		this.sessionProvider = sessionProvider;
	}

	@Override public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
		HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
//		String header = httpRequest.getHeader("auth-token");
		String header = httpRequest.getParameter("auth-token");
		User user = sessionProvider.getUser(header);
		if (header == null || user == null) {
			httpResponse.setStatus(HttpStatus.FORBIDDEN.value());
			return;
		}

		httpRequest.setAttribute("user", user);
		filterChain.doFilter(servletRequest, servletResponse);
	}
}
