package com.example.frontend.Logic;

import com.example.frontend.Network.IServerRequest;
import com.example.frontend.SupportingClasses.Constants;
import com.example.frontend.SupportingClasses.IView;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginLogic implements IVolleyListener {

    IView r;
    IServerRequest serverRequest;

    private static String authToken;
    private static String currentUsername;
    private static boolean isAdmin;

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

    public String getAuthToken(){
        return authToken;
    }

    public String getCurrentUsername(){
        return currentUsername;
    }

    public boolean getIsAdmin(){
        return isAdmin;
    }

    @Override
    public void onSuccess(String message) {
        try {
            authToken = serverRequest.getServerResponse().getString("authToken");
            currentUsername = serverRequest.getServerResponse().getString("username");
            isAdmin = serverRequest.getServerResponse().getBoolean("isAdmin");
        } catch (JSONException exception) {
            exception.printStackTrace();
        }

        r.switchActivity();
    }

    @Override
    public void onError (String errorMessage) {
        r.logText(errorMessage);
    }
}
