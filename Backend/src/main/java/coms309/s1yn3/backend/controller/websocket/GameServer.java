package coms309.s1yn3.backend.controller.websocket;

import coms309.s1yn3.backend.entity.Game;
import coms309.s1yn3.backend.entity.Material;
import coms309.s1yn3.backend.entity.MaterialSpawner;
import coms309.s1yn3.backend.entity.User;
import coms309.s1yn3.backend.entity.relation.GameUserMaterialRelation;
import coms309.s1yn3.backend.entity.relation.GameUserRelation;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

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
		// TODO disconnect user from previous sessions
		addSession(user, session);
		logger.infof("User <%s> connected to game <%s>", user.getUsername(), game.getId());
		initializeMaterials(game, user);
		offerSpawnerOptions(game, user);

		Timer timer = new Timer();
		// Get spawner request after thirty seconds
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				getSpawnerRequest(user);
			}
		}, 30000);
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
					"Game <%s>: <%s> already assigned initial materials",
					game.getId(),
					user.getUsername()
			);
			return;
		}
		logger.infof(
				"Game <%s>: Assigning materials to <%s>",
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
		gameUserRelation.setHasInitialMaterials(true);
		repositories().getGameUserRepository().save(gameUserRelation);
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
		int die1 = roll();
		int die2 = roll();
		message.put("dice", new JSONArray(Arrays.asList(die1, die2)));
		Map<Material, Integer> gatheredMaterials = new HashMap<>();
		// Start by giving 0 of each material
		for (Material material : repositories().getMaterialRepository().findAll()) {
			gatheredMaterials.put(material, 0);
		}
		// For each spawner that matches the number rolled,
		for (MaterialSpawner spawner : repositories()
				.getMaterialSpawnerRepository()
				.findByGameUserRelationAndSpawnNumber(gameUserRelation, die1 + die2)) {
			Material material = spawner.getMaterial();
			// Increment the amount gathered for that material.
			gatheredMaterials.put(
					material,
					gatheredMaterials.get(material) + 1
			);
		}
		// TODO Add resources gathered from built structures
		message.put("materials", new JSONObject(gatheredMaterials));
		logger.infof("Game <%s>: <%s> collected %s", game.getId(), user.getUsername(), gatheredMaterials);
		message(user, message);
	}

	public static void broadcast(Game game, Object message) {
		for (GameUserRelation gameUserRelation : game.getUserRelations()) {
			message(gameUserRelation.getUser(), message);
		}
	}
}
