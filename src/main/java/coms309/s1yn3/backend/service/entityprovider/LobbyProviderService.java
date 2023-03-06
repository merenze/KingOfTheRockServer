package coms309.s1yn3.backend.service.entityprovider;

import coms309.s1yn3.backend.entity.Lobby;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LobbyProviderService extends AbstractEntityProviderService {

	public Lobby findByCode(String code) {
		Lobby lobby;
		try {
			lobby = repositories().getLobbyRepository().findByCode(code).get(0);
		} catch (IndexOutOfBoundsException ex) {
			return null;
		}
		join(lobby);
		return lobby;
	}

	private void join(Lobby lobby) {
		lobby.setPlayers(repositories().getUserRepository().findByLobby(lobby));
	}
}
