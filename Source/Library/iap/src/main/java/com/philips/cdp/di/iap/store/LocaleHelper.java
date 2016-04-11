/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.store;

import android.content.Context;
import android.util.Log;

import com.philips.cdp.localematch.LocaleMatchListener;
import com.philips.cdp.localematch.PILLocale;
import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.LocaleMatchError;
import com.philips.cdp.localematch.enums.Platform;
import com.philips.cdp.localematch.enums.Sector;

import java.util.Locale;

class LocaleHelper {
    private static final String TAG = LocaleHelper.class.getSimpleName();

    private PILLocaleManager mLocaleManager;
    Context mContext;
    private String mCountryCode;
    private PILLocale mPILLocale;

    private static LocaleHelper sLocaleHelper;

    LocaleHelper(Context context, String countryCode) {
        mContext = context;
        mCountryCode = countryCode;
        initLocaleMatcher();
    }

    void initLocaleMatcher() {
        mLocaleManager = new PILLocaleManager();
        mLocaleManager.init(mContext, new LocaleMatchListener() {
            @Override
            public void onLocaleMatchRefreshed(final String s) {
                Log.d(TAG, "onLocaleMatchRefreshed=" + s);

                mPILLocale = mLocaleManager.currentLocaleWithCountryFallbackForPlatform(mContext, s,
                        Platform.PRX, Sector.B2C, Catalog.CONSUMER);
                Log.d(TAG, "onLocaleMatchRefreshed: mPILLocale.getLocaleCode()=" + mPILLocale
                        .getLocaleCode());
            }

            @Override
            public void onErrorOccurredForLocaleMatch(final LocaleMatchError localeMatchError) {

            }
        });
    }

    String getMixedLocale() {
        return Locale.getDefault().getLanguage() + "_" + mCountryCode;
    }

    void refresh() {
        mLocaleManager.refresh(mContext, Locale.getDefault().getLanguage(), mCountryCode);
    }

    String getLocaleCode() {
        if (mPILLocale != null) {
            return mPILLocale.getLocaleCode();
        }
        return null;
    }
}