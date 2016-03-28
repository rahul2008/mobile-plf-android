/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.session;

import android.os.Message;

import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.model.ModelConstants;
import com.philips.cdp.di.iap.model.NewOAuthRequest;
import com.philips.cdp.di.iap.model.RefreshOAuthRequest;
import com.philips.cdp.di.iap.store.Store;
import com.philips.cdp.di.iap.utils.IAPLog;

import java.util.HashMap;

public class TestEnvOAuthHandler implements OAuthHandler {
    private final String TAG = TestEnvOAuthHandler.class.getSimpleName();

    private String access_token;
    private NewOAuthRequest mOAuthRequest;
    private Store mStore;

    public TestEnvOAuthHandler() {
    }

    @Override
    public String getAccessToken() {
        if (mOAuthRequest == null) {
            mStore = HybrisDelegate.getInstance().getStore();
            mOAuthRequest = new NewOAuthRequest(mStore, null);
        }
        if (access_token == null) {
            requestSyncOAuthToken();
        }
        return access_token;
    }

    @Override
    public void refreshToken(RequestListener listener) {
        IAPLog.d(TAG,"requesting new access token using refreshtoken");
        HashMap<String, String> params = new HashMap<>();
        params.put(ModelConstants.REFRESH_TOKEN,mOAuthRequest.getrefreshToken());
        RefreshOAuthRequest request = new RefreshOAuthRequest(mStore, params);
        requestSyncRefreshToken(request, listener);
    }

    private void requestSyncOAuthToken() {
        SynchronizedNetwork network = new SynchronizedNetwork(new IAPHurlStack(mOAuthRequest).getHurlStack());
        network.performRequest(createOAuthRequest(mOAuthRequest), new SynchronizedNetworkCallBack() {
            @Override
            public void onSyncRequestSuccess(final Response response) {
                if (response != null && response.result != null) {
                    mOAuthRequest.parseResponse(response.result);
                    access_token = mOAuthRequest.getAccessToken();
                }
            }

            @Override
            public void onSyncRequestError(final VolleyError volleyError) {
                if(volleyError instanceof ServerError) {
                    //Try generating new JanRain token.
                    //Need to optimize this to handle other server errors
                    mStore.refreshLoginSession();
                    if(mStore.getUser().isTokenRefreshSuccessful()) {
                        requestSyncOAuthToken();
                    }

                }
            }
        });
    }

    private void requestSyncRefreshToken(RefreshOAuthRequest requestModel, final RequestListener listener) {
        SynchronizedNetwork network = new SynchronizedNetwork(new IAPHurlStack(mOAuthRequest).getHurlStack());
        network.performRequest(createOAuthRequest(requestModel), new SynchronizedNetworkCallBack() {
            @Override
            public void onSyncRequestSuccess(final Response response) {
                if (response != null && response.result != null) {
                    mOAuthRequest.parseResponse(response.result);
                    access_token = mOAuthRequest.getAccessToken();
                    Message msg = Message.obtain();
                    msg.obj = response;
                    listener.onSuccess(msg);
                }
            }

            @Override
            public void onSyncRequestError(final VolleyError volleyError) {
                Message msg = Message.obtain();
                msg.obj = volleyError;
                listener.onError(msg);
            }
        });
    }

    private IAPJsonRequest createOAuthRequest(final AbstractModel request) {
        return new IAPJsonRequest(request.getMethod(), request.getUrl(),
                request.requestBody(),null,null);
    }
}