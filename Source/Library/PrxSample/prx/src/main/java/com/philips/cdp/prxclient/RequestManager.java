package com.philips.cdp.prxclient;

import android.content.Context;

import com.philips.cdp.localematch.LocaleMatchListener;
import com.philips.cdp.localematch.PILLocale;
import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.localematch.enums.LocaleMatchError;
import com.philips.cdp.localematch.enums.Platform;
import com.philips.cdp.prxclient.Logger.PrxLogger;
import com.philips.cdp.prxclient.network.NetworkWrapper;
import com.philips.cdp.prxclient.request.PrxRequest;
import com.philips.cdp.prxclient.response.ResponseListener;

/**
 * Description : This is the entry class to start the PRX Request.
 * Project : PRX Common Component.
 * Created by naveen@philips.com on 02-Nov-15.
 */
public class RequestManager {

    private static final String TAG = RequestManager.class.getSimpleName();
    public static Context mContext = null;

    public void init(Context applicationContext) {
        mContext = applicationContext;
    }

    public void executeRequest(PrxRequest prxRequest, ResponseListener listener) {
        invokeLocaleMatch(prxRequest, listener);
    }

    public void cancelRequest(String requestTag) {
    }

    private void invokeLocaleMatch(final PrxRequest
                                           prxRequest, final ResponseListener listener) {
        final PILLocaleManager pilLocaleManager = new PILLocaleManager(mContext);
        /*final String[] mLocale = new String[1];*/
        pilLocaleManager.refresh(new LocaleMatchListener() {
                                     public void onLocaleMatchRefreshed(String locale) {
                                         PILLocale pilLocaleInstance = pilLocaleManager.
                                                 currentLocaleWithCountryFallbackForPlatform(
                                                         mContext, locale,
                                                         Platform.PRX, prxRequest.getSector(),
                                                         prxRequest.
                                                                 getCatalog());
                                         if (pilLocaleInstance != null) {
                                             prxRequest.setLocaleMatchResult(pilLocaleInstance.
                                                     getLocaleCode());
                                         } else {
                                             prxRequest.setLocaleMatchResult(pilLocaleManager.
                                                     getInputLocale());
                                         }
                                         makeRequest(prxRequest, listener);
                                     }

                                     public void onErrorOccurredForLocaleMatch(LocaleMatchError
                                                                                       error) {
                                         PrxLogger.d(getClass() + "", error.toString());
                                         prxRequest.setLocaleMatchResult(pilLocaleManager.
                                                 getInputLocale());
                                         makeRequest(prxRequest, listener);
                                     }
                                 }
        );
    }

    private void makeRequest(final PrxRequest prxRequest, final ResponseListener listener) {
        try {
            new NetworkWrapper(mContext).executeCustomJsonRequest(prxRequest, listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getLibVersion() {
        return BuildConfig.VERSION_NAME;
    }
}
