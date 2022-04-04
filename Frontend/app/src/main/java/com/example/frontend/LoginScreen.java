package com.example.frontend;

import static com.example.frontend.Constants.tag_json_obj;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class LoginScreen extends AppCompatActivity {

    private String TAG = LoginScreen.class.getSimpleName();
    private static String username; //changed to correct current username upon successful login
    private String password;
    private Button loginButton;
    private String url_coms309_backend_server = "http://coms-309-015.class.las.iastate.edu:8080";
    private static String authToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        loginButton = (Button)findViewById(R.id.activity_login_screen_button_login);

        loginButton.setOnClickListener(view -> {
            EditText etUsernameOrEmail = (EditText)findViewById(R.id.activity_login_screen_et_username);
            EditText etPassword = (EditText)findViewById(R.id.activity_login_screen_et_password);
            username = etUsernameOrEmail.getText().toString().trim();
            password = etPassword.getText().toString().trim();

            HashMap<String, String> parameters = new HashMap<String, String>();
            parameters.put("username", username);
            parameters.put("password", password);

            JSONObject jsonObject = new JSONObject(parameters);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, url_coms309_backend_server + "/login", jsonObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                authToken = response.getString("auth-token");
                                Toast.makeText(LoginScreen.this, authToken,
                                        Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.d(tag_json_obj, response.toString());
                            //save current username to class variable
                            try {
                                username = response.getString("username");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //TODO: switch screens on login
//                            try {
//                                startActivity(new Intent(view.getContext(), AfterLoginScreen.class));
//                            } catch (JSONException exception) {
//                                exception.printStackTrace();
//                            }
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

            //Add request to queue
            AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
        });
    }

    public static String getAuthToken(){
        return authToken;
    }

    public static String getUsername(){
        return username;
    }

}