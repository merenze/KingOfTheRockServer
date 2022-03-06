package com.example.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class RegisterScreen extends AppCompatActivity {

    private EditText etEmail, etUsername, etPassword;
    private String email, username, password;
    private String URL = "http://coms-309-015.class.las.iastate.edu:8080";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

        Button registerButton = (MaterialButton) findViewById(R.id.register_register_button);

        email = username = password = "";

        registerButton.setOnClickListener(view -> {
            etEmail = findViewById(R.id.register_email_text);
            etUsername = findViewById(R.id.register_username_text);
            etPassword = findViewById(R.id.register_password_text);
            email = etEmail.getText().toString().trim();
            username = etUsername.getText().toString().trim();
            password = etPassword.getText().toString().trim();

            HashMap<String, String> parameters = new HashMap<String, String>();
            parameters.put("email", email);
            parameters.put("username", username);
            parameters.put("password", password);

            JSONObject jsonObject = new JSONObject(parameters);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, URL + "/register", jsonObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if(response.has("status")){
                                try {
                                    if(response.getString("status").equals("OK")){
                                        startActivity(new Intent(view.getContext(), LoginScreen.class));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //todo
                            //unexpected response code because volley sucks
                            NetworkResponse response = error.networkResponse;
                            if(error instanceof ServerError && response != null){
                                try {
                                    String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                    JSONObject obj = new JSONObject(res);

                                    if (obj.has("username")) {
                                        try {
                                            Log.d("duplicate username: ", obj.getString("username"));
                                            startActivity(new Intent(view.getContext(), LoginScreen.class));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    if (obj.has("email")) {
                                        try {
                                            Log.d("duplicate email: ", obj.getString("email"));
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
}