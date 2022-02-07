package com.example.basictests;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonToCounter = findViewById(R.id.activity_main_button_to_counter);
        buttonToCounter.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //logic for button
                startActivity(new Intent(v.getContext(), Counter.class));
            }
        });
    }
}