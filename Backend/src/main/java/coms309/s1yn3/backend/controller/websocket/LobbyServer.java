package coms309.s1yn3.backend.controller.websocket;

import coms309.s1yn3.backend.entity.Lobby;
import coms309.s1yn3.backend.entity.User;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.springframework.web.bind.annotation.PathVariable;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/lobby/{lobby-code}/{auth-token}")
public class LobbyServer extends AbstractWebSocketServer {
	Logger logger = LoggerFactory.logger(LobbyServer.class);

	@OnOpen
	public void onOpen(
			@PathVariable("lobby-code") String lobbyCode,
			@PathVariable("auth-token") String authToken) {
		Lobby lobby = repositories().getLobbyRepository().findByCode(lobbyCode).get(0);
		User user = sessions().getUser(authToken);
		logger.info(String.format("%s connected to lobby %s", user.getUsername(), lobby.getCode()));
		// TODO broadcast join to other players
		// TODO start game
	}

	@OnClose
	public void onClose(
			@PathVariable("lobby-code") String lobbyCode,
			@PathVariable("auth-token") String authToken) {
		Lobby lobby = repositories().getLobbyRepository().findByCode(lobbyCode).get(0);
		User user = sessions().getUser(authToken);
		logger.info(String.format("%s disconnected from lobby %s", user.getUsername(), lobby.getCode()));
		// TODO broadcast leave to other players
	}
}
