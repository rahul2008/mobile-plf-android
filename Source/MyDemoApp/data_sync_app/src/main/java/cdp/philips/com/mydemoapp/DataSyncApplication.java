package cdp.philips.com.mydemoapp;

import android.app.Application;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.j256.ormlite.dao.Dao;
import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.registration.AppIdentityInfo;
import com.philips.cdp.registration.User;
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
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.core.listeners.SynchronisationCompleteListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DSLog;
import com.philips.platform.core.utils.UuidGenerator;
import com.philips.platform.datasync.userprofile.UserRegistrationInterface;
import com.squareup.leakcanary.LeakCanary;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;

import cdp.philips.com.mydemoapp.database.DatabaseHelper;
import cdp.philips.com.mydemoapp.database.ORMSavingInterfaceImpl;
import cdp.philips.com.mydemoapp.database.ORMUpdatingInterfaceImpl;
import cdp.philips.com.mydemoapp.database.OrmCreator;
import cdp.philips.com.mydemoapp.database.OrmDeleting;
import cdp.philips.com.mydemoapp.database.OrmDeletingInterfaceImpl;
import cdp.philips.com.mydemoapp.database.OrmFetchingInterfaceImpl;
import cdp.philips.com.mydemoapp.database.OrmSaving;
import cdp.philips.com.mydemoapp.database.OrmUpdating;
import cdp.philips.com.mydemoapp.database.table.BaseAppDateTime;
import cdp.philips.com.mydemoapp.database.table.OrmCharacteristics;
import cdp.philips.com.mydemoapp.database.table.OrmCharacteristicsDetail;
import cdp.philips.com.mydemoapp.database.table.OrmConsent;
import cdp.philips.com.mydemoapp.database.table.OrmConsentDetail;
import cdp.philips.com.mydemoapp.database.table.OrmDCSync;
import cdp.philips.com.mydemoapp.database.table.OrmMeasurement;
import cdp.philips.com.mydemoapp.database.table.OrmMeasurementDetail;
import cdp.philips.com.mydemoapp.database.table.OrmMeasurementGroup;
import cdp.philips.com.mydemoapp.database.table.OrmMeasurementGroupDetail;
import cdp.philips.com.mydemoapp.database.table.OrmMoment;
import cdp.philips.com.mydemoapp.database.table.OrmMomentDetail;
import cdp.philips.com.mydemoapp.database.table.OrmSettings;
import cdp.philips.com.mydemoapp.database.table.OrmSynchronisationData;
import cdp.philips.com.mydemoapp.error.ErrorHandlerInterfaceImpl;
import cdp.philips.com.mydemoapp.registration.UserRegistrationInterfaceImpl;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */


public class DataSyncApplication extends Application {

