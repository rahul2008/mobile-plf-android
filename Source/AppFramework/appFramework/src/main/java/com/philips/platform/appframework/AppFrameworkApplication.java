/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.support.multidex.MultiDex;

import com.facebook.stetho.Stetho;
import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.core.BaseAppCore;
import com.philips.platform.core.Eventing;
import com.philips.platform.core.utils.UuidGenerator;
import com.philips.platform.datasevices.database.DatabaseHelper;
import com.philips.platform.datasevices.utility.EventingImpl;
import com.philips.platform.datasync.Backend;
import com.philips.platform.datasync.userprofile.UserRegistrationFacade;
import com.philips.platform.modularui.statecontroller.UIFlowManager;
import com.philips.platform.modularui.stateimpl.IAPState;
import com.philips.platform.modularui.stateimpl.ProductRegistrationState;
import com.philips.platform.modularui.stateimpl.UserRegistrationState;

import java.util.Locale;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

/**
 * Application class is used for initialization
 */
public class AppFrameworkApplication extends Application {
    public UIFlowManager flowManager;
    private static Context context;
    public static AppInfraInterface appInfra;
    public static LoggingInterface loggingInterface;
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
        context = getApplicationContext();
        flowManager = new UIFlowManager();
        appInfra = new AppInfra.Builder().build(getApplicationContext());
        loggingInterface = appInfra.getLogging().createInstanceForComponent(BuildConfig.APPLICATION_ID, BuildConfig.VERSION_NAME);
        loggingInterface.enableConsoleLog(true);
        loggingInterface.enableFileLog(true);
        setLocale();

        userRegistrationState= new UserRegistrationState();
        userRegistrationState.init(this);
        productRegistrationState= new ProductRegistrationState();
        productRegistrationState.init(this);
        iapState = new IAPState();
        iapState.init(this);
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext(), new UuidGenerator());
        databaseHelper.getWritableDatabase();
         Stetho.initializeWithDefaults(this);

        //initializeUserRegistrationLibrary(Configuration.DEVELOPMENT);



    }

    public IAPState getIap(){
        return iapState;
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
