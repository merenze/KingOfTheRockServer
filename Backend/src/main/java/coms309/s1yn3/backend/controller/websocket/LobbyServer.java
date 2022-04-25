package coms309.s1yn3.backend.controller.websocket;

import coms309.s1yn3.backend.controller.websocket.encoder.GameEncoder;
import coms309.s1yn3.backend.controller.websocket.encoder.LobbyEncoder;
import coms309.s1yn3.backend.entity.Game;
import coms309.s1yn3.backend.entity.Lobby;
import coms309.s1yn3.backend.entity.User;
import coms309.s1yn3.backend.entity.relation.GameUserRelation;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.springframework.stereotype.Component;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Component
@ServerEndpoint(
		value = "/lobby/{lobby-code}/{auth-token}",
		encoders = {
				LobbyEncoder.class,
				GameEncoder.class
		}
)
public class LobbyServer extends AbstractWebSocketServer {
	private final Logger logger = LoggerFactory.logger(LobbyServer.class);
	/**
	 * Map a User ID to a Session.
	 */
	private static final Map<Integer, Session> uidToSession = new HashMap<>();
	/**
	 * Map a Session to a User ID.
	 */
	private static final Map<Session, Integer> sessionToUid = new HashMap<>();

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
		logger.debugf("User <%s> has session: %s", user, uidToSession.containsKey(user));
		logger.debugf("User <%s> lobby in database: %s", user, user.getLobby());
		if (uidToSession.containsKey(user.getId())) {
			logger.warnf("User <%s> attempted lobby connection; already connected", user.getUsername());
			session.getBasicRemote().sendText("You are already connected to a lobby or game.");
			session.close();
			return;
		}
		// Make sure the lobby exists
		Lobby lobby = lobbies().findByCode(lobbyCode);
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
		uidToSession.put(user.getId(), session);
		sessionToUid.put(session, user.getId());
		// Send the User the Lobby info
		session.getBasicRemote().sendObject(lobby);
		logger.infof("%s connected to lobby %s", user.getUsername(), lobby.getCode());
		// Broadcast the join message to the Lobby
		broadcast(lobby, "%s joined the lobby.", user.getUsername());
		// Start the game
		if (lobby.getPlayers().size() >= Game.MAX_PLAYERS) {
			logger.infof("Lobby <%s> is full, starting game", lobby.getCode());
			Game game = startGame(lobby);
			// Join game-user relations
			game = games().findById(game.getId());
			// Send the players the game start message
			for (User player : lobby.getPlayers()) {
				// TODO: This will except; Game needs an encoder.
				Session s = uidToSession.get(player.getId());
				s.getBasicRemote().sendObject(game);
				s.close();
			}
		}
	}

	/**
	 *
	 * @param session
	 * @throws IOException
	 */
	@OnClose
	public void onClose(Session session) throws IOException {
		// Remove the sessions from mapping
		User user;
		try {
			user = repositories().getUserRepository().findById(sessionToUid.get(session)).get();
		} catch (NoSuchElementException ex) {
			// This shouldn't happen!
			int uid = sessionToUid.get(session);
			logger.warnf("Session closed for non-existent user with id <%d>", uid);
			uidToSession.remove(uid);
			sessionToUid.remove(session);
			session.close();
			return;
		}
		sessionToUid.remove(session);
		uidToSession.remove(user.getId());
		// Assigned like this in order to get joins from provider
		Lobby lobby = lobbies().findByCode(user.getLobby().getCode());
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
	 * @param lobby
	 * @param format
	 * @param o
	 */
	private void broadcast(Lobby lobby, String format, Object... o) {
		String message = String.format(format, o);
		logger.infof("Broadcast to <%s>: %s", lobby.getCode(), message);
		for (User player : lobby.getPlayers()) {
			try {
				uidToSession.get(player.getId()).getBasicRemote().sendText(message);
			} catch (NullPointerException ex) {
				logger.warnf("<%s> has lobby <%s> in database but no active session", player.getUsername(), lobby.getCode());
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Start a game for the players in a Lobby.
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