    public static AppInfraInterface gAppInfra;
    ServiceDiscoveryInterface serviceDiscoveryInterface;
    public static LoggingInterface loggingInterface;
    private AppConfigurationInterface.AppConfigurationError configError;
    DataServicesManager mDataServicesManager;
    String mDataCoreUrl = null;
    private final String APP_NAME = "appname";
    private final String DATACORE_FALLBACK_URL = "dataCoreUrl";
    private final String DATASERVICES_KEY = "dataservices";

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
        Stetho.initializeWithDefaults(this);
        mDataServicesManager = DataServicesManager.getInstance();
        initAppInfra();
        setLocale();
        initializeUserRegistrationLibrary(Configuration.STAGING);
        init();
        fetchDataServicesUrl();
    }

    private void initAppInfra() {
        gAppInfra = new AppInfra.Builder().build(getApplicationContext());
        serviceDiscoveryInterface = gAppInfra.getServiceDiscovery();
        configError = new
                AppConfigurationInterface.AppConfigurationError();
        loggingInterface = gAppInfra.getLogging().createInstanceForComponent("DataSync", "DataSync");
    }

    private void init() {
        SynchronisationCompleteListener synchronisationCompleteListener = new SynchronisationCompleteListener() {
            @Override
            public void onSyncComplete() {

            }

            @Override
            public void onSyncFailed(Exception exception) {

            }
        };
        OrmCreator creator = new OrmCreator(new UuidGenerator());
        UserRegistrationInterface userRegistrationInterface = new UserRegistrationInterfaceImpl(this, new User(this));
        ErrorHandlerInterfaceImpl errorHandlerInterface = new ErrorHandlerInterfaceImpl();
        mDataServicesManager.initializeDataServices(this, creator, userRegistrationInterface, errorHandlerInterface);
        injectDBInterfacesToCore();
        mDataServicesManager.initializeSyncMonitors(this, null, null,synchronisationCompleteListener);
    }

    void injectDBInterfacesToCore() {
        final DatabaseHelper databaseHelper = new DatabaseHelper(this, new UuidGenerator());
        try {
            Dao<OrmMoment, Integer> momentDao = databaseHelper.getMomentDao();
            Dao<OrmMomentDetail, Integer> momentDetailDao = databaseHelper.getMomentDetailDao();
            Dao<OrmMeasurement, Integer> measurementDao = databaseHelper.getMeasurementDao();
            Dao<OrmMeasurementDetail, Integer> measurementDetailDao = databaseHelper.getMeasurementDetailDao();
            Dao<OrmSynchronisationData, Integer> synchronisationDataDao = databaseHelper.getSynchronisationDataDao();
            Dao<OrmMeasurementGroup, Integer> measurementGroup = databaseHelper.getMeasurementGroupDao();
            Dao<OrmMeasurementGroupDetail, Integer> measurementGroupDetails = databaseHelper.getMeasurementGroupDetailDao();

            Dao<OrmConsent, Integer> consentDao = databaseHelper.getConsentDao();
            Dao<OrmConsentDetail, Integer> consentDetailsDao = databaseHelper.getConsentDetailsDao();
            Dao<OrmCharacteristics, Integer> characteristicsesDao = databaseHelper.getCharacteristicsDao();
            Dao<OrmCharacteristicsDetail, Integer> characteristicsDetailsDao = databaseHelper.getCharacteristicsDetailsDao();

            Dao<OrmSettings, Integer> settingsDao = databaseHelper.getSettingsDao();


            OrmSaving saving = new OrmSaving(momentDao, momentDetailDao, measurementDao, measurementDetailDao,
                    synchronisationDataDao, consentDao, consentDetailsDao, measurementGroup, measurementGroupDetails, characteristicsesDao, characteristicsDetailsDao, settingsDao);

            Dao<OrmDCSync, Integer> dcSyncDao = databaseHelper.getDCSyncDao();
            OrmUpdating updating = new OrmUpdating(momentDao, momentDetailDao, measurementDao, measurementDetailDao, consentDao, settingsDao, dcSyncDao, measurementGroup, synchronisationDataDao, measurementGroupDetails);
            OrmFetchingInterfaceImpl fetching = new OrmFetchingInterfaceImpl(momentDao, synchronisationDataDao, consentDao, consentDetailsDao, characteristicsesDao, settingsDao, characteristicsDetailsDao, dcSyncDao);
            OrmDeleting deleting = new OrmDeleting(momentDao, momentDetailDao, measurementDao,
                    measurementDetailDao, synchronisationDataDao, measurementGroupDetails, measurementGroup, consentDao, consentDetailsDao, characteristicsesDao, characteristicsDetailsDao, settingsDao);


            BaseAppDateTime uGrowDateTime = new BaseAppDateTime();
            ORMSavingInterfaceImpl ORMSavingInterfaceImpl = new ORMSavingInterfaceImpl(saving, updating, fetching, deleting, uGrowDateTime);
            OrmDeletingInterfaceImpl ORMDeletingInterfaceImpl = new OrmDeletingInterfaceImpl(deleting, saving);
            ORMUpdatingInterfaceImpl dbInterfaceOrmUpdatingInterface = new ORMUpdatingInterfaceImpl(saving, updating, fetching, deleting);
            OrmFetchingInterfaceImpl dbInterfaceOrmFetchingInterface = new OrmFetchingInterfaceImpl(momentDao, synchronisationDataDao, consentDao, consentDetailsDao, characteristicsesDao, settingsDao, characteristicsDetailsDao, dcSyncDao);

            mDataServicesManager.initializeDBMonitors(this, ORMDeletingInterfaceImpl, dbInterfaceOrmFetchingInterface, ORMSavingInterfaceImpl, dbInterfaceOrmUpdatingInterface);
        } catch (SQLException exception) {
            Toast.makeText(this, "db injection failed to dataservices", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * For doing dynamic initialisation Of User registration
     *
     * @param configuration The environment ype as required by UR
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

        ArrayList<String> providers = new ArrayList<>();
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
        editor.apply();


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
        gAppInfra.
                getConfigInterface().setPropertyForKey(
                "HSDPConfiguration.ApplicationName",
                URConfigurationConstants.UR,
                loadAppNameFromConfigParams(APP_NAME),
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
                mDataCoreUrl,
                configError);
    }

    protected void fetchDataServicesUrl() {
        serviceDiscoveryInterface.getServiceUrlWithCountryPreference("ds.dataservice", new
                ServiceDiscoveryInterface.OnGetServiceUrlListener() {
                    @Override
                    public void onError(ERRORVALUES errorvalues, String s) {
                        DSLog.e(DSLog.LOG, "Error");
                        mDataCoreUrl = loadAppNameFromConfigParams(DATACORE_FALLBACK_URL);
                        initHSDP();
                    }

                    @Override
                    public void onSuccess(URL url) {
                        DSLog.e(DSLog.LOG, "Success = " + url);
                        if (url.toString().isEmpty()) {
                            mDataCoreUrl = loadAppNameFromConfigParams(DATACORE_FALLBACK_URL);
                        } else {

                            mDataCoreUrl = url.toString();
                        }
                        initHSDP();
                    }
                });
    }

    private String loadAppNameFromConfigParams(String propertyKey) {
        String appname = (String) gAppInfra.getConfigInterface().getPropertyForKey(propertyKey, DATASERVICES_KEY, configError);
        if (configError.getErrorCode() != null) {
            DSLog.e(DSLog.LOG, "VerticalAppConfig ==loadConfigurationFromAsset " + configError.getErrorCode().toString());
        }
        return appname;
    }
}
