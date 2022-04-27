package com.example.frontend.Network;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import com.example.frontend.SupportingClasses.AppController;
import com.example.frontend.Logic.IVolleyListener;

public class ServerRequest implements IServerRequest {

    private String tag_json_obj = "json_obj_req";
    private IVolleyListener l;
    private JSONObject serverResponse;

    @Override
    public void sendToServer(String url, JSONObject newUserObj, String methodType) {

        int method = Request.Method.GET;
        if (methodType.equals("POST")) {
            method = Request.Method.POST;
        }

        JsonObjectRequest userRequest = new JsonObjectRequest(method, url, newUserObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("ServerRequest", "in onResponse method");
                        if (response != null ) {
                            Log.d("ServerRequest", response.toString());
                            serverResponse = response;
                            l.onSuccess(response);
                        } else {
                            l.onError("Null Response object received");
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ServerRequest", "in onErrorResponse method");
                        if (error.getMessage() != null){
                            l.onError(error.getMessage());
                        } else {
                            l.onError("Error, no error message received");
                        }
                    }
                }
        );

        AppController.getInstance().addToRequestQueue(userRequest, tag_json_obj);
    }

    public JSONObject getServerResponse(){
        return serverResponse;
    }

    @Override
    public void addVolleyListener(IVolleyListener logic) {
        l = logic;
    }
}
