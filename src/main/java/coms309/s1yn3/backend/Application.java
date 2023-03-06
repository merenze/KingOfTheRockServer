package coms309.s1yn3.backend;

import coms309.s1yn3.backend.entity.Material;
import coms309.s1yn3.backend.entity.Structure;
import coms309.s1yn3.backend.entity.relation.StructureMaterialRelation;
import coms309.s1yn3.backend.filter.AuthFilter;
import coms309.s1yn3.backend.service.AbstractEntityManagerService;
import coms309.s1yn3.backend.service.SessionProviderService;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

@SpringBootApplication
@EnableWebMvc
public class Application extends AbstractEntityManagerService {
	@Autowired SessionProviderService sessionProvider;

	private static final Logger logger = LoggerFactory.logger(Application.class);

	/**
	 * Routes which should be protected by the AuthFilter.
	 */
	private static final String[] ROUTES_USER = {
			"/game/build/*",
			"/game/spawners/*",
			"/game/trade/*",
			"/game/wants/*",
			"/lobby/host",
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
		// Parse the structure config into a JSON object.
		JSONObject structureConfig = parseJsonFile("src/main/resources/config/structures.json");
		// For each structure in the JSON file,
		for (String structureName : structureConfig.keySet()) {
			// Check if the structure is already in the database.
			Structure structure = entityProviders().getStructureProvider().findByName(structureName);
			if (structure == null) {
				// If not, add it.
				JSONObject jsonStructure = structureConfig.getJSONObject(structureName);
				structure = repositories().getStructureRepository().save(new Structure(
						structureName,
						jsonStructure.getInt("points")
				));
				logger.infof("Added structure <%s> to database", structure.getName());
				// Then add its recipe to the database.
				JSONObject recipe = jsonStructure.getJSONObject("recipe");
				// For each material necessary in the recipe,
				for (String materialName : recipe.keySet()) {
					// Check if the material is in the database.
					Material material = entityProviders().getMaterialProvider().findByName(materialName);
					if (material == null) {
						// If not, add it.
						material = repositories().getMaterialRepository().save(new Material(materialName));
						logger.infof("Added material <%s> to database", material.getName());
					}
					// Then add the amount required for the recipe.
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
		// Reads the whole JSON file into a single String,
		// and uses that String to instantiate a JSON object.
		Scanner scanner = new Scanner(new File(file));
		String result = "";
		while (scanner.hasNextLine()) {
			result += scanner.nextLine();
		}
		return new JSONObject(result);
	}
}
