package com.example.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frontend.Entities.IUser;
import com.example.frontend.Logic.LoginLogic;
import com.example.frontend.Logic.TradeLogic;
import com.example.frontend.Network.ServerRequest;
import com.example.frontend.SupportingClasses.AppController;
import com.example.frontend.SupportingClasses.IView;

import org.json.JSONException;

/**
 * Class for the logic of the screen to trade resources
 *
 * @author Noah Cordova
 */
public class TradeScreen extends AppCompatActivity implements IView {

    private String TAG = TradeScreen.class.getSimpleName();
    private TradeLogic logic;
    private IUser currentUser;
    private int stoneQuantity, woodQuantity, foodQuantity, waterQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new AppController();
        setContentView(R.layout.activity_trade_screen);

        currentUser = LoginScreen.getCurrentUser();

        ServerRequest serverRequest = new ServerRequest();
        logic = new TradeLogic(this, serverRequest, currentUser);

        TextView tvStoneQuantity = (TextView) findViewById(R.id.activity_trade_screen_tv_resource_stone_quantity);
        TextView tvWoodQuantity = (TextView) findViewById(R.id.activity_trade_screen_tv_resource_wood_quantity);
        TextView tvFoodQuantity = (TextView) findViewById(R.id.activity_trade_screen_tv_resource_food_quantity);
        TextView tvWaterQuantity = (TextView) findViewById(R.id.activity_trade_screen_tv_resource_water_quantity);

        Button cancelButton = (Button) findViewById(R.id.activity_trade_screen_button_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), GameViewScreen.class));
            }
        });

        Button submitButton = (Button) findViewById(R.id.activity_trade_screen_button_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    logic.submitTrade(stoneQuantity, woodQuantity, foodQuantity, waterQuantity);
                } catch (JSONException exception) {
                    exception.printStackTrace();
                }
            }
        });

        Button stoneMinusButton = (Button) findViewById(R.id.activity_trade_screen_button_resource_stone_minus);
        stoneMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stoneQuantity > 0){
                    stoneQuantity--;
                }
                tvStoneQuantity.setText("" + stoneQuantity);
            }
        });
        Button stonePlusButton = (Button) findViewById(R.id.activity_trade_screen_button_resource_stone_plus);
        stonePlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stoneQuantity++;
                tvStoneQuantity.setText("" + stoneQuantity);
            }
        });

        Button woodMinusButton = (Button) findViewById(R.id.activity_trade_screen_button_resource_wood_minus);
        woodMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (woodQuantity > 0){
                    woodQuantity--;
                }
                tvWoodQuantity.setText("" + woodQuantity);
            }
        });
        Button woodPlusButton = (Button) findViewById(R.id.activity_trade_screen_button_resource_wood_plus);
        woodPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                woodQuantity++;
                tvWoodQuantity.setText("" + woodQuantity);
            }
        });

        Button foodMinusButton = (Button) findViewById(R.id.activity_trade_screen_button_resource_food_minus);
        foodMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (foodQuantity > 0){
                    foodQuantity--;
                }
                tvFoodQuantity.setText("" + foodQuantity);
            }
        });
        Button foodPlusButton = (Button) findViewById(R.id.activity_trade_screen_button_resource_food_plus);
        foodPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                foodQuantity++;
                tvFoodQuantity.setText("" + foodQuantity);
            }
        });

        Button waterMinusButton = (Button) findViewById(R.id.activity_trade_screen_button_resource_water_minus);
        waterMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (waterQuantity > 0){
                    waterQuantity--;
                }
                tvWaterQuantity.setText("" + waterQuantity);
            }
        });
        Button waterPlusButton = (Button) findViewById(R.id.activity_trade_screen_button_resource_water_plus);
        waterPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                waterQuantity++;
                tvWaterQuantity.setText("" + waterQuantity);
            }
        });
    }

    @Override
    public void logText(String s) {
        Log.d(TAG, s);
    }

    @Override
    public void switchActivity() {}

    @Override
    public void makeToast(String s) {
        Log.d(TAG, "making Toast...");
        Toast.makeText(TradeScreen.this, s, Toast.LENGTH_LONG).show();
    }
}