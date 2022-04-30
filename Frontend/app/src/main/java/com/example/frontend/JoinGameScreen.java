package com.example.frontend;

import static com.example.frontend.SupportingClasses.Constants.WSURL;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

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

                Intent intent = new Intent(view.getContext(), GuestLobby.class);
                intent.putExtra("lobbyCode", lobbyCode);

                startActivity(intent);
            }
        });

    }
}
