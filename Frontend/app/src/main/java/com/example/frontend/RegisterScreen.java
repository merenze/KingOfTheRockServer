package com.example.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.frontend.Logic.RegistrationLogic;
import com.example.frontend.Network.ServerRequest;
import com.example.frontend.SupportingClasses.AppController;
import com.example.frontend.SupportingClasses.IView;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;

public class RegisterScreen extends AppCompatActivity implements IView {

    private String TAG = LoginScreen.class.getSimpleName();
    private EditText etEmail, etUsername, etPassword;
    private Button registerButton;
    private CheckBox cbAdminCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new AppController();
        setContentView(R.layout.activity_register_screen);

        etEmail = findViewById(R.id.register_email_text);
        etUsername = findViewById(R.id.register_username_text);
        etPassword = findViewById(R.id.register_password_text);
        registerButton = (MaterialButton) findViewById(R.id.register_register_button);
        cbAdminCheckBox = findViewById(R.id.register_screen_admin_checkbox);

        ServerRequest serverRequest = new ServerRequest();
        final RegistrationLogic logic = new RegistrationLogic(this, serverRequest);

        registerButton.setOnClickListener(view -> {
            String email = etEmail.getText().toString().trim();
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            boolean adminBool = cbAdminCheckBox.isChecked();

            try {
                logic.registerUser(email, username, password, adminBool);
            } catch (JSONException exception) {
                exception.printStackTrace();
            }
        });
    }

    @Override
    public void logText(String s) {
        Log.d("RegisterScreen", s);
    }

    @Override
    public void makeToast(String message) {
        Toast.makeText(RegisterScreen.this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void switchActivity() {
        startActivity(new Intent(getApplicationContext(), LoginScreen.class));
    }
}