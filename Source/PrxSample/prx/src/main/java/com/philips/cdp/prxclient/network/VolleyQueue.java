package com.philips.cdp.prxclient.network;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * This is Singleton class responsible to maintain all the networkrequests under one queue
 * <p/>
 * Created by naveen@philips.com on 12-Jan-16.
 */
public class VolleyQueue {

    private static VolleyQueue mVolleyQueue = null;
    private RequestQueue mRequestQueue = null;

    /**
     * Singleton class to maintain the active instance across the application.
     *
     * @return VolleyQueue
     */
    public static VolleyQueue getInstance() {
        if (mVolleyQueue == null)
            mVolleyQueue = new VolleyQueue();
        return mVolleyQueue;
    }


    /**
     * This method responsibles to maintain & serve the all the  network queues in one Object.
     *
     * @param context
     * @return RequestQueue
     */
    public RequestQueue getRequestQueue(Context context) {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return mRequestQueue;
    }

}
