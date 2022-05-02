package coms309.s1yn3.backend.controller;

import coms309.s1yn3.backend.entity.MaterialSpawner;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class GameController extends AbstractController {
	private static final Logger logger = LoggerFactory.logger(GameController.class);

	@PostMapping("/game/spawners/{gameId}")
	public ResponseEntity chooseSpawners(
			HttpServletRequest request,
			@PathVariable int gameId,
//			@RequestBody JSONObject requestBody
			@RequestBody Map<String, int[]> requestBody
			) {
		// Turn the response body into an object I can actually use
		JSONObject jsonRequestBody = new JSONObject();
		for (String materialName : requestBody.keySet()) {
			jsonRequestBody.put(materialName, new JSONArray(requestBody.get(materialName)));
		}
		logger.debug(jsonRequestBody);
		// TODO Validate that spawners chosen are among those offered
		// TODO Validate that only four spawners are sent
		// TODO Validate that spawners have not already been chosen
		for (String materialName : jsonRequestBody.keySet()) {
			for (Object o : jsonRequestBody.getJSONArray(materialName).toList()) {
				int spawnNumber = (Integer) o;
				// TODO validate material names
				repositories()
						.getMaterialSpawnerRepository()
						.save(new MaterialSpawner(
								// GameUserRelation
								entityProviders()
										.getGameUserProvider()
										.findByGameAndUser(
												// Game
												repositories()
														.getGameRepository()
														.findById(gameId),
												// User
												sender(request)
										),
								// Material
								entityProviders()
										.getMaterialProvider()
										.findByName(materialName),
								// Spawn Number
								spawnNumber
						));
			}
		}
		JSONObject responseBody = new JSONObject();
		responseBody.put("status", HttpStatus.OK);
		return new ResponseEntity(responseBody.toMap(), HttpStatus.OK);
	}
}
