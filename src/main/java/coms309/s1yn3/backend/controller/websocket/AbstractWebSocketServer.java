package coms309.s1yn3.backend.controller.websocket;

import coms309.s1yn3.backend.entity.User;
import coms309.s1yn3.backend.service.AbstractEntityManagerService;
import coms309.s1yn3.backend.service.SessionProviderService;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.websocket.Session;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AbstractWebSocketServer extends AbstractEntityManagerService {
	private static final Logger logger = LoggerFactory.logger(AbstractWebSocketServer.class);

	private static final Map<Integer, Session> uidToSession = new HashMap<>();
	private static final Map<Session, Integer> sessionToUid = new HashMap<>();

	private static SessionProviderService sessionProviderService;

	@Autowired
	public void setSessionProviderService(SessionProviderService sessionProviderService) {
		AbstractWebSocketServer.sessionProviderService = sessionProviderService;
	}

	protected static void addSession(User user, Session session) {
		sessionToUid.put(session, user.getId());
		uidToSession.put(user.getId(), session);
	}

	protected static Session getSession(User user) {
		return uidToSession.get(user.getId());
	}

	protected static User getUser(Session session) {
		// TODO add joins
		return repositories().getUserRepository().findById(sessionToUid.get(session)).get();
	}

	protected static void removeSession(User user) {
		sessionToUid.remove(uidToSession.get(user.getId()));
		uidToSession.remove(user.getId());
	}

	protected static SessionProviderService authSessions() {
		return sessionProviderService;
	}

	public static boolean hasUser(User user) {
		return uidToSession.containsKey(user.getId());
	}

	public static void message(User user, Object message) {
		try {
			getSession(user)
					.getBasicRemote()
					.sendText(
							message.toString()
					);
		} catch (IOException | NullPointerException ex) {
			logger.warnf("Failed to message <%s>", user.getUsername());
		}
	}
}
