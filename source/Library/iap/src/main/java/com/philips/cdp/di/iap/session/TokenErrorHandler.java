/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.session;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.model.NewOAuthRequest;
import com.philips.cdp.di.iap.response.oauth.OAuthResponse;

public class TokenErrorHandler {
    private RequestListener mRequestListener;
    private AbstractModel mModel;
    private OAuthHandler mAuthHandler;
    private Context mContext;

    private String mJanRainToken;
    private String mAccessToken;

    public TokenErrorHandler(AbstractModel model, RequestListener requestListener) {
        mModel = model;
        mRequestListener = requestListener;
        handleJanRainFailure();
    }

    public void handleJanRainFailure() {
        mModel.getStore().getUser().refreshLoginSession();
    }

    public void proceedCallWithOAuth() {
        NewOAuthRequest request = new NewOAuthRequest(mModel.getStore(), null);
        SynchronizedNetwork network = new SynchronizedNetwork(new IAPHurlStack(request).getHurlStack());
        network.performRequest(createOAuthRequest(request), new SynchronizedNetworkCallBack() {
            @Override
            public void onSyncRequestSuccess(final Response response) {
                if (response != null && response.result != null) {
                    final OAuthResponse authResponse = new Gson().fromJson(response.result.toString(),
                            OAuthResponse.class);
                    mAccessToken = authResponse.getAccessToken();
                }
            }

            @Override
            public void onSyncRequestError(final VolleyError volleyError) {

            }
        });
    }

    private IAPJsonRequest createOAuthRequest(final NewOAuthRequest request) {
        IAPJsonRequest jsonRequest = new IAPJsonRequest(request.getMethod(), request.getUrl(),
                null,null,null);
        return jsonRequest;
    }

    public String getAccessToken() {
        return mAccessToken;
    }
}
