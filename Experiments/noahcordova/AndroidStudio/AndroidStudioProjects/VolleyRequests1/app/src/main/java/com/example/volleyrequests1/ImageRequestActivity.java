package com.example.volleyrequests1;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.Cache;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.volleyrequests1.app.AppController;
import com.example.volleyrequests1.net_utils.Const;

import java.io.UnsupportedEncodingException;

public class ImageRequestActivity extends Activity {
    private static final String TAG = ImageRequestActivity.class.getSimpleName();
    private Button btnImageReq;
    private NetworkImageView imgNetWorkView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_request);
        btnImageReq = (Button) findViewById(R.id.activity_image_request_button);
        imgNetWorkView = (NetworkImageView) findViewById(R.id.activity_image_request_network_image);
        imageView = (ImageView) findViewById(R.id.activity_image_request_image_view);
        btnImageReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeImageRequest();
            }
        });
    }

    private void makeImageRequest(){
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        //NetworkImageView
        imgNetWorkView.setImageUrl(Const.URL_IMAGE, imageLoader);

        imageLoader.get(Const.URL_IMAGE, ImageLoader.getImageListener(
                imageView, R.drawable.ico_loading, R.drawable.ico_error));
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(Const.URL_IMAGE);
        if(entry != null){
            try {
                String data = new String(entry.data, "UTF-8");
            // handle data, like converting it to xml, json, bitmap etc.,
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else{
            // cached response doesn't exists. Make a network call here
        }

    }

}