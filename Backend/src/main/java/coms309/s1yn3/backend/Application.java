package coms309.s1yn3.backend;

import coms309.s1yn3.backend.entity.Material;
import coms309.s1yn3.backend.entity.Structure;
import coms309.s1yn3.backend.entity.relation.StructureMaterialRelation;
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
	 *
	 * @throws IOException
	 */
	private static void registerStructures() throws IOException {
		JSONObject structureConfig = parseJsonFile("src/main/resources/config/structures.json");
		for (String structureName : structureConfig.keySet()) {
			Structure structure = entityProviders().getStructureProvider().findByName(structureName);
			if (structure == null) {
				JSONObject jsonStructure = structureConfig.getJSONObject(structureName);
				structure = repositories().getStructureRepository().save(new Structure(
						structureName,
						jsonStructure.getInt("points")
				));
				logger.infof("Added structure <%s> to database", structure.getName());
				JSONObject recipe = jsonStructure.getJSONObject("recipe");
				for (String materialName : recipe.keySet()) {
					Material material = entityProviders().getMaterialProvider().findByName(materialName);
					if (material == null) {
						material = repositories().getMaterialRepository().save(new Material(materialName));
						logger.infof("Added material <%s> to database", material.getName());
					}
					StructureMaterialRelation structureMaterialRelation = repositories().getStructureMaterialRepository().save(new StructureMaterialRelation(
							structure,
							material,
							recipe.getInt(materialName)
					));
					logger.infof(
							"Added %d %s to recipe for <%s>",
							structureMaterialRelation.getAmount(),
							structureMaterialRelation.getMaterial().getName(),
							structureMaterialRelation.getStructure().getName());
				}
			}
		}
	}

	private static JSONObject parseJsonFile(String file) throws IOException {
		return new JSONObject(Files.readString(Paths.get(file)));
	}
}
