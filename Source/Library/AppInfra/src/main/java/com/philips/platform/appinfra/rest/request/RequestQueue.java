package com.philips.platform.appinfra.rest.request;

import android.os.Handler;
import android.os.Looper;

import com.android.volley.Cache;
import com.android.volley.ExecutorDelivery;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.ResponseDelivery;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;


/**
 * A wrapper clas of request dispatch queue with a thread pool of dispatchers.
 *
 * Calling {@link #add(Request)} will enqueue the given Request for dispatch,
 * resolving from either cache or network on a worker thread, and then delivering
 * a parsed response on the main thread.
 */

public class RequestQueue extends com.android.volley.RequestQueue {
    private final ResponseDelivery mHttpErrorDelivery;

    public RequestQueue(Cache cache, Network network) {
        super(cache, network);
        VolleyLog.DEBUG = false;
        mHttpErrorDelivery = new ExecutorDelivery(new Handler(Looper.getMainLooper()));
    }

    @Override
    public <T> Request<T> add(Request<T> request) {
        final String url = request.getUrl();
        if (!url.trim().toLowerCase().startsWith("https://")) {
            if (url.trim().startsWith("serviceid://")) {
                return super.add(request);
            } else {
                mHttpErrorDelivery.postError(request, new VolleyError("HttpForbiddenException-http calls are" +
                        " deprecated use https calls only"));
                return null;
            }
        }
        return super.add(request);
    }
}