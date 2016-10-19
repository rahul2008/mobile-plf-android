package cdp.philips.com.mydemoapp;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Handler;

//import com.facebook.stetho.Stetho;
import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.registration.AppIdentityInfo;
import com.philips.cdp.registration.configuration.Configuration;
import com.philips.cdp.registration.configuration.URConfigurationConstants;
import com.philips.cdp.registration.ui.utils.URDependancies;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.cdp.registration.ui.utils.URSettings;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.appidentity.AppIdentityInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.core.BaseAppCore;
import com.philips.platform.core.Eventing;
import com.philips.platform.core.utils.UuidGenerator;
import com.philips.platform.datasync.Backend;
import com.philips.platform.datasync.userprofile.UserRegistrationFacade;

import java.util.ArrayList;
import java.util.Locale;

import javax.inject.Inject;

import cdp.philips.com.mydemoapp.database.DatabaseHelper;
import cdp.philips.com.mydemoapp.injection.AppComponent;
import cdp.philips.com.mydemoapp.injection.ApplicationModule;
import cdp.philips.com.mydemoapp.injection.BackendModule;
import cdp.philips.com.mydemoapp.injection.CoreModule;
import cdp.philips.com.mydemoapp.injection.DaggerAppComponent;
import cdp.philips.com.mydemoapp.injection.DatabaseModule;
import cdp.philips.com.mydemoapp.injection.MonitorModule;
import cdp.philips.com.mydemoapp.injection.RegistrationModule;
import cdp.philips.com.mydemoapp.utility.EventingImpl;
import de.greenrobot.event.EventBus;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */


public class DataSyncApplication extends Application{

    @Inject
    Backend backend;

    @Inject
    BaseAppCore core;

    @Inject
    Eventing eventing;

    AppComponent appComponent;

    @Inject
    UserRegistrationFacade userRegistrationFacade;
    public static AppInfraInterface gAppInfra;
    public static LoggingInterface loggingInterface;

    @Override
    public void onCreate() {
        super.onCreate();

        gAppInfra = new AppInfra.Builder().build(getApplicationContext());
        loggingInterface = gAppInfra.getLogging().createInstanceForComponent("DataSync", "DataSync");
        loggingInterface.enableConsoleLog(true);
        loggingInterface.enableFileLog(true);
        setLocale();

        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext(), new UuidGenerator());
        databaseHelper.getWritableDatabase();
       // Stetho.initializeWithDefaults(this);

        initializeUserRegistrationLibrary(Configuration.DEVELOPMENT);

