package com.example.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.frontend.Entities.IUser;

/**
 * Class for the logic of the screen of the main game of the user
 *
 * @author Noah Cordova
 */
public class GameViewScreen extends AppCompatActivity {

    private IUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_view_screen);

        currentUser = LoginScreen.getCurrentUser();
        Log.d("GameViewScreen", currentUser.toString());

        Button tradeButton = (Button)findViewById(R.id.activity_game_view_screen_button_trade);
        tradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), TradeScreen.class));
            }
        });

        Button buildButton = (Button)findViewById(R.id.activity_game_view_screen_button_build);
        buildButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), BuildScreen.class));
            }
        });

        Button menuButton = (Button)findViewById(R.id.activity_game_view_screen_button_menu);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start menu pop-up
            }
        });

        Button chatButton = (Button)findViewById(R.id.activity_game_view_screen_button_chat);
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start chat pop-up
            }
        });

    }
}