package com.example.frontend;

import static com.example.frontend.SupportingClasses.Constants.WSURL;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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

    Draft[] drafts = {
            new Draft_6455()
    };
    private void instantiateWebsocket() {
        try {
            String endpoint = String.format("%s/lobby/%s/%s", WSURL, lobbyCode, authToken);
            Log.d(GameLobby.class.toString(), String.format("Attempting WS connection to %s", endpoint));
            lobbyWebSocket = new WebSocketClient(new URI(endpoint), (Draft) drafts[0]) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    Log.d("OPEN", "Opening guests websocket, lc: " + lobbyCode);
                }

                @Override
                public void onMessage(String message) {
                    View myView = findViewById(android.R.id.content).getRootView();
                    myView.postInvalidate();
                    Log.d("Websocket Message: ", message);
                    TextView playerCount = (TextView) findViewById(R.id.join_game_player_count_textview);
                    try {
                        JSONObject jsonMessage = new JSONObject(message);
                        if (jsonMessage.getString("type").equals("player-join")) {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_game_lobby);

        TextView guestLobbyCodeText = (TextView) findViewById(R.id.join_game_lobby_code_textview);

        guestLobbyCodeText.setText(lobbyCode);

//        JsonObjectRequest hostLobbyRequest = new JsonObjectRequest(
//                Request.Method.POST,
//                URL + "/lobby/join/" + lobbyCode + "?auth-token=" + authToken,
//                null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            lobbyCode = response.getString("code");
//                            Log.d(GameLobby.class.toString(), lobbyCode);
//                            holdResponse();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        NetworkResponse response = error.networkResponse;
//                        if ((error instanceof ServerError || error instanceof NetworkError || error instanceof TimeoutError || error instanceof AuthFailureError || error instanceof ParseError)) {
//                            try {
//                                String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
//                                JSONObject obj = new JSONObject(res);
//                                if (obj.has("message")) {
//                                    try {
//                                        Log.d(tag_json_obj, obj.getString("message"));
//                                    } catch (JSONException e) {
//                                        Log.e(GameLobby.class.toString(), Log.getStackTraceString(e));
//                                    }
//                                }
//                            } catch (UnsupportedEncodingException | JSONException e) {
//                                Log.e(GameLobby.class.toString(), Log.getStackTraceString(e));
//                            }
//                        }
//                    }
//                });
//
//        AppController.getInstance().addToRequestQueue(hostLobbyRequest);
    }
}
