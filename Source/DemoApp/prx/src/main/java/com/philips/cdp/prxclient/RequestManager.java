package com.philips.cdp.prxclient;

import android.content.Context;

import com.philips.cdp.localematch.LocaleMatchListener;
import com.philips.cdp.localematch.PILLocale;
import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.localematch.enums.LocaleMatchError;
import com.philips.cdp.localematch.enums.Platform;
import com.philips.cdp.prxclient.Logger.PrxLogger;
import com.philips.cdp.prxclient.network.NetworkWrapper;
import com.philips.cdp.prxclient.prxdatabuilder.PrxDataBuilder;
import com.philips.cdp.prxclient.response.ResponseListener;

/**
 * Description : This is the entry class to start the PRX Request.
 * Project : PRX Common Component.
 * Created by naveen@philips.com on 02-Nov-15.
 */
public class RequestManager {

    private Context mContext = null;

    public void init(Context applicationContext) {
        mContext = applicationContext;
    }

    public void executeRequest(int requestType, PrxDataBuilder prxDataBuilder, ResponseListener listener) {
        String[] locales = prxDataBuilder.getLocale().split("_");
        setLocale(locales[0], locales[1], requestType, prxDataBuilder, listener);
    }

    public void executeRequest(PrxDataBuilder prxDataBuilder, ResponseListener responseListener) {
        new NetworkWrapper(mContext).executeJsonObjectRequest(prxDataBuilder, responseListener);
    }

    public void cancelRequest(String requestTag) {
    }

    private void setLocale(String languageCode, String countryCode, final int requestType, final PrxDataBuilder prxDataBuilder, final ResponseListener listener) {
        final PILLocaleManager pilLocaleManager = new PILLocaleManager();
        final String[] mLocale = new String[1];
        pilLocaleManager.init(mContext, new LocaleMatchListener() {
                    public void onLocaleMatchRefreshed(String locale) {
                        PILLocale pilLocaleInstance = pilLocaleManager.currentLocaleWithCountryFallbackForPlatform(mContext, locale,
                                Platform.PRX, prxDataBuilder.getSector(), prxDataBuilder.getCatalog());
                        mLocale[0] = pilLocaleInstance.getLanguageCode() + "_" + pilLocaleInstance.getCountrycode();
                        prxDataBuilder.setmLocale(mLocale[0]);

                        makeRequest(requestType, prxDataBuilder, listener);
                    }

                    public void onErrorOccurredForLocaleMatch(LocaleMatchError error) {
                        PrxLogger.d(getClass() + "", error.toString());
                        makeRequest(requestType, prxDataBuilder, listener);
                    }
                }
        );
        pilLocaleManager.refresh(mContext, languageCode, countryCode);
    }

    private void makeRequest(final int requestType, final PrxDataBuilder prxDataBuilder, final ResponseListener listener) {
        try {
            new NetworkWrapper(mContext).executeCustomRequest(requestType, prxDataBuilder, listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
