package com.example.frontend;

import static com.example.frontend.SupportingClasses.Constants.WSURL;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class JoinGameScreen extends AppCompatActivity {
    private String lobbyCode;
    private String authToken = LoginScreen.getAuthToken();

    private WebSocketClient lobbyWebSocket;

    Draft[] drafts = {
            new Draft_6455()
    };

    private void instantiateWebsocket() {
        try {
            String endpoint = String.format("%s/lobby/%s/%s", WSURL, lobbyCode, authToken);
            Log.d(JoinGameScreen.class.toString(), String.format("Attempting WS connection to %s", endpoint));
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
                        if(jsonMessage.getString("type").equals("player-join")){
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
        setContentView(R.layout.activity_join_game_screen);


//        Button buttonToQuickPlay = (Button) findViewById(R.id.join_game_quick_play_button);
//
//        buttonToQuickPlay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // TODO
//                JsonObjectRequest quickPlayRequest = new JsonObjectRequest
//                        (Request.Method.POST, URL + "/lobby/join" + "?auth-token=" + authToken, null, new Response.Listener<JSONObject>() {
//                            @Override
//                            public void onResponse(JSONObject response) {
//                                // TODO
//                                // connect to websocket
//                                //switch screens on connection to lobby
//                            }
//                        }, new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                Log.d("TestTag", "in onErrorResponse body");
//                                NetworkResponse response = error.networkResponse;
//                                if(error instanceof ServerError && response != null){
//                                    try {
//                                        String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
//                                        JSONObject obj = new JSONObject(res);
//                                    } catch (UnsupportedEncodingException | JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }
//                        });
//
//                //Add request to queue
//                AppController.getInstance().addToRequestQueue(quickPlayRequest, tag_json_obj);
//            }
//        });

        Button buttonToSubmitCode = (Button) findViewById(R.id.join_game_submit_code_button);

        buttonToSubmitCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), GuestLobby.class));
                EditText etLobbyCode = (EditText) findViewById(R.id.join_game_code_text);

                lobbyCode = etLobbyCode.getText().toString().trim();

                instantiateWebsocket();
                lobbyWebSocket.connect();
            }
        });

    }
}
