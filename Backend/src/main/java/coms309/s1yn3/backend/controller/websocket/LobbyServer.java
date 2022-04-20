package coms309.s1yn3.backend.controller.websocket;

import coms309.s1yn3.backend.entity.Lobby;
import coms309.s1yn3.backend.entity.User;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.jandex.Index;
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
@ServerEndpoint("/lobby/{lobby-code}/{auth-token}")
public class LobbyServer extends AbstractWebSocketServer {
	private final Logger logger = LoggerFactory.logger(LobbyServer.class);
	private static Map<User, Session> sessions = new HashMap<>();

	/**
	 *
	 * @param session WebSocket connection.
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
		Lobby lobby;
		try {
			lobby = repositories().getLobbyRepository().findByCode(lobbyCode).get(0);
		} catch (IndexOutOfBoundsException ex) {
			logger.warnf("User <%s> attempted connection to nonexistent lobby <%s>", user, lobbyCode);
			session.getBasicRemote().sendText(String.format("No lobby with code '%s'", lobbyCode));
			session.close();
			return;
		}
		// Update the User's Lobby
		user.setLobby(lobby);
		repositories().getUserRepository().saveAndFlush(user);
		logger.info(String.format("%s connected to lobby %s", user.getUsername(), lobby.getCode()));
		for(User player : lobby.getPlayers()) {
			sessions.get(user).getBasicRemote().sendText(String.format("%s joined the lobby.", user.getUsername()));
		}
		sessions.put(user, session);
		session.getBasicRemote().sendObject(lobby);
		// TODO start game
	}

	@OnClose
	public void onClose(
			@PathParam("lobby-code") String lobbyCode,
			@PathParam("auth-token") String authToken) {
		User user = authSessions().getUser(authToken);
		if (user == null) {
			logger.warnf("Failed to resolve auth token <%s> for lobby disconnect", authToken);
			return;
		}
		Lobby lobby = repositories().getLobbyRepository().findByCode(lobbyCode).get(0);
		if (lobby == null) {
			logger.warnf("User <%s> attempted disconnect from nonexistent lobby endpoint <%s>");
			return;
		}
		logger.info(String.format("%s disconnected from lobby %s", user.getUsername(), lobby.getCode()));
		// TODO broadcast leave to other players
	}
}
