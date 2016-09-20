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
import com.philips.platform.modularui.stateimpl.InAppPurchaseFragmentState;
import com.philips.platform.modularui.stateimpl.ProductRegistrationState;
import com.philips.platform.modularui.stateimpl.UserRegistrationState;
import com.philips.platform.uappframework.uappinput.UappSettings;

import java.util.ArrayList;
import java.util.Locale;
/**
 * Application class has following initializations
 *  1. App infra object creation for initializion other cocos and Logging
 *  2. Initialise User Registration
 *  3. Initialise In App Purchase
 *  4. Initialise Product Registration
 *
 */
public class AppFrameworkApplication extends Application {
    public UIFlowManager flowManager;
    private static Context mContext;
    public static AppInfraInterface gAppInfra;
    public static LoggingInterface loggingInterface;
     /**
     * @return instance of this class
     */



    @SuppressWarnings("deprecation")
    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();
        mContext = getApplicationContext();
        flowManager = new UIFlowManager();
        gAppInfra = new AppInfra.Builder().build(getApplicationContext());
        loggingInterface = gAppInfra.getLogging().createInstanceForComponent(BuildConfig.APPLICATION_ID, BuildConfig.VERSION_NAME);
        loggingInterface.enableConsoleLog(true);
        loggingInterface.enableFileLog(true);
        setLocale();
        UserRegistrationState ur= new UserRegistrationState(UIState.UI_USER_REGISTRATION_STATE);
        ur.init(this);
        ProductRegistrationState pr= new ProductRegistrationState(UIState.UI_PROD_REGISTRATION_STATE);
        pr.init(this);
        InAppPurchaseFragmentState iap = new InAppPurchaseFragmentState(UIState.UI_IAP_SHOPPING_FRAGMENT_STATE);
        iap.init(this);

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
        return mContext;
    }


    }
