package com.example.frontend;

import static com.example.frontend.SupportingClasses.Constants.URL;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.frontend.SupportingClasses.AppController;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * Class for the logic of the registration screen
 *
 * @author Dan Rosenhamer
 */
public class RegisterScreen extends AppCompatActivity {

    private EditText etEmail, etUsername, etPassword;
    private String email, username, password;
    boolean adminBool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

        Button registerButton = (MaterialButton) findViewById(R.id.register_register_button);

        email = username = password = "";
        adminBool = false;

        registerButton.setOnClickListener(view -> {
            etEmail = findViewById(R.id.register_email_text);
            etUsername = findViewById(R.id.register_username_text);
            etPassword = findViewById(R.id.register_password_text);
            email = etEmail.getText().toString().trim();
            username = etUsername.getText().toString().trim();
            password = etPassword.getText().toString().trim();

            CheckBox adminCheckBox = findViewById(R.id.register_screen_admin_checkbox);
            adminBool = adminCheckBox.isChecked();

            HashMap<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("email", email);
            parameters.put("username", username);
            parameters.put("password", password);
            parameters.put("isAdmin", adminBool);

            JSONObject jsonObject = new JSONObject(parameters);

            //Request to register user
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, URL + "/register" + "?auth-token=" + LoginScreen.getAuthToken(), jsonObject, new Response.Listener<JSONObject>() {
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
                            NetworkResponse response = error.networkResponse;
                            if(error instanceof ServerError && response != null){
                                try {
                                    String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                    JSONObject obj = new JSONObject(res);

                                    if(obj.has("email") && obj.has("username")){
                                        Toast.makeText(RegisterScreen.this, obj.getString("email") + "\n" + obj.getString("username"),
                                                Toast.LENGTH_LONG).show();
                                    }
                                    if (obj.has("email")) {
                                        try {
                                            Log.d("duplicate email: ", obj.getString("email"));
                                            Toast.makeText(RegisterScreen.this, obj.getString("email"),
                                                    Toast.LENGTH_LONG).show();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    if (obj.has("username")) {
                                        try {
                                            Log.d("duplicate username: ", obj.getString("username"));
                                            Toast.makeText(RegisterScreen.this, obj.getString("username"),
                                                    Toast.LENGTH_LONG).show();
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

            //Add request to queue
            AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        });
    }
}