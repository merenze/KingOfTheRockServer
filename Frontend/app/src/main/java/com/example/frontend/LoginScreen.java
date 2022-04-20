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
    private LoginLogic logic;
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
        LoginLogic logic = new LoginLogic(this, serverRequest);

        loginButton.setOnClickListener(view -> {
            String username = etUsernameOrEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            try {
                logic.loginUser(username, password);
            } catch (JSONException exception) {
                exception.printStackTrace();
            }
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

    @Override
    public void logText(String s) {
        Log.d("RegisterScreen", s);
    }

    @Override
    public void switchActivity(){
        if(logic.getIsAdmin()){
            startActivity(new Intent(getApplicationContext(), AdminDashboard.class));
        } else {
            startActivity(new Intent(getApplicationContext(), UserDashboard.class));
        }
    }
}