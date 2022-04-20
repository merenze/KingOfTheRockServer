package com.example.frontend;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class GameChat extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_chat);
    }
}

public class MemberData {
    private String name;
    private String color;

    public MemberData(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public MemberData(){
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    @Override
    public String toString() {
        return "MemberData{" +
                "name'" + name +'\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
