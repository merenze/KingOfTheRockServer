package com.example.frontend;

import static com.example.frontend.SupportingClasses.Constants.WSURL;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;

public class GameWebsocket {
    private WebSocketClient lobbyWebSocket;

    Draft[] drafts = {
            new Draft_6455()
    };

    public GameWebsocket(String lobbyCode, String authToken) {

    }

//    private void instantiateWebsocket() {
//        String endpoint = String.format("%s/lobby/%s/%s", WSURL, lobbyCode, authToken);
//    }
}
