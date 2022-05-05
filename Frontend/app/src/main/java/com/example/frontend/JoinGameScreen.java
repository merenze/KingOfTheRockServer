package com.example.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.frontend.Entities.IUser;

public class JoinGameScreen extends AppCompatActivity {
    private String lobbyCode;
    private IUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game_screen);

        currentUser = LoginScreen.getCurrentUser();

        Button buttonToSubmitCode = (Button) findViewById(R.id.join_game_submit_code_button);

        buttonToSubmitCode.setOnClickListener(view -> {

            EditText etLobbyCode = (EditText) findViewById(R.id.join_game_code_text);
            lobbyCode = etLobbyCode.getText().toString().trim();

            Intent intent = new Intent(view.getContext(), Lobby.class);
            intent.putExtra("lobbyCode", lobbyCode);
            Log.d("Guest lobbyCode: ", lobbyCode);
            currentUser.setIsHost(false);

            startActivity(intent);
        });

    }
}
