package com.example.frontend;

import static com.example.frontend.SupportingClasses.Constants.WSURL;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

public class GuestLobby extends AppCompatActivity {
    String authToken = LoginScreen.getAuthToken();
    String lobbyCode;
    private WebSocketClient lobbyWebSocket;

    Draft[] drafts;

    {
        drafts = new Draft[]{
                new Draft_6455()
        };
    }

    /**
     * Creates a lobby websocket connection and handles all messages
     */
    private void instantiateWebsocket() {
        try {
            String endpoint = String.format("%s/lobby/%s/%s", WSURL, lobbyCode, authToken);
            Log.d(GameLobby.class.toString(), String.format("Attempting WS connection to %s", endpoint));
            lobbyWebSocket = new WebSocketClient(new URI(endpoint), drafts[0]) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    Log.d("OPEN", "Opening guests websocket, lc: " + lobbyCode);
                }

                @Override
                public void onMessage(String message) {
                    View myView = findViewById(android.R.id.content).getRootView();
                    myView.postInvalidate();
                    Log.d("Websocket Message: ", message);
                    try {
                        JSONObject jsonMessage = new JSONObject(message);

                        if(jsonMessage.getString("type").equals("lobby")) {
                            String lobbyCodeString = jsonMessage.getJSONObject("lobby").getString("code");
                            TextView lobbyCode = findViewById(R.id.join_game_lobby_code_textview);
                            lobbyCode.setText(lobbyCodeString);
                        }

                        if (jsonMessage.getString("type").equals("player-join")) {
                            int numPlayers = jsonMessage.getInt("num-players");
                            String numPlayerString = "Players: " + numPlayers + "/4";
                            TextView playerCount = findViewById(R.id.join_game_player_count_textview);
                            playerCount.setText(numPlayerString);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    Log.d("CLOSE", "onClose() returned: " + reason);
                }

                @Override
                public void onError(Exception ex) {
                    Log.d("Exception:", ex.toString());
                }
            };
        } catch (URISyntaxException uriSyntaxException) {
            uriSyntaxException.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getBaseContext(), "pls no", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_game_lobby);

        Bundle bundle = getIntent().getExtras();

        lobbyCode = bundle.getString("lobbyCode");
        Log.d("lobby code: ", lobbyCode);

        instantiateWebsocket();
        lobbyWebSocket.connect();
    }
}
