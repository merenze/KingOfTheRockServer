package com.example.volleyrequests1;

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

        //buttons logic
        Button buttonToStringRequest = findViewById(R.id.activity_main_button_to_string_request);
        buttonToStringRequest.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //logic for button
                startActivity(new Intent(v.getContext(), StringRequestActivity.class));
            }
        });
        Button buttonToJsonRequest = findViewById(R.id.activity_main_button_to_json_request);
        buttonToJsonRequest.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //logic for button
                startActivity(new Intent(v.getContext(), JsonRequestActivity.class));
            }
        });
        Button buttonToImageRequest = findViewById(R.id.activity_main_button_to_image_request);
        buttonToImageRequest.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //logic for button
                startActivity(new Intent(v.getContext(), ImageRequestActivity.class));
            }
        });
    }
}