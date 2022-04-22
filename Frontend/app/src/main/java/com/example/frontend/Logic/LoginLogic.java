package com.example.frontend.Logic;

import android.util.Log;

import com.example.frontend.Entities.IUser;
import com.example.frontend.Entities.User;
import com.example.frontend.Network.IServerRequest;
import com.example.frontend.SupportingClasses.Constants;
import com.example.frontend.SupportingClasses.IView;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginLogic implements IVolleyListener {

    IView r;
    IServerRequest serverRequest;

    private User currentUser;

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

        //String authToken = serverRequest.getServerResponse().getString("auth-token");
        //boolean isAdmin = serverRequest.getServerResponse().getBoolean("isAdmin");

        //currentUser = new User(authToken, username, isAdmin);
        //Log.d("LoginLogic", currentUser.toString());
    }

    public User getCurrentUser(){
        return currentUser;
    }

    @Override
    public void onSuccess(String message) {
        r.logText(message);
        r.switchActivity();
    }

    @Override
    public void onError (String errorMessage) {
        r.logText(errorMessage);
    }
}
