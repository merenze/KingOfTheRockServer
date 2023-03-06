package coms309.s1yn3.backend.controller.websocket;

import coms309.s1yn3.backend.entity.*;
import coms309.s1yn3.backend.entity.relation.GameUserMaterialRelation;
import coms309.s1yn3.backend.entity.relation.GameUserRelation;
import coms309.s1yn3.backend.entity.relation.GameUserStructureRelation;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;

@Component
@ServerEndpoint("/game/{game-id}/{auth-token}")
public class GameServer extends AbstractWebSocketServer {
	private static final Logger logger = LoggerFactory.logger(AbstractWebSocketServer.class);
	private static final Timer timer = new Timer();
	private static final Map<Integer, TimerTask> timerTasks = new HashMap<>();

	/**
	 * The number of points required to win.
	 */
	public static final int VICTORY_SCORE = 10;

	@OnOpen
	public void onOpen(
			Session session,
			@PathParam("game-id") int gameId,
			@PathParam("auth-token") String authToken) throws IOException {
		logger.infof("Web Socket connection opened at /game/%s/%s", gameId, authToken);
		// Get the connecting User
		User user = authSessions().getUser(authToken);
		if (user == null) {
			logger.warnf("Failed to resolve auth token <%s> for lobby join", authToken);
			session.close();
			return;
		}
		// TODO Don't allow open connections to closed games
		Game game = entityProviders().getGameProvider().findById(gameId);
		if (!game.hasPlayer(user)) {
			logger.warnf(
					"User <%s> attempted to connect to game <%s>, but has no relation in database.",
					user.getUsername(),
					game.getId()
			);
			session.close();
		}
		// TODO Don't allow joining a finished game
		// TODO disconnect user from previous sessions
		addSession(user, session);
		logger.infof("User <%s> connected to game <%s>", user.getUsername(), game.getId());
		initializeStructures(game, user);
		initializeMaterials(game, user);
		offerSpawnerOptions(game, user);

		// Get spawner request after thirty seconds
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				getSpawnerRequest(user);
			}
		}, 30000);
	}

	@OnClose
	public void onClose(Session session) {
		User user = getUser(session);
		logger.infof("Closing connection for %s", user.getUsername());
		removeSession(getUser(session));
	}

	/**
	 * Set the User's initial materials for the Game.
	 *
	 * @param game
	 * @param user
	 */
	private static void initializeMaterials(Game game, User user) {
		GameUserRelation gameUserRelation =
				entityProviders()
						.getGameUserProvider()
						.findByGameAndUser(game, user);
		if (gameUserRelation.getHasInitialMaterials()) {
			logger.infof(
					"Game <%s>: <%s> already assigned initial structures",
					game.getId(),
					user.getUsername()
			);
			return;
		}
		logger.infof(
				"Game <%s>: Assigning structures to <%s>",
				game.getId(),
				user.getUsername()
		);
		List<Material> materials = entityProviders().getMaterialProvider().findAll();
		// Give the user a relation to every material
		for (Material material : materials) {
			gameUserRelation.addMaterialRelation(
					repositories()
							.getGameUserMaterialRepository()
							.save(
									new GameUserMaterialRelation(
											entityProviders()
													.getGameUserProvider()
													.findByGameAndUser(game, user),
											material
									)
							)
			);
		}
		// Give player four random resources
		Random random = new Random();
		for (int i = 0; i < 4; i++) {
			Material material = materials.get(random.nextInt(materials.size()));
			GameUserMaterialRelation gameUserMaterialRelation =
					entityProviders()
							.getGameUserMaterialProvider()
							.findByGameAndUserAndMaterial(game, user, material);
			gameUserMaterialRelation.add(1);
			logger.infof(
					"Game <%s>: Granted %s to <%s>",
					game.getId(),
					material.getName(),
					user.getUsername()
			);
			repositories().getGameUserMaterialRepository().save(gameUserMaterialRelation);
		}
		gameUserRelation.setHasInitialStructures(true);
		gameUserRelation.setHasInitialMaterials(true);
		repositories().getGameUserRepository().save(gameUserRelation);
	}

	/**
	 * Set the User's initial Structures for the Game.
	 */
	public static void initializeStructures(Game game, User user) {
		GameUserRelation gameUserRelation =
				entityProviders()
						.getGameUserProvider()
						.findByGameAndUser(game, user);
		if (gameUserRelation.getHasInitialStructures()) {
			logger.infof(
					"Game <%s>: <%s> already assigned initial structures",
					game.getId(),
					user.getUsername()
			);
			return;
		}

		List<Structure> structures = entityProviders().getStructureProvider().findAll();
		// Give the user a relation to every material
		for (Structure structure : structures) {
			gameUserRelation.addStructureRelation(
					repositories()
							.getGameUserStructureRepository()
							.save(
									new GameUserStructureRelation(
											gameUserRelation,
											structure
									)
							)
			);
		}
		// Map structures to JSON
		JSONObject message = new JSONObject();
		message.put("type", "structures");
		message.put("structures", new JSONObject());
		for (GameUserStructureRelation gameUserStructureRelation : repositories()
				.getGameUserStructureRepository()
				.findByGameUserRelation(gameUserRelation)) {
			message.getJSONObject("structures").put(
					gameUserStructureRelation.getStructureName(),
					gameUserStructureRelation.getAmount());
		}
		message(user, message);
	}

	/**
	 * Set the User's initial resource spawners for the Game.
	 * Should only be called AFTER initializeMaterials,
	 * as it depends on preexisting GameUserMaterialRelations.
	 */
	private static void offerSpawnerOptions(Game game, User user) {
		GameUserRelation gameUserRelation =
				entityProviders()
						.getGameUserProvider()
						.findByGameAndUser(game, user);
		JSONObject message = new JSONObject();
		message.put("type", "spawner-options");
		JSONObject spawnerOptions = new JSONObject();
		// For each material,
		for (GameUserMaterialRelation gameUserMaterialRelation : gameUserRelation.getMaterialRelations()) {
			// Create an array of options.
			JSONArray options = new JSONArray();
			// Add four options to the array.
			for (int i = 0; i < 4; i++) {
				options.put(roll() + roll());
			}
			spawnerOptions.put(gameUserMaterialRelation.getMaterial().getName(), options);
		}
		message.put("options", spawnerOptions);
		logger.infof(
				"Game <%s>: Giving spawner options to <%s>: %s",
				game.getId(),
				user.getUsername(),
				message.toString()
		);
		message(user, message);
		// TODO Store this object somewhere, so requests can be verified.
	}

	/**
	 * @return An integer from [1, 6].
	 */
	public static int roll() {
		return new Random().nextInt(6) + 1;
	}

	private static void getSpawnerRequest(User user) {
		JSONObject message = new JSONObject();
		message.put("type", "end-selection-timer");
		message(user, message);
	}

	public static void collectMaterials(Game game, User user) {
		GameUserRelation gameUserRelation =
				entityProviders()
						.getGameUserProvider()
						.findByGameAndUser(game, user);
		JSONObject message = new JSONObject();
		message.put("type", "material-update");
		// Get spawn numbers
		int die1 = roll();
		int die2 = roll();
		message.put("dice", new JSONArray(Arrays.asList(die1, die2)));
		// Get materials
		List<Material> allMaterials = repositories().getMaterialRepository().findAll();
		Map<String, Integer> collectedMaterials = new HashMap<>();
		// Start by giving 0 of each material
		for (Material material : allMaterials) {
			collectedMaterials.put(material.getName(), 0);
		}
		// For each spawner that matches the number rolled,
		for (MaterialSpawner spawner : repositories()
				.getMaterialSpawnerRepository()
				.findByGameUserRelationAndSpawnNumber(gameUserRelation, die1 + die2)) {
			String materialName = spawner.getMaterialName();
			// Increment the amount gathered for that material.
			collectedMaterials.put(
					materialName,
					collectedMaterials.get(materialName) + 1
			);
		}
		// For each material,
		for (Material material : allMaterials) {
			// Get the relation to that material.
			GameUserMaterialRelation gameUserMaterialRelation =
					entityProviders()
							.getGameUserMaterialProvider()
							.findByGameAndUserAndMaterial(game, user, material);
			// Add the collected amount of the material to that relation.
			gameUserMaterialRelation.add(collectedMaterials.get(material.getName()));
			// Save that relation.
			repositories().getGameUserMaterialRepository().save(gameUserMaterialRelation);
		}
		// Add resources gathered from built structures
		addStructureBonus(gameUserRelation, collectedMaterials, "mine", "stone");
		addStructureBonus(gameUserRelation, collectedMaterials, "lumberyard", "wood");
		addStructureBonus(gameUserRelation, collectedMaterials, "garden", "food");
		addStructureBonus(gameUserRelation, collectedMaterials, "well", "water");
		// Build the message
		message.put("materials", new JSONObject(collectedMaterials));
		logger.infof("Game <%s>: <%s> collected %s", game.getId(), user.getUsername(), collectedMaterials);
		message(user, message);
	}

	public static void broadcast(Game game, Object message) {
		for (GameUserRelation gameUserRelation : game.getUserRelations()) {
			message(gameUserRelation.getUser(), message);
		}
	}

	/**
	 * Add material bonus for structures.
	 * @param gameUserRelation The GameUserRelation to which this bonus is added.
	 * @param structureName The name of the Structure that provides the bonus.
	 * @param materialName The name of the Material for which this Structure provides a bonus.
	 */
	private static void addStructureBonus(
			GameUserRelation gameUserRelation,
			Map<String, Integer> collectedMaterials,
			String structureName,
			String materialName
	) {
		GameUserMaterialRelation gameUserMaterialRelation;
		// Get the material relation
		gameUserMaterialRelation =
				entityProviders()
						.getGameUserMaterialProvider()
						.findByGameUserRelationAndMaterial(
								gameUserRelation,
								entityProviders()
										.getMaterialProvider()
										.findByName(materialName));
		// Add a material for each structure
		int bonus = entityProviders()
				.getGameUserStructureProvider()
				.findByGameUserRelationAndStructure(
						gameUserRelation,
						entityProviders()
								.getStructureProvider()
								.findByName(structureName)

				)
				.getAmount();
		gameUserMaterialRelation.add(bonus);
		repositories()
				.getGameUserMaterialRepository()
				.save(gameUserMaterialRelation);
		// Update message
		collectedMaterials.put(materialName, collectedMaterials.get(materialName) + bonus);
		// Log
		logger.debugf(
				"Game <%s>: +%d <%s> to <%s> for <%s>",
				gameUserRelation.getGameId(),
				bonus,
				materialName,
				gameUserRelation.getUser().getUsername(),
				structureName
		);
	}

	public static class CollectionTimerTask extends TimerTask {
		Game game;
		User user;

		private CollectionTimerTask(Game game, User user) {
			this.game = game;
			this.user = user;
		}

		@Override
		public void run() {
			collectMaterials(game, user);
		}

		/**
		 * Schedule the collection task for the game and user.
		 * @param game
		 * @param user
		 */
		public static void add(Game game, User user) {
			timerTasks.put(user.getId(), new CollectionTimerTask(game, user));
			timer.schedule(timerTasks.get(user.getId()), 0, 3000);
		}

		/**
		 * Cancel the collection tasks for the Game and remove them from the map.
		 * @param game
		 */
		public static void remove(Game game) {
			for (GameUserRelation gameUserRelation : game.getUserRelations()) {
				TimerTask task = timerTasks.get(gameUserRelation.getUserId());
				if (task != null) {
					task.cancel();
					timerTasks.remove(gameUserRelation.getGameId());
				}
			}
		}
	}
}
