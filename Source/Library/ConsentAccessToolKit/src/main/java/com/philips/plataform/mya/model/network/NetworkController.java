package com.philips.plataform.mya.model.network;


import android.content.Context;
import android.os.Message;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.JsonArray;
import com.philips.plataform.mya.model.error.ConsentNetworkError;
import com.philips.plataform.mya.model.listener.RequestListener;
import com.philips.plataform.mya.model.request.ConsentRequest;
import com.philips.plataform.mya.model.utils.ConsentUtil;
import com.philips.platform.appinfra.AppInfra;

/**
 * Created by Maqsood on 10/12/17.
 */

public class NetworkController {

    private Context mContext;

    public void sendConsentRequest(Context context, final int requestCode, final NetworkAbstractModel model, final RequestListener requestListener) {
        mContext = context;
        sendRequest(requestCode, model, requestListener);
    }

    private void sendRequest(final int requestCode, final NetworkAbstractModel model, final RequestListener requestListener) {
        Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                Log.d("sendRequest ", "failed : " + error.getMessage());
                if (requestListener != null && error != null) {
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
                }
            }
        };

        ConsentRequest consentRequest = getConsentJsonRequest(model, error, response);
        addRequestToQueue(consentRequest,mContext);
    }

    private void addRequestToQueue(ConsentRequest consentRequest, Context context){
        AppInfra ai = new AppInfra.Builder().build(context);
        if (consentRequest != null) {
            if (ai.getRestClient() != null) {
                ai.getRestClient().getRequestQueue().add(consentRequest);
            } else {
                Log.d("Rest client", "Couldn't initialise REST Client");

            }
        }
    }

    ConsentRequest getConsentJsonRequest(final NetworkAbstractModel model, final Response.ErrorListener error, final Response.Listener<JsonArray> response) {
        return new ConsentRequest(model.getMethod(), model.getUrl(),
                model.requestHeader(), model.requestBody(), response, error);
    }
}
