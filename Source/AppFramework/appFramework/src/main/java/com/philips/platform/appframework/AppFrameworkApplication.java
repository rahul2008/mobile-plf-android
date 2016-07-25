/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.tagging.Tagging;
import com.philips.platform.appframework.utility.SharedPreferenceUtility;
import com.philips.platform.modularui.statecontroller.FlowManager;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.AppInfraSingleton;
import com.philips.platform.appinfra.logging.LoggingInterface;

import java.util.Locale;


public class AppFrameworkApplication extends Application {
    public FlowManager flowManager;
    private static Context mContext;
    public static AppInfraInterface gAppInfra;
    public static LoggingInterface loggingInterface;

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();
        mContext = getApplicationContext();
        SharedPreferenceUtility.getInstance().Initialize(getApplicationContext());
        initializeUserRegistrationLibrary();
        flowManager = new FlowManager();
        AppInfraSingleton.setInstance(gAppInfra = new AppInfra.Builder().build(getApplicationContext()));
        gAppInfra = AppInfraSingleton.getInstance();
        loggingInterface = gAppInfra.getLogging().createInstanceForComponent(BuildConfig.APPLICATION_ID, BuildConfig.VERSION_NAME);
        loggingInterface.enableConsoleLog(true);
        loggingInterface.enableFileLog(true);
    }

    public FlowManager getFlowManager() {
        return flowManager;
    }

    public static Context getContext() {
        return mContext;
    }

    private void initializeUserRegistrationLibrary() {
        RLog.d(RLog.APPLICATION, "RegistrationApplication : onCreate");
        RLog.d(RLog.JANRAIN_INITIALIZE, "RegistrationApplication : Janrain initialization with locale : " + Locale.getDefault());
        Tagging.enableAppTagging(true);
        Tagging.setTrackingIdentifier("integratingApplicationAppsId");
        Tagging.setLaunchingPageName("demoapp:home");
        RegistrationConfiguration.getInstance().setPrioritisedFunction(RegistrationFunction.Registration);

        String languageCode = Locale.getDefault().getLanguage();
        String countryCode = Locale.getDefault().getCountry();

        PILLocaleManager localeManager = new PILLocaleManager(this);
        localeManager.setInputLocale(languageCode, countryCode);

        RegistrationHelper.getInstance().initializeUserRegistration(this);
        Tagging.init(this, "Philips Registration");
    }
}
