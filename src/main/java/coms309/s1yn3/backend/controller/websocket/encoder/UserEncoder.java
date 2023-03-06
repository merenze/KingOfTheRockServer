package coms309.s1yn3.backend.controller.websocket.encoder;

import coms309.s1yn3.backend.entity.User;
import org.json.JSONObject;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class UserEncoder implements Encoder.Text<User> {
	@Override public String encode(User user) throws EncodeException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("username", user.getUsername());

		return jsonObject.toString();
	}

	@Override public void init(EndpointConfig endpointConfig) {

	}

	@Override public void destroy() {

	}
}
