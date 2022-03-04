package com.example.loginscreen;

import com.example.loginscreen.LoginScreen;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class AfterLoginScreen extends AppCompatActivity {
    private String TAG = AfterLoginScreen.class.getSimpleName();
    private TextView loginCredentials;
    private String tag_json_obj = "jobj_req";
    private String usernameResponse = LoginScreen.usernameResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login_screen);

        loginCredentials = (TextView)findViewById(R.id.activity_after_login_screen_tv_loginCredentials);
        loginCredentials.setText("Welcome, " + usernameResponse);
    }

}