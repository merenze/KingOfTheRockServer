package coms309.s1yn3.backend.controller.websocket;

import coms309.s1yn3.backend.entity.Lobby;
import coms309.s1yn3.backend.entity.User;
import coms309.s1yn3.backend.service.RepositoryProviderService;
import coms309.s1yn3.backend.service.SessionProviderService;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@Controller
@ServerEndpoint("/lobby/{lobby-code}/{auth-token}")
public class LobbyServer extends AbstractWebSocketServer {
	Logger logger = LoggerFactory.logger(LobbyServer.class);

	@OnOpen
	public void onOpen(
			@PathParam("lobby-code") String lobbyCode,
			@PathParam("auth-token") String authToken) {
		User user = sessions().getUser(authToken);
		if (user == null) {
			logger.warnf("Failed to resolve auth token <%s> for lobby join", authToken);
			return;
		}
		Lobby lobby = repositories().getLobbyRepository().findByCode(lobbyCode).get(0);
		if (lobby == null) {
			logger.warnf("User <%s> attempted connection to nonexistent lobby <%s>", user, lobbyCode);
			return;
		}
		logger.info(String.format("%s connected to lobby %s", user.getUsername(), lobby.getCode()));
		// TODO broadcast join to other players
		// TODO start game
	}

	@OnClose
	public void onClose(
			@PathParam("lobby-code") String lobbyCode,
			@PathParam("auth-token") String authToken) {
		User user = sessions().getUser(authToken);
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
