package com.example.frontend;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class JoinGameScreen extends AppCompatActivity {

    private String lobbyCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        Button buttonToQuickPlay = (Button) findViewById(R.id.join_game_quick_play_button);

        buttonToQuickPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
                //backend sends response containing a public lobby code
                //attempt to join lobby
            }
        });

        Button buttonToSubmitCode = (Button) findViewById(R.id.join_game_submit_code_button);

        buttonToSubmitCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText etLobbyCode = (EditText) findViewById(R.id.join_game_code_text);
                lobbyCode = etLobbyCode.getText().toString().trim();
                //attempt to join lobby
            }
        });

    }
}
