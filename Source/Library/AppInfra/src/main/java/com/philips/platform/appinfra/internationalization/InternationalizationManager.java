/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.internationalization;

import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.RequestManager;

import java.util.Locale;


public class InternationalizationManager implements InternationalizationInterface {

    AppInfra mAppInfra;
    Context context;
    String mCountry;
    private static final String COUNTRY_URL = "";
    SharedPreferences pref;

    public InternationalizationManager(AppInfra aAppInfra) {
        mAppInfra = aAppInfra;
        context = mAppInfra.getAppInfraContext();
//        monCountryResponse = this;
        // Class shall not presume appInfra to be completely initialized at this point.
        // At any call after the constructor, appInfra can be presumed to be complete.

    }

    @Override
    public Locale getUILocale() {
        if (Locale.getDefault() != null) {
            if (mAppInfra.getTagging() != null) {
                mAppInfra.getTagging().trackActionWithInfo("LocalPage", "KeyLocal", "ValueLocal");
            }

            return Locale.getDefault();
        } else {
            return null;
        }
    }

    public String getCountry() {
        pref = context.getSharedPreferences(RequestManager.COUNTRY_PRREFERENCE, Context.MODE_PRIVATE);
        if (mCountry == null) {
            mCountry = pref.getString(RequestManager.COUNTRY_NAME, null);
            // Log.i("Country", " "+mCountry);
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "Country", mCountry);
            if (mCountry != null) {
                if (mAppInfra.getTagging() != null) {
                    mAppInfra.getTagging().trackActionWithInfo("LocalPage", "KeyCountry", "ValueCountry");
                }
                return mCountry.toUpperCase();
            }
        }
        if (mCountry == null) {
            SharedPreferences.Editor editor = context.getSharedPreferences(RequestManager.COUNTRY_PRREFERENCE, Context.MODE_PRIVATE).edit();
            try {
                final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                final String simCountry = tm.getSimCountryIso();
                if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                    mCountry = simCountry.toUpperCase(Locale.US);

                    editor.putString(RequestManager.COUNTRY_NAME, mCountry);
                    editor.commit();
                    if (mCountry != null)
                        return mCountry.toUpperCase();
                } else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { //
                    String networkCountry = tm.getNetworkCountryIso();
                    if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                        mCountry = networkCountry.toUpperCase(Locale.US);
                        editor.putString(RequestManager.COUNTRY_NAME, mCountry);
                        editor.commit();
                        if (mCountry != null)
                            return mCountry.toUpperCase();
                    }
                }
            } catch (Exception e) {
            }
        }

        return mCountry;
    }

}
