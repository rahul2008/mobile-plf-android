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
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.modularui.statecontroller.UIFlowManager;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.modularui.stateimpl.IAPState;
import com.philips.platform.modularui.stateimpl.ProductRegistrationState;
import com.philips.platform.modularui.stateimpl.UserRegistrationState;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappSettings;

import java.util.Locale;
/**
 * Application class has following initializations
 *  1. App infra object creation for initializion other cocos and Logging
 *  2. Initialise User Registration
 *  3. Initialise In App PuserRegistrationStatechase
 *  4. Initialise Product Registration
 *
 */
public class AppFrameworkApplication extends Application {
    public UIFlowManager flowManager;
    private static Context context;
    public static AppInfraInterface gAppInfra;
    public static LoggingInterface loggingInterface;
    UserRegistrationState userRegistrationState;
    IAPState iap;
    UappDependencies uappDependencies;
    UappSettings uappSettings;
     /**
     * @retuserRegistrationStaten instance of this class
     */



    @SuppressWarnings("deprecation")
    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();
        context = getApplicationContext();
        flowManager = new UIFlowManager();
        gAppInfra = new AppInfra.Builder().build(getApplicationContext());
        loggingInterface = gAppInfra.getLogging().createInstanceForComponent(BuildConfig.APPLICATION_ID, BuildConfig.VERSION_NAME);
        loggingInterface.enableConsoleLog(true);
        loggingInterface.enableFileLog(true);
        setLocale();
        uappDependencies = new UappDependencies(gAppInfra);
        uappSettings = new UappSettings(this);
        userRegistrationState= new UserRegistrationState(UIState.UI_USER_REGISTRATION_STATE);
        userRegistrationState.init(uappDependencies,uappSettings);
        ProductRegistrationState pr= new ProductRegistrationState(UIState.UI_PROD_REGISTRATION_STATE);
        pr.init(uappDependencies,uappSettings);
        iap = new IAPState(UIState.UI_IAP_SHOPPING_FRAGMENT_STATE);
        iap.init(uappDependencies,uappSettings);
    }

    public IAPState getIap(){
        return iap;
    }
/**
 * Method for initializing IAP
 *
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

    public UIFlowManager getFlowManager() {
        return flowManager;
    }

    public static Context getContext() {
        return context;
    }


    }
