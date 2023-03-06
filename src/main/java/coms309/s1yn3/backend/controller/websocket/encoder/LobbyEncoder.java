package coms309.s1yn3.backend.controller.websocket.encoder;

import coms309.s1yn3.backend.entity.Lobby;
import coms309.s1yn3.backend.entity.User;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class LobbyEncoder implements Encoder.Text<Lobby> {
	@Override
	public String encode(Lobby lobby) throws EncodeException {
		JSONObject jsonObject = new JSONObject();
		// Raw fields
		jsonObject.put("code", lobby.getCode());
		jsonObject.put("host", lobby.getHost());
		// Player list
		JSONArray players = new JSONArray();
		for (User player : lobby.getPlayers()) {
			players.put(new JSONObject(new UserEncoder().encode(player)));
		}
		jsonObject.put("players", players);
		// Result
		return jsonObject.toString();
	}

	@Override
	public void init(EndpointConfig endpointConfig) {

	}

	@Override
	public void destroy() {

	}
}
