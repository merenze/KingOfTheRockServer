package com.example.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TradeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_screen);

        TextView stoneQuantity = (TextView) findViewById(R.id.activity_trade_screen_tv_resource_stone_quantity);
        TextView woodQuantity = (TextView) findViewById(R.id.activity_trade_screen_tv_resource_wood_quantity);
        TextView foodQuantity = (TextView) findViewById(R.id.activity_trade_screen_tv_resource_food_quantity);
        TextView waterQuantity = (TextView) findViewById(R.id.activity_trade_screen_tv_resource_water_quantity);

        Button stoneMinusButton = (Button) findViewById(R.id.activity_trade_screen_button_resource_stone_minus);
        stoneMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //edit stoneQuantity
            }
        });
        Button stonePlusButton = (Button) findViewById(R.id.activity_trade_screen_button_resource_stone_plus);
        stonePlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //edit stoneQuantity
            }
        });

        Button woodMinusButton = (Button) findViewById(R.id.activity_trade_screen_button_resource_wood_minus);
        woodMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //edit woodQuantity
            }
        });
        Button woodPlusButton = (Button) findViewById(R.id.activity_trade_screen_button_resource_wood_plus);
        woodPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //edit woodQuantity
            }
        });

        Button foodMinusButton = (Button) findViewById(R.id.activity_trade_screen_button_resource_food_minus);
        woodMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //edit foodQuantity
            }
        });
        Button foodPlusButton = (Button) findViewById(R.id.activity_trade_screen_button_resource_food_plus);
        woodPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //edit foodQuantity
            }
        });

        Button waterMinusButton = (Button) findViewById(R.id.activity_trade_screen_button_resource_water_minus);
        woodMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //edit waterQuantity
            }
        });
        Button waterPlusButton = (Button) findViewById(R.id.activity_trade_screen_button_resource_water_plus);
        woodPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //edit waterQuantity
            }
        });

        Button cancelButton = (Button) findViewById(R.id.activity_trade_screen_button_cancel);
        woodPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cancel logic
            }
        });

        Button submitButton = (Button) findViewById(R.id.activity_trade_screen_button_submit);
        woodPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //submit logic
            }
        });
    }
}