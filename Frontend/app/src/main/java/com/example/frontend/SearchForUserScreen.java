package com.example.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.HashMap;

public class SearchForUserScreen extends AppCompatActivity {

    private String TAG = SearchForUserScreen.class.getSimpleName();
    private String usernameEntry;
    private Button searchButton;
    private String tag_json_arr = "jarr_req";
    private String url_coms309_backend_server = "http://coms-309-015.class.las.iastate.edu:8080";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_for_user_screen);

        searchButton = (Button)findViewById(R.id.activity_search_for_user_button_search);

        searchButton.setOnClickListener(view -> {
            EditText etUsernameEntry = (EditText)findViewById(R.id.activity_search_for_user_screen_et_searchEntry);
            usernameEntry = etUsernameEntry.getText().toString().trim();
            //loginCredentials = (TextView)findViewById(R.id.activity_main_tv_loginCredentials);
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                    (Request.Method.GET, url_coms309_backend_server + "/search/?q=" + usernameEntry, null, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.d(tag_json_arr, response.toString());
                            //check that array is non-empty
                            try{
                                response.get(0); //response.isEmpty()
                            }catch(JSONException exception){
                                Log.d(tag_json_arr, "Username not found");
                            }
//                            response.toList();
//                            ArrayAdapter adapter = new ArrayAdapter<String>(SearchForUserScreen.class,
//                                    R.layout.activity_search_for_user_screen_listview, response);
//
//                            ListView listView = (ListView) findViewById(R.id.mobile_list);
//                            listView.setAdapter(adapter);

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("TestTag", "in onErrorResponse body");
                        }
                    });

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsonArrayRequest);
        });
    }

}