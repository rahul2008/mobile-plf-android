package com.philips.platform.catk.network;


import android.os.Message;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.JsonArray;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.catk.CatkConstants;
import com.philips.platform.catk.ConsentAccessToolKit;
import com.philips.platform.catk.error.ConsentNetworkError;
import com.philips.platform.catk.listener.RequestListener;
import com.philips.platform.catk.request.ConsentRequest;

import javax.inject.Inject;

public class NetworkController {

    @Inject
    RestInterface restInterface;

    public NetworkController(){
        init();
    }

    protected void init() {
        ConsentAccessToolKit.getInstance().getCatkComponent().inject(this);
    }

    public void sendConsentRequest(final NetworkAbstractModel model, final RequestListener requestListener) {
        sendRequest(model, requestListener);
    }

    private void sendRequest(final NetworkAbstractModel model, final RequestListener requestListener) {
        Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                if (requestListener != null && error != null) {
                    new ConsentNetworkError(error, model.getMethod(), requestListener);
                }
            }
        };

        Response.Listener<JsonArray> response = new Response.Listener<JsonArray>() {
            @Override
            public void onResponse(JsonArray response) {
                if (requestListener != null) {
                    Message msg = Message.obtain();
                    msg.what = model.getMethod();

                    if (response != null && response.size() == 0) {
                        msg.obj = CatkConstants.EMPTY_RESPONSE;
                    } else {
                        msg.obj = model.parseResponse(response);
                    }
                    requestListener.onResponseSuccess(msg);
                }
            }
        };

        ConsentRequest consentRequest = getConsentJsonRequest(model, error, response);
        addRequestToQueue(consentRequest);
    }

    public void addRequestToQueue(ConsentRequest consentRequest){
        if (consentRequest != null) {
            if (restInterface != null) {
                restInterface.getRequestQueue().add(consentRequest);
            } else {
                //Need to error handle
                Log.d("Rest client", "Couldn't initialise REST Client");

            }
        }
    }

    protected ConsentRequest getConsentJsonRequest(final NetworkAbstractModel model, final Response.ErrorListener error, final Response.Listener<JsonArray> response) {
        return new ConsentRequest(model.getMethod(), model.getUrl(),
                model.requestHeader(), model.requestBody(), response, error);
    }
}
