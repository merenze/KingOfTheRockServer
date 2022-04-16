package com.example.frontend.Logic;

import com.example.frontend.Network.IServerRequest;
import com.example.frontend.SupportingClasses.Constants;
import com.example.frontend.SupportingClasses.IView;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginLogic implements IVolleyListener {

    IView r;
    IServerRequest serverRequest;

    public LoginLogic(IView r, IServerRequest serverRequest) {
        this.r = r;
        this.serverRequest = serverRequest;
        serverRequest.addVolleyListener(this);
    }

    public void loginUser(String username, String password) throws JSONException {
        String url =  Constants.URL + "/login";
        JSONObject newUserObj = new JSONObject();
        newUserObj.put("username", username);
        newUserObj.put("password", password);

        serverRequest.sendToServer(url, newUserObj, "POST");
    }

    @Override
    public void onSuccess(String message) {
        r.switchActivity();
    }

    @Override
    public void onError (String errorMessage) {
        r.logText(errorMessage);
    }
}
