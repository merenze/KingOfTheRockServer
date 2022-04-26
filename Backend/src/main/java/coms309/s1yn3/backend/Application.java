package coms309.s1yn3.backend;

import coms309.s1yn3.backend.entity.Structure;
import coms309.s1yn3.backend.filter.AuthFilter;
import coms309.s1yn3.backend.service.RepositoryProviderService;
import coms309.s1yn3.backend.service.SessionProviderService;
import coms309.s1yn3.backend.service.entityprovider.AbstractEntityManagerService;
import coms309.s1yn3.backend.service.entityprovider.AbstractEntityProviderService;
import coms309.s1yn3.backend.service.entityprovider.EntityProviderService;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootApplication
@EnableWebMvc
public class Application extends AbstractEntityManagerService {
	@Autowired SessionProviderService sessionProvider;

	private static final Logger logger = LoggerFactory.logger(Application.class);

	/**
	 * Routes which should be protected by the AuthFilter.
	 */
	private static final String[] ROUTES_USER = {
			"/lobby/host",
			"/lobby/disconnect",
			"/users",
			"/users/*"
	};

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		try {
			registerStructures();
		} catch (IOException ex) {
			logger.errorf("Failed to register structures.");
			ex.printStackTrace();
		}
	}

	/**
	 * Declare routes protected by AuthFilter.
	 *
	 * @return
	 */
	@Bean
	public FilterRegistrationBean<AuthFilter> authFilter() {
		FilterRegistrationBean<AuthFilter> bean = new FilterRegistrationBean<>();
		bean.setFilter(new AuthFilter(sessionProvider));
		bean.addUrlPatterns(ROUTES_USER);
		return bean;
	}

	/**
	 * Register structures from the JSON configuration.
	 * @throws IOException
	 */
	private static void registerStructures() throws IOException {
		JSONObject jsonObject = parseJsonFile("src/main/resources/config/structures.json");
		for (String structureName : jsonObject.keySet()) {
			Structure structure = entityProviders().getStructureProvider().findByName(structureName);
			if (structure == null) {
				structure = new Structure(
						structureName,
						jsonObject.getJSONObject(structureName).getInt("points")
				);
				logger.infof("Added structure <%s> to database", structure.getName());
				repositories().getStructureRepository().save(structure);
			}
		}
	}

	private static JSONObject parseJsonFile(String file) throws IOException {
		return new JSONObject(Files.readString(Paths.get(file)));
	}
}
