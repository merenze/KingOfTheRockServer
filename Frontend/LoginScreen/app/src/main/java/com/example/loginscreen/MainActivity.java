package com.example.loginscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText usernameOrEmail;
    private EditText password;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameOrEmail = (EditText)findViewById(R.id.activity_main_et_name);
        password = (EditText)findViewById(R.id.activity_main_et_password);
        loginButton = (Button)findViewById(R.id.activity_main_button_login);

        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //logic for button
                validateLogin(usernameOrEmail.getText().toString(), password.getText().toString());
            }
        });
    }

    /*
    private void validateLogin(String databaseUsername, String databaseEmail, String databasePassword){
        //if ((usernameOrEmail.equals(databaseUsername) || usernameOrEmail.equals(databaseEmail)) && password.equals(databasePassword))
        if ((usernameOrEmail.equals("noahcordova") || usernameOrEmail.equals(databaseEmail)) && password.equals("password"))
        {
            Intent intent = new Intent(MainActivity.this, AfterLoginScreen.class);
            startActivity(intent);
        }
    }
    */
    private void validateLogin(String databaseUsername, String databasePassword){
        //if ((usernameOrEmail.equals(databaseUsername) || usernameOrEmail.equals(databaseEmail)) && password.equals(databasePassword))
        if (usernameOrEmail.getText().toString().equals("noahcordova") && password.getText().toString().equals("password"))
        {
            startActivity(new Intent(MainActivity.this, AfterLoginScreen.class));
        }
    }

}