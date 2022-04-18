package com.example.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class BuildScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_screen);

        Button townButton = (Button)findViewById(R.id.activity_build_screen_button_structure_town);
        townButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //logic to build a town
            }
        });

        Button houseButton = (Button)findViewById(R.id.activity_build_screen_button_structure_house);
        houseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //logic to build a house
            }
        });

        Button mineButton = (Button)findViewById(R.id.activity_build_screen_button_structure_mine);
        mineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //logic to build a mine
            }
        });

        Button lumberyardButton = (Button)findViewById(R.id.activity_build_screen_button_structure_lumberyard);
        lumberyardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //logic to build a lumberyard
            }
        });

        Button farmButton = (Button)findViewById(R.id.activity_build_screen_button_structure_farm);
        farmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //logic to build a farm
            }
        });

        Button wellButton = (Button)findViewById(R.id.activity_build_screen_button_structure_well);
        wellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //logic to build a well
            }
        });

        Button cancelButton = (Button)findViewById(R.id.activity_build_screen_button_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), GameViewScreen.class));
            }
        });

        Button chatButton = (Button)findViewById(R.id.activity_build_screen_button_chat);
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //logic to open chat pop-up
            }
        });

        Button menuButton = (Button)findViewById(R.id.activity_build_screen_button_menu);
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //logic to open menu pop-up
            }
        });
    }
}