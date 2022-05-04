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
import com.example.frontend.Logic.LoginLogic;
import com.example.frontend.Network.ServerRequest;
import com.example.frontend.SupportingClasses.AppController;
import com.example.frontend.SupportingClasses.IView;
import com.example.frontend.SupportingClasses.Constants;

import org.json.JSONException;

/**
 * Class for the logic of the screen to login
 *
 * @author Noah Cordova
 */
public class LoginScreen extends AppCompatActivity implements IView {

    private String TAG = LoginScreen.class.getSimpleName();
    private static IUser currentUser;
    private Button loginButton;
    private LoginLogic logic;
    private EditText etUsernameOrEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new AppController();
        setContentView(R.layout.activity_login_screen);

        etUsernameOrEmail = (EditText)findViewById(R.id.activity_login_screen_et_username);
        etPassword = (EditText)findViewById(R.id.activity_login_screen_et_password);
        loginButton = (Button)findViewById(R.id.activity_login_screen_button_login);

        ServerRequest serverRequest = new ServerRequest();
        logic = new LoginLogic(this, serverRequest);

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

    public static IUser getCurrentUser() {
        return currentUser;
    }

    @Override
    public void logText(String s) {
        Log.d("LoginScreen", s);
    }

    @Override
    public void makeToast(String message) {
        Log.d("LoginScreen", "making Toast...");
        Toast.makeText(LoginScreen.this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void switchActivity() {
        currentUser = logic.getCurrentUser();
        Log.d("LoginScreen", currentUser.toString());
        if (currentUser.getIsAdmin()) {
            startActivity(new Intent(getApplicationContext(), AdminDashboard.class));
        } else {
            startActivity(new Intent(getApplicationContext(), UserDashboard.class));
        }
    }
}