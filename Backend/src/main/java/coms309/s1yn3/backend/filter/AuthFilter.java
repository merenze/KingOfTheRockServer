package coms309.s1yn3.backend.filter;

import coms309.s1yn3.backend.service.SessionProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Requires request sender to have a valid user session.
 */
@Component
@Order(1)
public class AuthFilter implements Filter {
	@Autowired SessionProviderService sessions;
	@Override public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
		HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
		String header = httpRequest.getHeader("auth-token");
		if (header == null || sessions.getUser(header) == null) {
			httpResponse.setStatus(HttpStatus.FORBIDDEN.value());
		}
	}
}
