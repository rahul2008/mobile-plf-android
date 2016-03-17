/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.session;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.model.NewOAuthRequest;
import com.philips.cdp.di.iap.store.IAPUser;

import org.json.JSONObject;

public class TokenErrorHandler {
    private RequestListener mRequestListener;
    private AbstractModel mModel;
    private OAuthHandler mAuthHandler;
    private Context mContext;

    private String mJanRainToken;

    public TokenErrorHandler(AbstractModel model, RequestListener requestListener) {
        mModel = model;
        mRequestListener = requestListener;
        handleJanRainFailure();
    }

    public void handleJanRainFailure() {
        mModel.getStore().getUser().refreshUser(new IAPUser.TokenRefreshCallBack() {
            @Override
            public void onTokenRefresh(boolean success) {
                if (!success) {
                    proceedCallWithOAuth();
                } else {
                    //Todo add notify back logic
                }
            }
        });
    }

    private void proceedCallWithOAuth() {
        NewOAuthRequest request = new NewOAuthRequest(mModel.getStore(), null);
        SynchronizedNetwork network = new SynchronizedNetwork(new IAPHurlStack(request).getHurlStack());
        network.performRequest(createOAuthRequest(request));
    }

    private IAPJsonRequest createOAuthRequest(final NewOAuthRequest request) {
        IAPJsonRequest jsonRequest = new IAPJsonRequest(request.getMethod(), request.getUrl(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {
                        proceedWithOriginalRequest(request);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(final VolleyError error) {

                    }
                });
        return jsonRequest;
    }

    private void proceedWithOriginalRequest(OAuthHandler handler) {
        SynchronizedNetwork network = new SynchronizedNetwork(new IAPHurlStack(handler).getHurlStack());
        network.performRequest(createOriginalRequest(mModel));
    }

    private IAPJsonRequest createOriginalRequest(final AbstractModel request) {
        IAPJsonRequest jsonRequest = new IAPJsonRequest(request.getMethod(), request.getUrl(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(final VolleyError error) {

                    }
                });
        return jsonRequest;
    }
}
