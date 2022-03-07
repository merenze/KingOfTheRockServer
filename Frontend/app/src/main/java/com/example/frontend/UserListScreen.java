package com.example.frontend;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.frontend.Constants.URL;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class UserListScreen extends AppCompatActivity {

    private ListView listView;

    private String username, accountType;

    private ArrayList<HashMap<String, String>> userArrayList;
    private ArrayList<String> usernameArrayList;
    private ArrayList<String> accountTypeArrayList;
    private HashMap<String, String> userListHashMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        userArrayList = new ArrayList<>();
        usernameArrayList = new ArrayList<>();
        accountTypeArrayList = new ArrayList<>();

        GetData getData = new GetData();
        getData.execute();

        listView = (ListView) findViewById(R.id.activity_userlist_listview);
        CustomBaseAdapter customBaseAdapter = new CustomBaseAdapter(getApplicationContext(), usernameArrayList, accountTypeArrayList);
        listView.setAdapter(customBaseAdapter);
    }

    public class GetData extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String current = "";

            try {
                URL url;
                HttpURLConnection urlConnection = null;

                try {
                    url = new URL(URL + "/users");
                    urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = urlConnection.getInputStream();
                    InputStreamReader isr = new InputStreamReader(in);

                    int data = isr.read();
                    while (data != -1) {
                        current += (char) data;
                        data = isr.read();
                    }
                    return current;
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return current;
        }

        @Override
        protected void onPostExecute(String s) {
            RequestQueue requestQueue = Volley.newRequestQueue(UserListScreen.this);

            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                    Request.Method.GET,
                    URL + "/users",
                    null,
                    response -> {
                        // Process the JSON
                        try {
                            // Loop through the array elements
                            for (int i = 0; i < response.length(); i++) {
                                // Get current json object
                                JSONObject jsonObject = response.getJSONObject(i);

                                username = jsonObject.getString("username");
                                if (jsonObject.getBoolean("isAdmin")) {
                                    accountType = "Admin";
                                } else {
                                    accountType = "User";
                                }

                                userListHashMap = new HashMap<>();
                                userListHashMap.put("username", username);
                                userListHashMap.put("accountType", accountType);
                                userArrayList.add(userListHashMap);
                                usernameArrayList.add(username);
                                accountTypeArrayList.add(accountType);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Volley Error: ", error.getMessage());
                        }
                    }
            );
            requestQueue.add(jsonArrayRequest);

        }
    }

}
