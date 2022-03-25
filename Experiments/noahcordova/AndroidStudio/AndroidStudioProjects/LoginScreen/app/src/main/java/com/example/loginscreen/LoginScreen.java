package com.example.loginscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class LoginScreen extends AppCompatActivity {

    private String TAG = LoginScreen.class.getSimpleName();
    private String username;
    private String password;
    public static String usernameResponse;
    private TextView loginCredentials;
    private Button loginButton;
    private String tag_json_obj = "jobj_req";
    private String url_coms309_backend_server = "http://coms-309-015.class.las.iastate.edu:8080";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = (Button)findViewById(R.id.activity_main_button_login);

        loginButton.setOnClickListener(view -> {
            EditText etUsernameOrEmail = (EditText)findViewById(R.id.activity_main_et_name);
            EditText etPassword = (EditText)findViewById(R.id.activity_main_et_password);
            username = etUsernameOrEmail.getText().toString().trim();
            password = etPassword.getText().toString().trim();
            loginCredentials = (TextView)findViewById(R.id.activity_main_tv_loginCredentials);

            HashMap<String, String> parameters = new HashMap<String, String>();
            parameters.put("username", username);
            parameters.put("password", password);

            JSONObject jsonObject = new JSONObject(parameters);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, url_coms309_backend_server + "/login", jsonObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(tag_json_obj, response.toString());
                            try {
                                usernameResponse = response.getString("username");
                                loginCredentials.setText("Welcome, " + response.getString("username"));
                                startActivity(new Intent(view.getContext(), AfterLoginScreen.class));
                            } catch (JSONException exception) {
                                exception.printStackTrace();
                            }
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
                                    if (obj.has(username)) {
                                        try {
                                            Log.d(TAG, obj.getString(username));
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

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsonObjectRequest);
        });
    }

//    private JSONObject makeJsonObjectForLogin(String usernameOrEmail, String password) throws JSONException {
//        JSONObject loginCredentialsJsonObject = new JSONObject();
//        //currently only accepts username, change as project moves along
//        loginCredentialsJsonObject.put("username", usernameOrEmail);
//        loginCredentialsJsonObject.put("password", password);
//
//        return loginCredentialsJsonObject;
//    }

    public String getUsernameFromResponse(){
        return usernameResponse;
    }


}