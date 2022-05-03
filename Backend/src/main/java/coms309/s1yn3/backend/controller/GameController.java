package coms309.s1yn3.backend.controller;

import coms309.s1yn3.backend.controller.websocket.GameServer;
import coms309.s1yn3.backend.entity.*;
import coms309.s1yn3.backend.entity.relation.GameUserMaterialRelation;
import coms309.s1yn3.backend.entity.relation.GameUserRelation;
import coms309.s1yn3.backend.entity.relation.GameUserStructureRelation;
import coms309.s1yn3.backend.entity.relation.StructureMaterialRelation;
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

	/**
	 * Request material spawners.
	 * @param request
	 * @param gameId
	 * @param requestBody
	 * @return
	 */
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
		}, 0, 30000);

		// Return the spawner list as a response
		return new ResponseEntity(repositories().getMaterialSpawnerRepository().findByGameUserRelation(gameUserRelation), HttpStatus.OK);
	}

	/**
	 * Request to build a structure.
	 * @param request
	 * @param gameId
	 * @param structureName
	 * @return
	 */
	@PostMapping("/game/build/{gameId}/{structureName}")
	public ResponseEntity build(
			HttpServletRequest request,
			@PathVariable int gameId,
			@PathVariable String structureName
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
		// TODO Check that user is connected to the game WS endpoint
		// Get the game-user relation
		GameUserRelation gameUserRelation = entityProviders().getGameUserProvider().findByGameAndUser(game, user);
		if (gameUserRelation == null) {
			Map<String, String> responseBody = new HashMap<>();
			responseBody.put("message", String.format("User <%s> is not a member of game <%s>", user.getUsername(), game.getId()));
			return new ResponseEntity(responseBody, HttpStatus.FORBIDDEN);
		}
		// Get the structure
		Structure structure = entityProviders().getStructureProvider().findByName(structureName);
		if (structure == null) {
			Map<String, String> responseBody = new HashMap<>();
			responseBody.put("message", String.format("No structure found with name <%s>", structureName));
			return new ResponseEntity(responseBody, HttpStatus.NOT_FOUND);
		}
		// Get the recipe
		List<StructureMaterialRelation> neededMaterials = structure.getMaterialRelations();
		// Make sure the user has all required materials
		for (StructureMaterialRelation neededMaterial : neededMaterials) {
			Material material = neededMaterial.getMaterial();
			int requiredAmount =
					entityProviders()
							.getStructureMaterialProvider()
							.findByStructureAndMaterial(structure, material)
							.getAmount();
			int holdingAmount = entityProviders()
					.getGameUserMaterialProvider()
					.findByGameAndUserAndMaterial(game, user, material)
					.getAmount();
			logger.debugf(
					"Game <%s>: <%s> has %d/%d %s for %s",
					gameId,
					user.getUsername(),
					holdingAmount,
					requiredAmount,
					material.getName(),
					structureName
			);
			if (holdingAmount < requiredAmount) {
				Map<String, Object> responseBody = new HashMap<>();
				responseBody.put("success", false);
				responseBody.put("message", "Not enough required materials.");
				return new ResponseEntity(responseBody, HttpStatus.BAD_REQUEST);
			}
		}
		// Find the structure relation
		GameUserStructureRelation gameUserStructureRelation =
				entityProviders()
						.getGameUserStructureProvider()
								.findByGameUserRelationAndStructure(
										gameUserRelation,
										structure
								);
		// Increment the amount built
		gameUserStructureRelation.incrementAmount();
		repositories()
				.getGameUserStructureRepository()
						.save(gameUserStructureRelation);
		logger.infof("Game <%s>: <%s> built <%s>", gameId, user.getUsername(), structureName);
		// Remove used materials from the User's inventory
		for (StructureMaterialRelation structureMaterialRelation : neededMaterials) {
			GameUserMaterialRelation gameUserMaterialRelation =
					entityProviders()
							.getGameUserMaterialProvider()
							.findByGameAndUserAndMaterial(
									game,
									user,
									structureMaterialRelation.getMaterial()
							);
			gameUserMaterialRelation.remove(structureMaterialRelation.getAmount());
			repositories()
					.getGameUserMaterialRepository()
					.save(gameUserMaterialRelation);
		}
		// Add to the User's score
		gameUserRelation.addScore(structure.getPoints());
		repositories().getGameUserRepository().save(gameUserRelation);
		// Check for victory
		if (gameUserRelation.getScore() >= GameServer.VICTORY_SCORE) {
			JSONObject message = new JSONObject();
			message.put("type", "game-over");
			message.put("victor", user);
			GameServer.broadcast(game, message);
		}
		// Build response body
		JSONObject responseBody = new JSONObject();
		responseBody.put("success", true);
		responseBody.put("structures", new JSONObject());
		responseBody.put("score", gameUserRelation.getScore());
		for (GameUserStructureRelation gusRelation : repositories()
				.getGameUserStructureRepository()
				.findByGameUserRelation(gameUserRelation)) {
			responseBody.getJSONObject("structures")
					.put(gusRelation.getStructureName(), gusRelation.getAmount());
		}
		responseBody.put("materials", new JSONObject());
		for (GameUserMaterialRelation gameUserMaterialRelation : repositories()
				.getGameUserMaterialRepository()
				.findByGameUserRelation(gameUserRelation)) {
			responseBody
					.getJSONObject("materials")
					.put(
							gameUserMaterialRelation.getMaterial().getName(),
							gameUserMaterialRelation.getAmount()
					);
		}
		logger.debugf("Game <%s>: <%s> now has %s", gameId, user.getUsername(), responseBody.toString());
		return new ResponseEntity(responseBody.toMap(), HttpStatus.OK);
	}

	/**
	 * Request to get materials in a trade.
	 * @param request
	 * @param gameId ID of the game where the trade happens
	 * @param wantsNames Array of the names of materials which the User wants
	 */
	@PostMapping("/game/wants/{gameId}")
	public ResponseEntity tradeRequest(
			HttpServletRequest request,
			@PathVariable int gameId,
			@RequestBody String[] wantsNames
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
		// Get the game-user relation
		GameUserRelation gameUserRelation = entityProviders()
				.getGameUserProvider()
				.findByGameAndUser(game, user);
		if (gameUserRelation == null) {
			Map<String, String> responseBody = new HashMap<>();
			responseBody.put("message", String.format("User <%s> is not a member of game <%s>", user.getUsername(), game.getId()));
			return new ResponseEntity(responseBody, HttpStatus.FORBIDDEN);
		}
		// Check that user is connected to the game WS endpoint
		if (!GameServer.hasUser(user)) {
			Map<String, String> responseBody = new HashMap<>();
			responseBody.put("message", String.format("User <%s> not connected to game <%s>", user.getUsername(), gameId));
			return new ResponseEntity(responseBody, HttpStatus.FORBIDDEN);
		}
		JSONObject message = new JSONObject();
		message.put("type", "material-wants");
		message.put("user", user.getUsername());
		message.put("wants", new JSONArray(wantsNames));
		// For each user in the game
		for (GameUserRelation gur : game.getUserRelations()) {
			// If the user is not the requesting user
			if (!gur.getUser().equals(user)) {
				// Send the material wants message to the user
				GameServer.message(gur.getUser(), message);
			}
		}
		Map<String, String> responseBody = new HashMap<>();
		responseBody.put("status", HttpStatus.OK.toString());
		return new ResponseEntity(HttpStatus.OK);
	}

	@PostMapping("/game/trade/{gameId}/request/{userId}")
	public ResponseEntity tradeRequest(
			HttpServletRequest request,
			@PathVariable int gameId,
			@PathVariable int userId
	) {
		// TODO
		return new ResponseEntity(HttpStatus.OK);
	}

	@PostMapping("/game/trade/{gameId}/withdraw/{userId}")
	public ResponseEntity tradeWithdraw(
			HttpServletRequest request,
			@PathVariable int gameId,
			@PathVariable int userId
	) {
		// TODO
		return new ResponseEntity(HttpStatus.OK);
	}
}
