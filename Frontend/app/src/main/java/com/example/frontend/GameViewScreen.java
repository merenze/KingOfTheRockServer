package com.example.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.frontend.Entities.IUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class for the logic of the screen of the main game of the user
 *
 * @author Noah Cordova
 */
public class GameViewScreen extends AppCompatActivity {

    private IUser currentUser;
    private JSONObject jsonGameObject;

    private TextView waterQty;
    private TextView stoneQty;
    private TextView foodQty;
    private TextView woodQty;
    private TextView myUsername;
    private TextView username1;
    private TextView username2;
    private TextView username3;
    private TextView[] usernameTVArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_view_screen);

        foodQty = findViewById(R.id.activity_game_view_screen_tv_resource_food_quantity);
        stoneQty = findViewById(R.id.activity_game_view_screen_tv_resource_stone_quantity);
        waterQty = findViewById(R.id.activity_game_view_screen_tv_resource_water_quantity);
        woodQty = findViewById(R.id.activity_game_view_screen_tv_resource_wood_quantity);
        myUsername = findViewById(R.id.activity_game_view_tv_current_username);
        username1 = findViewById(R.id.activity_game_view_tv_username1);
        username2 = findViewById(R.id.activity_game_view_tv_username2);
        username3 = findViewById(R.id.activity_game_view_tv_username3);

        usernameTVArray = new TextView[3];
        usernameTVArray[0] = username1;
        usernameTVArray[1] = username2;
        usernameTVArray[2] = username3;


        currentUser = LoginScreen.getCurrentUser();
        Log.d("GameViewScreen", currentUser.toString());

        Bundle bundle = getIntent().getExtras();
        String jsonGameObjectString = bundle.getString("game-object-string");

        try {
            jsonGameObject = new JSONObject(jsonGameObjectString);
            initialTextUpdate();
        } catch (JSONException e) {
            Log.d(GameViewScreen.class.toString(), "Error converting JSON string to JSON");
            e.printStackTrace();
        }

        Button tradeButton = (Button) findViewById(R.id.activity_game_view_screen_button_trade);
        tradeButton.setOnClickListener(view -> startActivity(new Intent(view.getContext(), TradeScreen.class)));

        Button buildButton = (Button)findViewById(R.id.activity_game_view_screen_button_build);
        buildButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), BuildScreen.class);
                intent.putExtra("game-object-string", jsonGameObjectString);
                startActivity(intent);
            }
        });

        Button menuButton = (Button) findViewById(R.id.activity_game_view_screen_button_menu);
        menuButton.setOnClickListener(view -> {
            //start menu pop-up
        });

        Button chatButton = (Button) findViewById(R.id.activity_game_view_screen_button_chat);
        chatButton.setOnClickListener(view -> {
            //start chat pop-up
//                createChatBoxDialog();

        });

    }

    public void initialTextUpdate() {
        try {
            JSONArray playerArray = jsonGameObject.getJSONObject("game").getJSONArray("players");

            int userCount = 0;
            boolean pastMe = false;
            for (int i = 0; i < playerArray.length(); i++) {
                JSONObject playerObject = (JSONObject) playerArray.get(i);
                Log.d("Currentplayer", playerObject.getString("username"));
                if (currentUser.getUsername().equals(playerObject.getString("username")) && !pastMe) {
                    myUsername.setText("King " + playerObject.getString("username"));
                    Log.d("My username: ", playerObject.getString("username"));
                    pastMe = true;
                } else {
                    TextView currentTV = usernameTVArray[userCount];
                    userCount++;
                    currentTV.setText("King " + playerObject.getString("username"));
                    Log.d("Other username: ", playerObject.getString("username"));
                }
            }
        } catch (JSONException e) {
            Log.d(GameViewScreen.class.toString(), "Error getting player array from game object");
        }
        foodQty.setText("0");
        woodQty.setText("0");
        stoneQty.setText("0");
        waterQty.setText("0");
    }

    /*
    public void createChatBoxDialog() {
        dialogBuilder = new AlertDialog.Builder(this);
        final View chatPopupView = getLayoutInflater().inflate(R.layout.activity_game_chat_popup, null);

        dialogBuilder.setView(chatPopupView);
        dialog = dialogBuilder.create();
        dialog.show();
    }
     */
}