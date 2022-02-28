package com.example.registrationscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
                    (Request.Method.POST, (URL + "/users"), jsonObject, response -> {
                        if(response.equals("Saved.")) {
                            startActivity(new Intent(view.getContext(), HomeScreen.class));
                        }
                    }, error -> {
                        // TODO: Handle error
                        Log.e("LOG_VOLLEY: ", error.getMessage());
                        startActivity(new Intent(view.getContext(), LoginScreen.class));
                    });

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsonObjectRequest);
        });
    }
}