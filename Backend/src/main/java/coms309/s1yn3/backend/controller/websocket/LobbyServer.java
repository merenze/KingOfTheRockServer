package coms309.s1yn3.backend.controller.websocket;

import coms309.s1yn3.backend.controller.websocket.encoder.GameEncoder;
import coms309.s1yn3.backend.controller.websocket.encoder.LobbyEncoder;
import coms309.s1yn3.backend.entity.Game;
import coms309.s1yn3.backend.entity.Lobby;
import coms309.s1yn3.backend.entity.User;
import coms309.s1yn3.backend.entity.relation.GameUserRelation;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@Component
@ServerEndpoint("/lobby/{lobby-code}/{auth-token}")
public class LobbyServer extends AbstractWebSocketServer {
	private static final Logger logger = LoggerFactory.logger(LobbyServer.class);

	/**
	 * @param session   WebSocket connection.
	 * @param lobbyCode Code for the destination Lobby.
	 * @param authToken Auth token for the connecting User.
	 */
	@OnOpen
	public void onOpen(
			Session session,
			@PathParam("lobby-code") String lobbyCode,
			@PathParam("auth-token") String authToken)
			throws IOException, EncodeException {
		logger.infof("Web Socket connection opened at /lobby/%s/%s", lobbyCode, authToken);
		// Get the connecting User
		User user = authSessions().getUser(authToken);
		if (user == null) {
			logger.warnf("Failed to resolve auth token <%s> for lobby join", authToken);
			session.close();
			return;
		}
		// Make sure the User is not already connected to a lobby
		if (getSession(user) != null) {
			logger.warnf("User <%s> attempted lobby connection; already connected", user.getUsername());
			session.getBasicRemote().sendText("You are already connected to a lobby or game.");
			session.close();
			return;
		}
		// Make sure the lobby exists
		Lobby lobby = entityProviders().getLobbyProvider().findByCode(lobbyCode);
		if (lobby == null) {
			logger.infof("User <%s> attempted connection to nonexistent lobby <%s>", user, lobbyCode);
			session.getBasicRemote().sendText(String.format("No lobby with code '%s'", lobbyCode));
			session.close();
			return;
		}
		// Make sure the lobby is not full
		if (lobby.getPlayers().size() >= Game.MAX_PLAYERS) {
			logger.infof("User <%s> attempted connection to full lobby <%s>", user, lobby.getCode());
			session.getBasicRemote().sendText("That lobby is already full.");
			session.close();
			return;
		}
		// Update the User's Lobby
		lobby.addPlayer(user);
		repositories().getUserRepository().saveAndFlush(user);
		// Store the User's WebSocket connection
		addSession(user, session);
		// Send the User the Lobby info
		JSONObject lobbyMessage = new JSONObject();
		lobbyMessage.put("type", "lobby");
		lobbyMessage.put("lobby", new JSONObject(new LobbyEncoder().encode(lobby)));
		session.getBasicRemote().sendText(lobbyMessage.toString());
		logger.infof("%s connected to lobby %s", user.getUsername(), lobby.getCode());
		// Broadcast the join message to the Lobby
		broadcast(lobby, "%s joined the lobby.", user.getUsername());
		// Start the game
		if (lobby.getPlayers().size() >= Game.MAX_PLAYERS) {
			logger.infof("Lobby <%s> is full, starting game", lobby.getCode());
			Game game = startGame(lobby);
			// Join game-user relations
			game = entityProviders().getGameProvider().findById(game.getId());
			// Build the message
			JSONObject message = new JSONObject();
			message.put("type", "start-game");
			message.put("game", new JSONObject(new GameEncoder().encode(game)));
			// Send the players the game start message
			for (User player : lobby.getPlayers()) {
				Session s = getSession(player);
				s.getBasicRemote().sendText(message.toString());
				s.close();
			}
		}
	}

	/**
	 * @param session
	 * @throws IOException
	 */
	@OnClose
	public void onClose(Session session) throws IOException {
		// Remove the sessions from mapping
		User user = getUser(session);
		removeSession(user);
		// Assigned like this in order to get joins from provider
		Lobby lobby = entityProviders().getLobbyProvider().findByCode(user.getLobby().getCode());
		// Disconnect the User
		lobby.removePlayer(user);
		repositories().getUserRepository().saveAndFlush(user);
		logger.infof("%s disconnected from lobby <%s>", user.getUsername(), lobby.getCode());
		// Destroy an empty lobby
		if (lobby.getPlayers().size() <= 0) {
			logger.infof("Lobby <%s> is now empty, destroying.", lobby.getCode());
			repositories().getLobbyRepository().delete(lobby);
			logger.infof("Lobby <%s> destroyed.", lobby.getCode());
		}
		// Broadcast disconnect to remaining players
		else {
			broadcast(lobby, "%s disconnected from the lobby.", user.getUsername());
		}
	}

	/**
	 * Broadcast a message to all Users in a Lobby.
	 *
	 * @param lobby
	 * @param format
	 * @param o
	 */
	public static void broadcast(Lobby lobby, String format, Object... o) {
		String message = String.format(format, o);
		logger.infof("Broadcast to <%s>: %s", lobby.getCode(), message);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("type", "player-message");
		jsonObject.put("message", message);
		for (User player : lobby.getPlayers()) {
			try {
				getSession(player).getBasicRemote().sendText(jsonObject.toString());
			} catch (NullPointerException ex) {
				logger.warnf("<%s> has lobby <%s> in database but no active session", player.getUsername(), lobby.getCode());
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Start a game for the players in a Lobby.
	 *
	 * @param lobby
	 */
	private Game startGame(Lobby lobby) {
		broadcast(lobby, "Starting game");
		// Saving before instantiating so we can generate the ID
		Game game = repositories().getGameRepository().saveAndFlush(new Game());
		for (User player : lobby.getPlayers()) {
			GameUserRelation gameUserRelation = new GameUserRelation(game, player);
			repositories().getGameUserRepository().save(gameUserRelation);
			logger.debugf("Added relation between user <%s> and game <%d>", player.getUsername(), game.getId());
		}
		return game;
	}
}
