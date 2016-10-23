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

import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.registration.configuration.Configuration;
import com.philips.platform.appframework.database.DatabaseHelper;
import com.philips.platform.appframework.injection.AppComponent;
import com.philips.platform.appframework.injection.ApplicationModule;
import com.philips.platform.appframework.injection.BackendModule;
import com.philips.platform.appframework.injection.CoreModule;
import com.philips.platform.appframework.injection.DaggerAppComponent;
import com.philips.platform.appframework.injection.DatabaseModule;
import com.philips.platform.appframework.injection.MonitorModule;
import com.philips.platform.appframework.injection.RegistrationModule;
import com.philips.platform.appframework.utility.EventingImpl;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.core.BaseAppCore;
import com.philips.platform.core.Eventing;
import com.philips.platform.core.utils.UuidGenerator;
import com.philips.platform.datasync.Backend;
import com.philips.platform.datasync.userprofile.UserRegistrationFacade;
import com.philips.platform.modularui.statecontroller.UIFlowManager;
import com.philips.platform.modularui.stateimpl.IAPState;
import com.philips.platform.modularui.stateimpl.ProductRegistrationState;
import com.philips.platform.modularui.stateimpl.UserRegistrationState;

import java.util.Locale;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

import static com.philips.cdp.registration.configuration.URConfigurationConstants.UR;

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


     @Inject
     Backend backend;

    @Inject
    BaseAppCore core;

    @Inject
    Eventing eventing;

    AppComponent appComponent;

    @Inject
    UserRegistrationFacade userRegistrationFacade;


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

        prepareInjectionsGraph();
        appComponent.injectApplication(this);
        core.start();

        userRegistrationState= new UserRegistrationState();
        userRegistrationState.init(this);
        productRegistrationState= new ProductRegistrationState();
        productRegistrationState.init(this);
        iapState = new IAPState();
        iapState.init(this);
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext(), new UuidGenerator());
        databaseHelper.getWritableDatabase();
        // Stetho.initializeWithDefaults(this);

        //initializeUserRegistrationLibrary(Configuration.DEVELOPMENT);



    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public void setAppComponent(AppComponent appComponent) {
        this.appComponent = appComponent;
    }

    protected void prepareInjectionsGraph() {
        final DatabaseModule databaseModule = new DatabaseModule();
        final MonitorModule monitorModule = new MonitorModule(this);
        final RegistrationModule registrationModule = new RegistrationModule();
        BackendModule backendModule = new BackendModule();
        final CoreModule coreModule = new CoreModule(new EventingImpl(new EventBus(), new Handler()));
        final ApplicationModule applicationModule = new ApplicationModule(this);

        // initiating all application module events
        appComponent = DaggerAppComponent.builder().databaseModule(databaseModule).registrationModule(registrationModule).
                backendModule(backendModule).coreModule(coreModule).monitorModule(monitorModule).
                applicationModule(applicationModule).build();
        setAppComponent(appComponent);
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
