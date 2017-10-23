package com.philips.plataform.mya.model.network;


import android.content.Context;
import android.os.Message;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.philips.plataform.mya.model.error.ConsentNetworkError;
import com.philips.plataform.mya.model.listener.RequestListener;
import com.philips.plataform.mya.model.request.ConsentRequest;
import com.philips.plataform.mya.model.utils.ConsentUtil;

/**
 * Created by Maqsood on 10/12/17.
 */

public class NetworkController {

    private Context mContext;
    public void sendConsentRequest(Context context, final int requestCode, final NetworkAbstractModel model, final RequestListener requestListener) {
        mContext = context;
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendRequest(requestCode, model, requestListener);
            }
        }).start();
    }

    private void sendRequest(final int requestCode, final NetworkAbstractModel model, final RequestListener requestListener) {
        RequestQueue queue = Volley.newRequestQueue(mContext);
        Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                Log.d("sendRequest ","failed : "+error.getMessage());
                if (model.getUrl() != null && error != null) {
                    Log.d("Error", "Response from Consent request onError =" + error
                            .getLocalizedMessage() + " requestCode=" + requestCode + "in " +
                            requestListener.getClass().getSimpleName() + " " + model.getUrl());
                }
                if (requestListener != null) {
                    new ConsentNetworkError(error, requestCode, requestListener);
                }
            }
        };

        Response.Listener<JsonArray> response = new Response.Listener<JsonArray>() {

            @Override
            public void onResponse(JsonArray response) {
                if (requestListener != null) {
                    Message msg = Message.obtain();
                    msg.what = requestCode;

                    if (response != null && response.size() == 0) {
                        msg.obj = ConsentUtil.EMPTY_RESPONSE;
                    } else {
                        msg.obj = model.parseResponse(response);
                    }
                    requestListener.onResponseSuccess(msg);

                    if (model.getUrl() != null) {
                        Log.d("Response", "Response =" + msg + " requestCode=" + requestCode + "in " +
                                requestListener.getClass().getSimpleName() + "env = " + " " + model.getUrl());
                    }
                }
            }
        };

        ConsentRequest consentRequest = getConsentJsonRequest(model, error, response);
        queue.add(consentRequest);
    }

    ConsentRequest getConsentJsonRequest(final NetworkAbstractModel model, final Response.ErrorListener error, final Response.Listener<JsonArray> response) {
        Log.d("sendRequest ","header : "+model.requestHeader());
        Log.d("sendRequest","url : "+model.getUrl());
        Log.d("sendRequest","method : "+model.getMethod());
        return new ConsentRequest(model.getMethod(), model.getUrl(),
                model.requestHeader(),model.requestBody(), response, error);
    }
}
