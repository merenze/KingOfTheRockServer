package coms309.s1yn3.backend.controller.websocket;

import coms309.s1yn3.backend.entity.User;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.springframework.stereotype.Component;

import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

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
	}
}
