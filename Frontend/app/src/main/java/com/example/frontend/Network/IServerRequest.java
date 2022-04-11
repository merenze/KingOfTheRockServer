package com.example.frontend.Network;

import org.json.JSONObject;

import com.example.frontend.Logic.IVolleyListener;

public interface IServerRequest {
    public void sendToServer(String url, JSONObject newUserObj, String methodType);
    public void addVolleyListener(IVolleyListener logic);
}
