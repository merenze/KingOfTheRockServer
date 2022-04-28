package com.example.frontend;

import static com.example.frontend.SupportingClasses.Constants.URL;
import static com.example.frontend.SupportingClasses.Constants.tag_json_obj;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.frontend.SupportingClasses.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class JoinGameScreen extends AppCompatActivity {
    private String lobbyCode = "";
    private String authToken = LoginScreen.getAuthToken();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game_screen);

        Button buttonToQuickPlay = (Button) findViewById(R.id.join_game_quick_play_button);

        buttonToQuickPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO
                JsonObjectRequest quickPlayRequest = new JsonObjectRequest
                        (Request.Method.POST, URL + "/lobby/join" + "?auth-token=" + authToken, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // TODO
                                // connect to websocket
                                //switch screens on connection to lobby
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("TestTag", "in onErrorResponse body");
                                NetworkResponse response = error.networkResponse;
                                if(error instanceof ServerError && response != null){
                                    try {
                                        String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                        JSONObject obj = new JSONObject(res);
                                    } catch (UnsupportedEncodingException | JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });

                //Add request to queue
                AppController.getInstance().addToRequestQueue(quickPlayRequest, tag_json_obj);
            }
        });

        Button buttonToSubmitCode = (Button) findViewById(R.id.join_game_submit_code_button);

        buttonToSubmitCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText etLobbyCode = (EditText) findViewById(R.id.join_game_code_text);
                lobbyCode = etLobbyCode.getText().toString().trim();

                // TODO
                // attempt to join lobby
                HashMap<String, String> mapLobbyCode = new HashMap<>();
                mapLobbyCode.put("code", lobbyCode);

                JSONObject jsonLobbyCode = new JSONObject(mapLobbyCode);

                JsonObjectRequest joinViaLobbyCodeRequest = new JsonObjectRequest
                        (Request.Method.POST, URL + "/lobby/join" + "?auth-token=" + authToken, jsonLobbyCode, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // TODO
                                // connect to websocket
                                //switch screens on connection to lobby
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("TestTag", "in onErrorResponse body");
                                NetworkResponse response = error.networkResponse;
                                if(error instanceof ServerError && response != null){
                                    try {
                                        String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                        JSONObject obj = new JSONObject(res);
                                    } catch (UnsupportedEncodingException | JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });

                //Add request to queue
                AppController.getInstance().addToRequestQueue(joinViaLobbyCodeRequest, tag_json_obj);
            }
        });

    }
}
