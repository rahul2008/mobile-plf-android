/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.base;

import android.app.Application;
import android.support.multidex.MultiDex;

import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.platform.appframework.BuildConfig;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.FlowManager;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.baseapp.screens.inapppurchase.IAPRetailerFlowState;
import com.philips.platform.baseapp.screens.userregistration.UserRegistrationSplashState;
import com.philips.platform.core.utils.UuidGenerator;
import com.philips.platform.baseapp.screens.datasevices.database.DatabaseHelper;
import com.philips.platform.appframework.flowmanager.base.BaseUiFlowManager;
import com.philips.platform.baseapp.screens.inapppurchase.IAPState;
import com.philips.platform.baseapp.screens.productregistration.ProductRegistrationState;
import com.philips.platform.baseapp.screens.userregistration.UserRegistrationState;

import java.util.Locale;

/**
 * Application class is used for initialization
 */
public class AppFrameworkApplication extends Application {
    // TODO: Deepthi , should these be static.
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
        userRegistrationState = new UserRegistrationSplashState();
        userRegistrationState.init(this);
        productRegistrationState = new ProductRegistrationState();
        productRegistrationState.init(this);
        iapState = new IAPRetailerFlowState();
        iapState.init(this);
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext(), new UuidGenerator());
        databaseHelper.getWritableDatabase();
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

    public BaseUiFlowManager getTargetFlowManager() {
        return targetFlowManager;
    }
}
