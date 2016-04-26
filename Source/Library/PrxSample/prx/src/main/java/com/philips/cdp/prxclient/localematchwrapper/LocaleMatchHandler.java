package com.philips.cdp.prxclient.localematchwrapper;

import android.content.Context;

import com.philips.cdp.localematch.LocaleMatchListener;
import com.philips.cdp.localematch.PILLocale;
import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.LocaleMatchError;
import com.philips.cdp.localematch.enums.Platform;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.prxclient.Logger.PrxLogger;

import java.util.Locale;

/**
 * Created by naveen@philips.com on 26-Apr-16.
 */
public class LocaleMatchHandler implements LocaleMatchListener {

    private static final String TAG = LocaleMatchHandler.class.getSimpleName();
    private static LocaleMatchHandler localeMatchHandler;
    private static Context mContext = null;
    private static Sector mSector;
    private static Catalog mCatalog;
    public String mLocale = null;
    private PILLocaleManager localeManager = null;

    public static LocaleMatchHandler getInstance(Context context) {
        mContext = context;
        if (localeMatchHandler == null)
            localeMatchHandler = new LocaleMatchHandler();

        return localeMatchHandler;
    }

    public static LocaleMatchHandler getInstance(Sector sector, Catalog catalog) {
        mSector = sector;
        mCatalog = catalog;
        localeMatchHandler.initLocaleMatchRequest(mContext);
        return localeMatchHandler;
    }


    public void initLocaleMatchRequest(Context context) {
        if (localeManager == null)
            localeManager = new PILLocaleManager(context);
        if (mLocale == null)
            mLocale = localeManager.getInputLocale();
        localeManager.refresh(this);
    }

    @Override
    public void onLocaleMatchRefreshed(String s) {
        PrxLogger.d(TAG, "After Locale Refreshed : " + s);
        String requestedLocale = s;
        PILLocale piLocaleCountryFallBack = localeManager.currentLocaleWithCountryFallbackForPlatform(mContext,
                requestedLocale, Platform.PRX,
                mSector, mCatalog);
        PrxLogger.d(TAG, "After Locale Refreshed Language  : " + piLocaleCountryFallBack.getLanguageCode());
        PrxLogger.d(TAG, "After Locale Refreshed Country  : " + piLocaleCountryFallBack.getCountrycode());
        Locale locale = new Locale(piLocaleCountryFallBack.getLanguageCode(), piLocaleCountryFallBack.getCountrycode());
        mLocale = locale.toString();
    }

    @Override
    public void onErrorOccurredForLocaleMatch(LocaleMatchError localeMatchError) {
        PrxLogger.e(TAG, "LocaleMatch Response Error : " + LocaleMatchError.SERVER_ERROR);
    }


    public String getLocale() {
        return mLocale;
    }
}
