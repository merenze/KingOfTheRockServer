package coms309.s1yn3.backend.controller.websocket;

import coms309.s1yn3.backend.controller.websocket.encoder.LobbyEncoder;
import coms309.s1yn3.backend.entity.Lobby;
import coms309.s1yn3.backend.entity.User;
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

@Component
@ServerEndpoint(
		value = "/lobby/{lobby-code}/{auth-token}",
		encoders = {
				LobbyEncoder.class
		}
)
public class LobbyServer extends AbstractWebSocketServer {
	private final Logger logger = LoggerFactory.logger(LobbyServer.class);
	private static Map<User, Session> sessions = new HashMap<>();
	private static Map<Session, User> users = new HashMap<>();

	/**
	 * @param session   WebSocket connection.
	 * @param lobbyCode Code for the destination Lobby.
	 * @param authToken Auth token for the connecting User.
	 * @throws IOException
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
		logger.debugf("User <%s> has session: %s", user, sessions.containsKey(user));
		logger.debugf("User <%s> lobby in database: %s", user, user.getLobby());
		if (sessions.containsKey(user)) {
			logger.warnf("User <%s> attempted lobby connection; already connected", user.getUsername());
			session.getBasicRemote().sendText("You are already connected to a lobby or game.");
			session.close();
			return;
		}
		// Make sure the lobby exists
		Lobby lobby = lobbies().findByCode(lobbyCode);
		if (lobby == null) {
			logger.warnf("User <%s> attempted connection to nonexistent lobby <%s>", user, lobbyCode);
			session.getBasicRemote().sendText(String.format("No lobby with code '%s'", lobbyCode));
			session.close();
			return;
		}
		// Update the User's Lobby
		lobby.addPlayer(user);
		repositories().getUserRepository().saveAndFlush(user);
		// Store the User's WebSocket connection
		sessions.put(user, session);
		users.put(session, user);
		// Send the User the Lobby info
		session.getBasicRemote().sendObject(lobby);
		logger.info(String.format("%s connected to lobby %s", user.getUsername(), lobby.getCode()));
		// Broadcast the join message to the Lobby
		broadcast(lobby, "%s joined the lobby.", user.getUsername());
		// TODO start game
	}

	/**
	 *
	 * @param session
	 * @throws IOException
	 */
	@OnClose
	public void onClose(Session session) throws IOException {
		// Remove the sessions from mapping
		User user = users.get(session);
		users.remove(session);
		sessions.remove(user);
		// Assigned like this in order to get joins from provider
		Lobby lobby = lobbies().findByCode(user.getLobby().getCode());
		// Disconnect the User
		lobby.removePlayer(user);
		repositories().getUserRepository().saveAndFlush(user);
		logger.info(String.format("%s disconnected from lobby <%s>", user.getUsername(), lobby.getCode()));
		// Destroy an empty lobby
		if (lobby.getPlayers().size() <= 0) {
			logger.info("Lobby <%s> is now empty, destroying.");
			repositories().getLobbyRepository().delete(lobby);
			logger.info("Lobby <%s> destroyed.");
		}
		// Broadcast disconnect to remaining players
		broadcast(lobby, "%s disconnected from the lobby.", user.getUsername());
	}

	/**
	 * Broadcast a message to all Users in a Lobby.
	 * @param lobby
	 * @param format
	 * @param o
	 * @throws IOException
	 */
	private void broadcast(Lobby lobby, String format, Object... o) throws IOException {
		for (User player : lobby.getPlayers()) {
			try {
				sessions.get(player).getBasicRemote().sendText(String.format(format, o));
			} catch (NullPointerException ex) {
				logger.warnf("In Broadcast: <%s> has lobby <%s> in database but no active session", player.getUsername(), lobby.getCode());
			}
		}
	}
}
