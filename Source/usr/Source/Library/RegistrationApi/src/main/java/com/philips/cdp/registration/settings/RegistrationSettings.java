
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.settings;

import android.content.Context;
import android.content.SharedPreferences;

import com.philips.cdp.registration.configuration.RegistrationConfiguration;

public abstract class RegistrationSettings {

    protected String mProductRegisterUrl = null;

    protected String mProductRegisterListUrl = null;

    protected String mPreferredCountryCode = null;

    protected String mPreferredLangCode = null;

    protected String mResendConsentUrl = null;


    protected String mRegisterBaseCaptureUrl = null;

    public String REGISTRATION_USE_PRODUCTION = "REGISTRATION_USE_PRODUCTION";

    public String REGISTRATION_USE_EVAL = "REGISTRATION_USE_EVAL";

    public String REGISTRATION_USE_DEVICE = "REGISTRATION_USE_DEVICE";

    public static final String REGISTRATION_API_PREFERENCE = "REGAPI_PREFERENCE";

    public static final String MICROSITE_ID = "microSiteID";


    protected Context mContext = null;
    String mCaptureClientId = null;
    String mLocale = null;

    public void intializeRegistrationSettings(Context context, String captureClientId,
                                              String locale) {
        storeMicrositeId(context);

        mCaptureClientId = captureClientId;
        mLocale = locale;
        mContext = context;
        refreshLocale(locale);
    }

    public abstract void initialiseConfigParameters(String locale);

    public String getProductRegisterUrl() {
        return mProductRegisterUrl;
    }

    public String getProductRegisterListUrl() {
        return mProductRegisterListUrl;
    }

    public String getPreferredCountryCode() {
        return mPreferredCountryCode;
    }

    public String getPreferredLangCode() {
        return mPreferredLangCode;
    }

    public String getResendConsentUrl() {
        return mResendConsentUrl;
    }

    public String getmRegisterBaseCaptureUrl() {
        return mRegisterBaseCaptureUrl;
    }

    protected void storeMicrositeId(Context context) {
        SharedPreferences pref = context.getSharedPreferences(REGISTRATION_API_PREFERENCE, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(MICROSITE_ID, RegistrationConfiguration.getInstance().getMicrositeId());
        editor.commit();
    }

    public void refreshLocale(String localeCode) {

        if (localeCode != null) {
            localeCode = localeCode.replace("_", "-");
        } else {
            localeCode = "en-US";
        }

        if (RegistrationHelper.getInstance().getCountryCode().equals("CN")) {
            String localeArr[] = localeCode.split("-");
            if (!localeArr[0].equals("zh")) {
                localeCode = "en-US";
            }
        }
        initialiseConfigParameters(localeCode);
    }
}
