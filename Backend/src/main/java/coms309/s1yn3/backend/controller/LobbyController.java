package coms309.s1yn3.backend.controller;


import coms309.s1yn3.backend.entity.Lobby;
import coms309.s1yn3.backend.entity.User;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Random;

@RestController
public class LobbyController extends AbstractController {
	Logger logger = LoggerFactory.logger(LobbyController.class);

	@PostMapping("/lobby/join")
	public @ResponseBody ResponseEntity join(HttpServletRequest request, @RequestParam(name = "code", required = false) String code) {
		User user = sender(request);
		// If no code provided, do quick join.
		if (code == null) {
			// TODO Quick join
			logger.warnf("User <%s> attempted lobby quick join (not implemented)", user.getUsername());
			return new ResponseEntity(HttpStatus.OK);
		}
		// If code provided, get the Lobby with the given code.
		Lobby lobby;
		try {
			lobby = repositories().getLobbyRepository().findByCode(code).get(0);
		} catch (NullPointerException ex) {
			logger.warnf("User <%s> attempted direct join to nonexistent lobby <%s>", user.getUsername(), code);
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		// Add the requesting user to the Lobby.
		logger.infof("User <%s> direct joined lobby <%s>", user.getUsername(), code);
		logger.debugf("Before <%s> join: %s", user.getUsername(), lobby);
		lobby.addPlayer(user);
		repositories().getLobbyRepository().saveAndFlush(lobby);
		repositories().getUserRepository().saveAndFlush(user);
		logger.debugf("After <%s> join: %s", user.getUsername(), lobby);
		return new ResponseEntity(HttpStatus.OK);
	}

	@PostMapping("/lobby/host")
	public @ResponseBody ResponseEntity host(HttpServletRequest request) {
		// Grab a ref to the requesting User.
		User user = sender(request);
		// If user is in a lobby, deny the request.
		if (user.getLobby() != null) {
			HashMap<String, String> responseBody = new HashMap<>();
			responseBody.put("message", "User is already connected to a lobby.");
			logger.warnf("User <%s> attempted to host lobby (already connected to <%s>)", user.getUsername(), user.getLobby().getCode());
			return new ResponseEntity(responseBody, HttpStatus.FORBIDDEN);
		}
		// Create a new Lobby hosted by the requesting User.
		Lobby lobby = new Lobby(generateLobbyCode());
		lobby.addPlayer(user);
		lobby.setHost(user);
		repositories().getLobbyRepository().saveAndFlush(lobby);
		repositories().getUserRepository().saveAndFlush(user);
		logger.infof("User <%s> now hosting lobby <%s>", user.getUsername(), lobby.getCode());
		logger.debugf("New lobby created: %s", lobby);
		// Prepare and return the success response.
		HashMap<String, String> responseBody = new HashMap<>();
		responseBody.put("code", lobby.getCode());
		return new ResponseEntity(responseBody, HttpStatus.OK);
	}

	/**
	 * Request to disconnect from a lobby.
	 */
	@PostMapping("/lobby/disconnect")
	public @ResponseBody ResponseEntity disconnect(HttpServletRequest request) {
		User user = sender(request);
		Lobby lobby = user.getLobby();
		if (lobby == null) {
			HashMap<String, String> responseBody = new HashMap<>();
			responseBody.put("message", "User is not connected to a lobby.");
			logger.warnf("User <%s> attempted disconnect (not connected)", user.getUsername());
			return new ResponseEntity(responseBody, HttpStatus.OK);
		}
		// Remove the user from the lobby.
		lobby.removePlayer(user);
		logger.infof("User <%s> disconnected from lobby <%s>", user.getUsername(), lobby.getCode());
		logger.debugf("Before <%s> disconnect: %s", user, lobby);
		if (lobby.getHost() == user) {
			logger.warnf("User <%s> was hosting lobby <%s>; removing host", user.getUsername(), lobby.getCode());
			lobby.setHost(null);
		}
		repositories().getLobbyRepository().saveAndFlush(lobby);
		repositories().getUserRepository().saveAndFlush(user);
		logger.debugf("After <%s> disconnect: %s", user, lobby);
		// If the lobby is now empty, destroy it.
		if (lobby.getPlayers().size() <= 0) {
			logger.warnf("Lobby <%s> is now empty; destroying.", lobby.getCode());
			repositories().getLobbyRepository().delete(lobby);
			logger.warnf("Lobby <%s> destroyed.", lobby.getCode());
		}
		return new ResponseEntity(HttpStatus.OK);
	}

	/**
	 * Generate a code of length CODE_LENGTH,
	 * using the chars present in CODE_CHARS.
	 * There are (length of CODE_CHARS)^CODE_LENGTH possible codes,
	 * so collision should be unlikely, but is still handled.
	 * <p>
	 * TODO This shouldn't really be in the Controller-- better in a factory.
	 */
	private String generateLobbyCode() {
		// Don't allow if code already exists.
		String code = "";
		// Generate a random code.
		Random random = new Random();
		for (int i = 0; i < Lobby.CODE_LENGTH; i++) {
			code += Lobby.CODE_CHARS.charAt(random.nextInt(Lobby.CODE_CHARS.length()));
		}
		// Iterate over existing Lobby set.
		for (Lobby lobby : repositories().getLobbyRepository().findAll()) {
			// If the newly generate code is already in use,
			if (code.equals(lobby.getCode())) {
				// Throw it out and generate a new one.
				code = generateLobbyCode();
				break;
			}
		}
		// Give the result.
		return code;
	}
}
