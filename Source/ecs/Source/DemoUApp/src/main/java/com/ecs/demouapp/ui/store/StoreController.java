/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.ecs.demouapp.ui.store;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ecs.demouapp.ui.container.CartModelContainer;
import com.ecs.demouapp.ui.response.config.HybrisConfigResponse;
import com.ecs.demouapp.ui.session.IAPHurlStack;
import com.ecs.demouapp.ui.session.IAPJsonRequest;
import com.ecs.demouapp.ui.session.IAPNetworkError;
import com.ecs.demouapp.ui.session.RequestListener;
import com.ecs.demouapp.ui.session.SynchronizedNetwork;
import com.ecs.demouapp.ui.session.SynchronizedNetworkListener;
import com.ecs.demouapp.ui.utils.ECSUtility;
import com.google.gson.Gson;
import com.philips.cdp.di.ecs.integration.ECSCallback;


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

        ECSUtility.getInstance().getEcsServices().getECSConfig(new ECSCallback<com.philips.cdp.di.ecs.model.response.HybrisConfigResponse, Exception>() {
            @Override
            public void onResponse(com.philips.cdp.di.ecs.model.response.HybrisConfigResponse result) {

                mSiteID = result.getSiteId();
                mCampaignID = result.getRootCategory();
                mStoreConfig.generateStoreUrls();
                notifyConfigListener(true, null);
            }

            @Override
            public void onFailure(Exception error,String detailErrorMessage , int errorCode) {

                Message msg = Message.obtain();
                mSiteID = null;
                msg.obj = new IAPNetworkError(new VolleyError(error),hashCode(),null);
                notifyConfigListener(false, msg);
            }
        });

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