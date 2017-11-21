/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk.network;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.JsonArray;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.catk.ConsentAccessToolKit;
import com.philips.platform.catk.error.ConsentNetworkError;
import com.philips.platform.catk.listener.ConsentRequestListener;
import com.philips.platform.catk.listener.RefreshTokenListener;
import com.philips.platform.catk.request.ConsentRequest;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class NetworkController implements ConsentRequestListener, Response.ErrorListener {
    @Inject
    RestInterface restInterface;

    public NetworkController() {
        init();
    }

    protected void init() {
        ConsentAccessToolKit.getInstance().getCatkComponent().inject(this);
    }

    public void sendConsentRequest(final NetworkAbstractModel model) {
        ConsentRequest request = getConsentJsonRequest(model);
        request.setShouldCache(false);
        addRequestToQueue(request);
    }

    private void addRequestToQueue(ConsentRequest consentRequest) {
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
        return new ConsentRequest(model, model.getMethod(), model.getUrl(), requestHeader(), model.requestBody(), this, this);
    }

    @Override
    public void onResponse(ConsentRequest request, JsonArray response) {
        NetworkAbstractModel model = request.getModel();
        if (model != null) {
            model.onResponseSuccess(model.parseResponse(response));
        }
    }

    @Override
    public void onErrorResponse(ConsentRequest request, VolleyError error) {
        if (error instanceof AuthFailureError) {
            performRefreshToken(request, error);
        } else {
            NetworkAbstractModel model = request.getModel();
            if (model != null) {
                model.onResponseError(new ConsentNetworkError(error));
            }
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        // No need to handle, all response return via other 'onErrorResponse'
        Log.e("Consent access toolkit", "Error from volley", error);
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
        headers.remove("authorization");
        headers.put("authorization", "bearer " + ConsentAccessToolKit.getInstance().getCatkComponent().getUser().getHsdpAccessToken());
    }

    private void performRefreshToken(final ConsentRequest request, final VolleyError error) {
        refreshAccessToken(new RefreshTokenListener() {
            @Override
            public void onRefreshSuccess() {
                NetworkAbstractModel model = request.getModel();
                sendConsentRequest(model);
            }

            @Override
            public void onRefreshFailed(int errCode) {
                NetworkAbstractModel model = request.getModel();
                if (model != null) {
                    model.onResponseError(new ConsentNetworkError(error));
                }
            }
        });
    }

    private void refreshAccessToken(final RefreshTokenListener refreshTokenListener) {
        ConsentAccessToolKit.getInstance().getCatkComponent().getUser().refreshLoginSession(new RefreshLoginSessionHandler() {
            @Override
            public void onRefreshLoginSessionSuccess() {
                refreshTokenListener.onRefreshSuccess();
            }

            @Override
            public void onRefreshLoginSessionFailedWithError(int errCode) {
                refreshTokenListener.onRefreshFailed(errCode);
            }

            @Override
            public void onRefreshLoginSessionInProgress(String s) {
                // Need to handle
            }
        });
    }
}
