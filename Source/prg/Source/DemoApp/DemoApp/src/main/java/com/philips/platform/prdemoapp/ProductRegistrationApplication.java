package com.philips.platform.prdemoapp;

import android.app.Application;
import android.support.multidex.MultiDex;

import com.philips.cdp.prodreg.launcher.PRDependencies;
import com.philips.cdp.prodreg.launcher.PRInterface;
import com.philips.cdp.prodreg.launcher.PRSettings;
import com.philips.cdp.prodreg.launcher.PRUiHelper;
import com.philips.cdp.registration.configuration.Configuration;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.URDependancies;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.cdp.registration.ui.utils.URSettings;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.appidentity.AppIdentityInterface;
import com.philips.platform.uid.thememanager.UIDHelper;

import java.util.Locale;

public class ProductRegistrationApplication extends Application {

    private static AppInfraInterface mAppInfraInterface;
    final String AI = "appinfra";
    private final String UR = "UserRegistration";

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        UIDHelper.injectCalligraphyFonts();
        initAppInfra();
        initProductRegistration();
        initRegistration(Configuration.STAGING);
        RLog.enableLogging();
    }

    private void initProductRegistration() {
        PRDependencies prDependencies = new PRDependencies(mAppInfraInterface);
        PRSettings prSettings = new PRSettings(getApplicationContext());
        new PRInterface().init(prDependencies, prSettings);
        mAppInfraInterface.getTagging().setPreviousPage("demoapp:");
    }

    /**
     * For doing dynamic initialisation Of User registration
     *
     * @param configuration The environment ype as required by UR
     */
    public void initRegistration(Configuration configuration) {


        initAppIdentity(configuration);
        URDependancies urDependancies = new URDependancies(mAppInfraInterface);
        URSettings urSettings = new URSettings(this);
        URInterface urInterface = new URInterface();
        urInterface.init(urDependancies, urSettings);

    }



    @SuppressWarnings("deprecation")
    private void initAppInfra() {
        mAppInfraInterface = new AppInfra.Builder().build(getApplicationContext());
        PRUiHelper.getInstance().setAppInfraInstance(mAppInfraInterface);
    }

    private void initAppIdentity(Configuration configuration) {
        AppIdentityInterface mAppIdentityInterface;
        mAppIdentityInterface = mAppInfraInterface.getAppIdentity();
        AppConfigurationInterface appConfigurationInterface = mAppInfraInterface.
                getConfigInterface();

        //Dynamically set the values to appInfar and app state

        AppConfigurationInterface.AppConfigurationError configError = new
                AppConfigurationInterface.AppConfigurationError();
        mAppInfraInterface.
                getConfigInterface().setPropertyForKey(
                "appidentity.micrositeId",
                AI,
                "77000",
                configError);

        mAppInfraInterface.
                getConfigInterface().setPropertyForKey(
                "appidentity.sector",
                AI,
                "b2c",
                configError);

        mAppInfraInterface.
                getConfigInterface().setPropertyForKey(
                "appidentity.serviceDiscoveryEnvironment",
                AI,
                "Production",
                configError);

        switch (configuration) {
            case EVALUATION:
                mAppInfraInterface.
                        getConfigInterface().setPropertyForKey(
                        "appidentity.appState",
                        AI,
                        "ACCEPTANCE",
                        configError);
                break;
            case DEVELOPMENT:
                mAppInfraInterface.
                        getConfigInterface().setPropertyForKey(
                        "appidentity.appState",
                        AI,
                        "DEVELOPMENT",
                        configError);

                break;
            case PRODUCTION:
                mAppInfraInterface.
                        getConfigInterface().setPropertyForKey(
                        "appidentity.appState",
                        AI,
                        "PRODUCTION",
                        configError);
                break;
            case STAGING:
                mAppInfraInterface.
                        getConfigInterface().setPropertyForKey(
                        "appidentity.appState",
                        AI,
                        "STAGING",
                        configError);

                break;
            case TESTING:
                mAppInfraInterface.
                        getConfigInterface().setPropertyForKey(
                        "appidentity.appState",
                        AI,
                        "TEST",
                        configError);
                break;
        }

//        AppIdentityInfo appIdentityInfo = new AppIdentityInfo();
//        appIdentityInfo.setAppLocalizedNAme(mAppIdentityInterface.getLocalizedAppName());
//        appIdentityInfo.setSector(mAppIdentityInterface.getSector());
//        appIdentityInfo.setMicrositeId(mAppIdentityInterface.getMicrositeId());
//        appIdentityInfo.setAppName(mAppIdentityInterface.getAppName());
//        appIdentityInfo.setAppState(mAppIdentityInterface.getAppState().toString());
//        appIdentityInfo.setAppVersion(mAppIdentityInterface.getAppVersion());
//        appIdentityInfo.setServiceDiscoveryEnvironment(mAppIdentityInterface.getServiceDiscoveryEnvironment());
    }
}
