package com.example.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class UserDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        Button buttonToFindUser = findViewById(R.id.user_dashboard_find_user_button);
        buttonToFindUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), SearchForUserScreen.class));
            }
        });

        Button buttonToHostGame = findViewById(R.id.user_dashboard_host_game_button);
        buttonToHostGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
                //create game lobby from backend end yada yada
                startActivity(new Intent(view.getContext(), GameLobby.class));
            }
        });
    }
}
