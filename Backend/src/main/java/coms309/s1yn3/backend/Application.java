package coms309.s1yn3.backend;

import coms309.s1yn3.backend.entity.repository.PasswordRepository;
import coms309.s1yn3.backend.entity.repository.UserRepository;
import coms309.s1yn3.backend.filter.AuthFilter;
import coms309.s1yn3.backend.service.SessionProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

@SpringBootApplication
public class Application {
	@Autowired
	SessionProviderService sessions;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public FilterRegistrationBean<AuthFilter> authFilter() {
		FilterRegistrationBean<AuthFilter> bean = new FilterRegistrationBean<>();
		bean.setFilter(new AuthFilter(sessions));
		bean.addUrlPatterns("/users", "/users/*");
		return bean;
	}

}