        prepareInjectionsGraph();
        appComponent.injectApplication(this);
        core.start();
    }

    /**For doing dynamic initialisation Of User registration
     *
     * @param configuration  The environment ype as required by UR
     */
    public void initializeUserRegistrationLibrary(Configuration configuration) {
        final String UR = "UserRegistration";

        AppConfigurationInterface.AppConfigurationError configError = new
                AppConfigurationInterface.AppConfigurationError();
        gAppInfra.getConfigInterface().setPropertyForKey("JanRainConfiguration." +
                        "RegistrationClientID." + Configuration.DEVELOPMENT
                , UR,
                "8kaxdrpvkwyr7pnp987amu4aqb4wmnte",
                configError);
        gAppInfra.getConfigInterface().setPropertyForKey("JanRainConfiguration." +
                        "RegistrationClientID." + Configuration.TESTING
                , UR,
                "g52bfma28yjbd24hyjcswudwedcmqy7c",
                configError);
        gAppInfra.getConfigInterface().setPropertyForKey("JanRainConfiguration." +
                        "RegistrationClientID." + Configuration.EVALUATION
                , UR,
                "f2stykcygm7enbwfw2u9fbg6h6syb8yd",
                configError);
        gAppInfra.getConfigInterface().setPropertyForKey("JanRainConfiguration." +
                        "RegistrationClientID." + Configuration.STAGING
                , UR,
                "f2stykcygm7enbwfw2u9fbg6h6syb8yd",
                configError);
        gAppInfra.getConfigInterface().setPropertyForKey("JanRainConfiguration." +
                        "RegistrationClientID." + Configuration.PRODUCTION
                , UR,
                "9z23k3q8bhqyfwx78aru6bz8zksga54u",
                configError);


        gAppInfra.getConfigInterface().setPropertyForKey("PILConfiguration." +
                        "MicrositeID",
                UR,
                "77000",
                configError);
        gAppInfra.getConfigInterface().setPropertyForKey("PILConfiguration." +
                        "RegistrationEnvironment",
                UR,
                configuration.getValue(),
                configError);

        gAppInfra.
                getConfigInterface().setPropertyForKey("Flow." +
                        "EmailVerificationRequired",
                UR,
                "" + true,
                configError);
        gAppInfra.
                getConfigInterface().setPropertyForKey("Flow." +
                        "TermsAndConditionsAcceptanceRequired",
                UR,
                "" + true,
                configError);

        String minAge = "{ \"NL\":12 ,\"GB\":0,\"default\": 16}";
        gAppInfra.
                getConfigInterface().setPropertyForKey("Flow." +
                        "MinimumAgeLimit",
                UR,
                minAge,
                configError);

        initHSDP();

        ArrayList<String> providers = new ArrayList<String>();
        providers.add("facebook");
        providers.add("googleplus");
        providers.add("sinaweibo");
        providers.add("qq");
        gAppInfra.
                getConfigInterface().setPropertyForKey("SigninProviders." +
                        "NL",
                UR,
                providers,
                configError);

        gAppInfra.
                getConfigInterface().setPropertyForKey("SigninProviders." +
                        "US",
                UR,
                providers,
                configError);

        gAppInfra.
                getConfigInterface().setPropertyForKey("SigninProviders." +
                        "default",
                UR,
                providers,
                configError);


        SharedPreferences.Editor editor = getSharedPreferences("reg_dynamic_config", MODE_PRIVATE).edit();
        editor.putString("reg_environment", configuration.getValue());
        editor.commit();


        String languageCode = Locale.getDefault().getLanguage();
        String countryCode = Locale.getDefault().getCountry();

        PILLocaleManager localeManager = new PILLocaleManager(this);
        localeManager.setInputLocale(languageCode, countryCode);

        initAppIdentity(configuration);
        URDependancies urDependancies = new URDependancies(gAppInfra);
        URSettings urSettings = new URSettings(this);
        URInterface urInterface = new URInterface();
        urInterface.init(urDependancies, urSettings);

    }

    final String AI = "appinfra";
    private void initAppIdentity(Configuration configuration) {
        AppIdentityInterface mAppIdentityInterface;
        mAppIdentityInterface = gAppInfra.getAppIdentity();
        AppConfigurationInterface appConfigurationInterface = gAppInfra.
                getConfigInterface();

        //Dynamically set the values to appInfar and app state

        AppConfigurationInterface.AppConfigurationError configError = new
                AppConfigurationInterface.AppConfigurationError();
        gAppInfra.
                getConfigInterface().setPropertyForKey(
                "appidentity.micrositeId",
                AI,
                "77000",
                configError);

        gAppInfra.
                getConfigInterface().setPropertyForKey(
                "appidentity.sector",
                AI,
                "b2c",
                configError);

        gAppInfra.
                getConfigInterface().setPropertyForKey(
                "appidentity.serviceDiscoveryEnvironment",
                AI,
                "Production",
                configError);


        switch (configuration) {
            case EVALUATION:
                gAppInfra.
                        getConfigInterface().setPropertyForKey(
                        "appidentity.appState",
                        AI,
                        "ACCEPTANCE",
                        configError);
                break;
            case DEVELOPMENT:
                gAppInfra.
                        getConfigInterface().setPropertyForKey(
                        "appidentity.appState",
                        AI,
                        "DEVELOPMENT",
                        configError);

                break;
            case PRODUCTION:
                gAppInfra.
                        getConfigInterface().setPropertyForKey(
                        "appidentity.appState",
                        AI,
                        "PRODUCTION",
                        configError);
                break;
            case STAGING:
                gAppInfra.
                        getConfigInterface().setPropertyForKey(
                        "appidentity.appState",
                        AI,
                        "STAGING",
                        configError);

                break;
            case TESTING:
                gAppInfra.
                        getConfigInterface().setPropertyForKey(
                        "appidentity.appState",
                        AI,
                        "TEST",
                        configError);
                break;
        }

        AppIdentityInfo appIdentityInfo = new AppIdentityInfo();
        appIdentityInfo.setAppLocalizedNAme(mAppIdentityInterface.getLocalizedAppName());
        appIdentityInfo.setSector(mAppIdentityInterface.getSector());
        appIdentityInfo.setMicrositeId(mAppIdentityInterface.getMicrositeId());
        appIdentityInfo.setAppName(mAppIdentityInterface.getAppName());
        appIdentityInfo.setAppState(mAppIdentityInterface.getAppState().toString());
        appIdentityInfo.setAppVersion(mAppIdentityInterface.getAppVersion());
        appIdentityInfo.setServiceDiscoveryEnvironment(mAppIdentityInterface.getServiceDiscoveryEnvironment());

    }

    private void setLocale() {
        String languageCode = Locale.getDefault().getLanguage();
        String countryCode = Locale.getDefault().getCountry();

        PILLocaleManager localeManager = new PILLocaleManager(this);
        localeManager.setInputLocale(languageCode, countryCode);
    }

    public void initHSDP() {
        AppConfigurationInterface.AppConfigurationError configError = new
                AppConfigurationInterface.AppConfigurationError();
        gAppInfra.
                getConfigInterface().setPropertyForKey(
                "HSDPConfiguration.ApplicationName",
                URConfigurationConstants.UR,
                "Datacore",
                configError);

        gAppInfra.
                getConfigInterface().setPropertyForKey(
                "HSDPConfiguration.Secret",
                URConfigurationConstants.UR,
                "ad3d0618-be4d-4958-adc9-f6bcd01fde16",
                configError);

        gAppInfra.
                getConfigInterface().setPropertyForKey(
                "HSDPConfiguration.Shared",
                URConfigurationConstants.UR,
                "ba404af2-ee41-4e7c-9157-fd20663f2a6c",
                configError);

        gAppInfra.
                getConfigInterface().setPropertyForKey(
                "HSDPConfiguration.BaseURL",
                URConfigurationConstants.UR,
                "https://referenceplatform-ds-platforminfradev.cloud.pcftest.com",
                configError);
    }

    protected void prepareInjectionsGraph() {
        final DatabaseModule databaseModule = new DatabaseModule();
        final MonitorModule monitorModule = new MonitorModule(this);
        final RegistrationModule registrationModule = new RegistrationModule();
        BackendModule backendModule = new BackendModule();
        final CoreModule coreModule = new CoreModule(new EventingImpl(new EventBus(), new Handler()));
        final ApplicationModule applicationModule = new ApplicationModule(this);

        // initiating all application module events


        appComponent = DaggerAppComponent.builder().databaseModule(databaseModule).backendModule(backendModule).registrationModule(registrationModule).coreModule(coreModule).monitorModule(monitorModule).applicationModule(applicationModule).build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
