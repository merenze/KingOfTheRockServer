package com.example.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class JoinGameScreen extends AppCompatActivity {
    private String lobbyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game_screen);

        Button buttonToSubmitCode = (Button) findViewById(R.id.join_game_submit_code_button);

        buttonToSubmitCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText etLobbyCode = (EditText) findViewById(R.id.join_game_code_text);
                lobbyCode = etLobbyCode.getText().toString().trim();

                Intent intent = new Intent(view.getContext(), Lobby.class);
                intent.putExtra("lobbyCode", lobbyCode);
                intent.putExtra("isHost", false);

                startActivity(intent);
            }
        });

    }
}
