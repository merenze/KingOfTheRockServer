package com.example.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Class for the logic of the home screen
 *
 * @author Dan Rosenhamer
 */
public class HomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        Button buttonToLogin = findViewById(R.id.homescreen_login_button);
        buttonToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), LoginScreen.class));
            }
        });

        Button buttonToRegister = findViewById(R.id.homescreen_register_button);
        buttonToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), RegisterScreen.class));
            }
        });

        Button buttonToGame = findViewById(R.id.homescreen_game_button);
        buttonToGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), GameViewScreen.class));
            }
        });
    }
}