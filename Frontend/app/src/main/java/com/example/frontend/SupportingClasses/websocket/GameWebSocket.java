package com.example.frontend.SupportingClasses.websocket;

import android.util.Log;

import com.example.frontend.Entities.User;
import com.example.frontend.SupportingClasses.Constants;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

public class GameWebSocket {
    private static final String TAG = GameWebSocket.class.toString();
    private static final Draft DRAFT = new Draft_6455();

    private int gameId;
    private User user;
    private WebSocketClient client;


    public GameWebSocket(User user, int gameId) throws URISyntaxException {
        this.user = user;
        this.gameId = gameId;
    }

    public void open() throws URISyntaxException {
        Log.i(TAG, "Attempting WebSocket connection to " + url());
        client = new WebSocketClient(new URI(url()), DRAFT) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                Log.i(TAG, "Connected");
            }

            @Override
            public void onMessage(String message) {
                Log.d(TAG, "Received " + message);
                try {
                    JSONObject body = new JSONObject(message);
                    String type = body.getString("type");
                    if (type.equals("start-selection-timer")) {
                        // TODO start selection logic
                        return;
                    }
                    if (type.equals("end-selection-timer")) {
                        // TODO confirm selection logic
                        return;
                    }
                    if (type.equals("material-update")) {
                        // TODO notify user of collected materials
                        return;
                    }
                    if (type.equals("material-wants")) {
                        // TODO notify user of wanted items
                        return;
                    }
                    if (type.equals("trade-request")) {
                        // TODO notify user of trade request and allow them to accept
                        return;
                    }
                    if (type.equals("trade-accept")) {
                        // TODO give the user the trade screen
                        return;
                    }
                    if (type.equals("trade-decline")) {
                        // TODO notify user that a trade they offered has been declined
                        return;
                    }
                    if (type.equals("trade-withdraw")) {
                        // TODO notify user that a trade they were offered has been withdrawn
                        return;
                    }
                    if (type.equals("trade-update")) {
                        // TODO show the user the updated trade offer and require them to reconfirm the trade
                        return;
                    }
                    if (type.equals("trade-confirm")) {
                        // TODO notify the user the other player has confirmed the trade.
                        return;
                    }
                    if (type.equals("game-over")) {
                        // TODO endgame logic
                        return;
                    }
                } catch (JSONException e) {
                    Log.d(TAG, Log.getStackTraceString(e));
                }
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                // TODO redirect user to main menu
            }

            @Override
            public void onError(Exception e) {
                // TODO notify the user there has been an error.
                Log.e(TAG, Log.getStackTraceString(e));
            }
        };
    }

    public void close() {
        Log.i(TAG, "Connection to %s closed by client.");
        client.close();
    }

    private String url() throws URISyntaxException {
        return String.format("%s/game/%s/%s", Constants.URL, gameId, user.getAuthToken());
    }
}
