package coms309.s1yn3.backend.controller;

import coms309.s1yn3.backend.controller.websocket.GameServer;
import coms309.s1yn3.backend.entity.Game;
import coms309.s1yn3.backend.entity.Material;
import coms309.s1yn3.backend.entity.MaterialSpawner;
import coms309.s1yn3.backend.entity.User;
import coms309.s1yn3.backend.entity.relation.GameUserRelation;
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
import java.util.*;

@RestController
public class GameController extends AbstractController {
	private static final Logger logger = LoggerFactory.logger(GameController.class);

	@PostMapping("/game/spawners/{gameId}")
	public ResponseEntity chooseSpawners(
			HttpServletRequest request,
			@PathVariable int gameId,
			@RequestBody Map<String, int[]> requestBody
			) {
		// Get the user
		User user = sender(request);
		// Get the game
		Game game = entityProviders().getGameProvider().findById(gameId);
		if (game == null) {
			Map<String, String> responseBody = new HashMap<>();
			responseBody.put("message", String.format("No game found with id <%s>", gameId));
			return new ResponseEntity(responseBody, HttpStatus.NOT_FOUND);
		}
		// TODO Validate that the user has an open GameServer connection
		GameUserRelation gameUserRelation = entityProviders().getGameUserProvider().findByGameAndUser(game, user);
		if (gameUserRelation == null) {
			Map<String, String> responseBody = new HashMap<>();
			responseBody.put("message", String.format("User <%s> is not a member of game <%s>", user.getUsername(), game.getId()));
			return new ResponseEntity(responseBody, HttpStatus.FORBIDDEN);
		}
		// Validate that spawners have not already been assigned
		if (gameUserRelation.getHasInitialSpawners()) {
			Map<String, String> responseBody = new HashMap<>();
			responseBody.put("message", String.format("User <%s> has already received spawners for game <%s>", user.getUsername(), game.getId()));
			return new ResponseEntity(responseBody, HttpStatus.FORBIDDEN);
		}
		// Turn the response body into an object I can actually use
		JSONObject jsonRequestBody = new JSONObject();
		for (String materialName : requestBody.keySet()) {
			jsonRequestBody.put(materialName, new JSONArray(requestBody.get(materialName)));
		}
		// TODO Validate that spawners chosen are among those offered
		int numSpawners = 0;
		for (String materialName : jsonRequestBody.keySet()) {
			for (Object o : jsonRequestBody.getJSONArray(materialName).toList()) {
				// Validate that no more than four spawners are used.
				if (++numSpawners > 4) {
					break;
				}
				int spawnNumber = (Integer) o;
				// TODO validate material names
				repositories()
						.getMaterialSpawnerRepository()
						.save(new MaterialSpawner(
								// GameUserRelation
								gameUserRelation,
								// Material
								entityProviders()
										.getMaterialProvider()
										.findByName(materialName),
								// Spawn Number
								spawnNumber
						));
			}
		}
		// Give the user some random spawners if they don't have enough
		if (numSpawners < 4) {
			List<Material> materials = entityProviders().getMaterialProvider().findAll();
			Random random = new Random();
			while (numSpawners++ < 4) {
				repositories()
						.getMaterialSpawnerRepository()
						.save(new MaterialSpawner(
								// GameUserRelation
								gameUserRelation,
								// Material
								materials.get(random.nextInt(materials.size())),
								// Spawn number
								GameServer.roll() + GameServer.roll()
						));
			}
		}
		// No more spawners for you!
		gameUserRelation.setHasInitialSpawners(true);
		repositories().getGameUserRepository().save(gameUserRelation);
		// TODO this should be called at the same time for everyone, after all requests are in
		GameServer.timer.schedule(new TimerTask() {
			@Override
			public void run() {
				GameServer.collectMaterials(game, user);
			}
		}, 0, 5000); // TODO change to 30000

		// Return the spawner list as a response
		return new ResponseEntity(repositories().getMaterialSpawnerRepository().findByGameUserRelation(gameUserRelation), HttpStatus.OK);
	}
}
