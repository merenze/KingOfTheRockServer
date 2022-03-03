package com.example.loginscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.loginscreen.net_utils.Const;

import org.json.JSONException;
import org.json.JSONObject;

public class AfterLoginScreen extends AppCompatActivity {
    private String TAG = AfterLoginScreen.class.getSimpleName();
    private TextView loginCredentials;
    private String tag_json_obj = "jobj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login_screen);

        loginCredentials = (TextView)findViewById(R.id.activity_after_login_screen_tv_loginCredentials);
    }

}