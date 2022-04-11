package com.example.frontend;

import static com.example.frontend.Constants.URL;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class GameLobby extends AppCompatActivity {
    String lobbyCode = null;
    String authToken = LoginScreen.getAuthToken();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_game_lobby);

        JsonObjectRequest disconnectRequest = new JsonObjectRequest
                (Request.Method.POST, URL + "/lobby/disconnect"  + "?auth-token=" + authToken, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("Disconnect  response: ", response.getString("message"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse response = error.networkResponse;
                        if((error instanceof ServerError || error instanceof NetworkError || error instanceof TimeoutError || error instanceof AuthFailureError || error instanceof ParseError) && response != null){
                            try {
                                String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                JSONObject obj = new JSONObject(res);
                                if (obj.has("message")) {
                                    try {
                                        Log.d(tag_json_obj, obj.getString("message"));
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

        //TODO
        //add switch to make lobby private or public

        //TODO
        //Adjust parameters URL and request method
        //Make /hostGame whatever Renze wants it to be
        //Make request method GET or change jsonObject from null
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, URL + "/lobby/host"  + "?auth-token=" + authToken, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            lobbyCode = response.getString("code");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse response = error.networkResponse;
                        if((error instanceof ServerError || error instanceof NetworkError || error instanceof TimeoutError || error instanceof AuthFailureError || error instanceof ParseError) && response != null){
                            try {
                                String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                JSONObject obj = new JSONObject(res);
                                if (obj.has("message")) {
                                    try {
                                        Log.d(tag_json_obj, obj.getString("message"));
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

        AppController.getInstance().addToRequestQueue(disconnectRequest);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);

        TextView lobbyCodeText = (TextView) findViewById(R.id.host_game_lobby_code_textview);
        if(lobbyCode != null){
            lobbyCodeText.setText(lobbyCode);
        } else {
            lobbyCodeText.setText("eror");
        }
    }

}
