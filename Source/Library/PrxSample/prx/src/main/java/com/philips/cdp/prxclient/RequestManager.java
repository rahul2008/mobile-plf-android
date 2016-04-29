package com.philips.cdp.prxclient;

import android.content.Context;

import com.philips.cdp.prxclient.Logger.PrxLogger;
import com.philips.cdp.prxclient.localematchwrapper.LocaleMatchHandler;
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
    private LocaleMatchHandler localeMatchHandler;

    public void init(Context applicationContext) {
        mContext = applicationContext;

    }

    public void executeRequest(PrxRequest prxRequest, ResponseListener listener) {
        PrxLogger.d(TAG, "Locale is not Null");
        localeMatchHandler = LocaleMatchHandler.getInstance(mContext, prxRequest.getSector(), prxRequest.getCatalog());
        makeRequest(prxRequest, listener);         /* String[] locales = prxRequest.getLocale().split("_");
            setLocale(locales[0], locales[1], prxRequest, listener);*/

    }

    public void cancelRequest(String requestTag) {
    }

    /*private void setLocale(final String languageCode, final String countryCode, final PrxRequest prxRequest, final ResponseListener listener) {
        final PILLocaleManager pilLocaleManager = new PILLocaleManager(mC);
        final String[] mLocale = new String[1];
        pilLocaleManager.init(mContext, new LocaleMatchListener() {
                    public void onLocaleMatchRefreshed(String locale) {
                        PILLocale pilLocaleInstance = pilLocaleManager.currentLocaleWithCountryFallbackForPlatform(mContext, locale,
                                Platform.PRX, prxRequest.getSector(), prxRequest.getCatalog());
                        if (pilLocaleInstance != null) {
                            mLocale[0] = pilLocaleInstance.getLanguageCode() + "_" + pilLocaleInstance.getCountrycode();
                            prxRequest.setLocaleMatchResult(mLocale[0]);
                        }


                    }

                    public void onErrorOccurredForLocaleMatch(LocaleMatchError error) {
                        PrxLogger.d(getClass() + "", error.toString());
                        makeRequest(prxRequest, listener);
                    }
                }
        );
    }*/

    private void makeRequest(final PrxRequest prxRequest, final ResponseListener listener) {
        try {
            new NetworkWrapper(mContext).executeCustomJsonRequest(prxRequest, listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*public String getLibVersion() {
        return BuildConfig.VERSION_NAME;
    }*/
}
