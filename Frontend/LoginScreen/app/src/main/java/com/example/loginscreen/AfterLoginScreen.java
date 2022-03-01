package com.example.loginscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.loginscreen.app.AppController;
import com.example.loginscreen.net_utils.Const;

import org.json.JSONException;
import org.json.JSONObject;

public class AfterLoginScreen extends AppCompatActivity {
    private String TAG = AfterLoginScreen.class.getSimpleName();
    private TextView loginCredentials;
    private String tag_json_obj = "jobj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login_screen);

        loginCredentials = (TextView)findViewById(R.id.activity_after_login_screen_tv_loginCredentials);
    }

    //JSON object request
    private void makeJsonObjReq() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Const.URL_JSON_OBJECT, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TestingTag", "Inside onResponse()");
                        // null if login credentials are incorrect
                        if(response != null) {
                            Log.d(TAG, response.toString());
                            loginCredentials.setText(response.toString());
                        }
                        else {
                            Log.d(TAG, "Error");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        })
                //headers (not necessary?)
        {
            /*
             * Passing some request headers
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
             */
        };

        //add request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    private JSONObject makeJsonObjectForLogin(String usernameOrEmail, String password) throws JSONException {
        JSONObject loginCredentialsJsonObject = new JSONObject();
        //currently only accepts username, change as project moves along
        loginCredentialsJsonObject.put("username", usernameOrEmail);
        loginCredentialsJsonObject.put("password", password);

        return loginCredentialsJsonObject;
    }
}