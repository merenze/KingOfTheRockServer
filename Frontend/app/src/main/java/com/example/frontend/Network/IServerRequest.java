package com.example.frontend.Network;

import com.example.frontend.Logic.IVolleyListener;

import org.json.JSONObject;

public interface IServerRequest {
    public JSONObject getServerResponse();

    public void sendToServer(String url, JSONObject newUserObj, String methodType);

    public void addVolleyListener(IVolleyListener logic);
}
