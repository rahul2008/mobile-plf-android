/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.internationalization;

import android.content.Context;
import android.os.Build;
import android.os.LocaleList;

import com.philips.platform.appinfra.AppInfra;

import java.util.Locale;


public class InternationalizationManager implements InternationalizationInterface {

    AppInfra mAppInfra;
    Context context;

    public InternationalizationManager(AppInfra aAppInfra) {
        mAppInfra = aAppInfra;
        context = mAppInfra.getAppInfraContext();
//        monCountryResponse = this;
        // Class shall not presume appInfra to be completely initialized at this point.
        // At any call after the constructor, appInfra can be presumed to be complete.

    }

    @Override
    public Locale getUILocale() {
        return Locale.getDefault();
    }

    @Override
    public LocaleList getLocaleList() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return LocaleList.getDefault();
        }else {
            return null;
        }

    }


}
