package com.example.frontend;

import static com.example.frontend.SupportingClasses.Constants.URL;
import static com.example.frontend.SupportingClasses.Constants.WSURL;
import static com.example.frontend.SupportingClasses.Constants.tag_json_obj;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.frontend.SupportingClasses.AppController;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

public class Lobby extends AppCompatActivity {
    String authToken = LoginScreen.getAuthToken();
    String lobbyCode;
    private WebSocketClient lobbyWebSocket;

    private long pressedTime;

    Draft[] drafts = {
            new Draft_6455()
    };

    public void holdResponse() {
        TextView lobbyCodeText = findViewById(R.id.join_game_lobby_code_textview);
        if (lobbyCode != null) {
            lobbyCodeText.setText(lobbyCode);
        } else {
            lobbyCodeText.setText("error");
        }

        instantiateWebsocket();
        lobbyWebSocket.connect();
    }

    private void instantiateWebsocket() {
        try {
            String endpoint = String.format("%s/lobby/%s/%s", WSURL, lobbyCode, authToken);
            Log.d(Lobby.class.toString(), String.format("Attempting WS connection to %s", endpoint));
            lobbyWebSocket = new WebSocketClient(new URI(endpoint), drafts[0]) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    Log.d("OPEN", "Opening lobby websocket, lc: " + lobbyCode);
                }

                @Override
                public void onMessage(String message) {
                    View myView = findViewById(android.R.id.content).getRootView();
                    myView.postInvalidate();
                    Log.d("Websocket Message: ", message);
                    TextView playerCount = findViewById(R.id.join_game_player_count_textview);
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
                            playerCount.setText(numPlayerString);
                        }

                        if(jsonMessage.getString("type").equals("player-leave")) {
                            int numPlayers = jsonMessage.getInt("num-players");
                            String numPlayerString = "Players: " + numPlayers + "/4";
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

        if(pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            lobbyWebSocket.close();
            startActivity(new Intent(getBaseContext(), JoinGameScreen.class));
            Toast.makeText(getBaseContext(), "Leaving lobby", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }

    JsonObjectRequest hostLobbyRequest = new JsonObjectRequest(
            Request.Method.POST,
            URL + "/lobby/host" + "?auth-token=" + authToken,
            null,
            new Response.Listener<>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        lobbyCode = response.getString("code");
                        Log.d(Lobby.class.toString(), lobbyCode);
                        holdResponse();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
            error -> {
                NetworkResponse response = error.networkResponse;
                if ((error instanceof ServerError || error instanceof NetworkError || error instanceof TimeoutError || error instanceof AuthFailureError || error instanceof ParseError)) {
                    try {
                        String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        JSONObject obj = new JSONObject(res);
                        if (obj.has("message")) {
                            try {
                                Log.d(tag_json_obj, obj.getString("message"));
                            } catch (JSONException e) {
                                Log.e(Lobby.class.toString(), Log.getStackTraceString(e));
                            }
                        }
                    } catch (UnsupportedEncodingException | JSONException e) {
                        Log.e(Lobby.class.toString(), Log.getStackTraceString(e));
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        Bundle bundle = getIntent().getExtras();
        boolean isHost = bundle.getBoolean("isHost");
        lobbyCode = bundle.getString("lobbyCode");

        if(isHost) {
            AppController.getInstance().addToRequestQueue(hostLobbyRequest);
        } else {
            instantiateWebsocket();
            lobbyWebSocket.connect();
        }
    }
}


