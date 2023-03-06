package coms309.s1yn3.backend.config;

import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class WebSocketConfig {
	Logger logger = LoggerFactory.logger(WebSocketConfig.class);

	@Bean
	public ServerEndpointExporter serverEndpointExporter() {
		logger.debugf("Configuring WebSocket endpoint");
		return new ServerEndpointExporter();
	}
}
