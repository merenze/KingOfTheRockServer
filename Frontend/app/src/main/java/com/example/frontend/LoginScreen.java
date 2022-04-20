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
import com.example.frontend.Entities.IUser;
import com.example.frontend.Entities.User;
import com.example.frontend.SupportingClasses.AppController;
import com.example.frontend.SupportingClasses.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * Class for the logic of the screen to login
 *
 * @author Noah Cordova
 */
public class LoginScreen extends AppCompatActivity {

    private String TAG = LoginScreen.class.getSimpleName();
    private static User currentUser;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        loginButton = (Button)findViewById(R.id.activity_login_screen_button_login);

        loginButton.setOnClickListener(view -> {
            EditText etUsernameOrEmail = (EditText)findViewById(R.id.activity_login_screen_et_username);
            EditText etPassword = (EditText)findViewById(R.id.activity_login_screen_et_password);
            String username = etUsernameOrEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            HashMap<String, String> parameters = new HashMap<String, String>();
            parameters.put("username", username);
            parameters.put("password", password);

            JSONObject jsonObject = new JSONObject(parameters);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, Constants.URL + "/login", jsonObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(tag_json_obj, response.toString());

                            //switch screens on login
                            try {
                                if(response.getBoolean("isAdmin")){
                                    currentUser = new User(response.getString("auth-token"), response.getString("username"), true);
                                    startActivity(new Intent(view.getContext(), AdminDashboard.class));
                                } else {
                                    currentUser = new User(response.getString("auth-token"), response.getString("username"), false);
                                    //startActivity(new Intent(view.getContext(), UserDashboard.class));
                                    startActivity(new Intent(view.getContext(), GameViewScreen.class));
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

    public static User getCurrentUser() {
        return currentUser;
    }
}