/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk.network;

import android.os.Message;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.JsonArray;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.catk.CatkConstants;
import com.philips.platform.catk.ConsentAccessToolKit;
import com.philips.platform.catk.error.ConsentNetworkError;
import com.philips.platform.catk.listener.ConsentRequestListener;
import com.philips.platform.catk.listener.RefreshTokenListener;
import com.philips.platform.catk.request.ConsentRequest;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class NetworkController implements ConsentRequestListener, Response.ErrorListener {
    private NetworkAbstractModel model;

    @Inject
    RestInterface restInterface;

    public NetworkController() {
        init();
    }

    protected void init() {
        ConsentAccessToolKit.getInstance().getCatkComponent().inject(this);
    }

    public void sendConsentRequest(final NetworkAbstractModel model) {
        this.model = model;
        ConsentRequest request = getConsentJsonRequest(model);
        addRequestToQueue(request);
    }

    public void addRequestToQueue(ConsentRequest consentRequest) {
        if (consentRequest != null) {
            if (restInterface != null) {
                restInterface.getRequestQueue().add(consentRequest);
            } else {
                // Need to error handle
                Log.e("Rest client", "Couldn't initialise REST Client");
            }
        }
    }

    protected ConsentRequest getConsentJsonRequest(final NetworkAbstractModel model) {
        return new ConsentRequest(model.getMethod(), model.getUrl(), requestHeader(), model.requestBody(), this, this);
    }

    @Override
    public void onResponse(ConsentRequest request, JsonArray response) {
        postSuccessResponseOnUIThread(response);
    }

    @Override
    public void onErrorResponse(ConsentRequest request, VolleyError error) {
        if (error instanceof AuthFailureError) {
            performRefreshToken(request, error);
        } else {
            postErrorResponseOnUIThread(error);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        postErrorResponseOnUIThread(error);
    }

    public static Map<String, String> requestHeader() {
        Map<String, String> header = new HashMap<>();
        header.put("api-version", "1");
        header.put("content-type", "application/json");
        addAuthorization(header);
        header.put("performerid", ConsentAccessToolKit.getInstance().getCatkComponent().getUser().getHsdpUUID());
        header.put("cache-control", "no-cache");
        return header;
    }

    private static void addAuthorization(Map<String, String> headers) {
        headers.put("authorization", "bearer " + ConsentAccessToolKit.getInstance().getCatkComponent().getUser().getHsdpAccessToken());
    }

    private void performRefreshToken(final ConsentRequest request, final VolleyError error) {
        NetworkHelper.getInstance().refreshAccessToken(new RefreshTokenListener() {
            @Override
            public void onRefreshSuccess() {
                try {
                    addAuthorization(request.getHeaders());
                } catch (AuthFailureError authFailureError) {
                    // This should never happen since our getHeaders implementation does not throw.
                    // However, the interface enforces this exception so catch it here.
                    authFailureError.printStackTrace();
                }
                addRequestToQueue(request);
            }

            @Override
            public void onRefreshFailed(int errCode) {
                postErrorResponseOnUIThread(error);
            }
        });
    }

    private void postSuccessResponseOnUIThread(final JsonArray response) {
        if (model != null) {
            Message msg = Message.obtain();
            msg.what = model.getMethod();

            if (response != null && response.size() == 0) {
                msg.obj = CatkConstants.EMPTY_RESPONSE;
            } else {
                msg.obj = model.parseResponse(response);
            }
            model.onResponseSuccess(msg);
        }
    }

    private void postErrorResponseOnUIThread(final VolleyError error) {
        model.onResponseError(new ConsentNetworkError(error));
    }
}
