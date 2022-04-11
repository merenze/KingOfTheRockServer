package com.example.frontend.Logic;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.frontend.Constants;
import com.example.frontend.LoginScreen;
import com.example.frontend.RegisterScreen;
import com.example.frontend.SupportingClasses.IView;
import com.example.frontend.Network.IServerRequest;

public class RegistrationLogic extends AppCompatActivity implements IVolleyListener {

    IView r;
    IServerRequest serverRequest;

    public RegistrationLogic(IView r, IServerRequest serverRequest) {
        this.r = r;
        this.serverRequest = serverRequest;
        serverRequest.addVolleyListener(this);
    }

    public void registerUser(String email, String username, String password, boolean adminBool) throws JSONException {
        String url =  Constants.URL + "/register";
        JSONObject newUserObj = new JSONObject();
        newUserObj.put("email", email);
        newUserObj.put("username", username);
        newUserObj.put("password", password);
        newUserObj.put("isAdmin", adminBool);

        serverRequest.sendToServer(url, newUserObj, "POST");
    }

    @Override
    public void onSuccess(String email) {
        if (email.length() > 0) {
            startActivity(new Intent(view.getContext(), LoginScreen.class));
        } else {
            r.logText("Error with request, please try again");
        }
    }

    @Override
    public void onError (String errorMessage) {
        r.logText(errorMessage);
    }
}
