package com.philips.cdp.prxclient;

import android.content.Context;

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
    private Context mContext = null;
    private PRXDependencies mPrxDependencies;

    public void init(Context applicationContext, PRXDependencies prxDependencies) {
        mContext = applicationContext;
        mPrxDependencies = prxDependencies;
    }


    public void executeRequest(PrxRequest prxRequest, ResponseListener listener) {
        makeRequest(prxRequest, listener);
    }

    public void cancelRequest(String requestTag) {
    }

//    private void invokeLocaleMatch(final PrxRequest
//                                           prxRequest, final ResponseListener listener) {
//        final PILLocaleManager pilLocaleManager = new PILLocaleManager(mContext);
//        /*final String[] mLocale = new String[1];*/
//        pilLocaleManager.refresh(new LocaleMatchListener() {
//                                     public void onLocaleMatchRefreshed(String locale) {
//                                         PILLocale pilLocaleInstance = pilLocaleManager.
//                                                 currentLocaleWithCountryFallbackForPlatform(
//                                                         mContext, locale,
//                                                         Platform.PRX, prxRequest.getSector(),
//                                                         prxRequest.
//                                                                 getCatalog());
//                                         if (pilLocaleInstance != null) {
//                                             prxRequest.setLocaleMatchResult(pilLocaleInstance.
//                                                     getLocaleCode());
//                                         } else {
//                                             prxRequest.setLocaleMatchResult(pilLocaleManager.
//                                                     getInputLocale());
//                                         }
//                                         makeRequest(prxRequest, listener);
//                                     }
//
//                                     public void onErrorOccurredForLocaleMatch(LocaleMatchError
//                                                                                       error) {
//                                         PrxLogger.d(getClass() + "", error.toString());
//                                         prxRequest.setLocaleMatchResult(pilLocaleManager.
//                                                 getInputLocale());
//                                         makeRequest(prxRequest, listener);
//                                     }
//                                 }
//        );
//    }

    private void makeRequest(final PrxRequest prxRequest, final ResponseListener listener) {
        try {
            new NetworkWrapper(mContext, mPrxDependencies).executeCustomJsonRequest(prxRequest, listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getLibVersion() {
        return BuildConfig.VERSION_NAME;
    }
}
