/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk;

import android.util.Log;

import com.android.volley.VolleyError;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.catk.error.ConsentNetworkError;
import com.philips.platform.catk.listener.RefreshTokenListener;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;


public class NetworkController implements AuthErrorListener {
    @Inject
    RestInterface restInterface;

    RefreshTokenHandler refreshTokenHandler;


    NetworkController() {
        init();
    }

    void init() {
        ConsentsClient.getInstance().getCatkComponent().inject(this);
        refreshTokenHandler = new RefreshTokenHandler(ConsentsClient.getInstance().getCatkComponent().getUser());
    }

    void sendConsentRequest(final NetworkAbstractModel model) {
        ConsentRequest request = getConsentJsonRequest(model);
        request.setShouldCache(false);
        addRequestToQueue(request);
    }

    private void addRequestToQueue(ConsentRequest consentRequest) {
        if (consentRequest != null) {
            if (restInterface != null) {
                restInterface.getRequestQueue().add(consentRequest);
            } else {
                Log.e("Rest client", "Couldn't initialise REST Client");
            }
        }
    }

    ConsentRequest getConsentJsonRequest(final NetworkAbstractModel model) {
        return new ConsentRequest(model, model.getMethod(), model.getUrl(), requestHeader(), model.requestBody(), this);
    }

    @Override
    public void onAuthError(final NetworkAbstractModel model, final VolleyError error) {
        refreshTokenHandler.refreshToken(new RefreshTokenListener() {
            @Override
            public void onRefreshSuccess() {
                sendConsentRequest(model);
            }

            @Override
            public void onRefreshFailed(int errCode) {
                if (model != null) {
                    model.onResponseError(new ConsentNetworkError(error));
                }
            }
        });
    }


    public static Map<String, String> requestHeader() {
        Map<String, String> header = new HashMap<>();
        header.put("api-version", "1");
        header.put("content-type", "application/json");
        addAuthorization(header);
        header.put("performerid", ConsentsClient.getInstance().getCatkComponent().getUser().getHsdpUUID());
        header.put("cache-control", "no-cache");
        return header;
    }

    private static void addAuthorization(Map<String, String> headers) {
        headers.remove("authorization");
        headers.put("authorization", "bearer " + ConsentsClient.getInstance().getCatkComponent().getUser().getHsdpAccessToken());
    }

}
