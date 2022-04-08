package coms309.s1yn3.backend;

import coms309.s1yn3.backend.filter.AuthFilter;
import coms309.s1yn3.backend.service.SessionProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {
	@Autowired SessionProviderService sessionProvider;

	/**
	 * Routes which should be protected by the AuthFilter.
	 */
	private static final String[] ROUTES_USER = {
			"/lobby/*",
			"/users",
			"/users/*"
	};

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	/**
	 * Declare routes protected by AuthFilter.
	 * @return
	 */
	@Bean
	public FilterRegistrationBean<AuthFilter> authFilter() {
		FilterRegistrationBean<AuthFilter> bean = new FilterRegistrationBean<>();
		bean.setFilter(new AuthFilter(sessionProvider));
		bean.addUrlPatterns(ROUTES_USER);
		return bean;
	}
}
