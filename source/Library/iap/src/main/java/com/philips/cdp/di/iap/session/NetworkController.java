/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.session;

import android.content.Context;
import android.os.Message;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.philips.cdp.di.iap.analytics.IAPAnalytics;
import com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant;
import com.philips.cdp.di.iap.networkEssential.NetworkEssentials;
import com.philips.cdp.di.iap.store.StoreListener;
import com.philips.cdp.di.iap.integration.IAPSettings;
import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.utils.IAPLog;

import org.json.JSONObject;

public class NetworkController {
    protected Context context;
    protected HurlStack mIapHurlStack;
    protected RequestQueue mRequestQueue;
    protected StoreListener mStoreListener;
    protected OAuthListener mOAuthListener;
    protected NetworkEssentials mNetworkEssentials;

    public NetworkController(Context context) {
        this(context, null, null);
    }

    public NetworkController(Context context, NetworkEssentials essentials, IAPSettings iapSettings) {
        this.context = context;
        mNetworkEssentials = essentials;
        initStore(context, iapSettings);
        mOAuthListener = essentials.getOAuthHandler();
        initHurlStack(context);
        hybrisVolleyCreateConnection(context);
    }

    void initHurlStack(final Context context) {
        mIapHurlStack = mNetworkEssentials.getHurlStack(context, mOAuthListener);
    }

    void initStore(Context context, IAPSettings iapSettings) {
        mStoreListener = mNetworkEssentials.getStore(context, iapSettings);
    }

    public void hybrisVolleyCreateConnection(Context context) {
        mRequestQueue = VolleyWrapper.newRequestQueue(context, mIapHurlStack);
    }

    void refreshOAuthToken(RequestListener listener) {
        if (mOAuthListener != null) {
            mOAuthListener.refreshToken(listener);
        }
    }

    public void sendHybrisRequest(final int requestCode, final AbstractModel model, final RequestListener requestListener) {

        if (mStoreListener.isNewUser()) {
            mStoreListener.createNewUser(context);
            mOAuthListener.resetAccessToken();
        }

        Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                if (model.getUrl() != null && error != null) {
                    IAPLog.d(IAPLog.LOG, "Response from sendHybrisRequest onError =" + error
                            .getLocalizedMessage() + " requestCode=" + requestCode + "in " +
                            requestListener.getClass().getSimpleName() + " " + model.getUrl().substring(0, 20));
                }
                if (error != null && error.getMessage() != null) {
                    IAPAnalytics.trackAction(IAPAnalyticsConstant.SEND_DATA,
                            IAPAnalyticsConstant.ERROR, error.getMessage());
                }
                if (requestListener != null) {
                    new IAPNetworkError(error, requestCode, requestListener);
                }
            }
        };

        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(final JSONObject response) {
                if (requestListener != null) {
                    Message msg = Message.obtain();
                    msg.what = requestCode;

                    if (response != null && response.length() == 0) {
                        msg.obj = NetworkConstants.EMPTY_RESPONSE;
                    } else {
                        msg.obj = model.parseResponse(response);
                    }

                    requestListener.onSuccess(msg);

                    //For testing purpose
                    if (model.getUrl() != null) {
                        IAPLog.d(IAPLog.LOG, "Response from sendHybrisRequest onFetchOfProductList =" + msg + " requestCode=" + requestCode + "in " +
                                requestListener.getClass().getSimpleName() + "env = " + " " + model.getUrl().substring(0, 15));
                    }
                }
            }
        };

        IAPJsonRequest iapJsonRequest = getIapJsonRequest(model, error, response);
        addToVolleyQueue(iapJsonRequest);
    }

    IAPJsonRequest getIapJsonRequest(final AbstractModel model, final Response.ErrorListener error, final Response.Listener<JSONObject> response) {
        return new IAPJsonRequest(model.getMethod(), model.getUrl(),
                model.requestBody(), response, error);
    }

    public void addToVolleyQueue(final IAPJsonRequest jsonRequest) {
        mRequestQueue.add(jsonRequest);
    }

    public StoreListener getStore() {
        return mStoreListener;
    }

}