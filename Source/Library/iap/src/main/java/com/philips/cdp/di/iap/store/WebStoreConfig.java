/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.store;

import android.content.Context;
import android.os.Message;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.cdp.di.iap.response.config.WebStoreConfigResponse;
import com.philips.cdp.di.iap.session.IAPHurlStack;
import com.philips.cdp.di.iap.session.IAPJsonRequest;
import com.philips.cdp.di.iap.session.RequestListener;
import com.philips.cdp.di.iap.session.SynchronizedNetwork;
import com.philips.cdp.di.iap.session.SynchronizedNetworkCallBack;
import com.philips.cdp.localematch.LocaleMatchListener;
import com.philips.cdp.localematch.PILLocale;
import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.LocaleMatchError;
import com.philips.cdp.localematch.enums.Platform;
import com.philips.cdp.localematch.enums.Sector;

import org.json.JSONObject;

import java.util.Locale;

class WebStoreConfig {
    private static final String TAG = WebStoreConfig.class.getSimpleName();
    final Context mContext;

    private String mSiteID;
    PILLocaleManager mLocaleManager;
    PILLocale mPILLocale;
    StoreConfiguration mStoreConfig;

    private RequestListener mResponseListener;

    public WebStoreConfig(Context context, StoreConfiguration storeConfig) {
        mContext = context;
        mStoreConfig = storeConfig;
    }

    String getSiteID() {
        return mSiteID;
    }

    String getLocale() {
        if (mPILLocale != null) {
            return mPILLocale.getLocaleCode();
        }
        return null;
    }

    public void initConfig(String countryCode, final RequestListener listener) {
        mResponseListener = listener;
        initLocaleMatcher();
        refresh(countryCode);
    }

    void initLocaleMatcher() {
        mLocaleManager = new PILLocaleManager();
            mLocaleManager.init(mContext, new LocaleMatchListener() {
            @Override
            public void onLocaleMatchRefreshed(final String s) {
                mPILLocale = mLocaleManager.currentLocaleWithCountryFallbackForPlatform(mContext, s,
                        Platform.PRX, Sector.B2C, Catalog.CONSUMER);
                startConfigDownloadThread();
            }

            @Override
            public void onErrorOccurredForLocaleMatch(final LocaleMatchError localeMatchError) {
                if(mResponseListener != null) {
                    Message msg = Message.obtain();
                    mResponseListener.onError(msg);
                }
            }
        });
    }

    void refresh(final String countryCode) {
        mLocaleManager.refresh(mContext, Locale.getDefault().getLanguage(), countryCode);
    }

    void startConfigDownloadThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                requestHybrisConfig();
            }
        }).start();
    }

    void requestHybrisConfig() {
        IAPJsonRequest request = new IAPJsonRequest(Request.Method.GET, mStoreConfig.getRawConfigUrl(), null,
                null, null);
        SynchronizedNetwork net = new SynchronizedNetwork((new IAPHurlStack(null).getHurlStack()));
        net.performRequest(request, new SynchronizedNetworkCallBack() {
            @Override
            public void onSyncRequestSuccess(final Response<JSONObject> jsonObjectResponse) {
                WebStoreConfigResponse resp = new Gson().fromJson(jsonObjectResponse.result.toString(),
                        WebStoreConfigResponse.class);
                mSiteID = resp.getSiteId();
                mStoreConfig.generateStoreUrls();
                if(mResponseListener != null) {
                    mResponseListener.onSuccess(null);
                }
            }

            @Override
            public void onSyncRequestError(final VolleyError volleyError) {
                if(mResponseListener != null) {
                    Message msg = Message.obtain();
                    msg.obj = volleyError;
                    mResponseListener.onError(msg);
                }
            }
        });
    }
}