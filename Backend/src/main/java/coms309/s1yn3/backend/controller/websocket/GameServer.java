package coms309.s1yn3.backend.controller.websocket;

import org.springframework.stereotype.Component;

import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@Component
@ServerEndpoint("/game/{game-id}/{auth-token}")
public class GameServer extends AbstractWebSocketServer {
	@OnOpen
	public void onOpen(
			Session session,
			@PathParam("game-id") int gameId,
			@PathParam("auth-token") String authToken) {

	}
}
