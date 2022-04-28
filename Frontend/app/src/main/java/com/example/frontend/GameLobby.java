package com.example.frontend;

import static com.example.frontend.Constants.URL;
import static com.example.frontend.Constants.WSURL;
import static com.example.frontend.Constants.tag_json_obj;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.frontend.SupportingClasses.AppController;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

public class GameLobby extends AppCompatActivity {
    String authToken = LoginScreen.getAuthToken();
    String lobbyCode;
    private WebSocketClient lobbyWebSocket;

    Draft[] drafts = {
            new Draft_6455()
    };

    public void holdResponse() {
        TextView lobbyCodeText = (TextView) findViewById(R.id.host_game_lobby_code_textview);
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
            Log.d(GameLobby.class.toString(), String.format("Attempting WS connection to %s", endpoint));
            lobbyWebSocket = new WebSocketClient(new URI(endpoint), (Draft) drafts[0]) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    Log.d("OPEN", "Opening hosts websocket, lc: " + lobbyCode);
                }

                @Override
                public void onMessage(String message) {
                    TextView playerCount = (TextView) findViewById(R.id.host_game_player_count_textview);
                    try {
                        JSONObject jsonMessage = new JSONObject(message);
                        int numPlayers = jsonMessage.getInt("num-players");
                        playerCount.setText(numPlayers);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d("Websocket Message: ", message);
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
        setContentView(R.layout.activity_host_game_lobby);

        JsonObjectRequest hostLobbyRequest = new JsonObjectRequest(
                Request.Method.POST,
                URL + "/lobby/host" + "?auth-token=" + authToken,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            lobbyCode = response.getString("code");
                            Log.d(GameLobby.class.toString(), lobbyCode);
                            holdResponse();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse response = error.networkResponse;
                        if ((error instanceof ServerError || error instanceof NetworkError || error instanceof TimeoutError || error instanceof AuthFailureError || error instanceof ParseError)) {
                            try {
                                String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                JSONObject obj = new JSONObject(res);
                                if (obj.has("message")) {
                                    try {
                                        Log.d(tag_json_obj, obj.getString("message"));
                                    } catch (JSONException e) {
                                        Log.e(GameLobby.class.toString(), Log.getStackTraceString(e));
                                    }
                                }
                            } catch (UnsupportedEncodingException | JSONException e) {
                                Log.e(GameLobby.class.toString(), Log.getStackTraceString(e));
                            }
                        }
                    }
                });

        AppController.getInstance().addToRequestQueue(hostLobbyRequest);
    }
}
