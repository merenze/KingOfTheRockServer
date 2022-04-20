package com.example.frontend;

import static com.example.frontend.SupportingClasses.Constants.URL;
import static com.example.frontend.SupportingClasses.Constants.tag_json_arr;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.frontend.Entities.IUser;
import com.example.frontend.SupportingClasses.AppController;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Class for the logic of the screen to display a list of all users
 *
 * @author Dan Rosenhamer
 */
public class UserListScreen extends AppCompatActivity {

    private IUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list_screen);

        currentUser = LoginScreen.getCurrentUser();

        //Request to pull list of users from the backend
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL + "/users" + "?auth-token=" + currentUser.getAuthToken(),
                null,
                response -> {
                    // Process the JSON
                    try {
                        response.get(0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // JSONArray into Array<String> to pass to adapter
                    ArrayList<String> listFromJSONArrayResponse = JSONArrayToList(response);
                    ArrayAdapter adapter = new ArrayAdapter<String>(getApplicationContext(),
                            R.layout.activity_individual_user, listFromJSONArrayResponse);

                    ListView listView = (ListView) findViewById(R.id.user_list_screen_listview);
                    listView.setAdapter(adapter);

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error: ", error.getMessage());
                    }
                }
        );

        //Add request to queue
        AppController.getInstance().addToRequestQueue(jsonArrayRequest, tag_json_arr);
    }

    /*
    Converts JSONArray to an ArrayList
     */
    private ArrayList<String> JSONArrayToList(JSONArray jarray){
        ArrayList<String> myList = new ArrayList<String>();
        for (int i = 0; i < jarray.length(); i++)
        {
            try {
                if(jarray.getJSONObject(i).getBoolean("isAdmin")){
                    myList.add(jarray.getJSONObject(i).getString("username") + "\n\t\t" + "Admin");
                } else {
                    myList.add(jarray.getJSONObject(i).getString("username") + "\n\t\t" + "Player");
                }
            } catch (JSONException exception) {
                exception.printStackTrace();
            }
        }
        return myList;
    }

}
