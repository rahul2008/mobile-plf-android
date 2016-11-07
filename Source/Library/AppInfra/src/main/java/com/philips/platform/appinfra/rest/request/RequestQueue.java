package com.philips.platform.appinfra.rest.request;

import android.os.Handler;
import android.os.Looper;

import com.android.volley.Cache;
import com.android.volley.ExecutorDelivery;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.ResponseDelivery;
import com.android.volley.VolleyError;


/**
 * Created by 310243577 on 11/4/2016.
 */

public class RequestQueue extends com.android.volley.RequestQueue {
    private final ResponseDelivery mHttpErrorDelivery;

    public RequestQueue(Cache cache, Network network) {
        super(cache, network);
        mHttpErrorDelivery = new ExecutorDelivery(new Handler(Looper.getMainLooper()));
    }

    @Override
    public <T> Request<T> add(Request<T> request) {
        String url = request.getUrl();
        if (!url.toLowerCase().startsWith("https://") && !isValidURL(url)) {
            mHttpErrorDelivery.postError(request, new VolleyError("HttpForbiddenException"));
            return null;
        }
        return super.add(request);
    }

    private boolean isValidURL(String url) {
        boolean isValidurl = false;
        String regex = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        if (null != url && url.matches(regex)) {
            isValidurl = true;
        }
        return isValidurl;
    }

}