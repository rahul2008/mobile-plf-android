
package com.philips.cdp.coppa.registration;

import android.app.*;
import android.content.*;
import android.os.*;
import android.support.multidex.*;

import com.philips.cdp.registration.*;
import com.philips.cdp.registration.configuration.*;
import com.philips.cdp.registration.coppa.utils.*;
import com.philips.cdp.registration.ui.utils.*;
import com.philips.platform.appinfra.*;
import com.philips.platform.appinfra.appconfiguration.*;

import java.util.*;

public class RegistrationCoppaApplication extends Application {


    private static volatile RegistrationCoppaApplication mRegistrationHelper = null;

    private AppInfraInterface mAppInfraInterface;

    /**
     * @return instance of this class
     */
    public synchronized static RegistrationCoppaApplication getInstance() {
        return mRegistrationHelper;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mRegistrationHelper = this;
        mAppInfraInterface = new AppInfra.Builder().build(this);
        SharedPreferences prefs = getSharedPreferences("reg_dynamic_config", MODE_PRIVATE);
        String restoredText = prefs.getString("reg_environment", null);
        if (restoredText != null) {
            initRegistration(RegUtility.getConfiguration(restoredText));
        } else {
            initRegistration(Configuration.STAGING);
        }
    }

    public void initRegistration(Configuration configuration) {
        AppConfigurationInterface.AppConfigurationError configError = new
                AppConfigurationInterface.AppConfigurationError();
        if(mAppInfraInterface == null){
            mAppInfraInterface = new AppInfra.Builder().build(this);
        }

        SharedPreferences.Editor editor = getSharedPreferences("reg_dynamic_config", MODE_PRIVATE).edit();
        editor.putString("reg_environment", configuration.getValue());
        editor.commit();

        String languageCode;
        String countryCode;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            languageCode = LocaleList.getDefault().get(0).getLanguage();
            countryCode = LocaleList.getDefault().get(0).getCountry();
        }else{
            languageCode = Locale.getDefault().getLanguage();
            countryCode = Locale.getDefault().getCountry();
        }

        //   localeManager.setInputLocale("zh", "CN");

        initAppIdentity(configuration);
        URDependancies urDependancies = new URDependancies(mAppInfraInterface);
        URSettings urSettings = new URSettings(this);
        URInterface urInterface = new CoppaInterface();
        urInterface.init(urDependancies, urSettings);

        mAppInfraInterface.getConfigInterface().setPropertyForKey("appidentity.micrositeId",
                "appinfra",
                "77000",
                configError);

    }

    public static final String SERVICE_DISCOVERY_TAG = "ServiceDiscovery";
    final String AI = "appinfra";
    private void initAppIdentity(Configuration configuration) {
        if(mAppInfraInterface == null){
            mAppInfraInterface = new AppInfra.Builder().build(this);
        }
        AppIdentityInterface mAppIdentityInterface;
        mAppIdentityInterface = mAppInfraInterface.getAppIdentity();
        AppConfigurationInterface appConfigurationInterface = mAppInfraInterface.
                getConfigInterface();

        //Dynamically set the values to appInfar and app state

        AppConfigurationInterface.AppConfigurationError configError = new
                AppConfigurationInterface.AppConfigurationError();
       /* mAppInfraInterface.
                getConfigInterface().setPropertyForKey(
                "appidentity.micrositeId",
                AI,
                "77000",
                configError);*/

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

    public AppInfraInterface getAppInfra() {
        return mAppInfraInterface;
    }
}