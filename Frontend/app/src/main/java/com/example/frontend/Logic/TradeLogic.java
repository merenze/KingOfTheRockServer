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

import java.util.Locale;

public class TradeLogic implements IVolleyListener {

    IView r;
    IServerRequest serverRequest;

    private String TAG = TradeLogic.class.getSimpleName();
    private IUser currentUser;

    public TradeLogic(IView r, IServerRequest serverRequest, IUser currentUser) {
        this.r = r;
        this.serverRequest = serverRequest;
        serverRequest.addVolleyListener(this);
        this.currentUser = currentUser;
    }

    public void submitTrade(int stoneQuantity, int woodQuantity, int foodQuantity, int waterQuantity) throws JSONException {
        Log.d(TAG, "attempting to create a trade request...");
        //String url =  Constants.URL + "/trade" + "?auth-token=" + currentUser.getAuthToken();
        String url =  "https://ec47ead7-50a1-4b83-a6e6-10fdf0916962.mock.pstmn.io/build?auth-token=000001";

        JSONObject newTradeObj = new JSONObject();
        newTradeObj.put("stone", stoneQuantity);
        newTradeObj.put("wood", woodQuantity);
        newTradeObj.put("food", foodQuantity);
        newTradeObj.put("water", waterQuantity);

        Log.d(TAG, "sending trade request...");
        serverRequest.sendToServer(url, newTradeObj, "POST");
    }

    public IUser getCurrentUser(){
        return currentUser;
    }

    @Override
    public void onSuccess(JSONObject response) {
        r.logText(response.toString());
        //r.makeToast(structureToBuild.substring(0,1).toUpperCase() + structureToBuild.substring(1) + " was built");
    }

    @Override
    public void onError (String errorMessage) {
        r.logText(errorMessage);
    }
}
