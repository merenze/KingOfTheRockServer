package com.example.volleyrequests1;

import com.example.volleyrequests1.app.AppController;
import com.example.volleyrequests1.net_utils.Const;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;


public class StringRequestActivity extends Activity {

    private String TAG = StringRequestActivity.class.getSimpleName();
    private Button btnStringReq;
    private TextView msgResponse;
    private ProgressDialog pDialog;
    private String tag_string_req = "string_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_string_request);

        btnStringReq = (Button) findViewById(R.id.activity_string_request_button);
        msgResponse = (TextView) findViewById(R.id.activity_string_request_textview_response);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);

        btnStringReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeStringReq();
            }
        });
    }

    private void makeStringReq() {
        if (!pDialog.isShowing())
            pDialog.show();

        StringRequest strReq = new StringRequest(Method.GET,
                Const.URL_STRING_REQ, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                msgResponse.setText(response.toString());
                //hideProgressDialog
                if (pDialog.isShowing())
                    pDialog.hide();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //hideProgressDialog
                if (pDialog.isShowing())
                    pDialog.hide();
            }
        });
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}