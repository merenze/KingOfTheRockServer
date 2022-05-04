package com.example.frontend.SupportingClasses;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Class for controlling a static instance of volley request queue.
 *
 * @author Dan Rosenhamer
 */
public class AppController extends Application {

    public static final String TAG = AppController.class
            .getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    /**
     * @return this AppController instance
     */
    public static synchronized AppController getInstance() {
        return mInstance;
    }

    /**
     * Returns the current instance of volley request queue.
     * Returns a new instance of volley request queue if none already exists.
     *
     * @return instance of volley request queue
     */
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    /**
     * Returns the current ImageLoader from the current instance of volley request queue.
     * Returns a new ImageLoader if none already exists.
     *
     * @return ImageLoader
     */
    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    /**
     * Adds a request to the volley request queue with given tag
     *
     * @param req request to be added to the queue
     * @param tag tag to identify the addition
     */
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    /**
     * Adds a request to the volley request queue with given tag
     *
     * @param req request to be added to the queue
     */
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    /**
     * Cancels any requests in the volley request queue
     *
     * @param tag tag to identify the cancellation
     */
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
