package com.example.simplelistviewexperiment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.sql.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<String> listFromJSONArrayResponse = new ArrayList<String>();
        listFromJSONArrayResponse.add("username1");
        listFromJSONArrayResponse.add("username2");

        ArrayAdapter adapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.activity_search_for_user_screen_listview, listFromJSONArrayResponse);

        ListView listView = (ListView) findViewById(R.id.activity_search_for_user_screen_lv_users);
        listView.setAdapter(adapter);
    }
}