package com.example.frontend;

import static com.example.frontend.SupportingClasses.Constants.URL;
import static com.example.frontend.SupportingClasses.Constants.tag_json_arr;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.frontend.Entities.IUser;
import com.example.frontend.SupportingClasses.AppController;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Class for the logic of the screen to search for other users
 *
 * @author Noah Cordova
 */
public class SearchForUserScreen extends AppCompatActivity {

    private String TAG = SearchForUserScreen.class.getSimpleName();
    private IUser currentUser;
    private String usernameEntry;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_for_user_screen);

        currentUser = LoginScreen.getCurrentUser();
        Log.d(TAG, currentUser.toString());

        searchButton = (Button) findViewById(R.id.activity_search_for_user_button_search);

        searchButton.setOnClickListener(view -> {
            EditText etUsernameEntry = (EditText) findViewById(R.id.activity_search_for_user_screen_et_searchEntry);
            usernameEntry = etUsernameEntry.getText().toString().trim();
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                    (Request.Method.GET, URL + "/search/?q=" + usernameEntry + "?auth-token=" + LoginScreen.getAuthToken(), null, new Response.Listener<>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.d(tag_json_arr, response.toString());

                            // check that array is non-empty
                            try {
                                response.get(0); // throws JSONException if empty
                            } catch (JSONException exception) {
                                Log.d(tag_json_arr, "Username not found");
                            }

                            // JSONArray into Array<String> to pass to adapter
                            ArrayList<String> listFromJSONArrayResponse = JSONArrayToList(response);
                            ArrayAdapter adapter = new ArrayAdapter<>(getApplicationContext(),
                                    R.layout.activity_search_for_user_screen_listview, listFromJSONArrayResponse);

                            ListView listView = (ListView) findViewById(R.id.activity_search_for_user_screen_lv_users);
                            listView.setAdapter(adapter);

                        }
                    }, error -> Log.d(SearchForUserScreen.class.toString(), "Error searching users: " + error.getMessage()));

            //Add request to queue
            AppController.getInstance().addToRequestQueue(jsonArrayRequest, tag_json_arr);
        });
    }

    private ArrayList<String> JSONArrayToList(JSONArray jarray) {
        ArrayList<String> myList = new ArrayList<>();
        for (int i = 0; i < jarray.length(); i++) {
            try {
                myList.add(jarray.getJSONObject(i).getString("username"));
            } catch (JSONException exception) {
                Log.d(SearchForUserScreen.class.toString(), "Error adding username to user arraylist");
                exception.printStackTrace();
            }
        }
        return myList;
    }

}