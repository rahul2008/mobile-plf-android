package com.philips.plataform.mya.model.network;


import android.content.Context;
import android.os.Message;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;
import com.philips.plataform.mya.model.listener.RequestListener;
import com.philips.plataform.mya.model.request.ConsentRequest;
import com.philips.plataform.mya.model.utils.ConsentUtil;

/**
 * Created by Maqsood on 10/12/17.
 */

public class NetworkController {

    private Context mContext;
    private User mUser;
    public void sendConsentRequest(Context context, final int requestCode, final NetworkAbstractModel model, final RequestListener requestListener) {
        mContext = context;
        mUser = new User(context);
        new Thread(new Runnable() {
            @Override
            public void run() {

                //Need to change this
                mUser.refreshLoginSession(new RefreshLoginSessionHandler() {
                    @Override
                    public void onRefreshLoginSessionSuccess() {
                        Log.d("Refresh user","Success : ");
                        sendRequest(requestCode, model, requestListener);
                    }

                    @Override
                    public void onRefreshLoginSessionFailedWithError(int errorCode) {
                        //Need to handle
                        Log.d("Refresh user","failed : "+errorCode);
                    }

                    @Override
                    public void onRefreshLoginSessionInProgress(String inProgress) {
                        //Need to handle
                    }
                });
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
                    //Need to handle
                }
                if (error != null && error.getMessage() != null) {
                    //Need to handle
                }
                if (requestListener != null) {
                    //Need to handle
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
                        //Need to handle
                    }
                }
            }
        };

        ConsentRequest consentRequest = getConsentJsonRequest(model, error, response);
        queue.add(consentRequest);
    }

    ConsentRequest getConsentJsonRequest(final NetworkAbstractModel model, final Response.ErrorListener error, final Response.Listener<JsonArray> response) {
        String url = model.getUrl();
        url += "?applicationName=OneBackend&propositionName=OneBackendProp";
        Log.d("sendRequest ","header : "+model.requestHeader());
        Log.d("sendRequest","url : "+model.getUrl());
        Log.d("sendRequest","method : "+model.getMethod());
        return new ConsentRequest(model.getMethod(), url,
                model.requestHeader(),model.requestBody(), response, error);
    }
}
