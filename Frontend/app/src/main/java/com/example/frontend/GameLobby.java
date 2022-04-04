package com.example.frontend;

import static com.example.frontend.Constants.URL;
import static com.example.frontend.Constants.tag_json_obj;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

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

public class GameLobby extends AppCompatActivity {

    private String authToken;
    private String lobbyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_game_lobby);

        authToken = LoginScreen.getAuthToken();

        //TODO
        //Adjust parameters URL and request method
        //Make /hostGame whatever Renze wants it to be
        //Make request method GET or change jsonObject from null
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, URL + "/hostGame", null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            lobbyCode = response.getString("lobby-code");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse response = error.networkResponse;
                        if(error instanceof ServerError && response != null){
                            try {
                                String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                JSONObject obj = new JSONObject(res);
                                if (obj.has("lobby-code")) {
                                    try {
                                        Log.d(tag_json_obj, obj.getString("lobby-code"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } catch (UnsupportedEncodingException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);

        TextView lobbyCodeText = (TextView) findViewById(R.id.host_game_lobby_code_textview);
        lobbyCodeText.setText(lobbyCode);
    }

}
