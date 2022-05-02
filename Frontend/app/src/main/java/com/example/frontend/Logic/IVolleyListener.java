package com.example.frontend.Logic;

import org.json.JSONObject;

public interface IVolleyListener {
    public void onSuccess(JSONObject response);

    public void onError(String message);
}
