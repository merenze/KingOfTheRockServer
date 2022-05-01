package com.example.frontend.Logic;

import android.util.Log;
import android.widget.Toast;

import com.example.frontend.Entities.IUser;
import com.example.frontend.Entities.User;
import com.example.frontend.Network.IServerRequest;
import com.example.frontend.SupportingClasses.Constants;
import com.example.frontend.SupportingClasses.IView;

import org.json.JSONException;
import org.json.JSONObject;

public class BuildLogic implements IVolleyListener {

    IView r;
    IServerRequest serverRequest;

    private String TAG = BuildLogic.class.getSimpleName();
    private IUser currentUser;

    public BuildLogic(IView r, IServerRequest serverRequest) {
        this.r = r;
        this.serverRequest = serverRequest;
        serverRequest.addVolleyListener(this);
    }

    public void buildStructure(String name) throws JSONException {
        Log.d(TAG, "attempting to build a structure...");
        String url =  Constants.URL + "/build";

        JSONObject newBuildObj = new JSONObject();
        newBuildObj.put("structure", name);

        Log.d(TAG, "sending build request...");
        serverRequest.sendToServer(url, newBuildObj, "POST");
    }

    public IUser getCurrentUser(){
        return currentUser;
    }

    @Override
    public void onSuccess(JSONObject response) {
        r.logText(response.toString());
    }

    @Override
    public void onError (String errorMessage) {
        r.logText(errorMessage);
    }
}
