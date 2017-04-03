/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.internationalization;

import android.content.Context;

import com.philips.platform.appinfra.AppInfra;

import java.util.Locale;


public class InternationalizationManager implements InternationalizationInterface {

    public InternationalizationManager(AppInfra aAppInfra) {
        Context context = aAppInfra.getAppInfraContext();
//        monCountryResponse = this;
        // Class shall not presume appInfra to be completely initialized at this point.
        // At any call after the constructor, appInfra can be presumed to be complete.

    }

    @Override
    public Locale getUILocale() {
        return Locale.getDefault();
    }

    @Override
    public String getUILocaleString() {
        final Locale mLocale = Locale.getDefault();
        if (mLocale.getCountry() == null || mLocale.getCountry().contains("NULL")) {
            return mLocale.getLanguage();
        } else {
            return mLocale.getLanguage() + "_" + mLocale.getCountry();
        }
    }
}
