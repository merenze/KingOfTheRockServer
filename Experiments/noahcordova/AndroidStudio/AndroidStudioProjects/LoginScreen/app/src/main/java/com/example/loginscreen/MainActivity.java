package com.example.loginscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.loginscreen.app.AppController;
import com.example.loginscreen.net_utils.Const;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private String username;
    private String password;
    private Button loginButton;
    private String tag_json_obj = "jobj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = (Button)findViewById(R.id.activity_main_button_login);

        loginButton.setOnClickListener(view -> {
            EditText etUsernameOrEmail = (EditText)findViewById(R.id.activity_main_et_name);
            EditText etPassword = (EditText)findViewById(R.id.activity_main_et_password);
            username = etUsernameOrEmail.getText().toString().trim();
            password = etPassword.getText().toString().trim();

            HashMap<String, String> parameters = new HashMap<String, String>();
            parameters.put("username", username);
            parameters.put("password", password);

            JSONObject jsonObject = new JSONObject(parameters);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, Const.URL_JSON_OBJECT + "/login", jsonObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("TestTag", "in onResponse body");
                            if(response.has(username)){
                                //something?
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("TestTag", "in onErrorResponse body");
                            NetworkResponse response = error.networkResponse;
                            if(error instanceof ServerError && response != null){
                                try {
                                    String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                    JSONObject obj = new JSONObject(res);
                                    if (obj.has(username)) {
                                        try {
                                            Log.d(TAG, obj.getString(username));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } catch (UnsupportedEncodingException | JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsonObjectRequest);
        });
    }


    /**
     *  TODO: actually validate with correct logic, not dummy logic
     *      * valid if receive JSON object as body of response
     *      * invalid if receive String as body of response
     */
    private boolean isValidLogin(){
        boolean isValid = true; //dummy value, change with logic based on backend response
        if (isValid) {
            return true;
        }
        else {
            return false;
        }
    }

    //JSON object request
    private void makeJsonObjReq(JSONObject jsonObject) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Const.URL_JSON_OBJECT, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        //TODO parse json object here
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                    }
        })
        {
            //Passing some request headers
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
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