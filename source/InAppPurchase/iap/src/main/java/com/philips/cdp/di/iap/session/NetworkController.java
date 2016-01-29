/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.session;

import android.content.Context;
import android.os.Message;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.philips.cdp.di.iap.model.ModelQuery;

public class NetworkController {
    RequestQueue volleyQueue;
    Context context;

    NetworkController(Context context) {
        this.context = context;
        volleyQueue = Volley.newRequestQueue(context, new HurlStack());
    }

    public void sendRequest(int requestCode, final RequestListener requestListener) {
        ModelQuery model = getModel(requestCode);
        volleyQueue.add(createRequest(requestCode, model,requestListener));
    }

    private JsonObjectRequest createRequest(final int requestCode, final ModelQuery modelQuery , final RequestListener requestListener) {
        Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                Message msg = Message.obtain();
                msg.what = requestCode;
                requestListener.onError(msg);
            }
        };

        Response.Listener response = new Response.Listener<JsonObject>() {

            @Override
            public void onResponse(final JsonObject response) {
                Message msg = Message.obtain();
                msg.what = requestCode;
                msg.obj = modelQuery.parseResponse(response);
                requestListener.onSuccess(msg);
            }
        };

        return new JsonObjectRequest(modelQuery.getMethod(), modelQuery.getUrl(requestCode),
                modelQuery.reqeustBody(), response,error);
    }

    //Add model specific implementation
    private ModelQuery getModel(final int requestCode) {
        return null;
    }
}
