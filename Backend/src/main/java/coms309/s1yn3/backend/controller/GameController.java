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

	private static final Map<Integer, Trade> trades = new HashMap<>();
	private static int nextTradeId = 1;

	/**
	 * Request material spawners.
	 *
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
		// Check connection
		JSONObject checkConnection = checkConnection(sender(request), gameId);
		if (!checkConnection.getBoolean("pass")) {
			return (ResponseEntity) checkConnection.get("response");
		}
		User user = (User) checkConnection.get("user");
		Game game = (Game) checkConnection.get("game");
		GameUserRelation gameUserRelation = (GameUserRelation) checkConnection.get("relation");
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
	 *
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
		// Check connection
		JSONObject checkConnection = checkConnection(sender(request), gameId);
		if (!checkConnection.getBoolean("pass")) {
			return (ResponseEntity) checkConnection.get("response");
		}
		User user = (User) checkConnection.get("user");
		Game game = (Game) checkConnection.get("game");
		GameUserRelation gameUserRelation = (GameUserRelation) checkConnection.get("relation");
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
	 *
	 * @param request
	 * @param gameId     ID of the game where the trade happens
	 * @param wantsNames Array of the names of materials which the User wants
	 */
	@PostMapping("/game/wants/{gameId}")
	public ResponseEntity wants(
			HttpServletRequest request,
			@PathVariable int gameId,
			@RequestBody String[] wantsNames
	) {
		// Check connection
		JSONObject checkConnection = checkConnection(sender(request), gameId);
		if (!checkConnection.getBoolean("pass")) {
			return (ResponseEntity) checkConnection.get("response");
		}
		User user = (User) checkConnection.get("user");
		Game game = (Game) checkConnection.get("game");
		GameUserRelation gameUserRelation = (GameUserRelation) checkConnection.get("relation");
		// Build the response
		JSONObject message = new JSONObject();
		message.put("type", "material-wants");
		message.put("user", new JSONObject());
		message.getJSONObject("user").put("username", user.getUsername());
		message.getJSONObject("user").put("id", user.getId());
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
		JSONObject checkConnection = checkConnection(sender(request), gameId);
		if (!checkConnection.getBoolean("pass")) {
			return (ResponseEntity) checkConnection.get("response");
		}
		User user = (User) checkConnection.get("user");
		Game game = (Game) checkConnection.get("game");
		GameUserRelation gameUserRelation = (GameUserRelation) checkConnection.get("relation");
		// Check target user connection
		User targetUser = repositories()
				.getUserRepository()
				.findById(userId);
		checkConnection = checkConnection(targetUser, gameId);
		if (!checkConnection.getBoolean("pass")) {
			return (ResponseEntity) checkConnection.get("response");
		}
		// Create the trade and add it to the list.
		int tradeId = nextTradeId++;
		trades.put(tradeId, new Trade(user, targetUser));
		// Build the trade request message.
		JSONObject message = new JSONObject()
				.put("type", "trade-request")
				.put("from", user.getUsername())
				.put("to", targetUser.getUsername())
				.put("trade-id", tradeId);
		GameServer.message(targetUser, message);
		// Return the HTTP response.
		return new ResponseEntity(message.toMap(), HttpStatus.OK);
	}

	@PostMapping("/game/trade/{gameId}/withdraw/{tradeId}")
	public ResponseEntity tradeWithdraw(
			HttpServletRequest request,
			@PathVariable int gameId,
			@PathVariable int tradeId
	) {
		// Check connection
		JSONObject checkConnection = checkConnection(sender(request), gameId);
		if (!checkConnection.getBoolean("pass")) {
			trades.remove(tradeId);
			return (ResponseEntity) checkConnection.get("response");
		}
		User user = (User) checkConnection.get("user");
		Game game = (Game) checkConnection.get("game");
		GameUserRelation gameUserRelation = (GameUserRelation) checkConnection.get("relation");
		// Check existence of trade
		if (!trades.containsKey(tradeId)) {
			return new ResponseEntity(
					new JSONObject()
							.put("message", String.format("No trade found with id <%s>", tradeId))
							.toMap(),
					HttpStatus.NOT_FOUND
			);
		}
		// Check target user connection
		User targetUser = trades.get(tradeId).toUser;
		checkConnection = checkConnection(targetUser, gameId);
		if (!checkConnection.getBoolean("pass")) {
			trades.remove(tradeId);
			return (ResponseEntity) checkConnection.get("response");
		}
		// Remove trade
		trades.remove(tradeId);
		// Build withdraw message
		JSONObject message = new JSONObject()
				.put("type", "trade-withdraw")
				.put("from", user.getUsername())
				.put("to", targetUser.getUsername())
				.put("trade-id", tradeId);
		GameServer.message(targetUser, message);
		return new ResponseEntity(message.toMap(), HttpStatus.OK);
	}

	@PostMapping("/game/trade/{gameId}/accept/{tradeId}")
	public ResponseEntity tradeAccept(
			HttpServletRequest request,
			@PathVariable int gameId,
			@PathVariable int tradeId
	) {
		// Check connection
		JSONObject checkConnection = checkConnection(sender(request), gameId);
		if (!checkConnection.getBoolean("pass")) {
			return (ResponseEntity) checkConnection.get("response");
		}
		User user = (User) checkConnection.get("user");
		Game game = (Game) checkConnection.get("game");
		GameUserRelation gameUserRelation = (GameUserRelation) checkConnection.get("relation");
		// Check existence of trade
		if (!trades.containsKey(tradeId)) {
			return new ResponseEntity(
					new JSONObject()
							.put("message", String.format("No trade found with id <%s>", tradeId))
							.toMap(),
					HttpStatus.NOT_FOUND
			);
		}
		// Check target user connection
		Trade trade = trades.get(tradeId);
		User targetUser = trade.toUser;
		checkConnection = checkConnection(targetUser, gameId);
		if (!checkConnection.getBoolean("pass")) {
			trades.remove(tradeId);
			return (ResponseEntity) checkConnection.get("response");
		}
		// Mark trade accepted
		trade.accepted = true;
		// Build the accept message
		JSONObject message = new JSONObject()
				.put("type", "trade-accept")
				.put("from", trade.fromUser.getUsername())
				.put("to", trade.toUser.getUsername())
				.put("trade-id", tradeId);
		GameServer.message(trade.fromUser, message);
		return new ResponseEntity(message.toMap(), HttpStatus.OK);
	}

	@PostMapping("/game/trade/{gameId}/decline/{tradeId}")
	public ResponseEntity tradeDecline(
			HttpServletRequest request,
			@PathVariable int gameId,
			@PathVariable int tradeId
	) {
		// Check connection
		JSONObject checkConnection = checkConnection(sender(request), gameId);
		if (!checkConnection.getBoolean("pass")) {
			return (ResponseEntity) checkConnection.get("response");
		}
		User user = (User) checkConnection.get("user");
		Game game = (Game) checkConnection.get("game");
		GameUserRelation gameUserRelation = (GameUserRelation) checkConnection.get("relation");
		// Check existence of trade
		if (!trades.containsKey(tradeId)) {
			return new ResponseEntity(
					new JSONObject()
							.put("message", String.format("No trade found with id <%s>", tradeId))
							.toMap(),
					HttpStatus.NOT_FOUND
			);
		}
		// Check target user connection
		Trade trade = trades.get(tradeId);
		User targetUser = trade.toUser;
		checkConnection = checkConnection(targetUser, gameId);
		if (!checkConnection.getBoolean("pass")) {
			trades.remove(tradeId);
			return (ResponseEntity) checkConnection.get("response");
		}
		// Remove the trade
		trades.remove(tradeId);
		// Build the decline message
		JSONObject message = new JSONObject()
				.put("type", "trade-decline")
				.put("from", trade.fromUser)
				.put("to", trade.toUser)
				.put("id", tradeId);
		GameServer.message(trade.fromUser, message);
		return new ResponseEntity(message.toMap(), HttpStatus.OK);
	}

	@PostMapping("/game/trade/{gameId}/add/{tradeId}/{materialName}")
	public ResponseEntity addToTrade(
			HttpServletRequest request,
			@PathVariable int gameId,
			@PathVariable int tradeId,
			@PathVariable String materialName
	) {
		// Check connection
		JSONObject checkConnection = checkConnection(sender(request), gameId);
		if (!checkConnection.getBoolean("pass")) {
			return (ResponseEntity) checkConnection.get("response");
		}
		User user = (User) checkConnection.get("user");
		Game game = (Game) checkConnection.get("game");
		GameUserRelation gameUserRelation = (GameUserRelation) checkConnection.get("relation");
		// Check existence of trade
		if (!trades.containsKey(tradeId)) {
			return new ResponseEntity(
					new JSONObject()
							.put("message", String.format("No trade found with id <%s>", tradeId))
							.toMap(),
					HttpStatus.NOT_FOUND
			);
		}
		// Check target user connection
		Trade trade = trades.get(tradeId);
		User targetUser = trade.toUser;
		checkConnection = checkConnection(targetUser, gameId);
		if (!checkConnection.getBoolean("pass")) {
			trades.remove(tradeId);
			return (ResponseEntity) checkConnection.get("response");
		}
		// Check the trade has been accepted
		if (!trade.accepted) {
			return new ResponseEntity(
					new JSONObject().put(
							"message",
							String.format(
									"<%s> has not accepted <%s>'s trade offer.",
									trade.toUser.getUsername(),
									trade.fromUser.getUsername()
							)
					).toMap(),
					HttpStatus.FORBIDDEN
			);
		}
		// Get the material
		Material material =
				entityProviders()
						.getMaterialProvider()
						.findByName(materialName);
		if (material == null) {
			return new ResponseEntity(
					new JSONObject()
							.put("message", String.format("Material not found for name <%s>", materialName))
							.toMap(),
					HttpStatus.NOT_FOUND
			);
		}
		// Get the user's material relation
		GameUserMaterialRelation gameUserMaterialRelation =
				entityProviders()
						.getGameUserMaterialProvider()
						.findByGameUserRelationAndMaterial(gameUserRelation, material);
		// Verify the User has the material to add.
		if (trade.offers.get(user.getId()).get(materialName) >= gameUserMaterialRelation.getAmount()) {
			return new ResponseEntity(
					new JSONObject()
							.put("message", String.format("Not enough <%s> in inventory.", materialName))
							.toMap(),
					HttpStatus.FORBIDDEN
			);
		}
		// Update amount
		trade.addToOffer(user, material);
		// Build update message
		JSONObject message = new JSONObject()
				.put("type", "trade-update")
				.put("trade-id", tradeId)
				.put("offer", new JSONObject(
						trade.offers.get(user.getId())
				));
		GameServer.message(trade.getOther(user), message);
		return new ResponseEntity(message.toMap(), HttpStatus.OK);
	}

	@PostMapping("/game/trade/{gameId}/remove/{tradeId}/{materialName}")
	public ResponseEntity removeFromTrade(
			HttpServletRequest request,
			@PathVariable int gameId,
			@PathVariable int tradeId,
			@PathVariable String materialName
	) {
		// Check connection
		JSONObject checkConnection = checkConnection(sender(request), gameId);
		if (!checkConnection.getBoolean("pass")) {
			return (ResponseEntity) checkConnection.get("response");
		}
		User user = (User) checkConnection.get("user");
		Game game = (Game) checkConnection.get("game");
		GameUserRelation gameUserRelation = (GameUserRelation) checkConnection.get("relation");
		// Check existence of trade
		if (!trades.containsKey(tradeId)) {
			return new ResponseEntity(
					new JSONObject()
							.put("message", String.format("No trade found with id <%s>", tradeId))
							.toMap(),
					HttpStatus.NOT_FOUND
			);
		}
		// Check target user connection
		Trade trade = trades.get(tradeId);
		User targetUser = trade.toUser;
		checkConnection = checkConnection(targetUser, gameId);
		if (!checkConnection.getBoolean("pass")) {
			trades.remove(tradeId);
			return (ResponseEntity) checkConnection.get("response");
		}
		// Check the trade has been accepted
		if (!trade.accepted) {
			return new ResponseEntity(
					new JSONObject().put(
							"message",
							String.format(
									"<%s> has not accepted <%s>'s trade offer.",
									trade.toUser.getUsername(),
									trade.fromUser.getUsername()
							)
					).toMap(),
					HttpStatus.FORBIDDEN
			);
		}
		// Check the offer has the material to remove.
		if (trade.offers.get(user.getId()).get(materialName) <= 0) {
			return new ResponseEntity(
					new JSONObject()
							.put("message", String.format("No <%s> in offer.", materialName))
							.toMap(),
					HttpStatus.FORBIDDEN
			);
		}
		// Get the material
		Material material =
				entityProviders()
						.getMaterialProvider()
						.findByName(materialName);
		// Update amount
		trade.removeFromOffer(user, material);
		// Build update message
		JSONObject message = new JSONObject()
				.put("type", "trade-update")
				.put("trade-id", tradeId)
				.put("offer", new JSONObject(
						trade.offers.get(user.getId())
				));
		GameServer.message(trade.getOther(user), message);
		return new ResponseEntity(message.toMap(), HttpStatus.OK);
	}

	@PostMapping("/game/trade/{gameId}/confirm/{tradeId}")
	public ResponseEntity tradeConfirm(
			HttpServletRequest request,
			@PathVariable int gameId,
			@PathVariable int tradeId
	) {

		// Check connection
		JSONObject checkConnection = checkConnection(sender(request), gameId);
		if (!checkConnection.getBoolean("pass")) {
			return (ResponseEntity) checkConnection.get("response");
		}
		User user = (User) checkConnection.get("user");
		Game game = (Game) checkConnection.get("game");
		GameUserRelation gameUserRelation = (GameUserRelation) checkConnection.get("relation");
		// Check existence of trade
		if (!trades.containsKey(tradeId)) {
			return new ResponseEntity(
					new JSONObject()
							.put("message", String.format("No trade found with id <%s>", tradeId))
							.toMap(),
					HttpStatus.NOT_FOUND
			);
		}
		// Check target user connection
		Trade trade = trades.get(tradeId);
		User targetUser = trade.toUser;
		checkConnection = checkConnection(targetUser, gameId);
		if (!checkConnection.getBoolean("pass")) {
			trades.remove(tradeId);
			return (ResponseEntity) checkConnection.get("response");
		}
		// Check the trade has been accepted
		if (!trade.accepted) {
			return new ResponseEntity(
					new JSONObject().put(
							"message",
							String.format(
									"<%s> has not accepted <%s>'s trade offer.",
									trade.toUser.getUsername(),
									trade.fromUser.getUsername()
							)
					).toMap(),
					HttpStatus.FORBIDDEN
			);
		}
		trade.confirm(user);
		// TODO Update player inventories

		JSONObject message = new JSONObject()
				.put("type", "trade-confirm")
				.put("trade-id", tradeId)
				.put("offer", new JSONObject(
						trade.offers
								.get(trade.getOther(user).getId())
				));
		GameServer.message(trade.getOther(user), message);
		return new ResponseEntity(message.toMap(), HttpStatus.OK);
	}

	private static JSONObject checkConnection(User user, int gameId) {
		// Check the game exists
		Game game = entityProviders().getGameProvider().findById(gameId);
		if (game == null) {
			Map<String, String> responseBody = new HashMap<>();
			responseBody.put("message", String.format("No game found with id <%s>", gameId));
			return new JSONObject()
					.put("pass", false)
					.put("response", new ResponseEntity(responseBody, HttpStatus.NOT_FOUND));
		}
		// Get the game-user relation
		GameUserRelation gameUserRelation = entityProviders()
				.getGameUserProvider()
				.findByGameAndUser(game, user);
		if (gameUserRelation == null) {
			Map<String, String> responseBody = new HashMap<>();
			responseBody.put("message", String.format("User <%s> is not a member of game <%s>", user.getUsername(), game.getId()));
			return new JSONObject()
					.put("pass", false)
					.put("response", new ResponseEntity(responseBody, HttpStatus.FORBIDDEN));
		}
		// Check that user is connected to the game WS endpoint
		if (!GameServer.hasUser(user)) {
			Map<String, String> responseBody = new HashMap<>();
			responseBody.put("message", String.format("User <%s> not connected to game <%s>", user.getUsername(), gameId));
			return new JSONObject()
					.put("pass", false)
					.put("response", new ResponseEntity(responseBody, HttpStatus.FORBIDDEN));
		}
		// Return true
		return new JSONObject()
				.put("pass", true)
				.put("user", user)
				.put("game", game)
				.put("relation", gameUserRelation);
	}

	private class Trade {
		/**
		 * The User requesting the trade.
		 */
		User fromUser;

		/**
		 * The User being requested to trade.
		 */
		User toUser;

		/**
		 * User ID -> {Material name -> amount offered by the User}
		 */
		Map<Integer, Map<String, Integer>> offers;

		/**
		 * Whether toUser has accepted the offer.
		 */
		boolean accepted;

		/**
		 * Whether fromUser has confirmed (finalized) the trade.
		 */
		boolean fromConfirmed;

		/**
		 * Whether toUser has confirmed (finalized) the trade.
		 */
		boolean toConfirmed;

		/**
		 * @param fromUser The User requesting to trade.
		 * @param toUser   The User being requested to trade.
		 */
		Trade(User fromUser, User toUser) {
			this.fromUser = fromUser;
			this.toUser = toUser;
			offers = new HashMap<>();
			offers.put(fromUser.getId(), new HashMap<>());
			offers.put(toUser.getId(), new HashMap<>());

			for (Material material : repositories().getMaterialRepository().findAll()) {
				Map<String, Integer> offer;

				offers.get(fromUser.getId()).put(material.getName(), 0);
				offers.get(toUser.getId()).put(material.getName(), 0);
			}
		}

		/**
		 * Add one of the Material to the User's offer.
		 *
		 * @param user     User adding the material.
		 * @param material Material to add.
		 */
		void addToOffer(User user, Material material) {
			if (!user.equals(fromUser) && !user.equals(toUser)) {
				throw new IllegalArgumentException("User must be associated with the trade.");
			}
			fromConfirmed = false;
			toConfirmed = false;
			Map<String, Integer> offer = offers.get(user.getId());
			offer.put(material.getName(), offer.get(material.getName()) + 1);
		}

		/**
		 * Remove one of the Material from the User's offer.
		 *
		 * @param user     User removing the material.
		 * @param material Material to add.
		 */
		void removeFromOffer(User user, Material material) {
			if (!user.equals(fromUser) && !user.equals(toUser)) {
				throw new IllegalArgumentException("User must be associated with the trade.");
			}
			fromConfirmed = false;
			toConfirmed = false;
			Map<String, Integer> offer = offers.get(user.getId());
			offer.put(material.getName(), offer.get(material) - 1);
		}

		/**
		 * Get the User of this trade which is NOT the given User.
		 *
		 * @param user
		 */
		User getOther(User user) {
			if (user.equals(fromUser)) {
				return toUser;
			}
			if (user.equals(toUser)) {
				return fromUser;
			}
			throw new IllegalArgumentException("User must be associated with the trade.");
		}


		/**
		 * Confirm (finalize) the trade for the given User.
		 * @param user User confirming the trade.
		 */
		void confirm(User user) {
			if (user.equals(fromUser)) {
				fromConfirmed = true;
			}
			if (user.equals(toUser)) {
				toConfirmed = true;
			}
			throw new IllegalArgumentException("User must be associated with the trade.");
		}


		/**
		 * @param user
		 * @return True if the User has confirmed (finalized) the trade.
		 */
		boolean confirmed(User user) {
			if (user.equals(fromUser)) {
				return fromConfirmed;
			}
			if (user.equals(toUser)) {
				return toConfirmed;
			}
			throw new IllegalArgumentException("User must be associated with the trade.");
		}

		@Override
		public String toString() {
			return String.format("TRADE(%s, %s)", fromUser.getUsername(), toUser.getUsername());
		}
	}
}
