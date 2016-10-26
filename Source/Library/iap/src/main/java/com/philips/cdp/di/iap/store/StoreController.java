/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.store;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.cdp.di.iap.response.config.HybrisConfigResponse;
import com.philips.cdp.di.iap.session.IAPHurlStack;
import com.philips.cdp.di.iap.session.IAPJsonRequest;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.session.RequestListener;
import com.philips.cdp.di.iap.session.SynchronizedNetwork;
import com.philips.cdp.di.iap.session.SynchronizedNetworkListener;
import com.philips.cdp.localematch.LocaleMatchListener;
import com.philips.cdp.localematch.PILLocale;
import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.LocaleMatchError;
import com.philips.cdp.localematch.enums.Platform;
import com.philips.cdp.localematch.enums.Sector;

import org.json.JSONObject;

public class StoreController {
    final Context mContext;
    protected PILLocaleManager mLocaleManager;
    protected PILLocale mPILLocale;
    protected StoreConfiguration mStoreConfig;
    protected RequestListener mRequestListener;
    protected String mFallBackLocale;
    protected String mSiteID;

    public StoreController(Context context, StoreConfiguration storeConfig) {
        mContext = context;
        mStoreConfig = storeConfig;
    }

    public void initConfig(final String language, String countryCode, final RequestListener listener) {
        mRequestListener = listener;
        refreshPILocaleManager(language, countryCode);
    }

    void refreshPILocaleManager(String language, String countryCode) {
        mLocaleManager = new PILLocaleManager(mContext);
        mLocaleManager.setInputLocale(language, countryCode);
        mLocaleManager.refresh(mLocaleMatchListener);
    }

    String getSiteID() {
        return mSiteID;
    }

    String getLocale() {
        if (mPILLocale != null) {
            return mPILLocale.getLocaleCode();
        }
        return mFallBackLocale;
    }

    void startConfigDownloadThread() {
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
                HybrisConfigResponse resp = new Gson().fromJson(jsonObjectResponse.result.toString(),
                        HybrisConfigResponse.class);
                mSiteID = resp.getSiteId();
                mStoreConfig.generateStoreUrls();
                notifyConfigListener(true, null);
            }

            @Override
            public void onSyncRequestError(final VolleyError volleyError) {
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

    private LocaleMatchListener mLocaleMatchListener = new LocaleMatchListener() {
        @Override
        public void onLocaleMatchRefreshed(final String s) {
            mFallBackLocale = s;
            mPILLocale = mLocaleManager.currentLocaleWithCountryFallbackForPlatform(mContext, s,
                    Platform.PRX, Sector.B2C, Catalog.CONSUMER);
            startConfigDownloadThread();
        }

        @Override
        public void onErrorOccurredForLocaleMatch(final LocaleMatchError localeMatchError) {
            if (mRequestListener != null) {
                Message msg = Message.obtain();
                if (LocaleMatchError.INPUT_VALIDATION_ERROR != localeMatchError) {
                    msg.obj = new IAPNetworkError(new NoConnectionError(), 0, null);
                }
                notifyConfigListener(false, msg);
            }
        }
    };
}