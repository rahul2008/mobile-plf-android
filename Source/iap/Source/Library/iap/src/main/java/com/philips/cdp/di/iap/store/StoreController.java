/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.store;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.response.config.HybrisConfigResponse;
import com.philips.cdp.di.iap.session.IAPHurlStack;
import com.philips.cdp.di.iap.session.IAPJsonRequest;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.session.RequestListener;
import com.philips.cdp.di.iap.session.SynchronizedNetwork;
import com.philips.cdp.di.iap.session.SynchronizedNetworkListener;

import org.json.JSONObject;

public class StoreController {
    final Context mContext;
    protected StoreConfiguration mStoreConfig;
    protected RequestListener mRequestListener;
    protected String mSiteID;
    protected String mCampaignID;

    public StoreController(Context context, StoreConfiguration storeConfig) {
        mContext = context;
        mStoreConfig = storeConfig;
    }

    public void initConfig(final RequestListener listener) {
        mRequestListener = listener;
        startConfigDownloadThread();
    }

    String getSiteID() {
        return mSiteID;
    }

    String getCampaignID() {
        return mCampaignID;
    }

    String getLocale() {
        if (CartModelContainer.getInstance().getCountry() == null || CartModelContainer.getInstance().getLanguage() == null)
            return null;
        return CartModelContainer.getInstance().getLanguage() + "_" + CartModelContainer.getInstance().getCountry();
    }

    private void startConfigDownloadThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                fetchConfiguration();
            }
        }).start();
    }

    void fetchConfiguration() {
        IAPJsonRequest request = new IAPJsonRequest(Request.Method.GET, mStoreConfig.getRawConfigUrl(), null,
                null, null);
        SynchronizedNetwork synchronizedNetwork = getSynchronizedNetwork();
        synchronizedNetwork.performRequest(request, new SynchronizedNetworkListener() {
            @Override
            public void onSyncRequestSuccess(final Response<JSONObject> jsonObjectResponse) {
                if(jsonObjectResponse == null || jsonObjectResponse.result == null){
                    postError(null);
                    return;
                }
                HybrisConfigResponse resp = new Gson().fromJson(jsonObjectResponse.result.toString(),
                        HybrisConfigResponse.class);
                mSiteID = resp.getSiteId();
                mCampaignID = resp.getRootCategory();
                mStoreConfig.generateStoreUrls();
                notifyConfigListener(true, null);
            }

            @Override
            public void onSyncRequestError(final VolleyError volleyError) {
                postError(volleyError);
            }

            private void postError(VolleyError volleyError) {
                Message msg = Message.obtain();
                mSiteID = null;
                msg.obj = new IAPNetworkError(volleyError, hashCode(), null);
                notifyConfigListener(false, msg);
            }
        });
    }

    //For testing purpose
    SynchronizedNetwork getSynchronizedNetwork() {
        IAPHurlStack hurlStack = new IAPHurlStack(null);
        return new SynchronizedNetwork(hurlStack.getHurlStack());
    }

    private void notifyConfigListener(final boolean success, final Message msg) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (mRequestListener != null) {
                    if (success) {
                        mRequestListener.onSuccess(msg);
                    } else {
                        mRequestListener.onError(msg);
                    }
                }
            }
        });
    }
}