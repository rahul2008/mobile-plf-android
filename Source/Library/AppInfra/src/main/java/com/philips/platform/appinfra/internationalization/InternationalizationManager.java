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
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.RequestManager;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;

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
}
