package coms309.s1yn3.backend.controller.websocket.encoder;

import coms309.s1yn3.backend.entity.Game;
import coms309.s1yn3.backend.entity.User;
import coms309.s1yn3.backend.entity.relation.GameUserRelation;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class GameEncoder implements Encoder.Text<Game> {
	@Override
	public String encode(Game game) throws EncodeException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", game.getId());
		JSONArray jsonArray = new JSONArray();
		for (GameUserRelation userRelation : game.getUserRelations()) {
			jsonArray.put(new JSONObject(new UserEncoder().encode(userRelation.getUser())));
		}
		jsonObject.put("players", jsonArray);
		return jsonObject.toString();
	}

	@Override
	public void init(EndpointConfig endpointConfig) {

	}

	@Override
	public void destroy() {

	}
}
