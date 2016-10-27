/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework;

import android.app.Application;
import android.support.multidex.MultiDex;

import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.platform.appframework.flowmanager.FlowManager;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.modularui.stateimpl.IAPState;
import com.philips.platform.modularui.stateimpl.ProductRegistrationState;
import com.philips.platform.modularui.stateimpl.UserRegistrationState;

import java.util.Locale;

/**
 * Application class is used for initialization
 */
public class AppFrameworkApplication extends Application {
    public static AppInfraInterface appInfra;
    public static LoggingInterface loggingInterface;
    protected FlowManager targetFlowManager;
    UserRegistrationState userRegistrationState;
    IAPState iapState;
    ProductRegistrationState productRegistrationState;

    /**
     * @return instance of this class
     */

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();
        targetFlowManager = FlowManager.getInstance( getApplicationContext(), R.string.com_philips_app_fmwk_app_flow_url);
        appInfra = new AppInfra.Builder().build(getApplicationContext());
        loggingInterface = appInfra.getLogging().createInstanceForComponent(BuildConfig.APPLICATION_ID, BuildConfig.VERSION_NAME);
        loggingInterface.enableConsoleLog(true);
        loggingInterface.enableFileLog(true);
        setLocale();
        userRegistrationState = new UserRegistrationState();
        userRegistrationState.init(this);
        productRegistrationState = new ProductRegistrationState();
        productRegistrationState.init(this);
        iapState = new IAPState();
        iapState.init(this);
    }

    public IAPState getIap() {
        return iapState;
    }

    /**
     * Method for initializing IAP
     */

    private void setLocale() {
        String languageCode = Locale.getDefault().getLanguage();
        String countryCode = Locale.getDefault().getCountry();

        PILLocaleManager localeManager = new PILLocaleManager(this);
        localeManager.setInputLocale(languageCode, countryCode);
    }

    @SuppressWarnings("deprecation")
    /**
     * Initializing Product registration
     */

    public FlowManager getTargetFlowManager() {
        return targetFlowManager;
    }
}
