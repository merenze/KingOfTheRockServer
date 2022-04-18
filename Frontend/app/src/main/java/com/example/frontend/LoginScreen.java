package com.example.frontend;

import static com.example.frontend.SupportingClasses.Constants.tag_json_obj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.example.frontend.Logic.LoginLogic;
import com.example.frontend.Network.ServerRequest;
import com.example.frontend.SupportingClasses.AppController;
import com.example.frontend.SupportingClasses.IView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class LoginScreen extends AppCompatActivity implements IView {

    private String TAG = LoginScreen.class.getSimpleName();
    private static String currentUsername;
    private static String authToken;
    private EditText etUsernameOrEmail, etPassword;
    private Button loginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new AppController();
        setContentView(R.layout.activity_login_screen);

        etUsernameOrEmail = (EditText)findViewById(R.id.activity_login_screen_et_username);
        etPassword = (EditText)findViewById(R.id.activity_login_screen_et_password);
        loginButton = (Button)findViewById(R.id.activity_login_screen_button_login);

        ServerRequest serverRequest = new ServerRequest();
        final LoginLogic logic = new LoginLogic(this, serverRequest);

        loginButton.setOnClickListener(view -> {
            String username = etUsernameOrEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            try {
                logic.loginUser(username, password);
            } catch (JSONException exception) {
                exception.printStackTrace();
            }

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

                            //switch screens on login
                            try {
                                //save current username to class variable
                                currentUsername = response.getString("username");

                                if(response.getBoolean("isAdmin")){
                                    startActivity(new Intent(view.getContext(), AdminDashboard.class));
                                } else {
                                    startActivity(new Intent(view.getContext(), UserDashboard.class));
                                }
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

            //Add request to queue
            AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
        });
    }

    public static String getAuthToken(){
        return authToken;
    }

    public static void setAuthToken(String givenAuthToken) {
        authToken = givenAuthToken;
    }

    public static String getCurrentUsername(){
        return currentUsername;
    }

    public static void setCurrentUsername(String givenCurrentUsername) {
        currentUsername = givenCurrentUsername;
    }

    public void switchActivity(){
        if(logic.getIsAdmin){
            startActivity(new Intent(view.getContext(), AdminDashboard.class));
        } else {
            startActivity(new Intent(view.getContext(), UserDashboard.class));
        }
    }
}