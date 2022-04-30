package coms309.s1yn3.backend.controller.websocket;

import coms309.s1yn3.backend.entity.Game;
import coms309.s1yn3.backend.entity.Material;
import coms309.s1yn3.backend.entity.User;
import coms309.s1yn3.backend.entity.relation.GameUserMaterialRelation;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.springframework.stereotype.Component;

import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Random;

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
		logger.infof("User <%s> connected to game <%s>", user.getUsername(), game.getId());
		initializeMaterials(game, user);
	}

	/**
	 * Set the User's initial materials for the Game.
	 *
	 * @param game
	 * @param user
	 */
	private static void initializeMaterials(Game game, User user) {
		logger.infof("Game <%s>: Assigning materials to <%s>", game.getId(), user.getUsername());
		List<Material> materials = entityProviders().getMaterialProvider().findAll();
		// Give the user a relation to every material
		for (Material material : materials) {
			entityProviders()
					.getGameUserProvider()
					.findByGameAndUser(game, user)
					.addMaterialRelation(
							repositories()
									.getGameUserMaterialRepository()
									.save(
											new GameUserMaterialRelation(
													entityProviders()
															.getGameUserProvider()
															.findByGameAndUser(game, user),
													material
											)
									))
			;
		}
		Random random = new Random();
		// Give player four random resources
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
	}
}
