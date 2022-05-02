package com.example.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.frontend.Entities.IUser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class for the logic of the screen of the main game of the user
 *
 * @author Noah Cordova
 */
public class GameViewScreen extends AppCompatActivity {

    private IUser currentUser;

    private TextView waterQty;
    private TextView stoneQty;
    private TextView foodQty;
    private TextView woodQty;

//    AlertDialog.Builder dialogBuilder;
//    AlertDialog dialog;

    JSONObject jsonGameObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_view_screen);

        foodQty = findViewById(R.id.activity_game_view_screen_tv_resource_food_quantity);
        stoneQty = findViewById(R.id.activity_game_view_screen_tv_resource_stone_quantity);
        waterQty = findViewById(R.id.activity_game_view_screen_tv_resource_water_quantity);
        woodQty = findViewById(R.id.activity_game_view_screen_tv_resource_wood_quantity);

        currentUser = LoginScreen.getCurrentUser();

        Bundle bundle = getIntent().getExtras();
        String jsonString = bundle.getString("game-object-string");

        try {
            jsonGameObject = new JSONObject(jsonString);
        } catch (JSONException e) {
            Log.d(GameViewScreen.class.toString(), "Error converting JSON string to JSON");
            e.printStackTrace();
        }

        initialTextUpdate();

        Button tradeButton = (Button) findViewById(R.id.activity_game_view_screen_button_trade);
        tradeButton.setOnClickListener(view -> startActivity(new Intent(view.getContext(), TradeScreen.class)));

        Button buildButton = (Button) findViewById(R.id.activity_game_view_screen_button_build);
        buildButton.setOnClickListener(view -> startActivity(new Intent(view.getContext(), BuildScreen.class)));

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
//        View myView = findViewById(android.R.id.content).getRootView();
//        myView.postInvalidate();
        foodQty.setText(0);
        woodQty.setText(0);
        stoneQty.setText(0);
        waterQty.setText(0);
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