/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package cdp.philips.com.mydemoapp;

import android.content.SharedPreferences;
import android.support.multidex.MultiDexApplication;

import com.philips.cdp.registration.AppIdentityInfo;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.Configuration;
import com.philips.cdp.registration.ui.utils.URDependancies;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.cdp.registration.ui.utils.URSettings;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.appidentity.AppIdentityInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.core.utils.UuidGenerator;
import com.philips.platform.dscdemo.database.DatabaseHelper;
import com.philips.platform.dscdemo.reciever.ScheduleSyncReceiver;
import com.philips.platform.dscdemo.registration.UserRegistrationInterfaceImpl;
import com.philips.platform.dscdemo.utility.SyncScheduler;

import java.util.ArrayList;




public class DataSyncApplication extends MultiDexApplication {
    public final DatabaseHelper databaseHelper = DatabaseHelper.getInstance(this, new UuidGenerator());
    public static AppInfraInterface gAppInfra;
    public static LoggingInterface loggingInterface;
    private AppConfigurationInterface.AppConfigurationError configError;
    //DataServicesManager mDataServicesManager;
    ScheduleSyncReceiver mScheduleSyncReceiver;
    UserRegistrationInterfaceImpl userRegImple;
    final String AI = "appinfra";

    @Override
    public void onCreate() {
        super.onCreate();
       // mDataServicesManager = DataServicesManager.getInstance();
        initAppInfra();

        initializeUserRegistrationLibrary(Configuration.STAGING);
        initHSDP();
        //init();
        mScheduleSyncReceiver = new ScheduleSyncReceiver();
        if(new User(this).isUserSignIn()) {
            SyncScheduler.getInstance().scheduleSync();
        }
    }

    private void initAppInfra() {
        gAppInfra = new AppInfra.Builder().build(getApplicationContext());
        configError = new
                AppConfigurationInterface.AppConfigurationError();
        loggingInterface = gAppInfra.getLogging().createInstanceForComponent("DataSync", "DataSync");
    }

    public UserRegistrationInterfaceImpl getUserRegImple() {
        return userRegImple;
    }

/*    private void init() {
        OrmCreator creator = new OrmCreator(new UuidGenerator());
        userRegImple = new UserRegistrationInterfaceImpl(this, new User(this));
        UserRegistrationInterface userRegistrationInterface = userRegImple;
        ErrorHandlerInterfaceImpl errorHandlerInterface = new ErrorHandlerInterfaceImpl();
        mDataServicesManager.initializeDataServices(this, creator, userRegistrationInterface, errorHandlerInterface);
        injectDBInterfacesToCore();
        mDataServicesManager.initializeSyncMonitors(this, null, null);
    }*/

/*    void injectDBInterfacesToCore() {
        try {
            Dao<OrmMoment, Integer> momentDao = databaseHelper.getMomentDao();
            Dao<OrmMomentDetail, Integer> momentDetailDao = databaseHelper.getMomentDetailDao();
            Dao<OrmMeasurement, Integer> measurementDao = databaseHelper.getMeasurementDao();
            Dao<OrmMeasurementDetail, Integer> measurementDetailDao = databaseHelper.getMeasurementDetailDao();
            Dao<OrmSynchronisationData, Integer> synchronisationDataDao = databaseHelper.getSynchronisationDataDao();
            Dao<OrmMeasurementGroup, Integer> measurementGroup = databaseHelper.getMeasurementGroupDao();
            Dao<OrmMeasurementGroupDetail, Integer> measurementGroupDetails = databaseHelper.getMeasurementGroupDetailDao();

            Dao<OrmConsentDetail, Integer> consentDetailsDao = databaseHelper.getConsentDetailsDao();
            Dao<OrmCharacteristics, Integer> characteristicsesDao = databaseHelper.getCharacteristicsDao();

            Dao<OrmSettings, Integer> settingsDao = databaseHelper.getSettingsDao();
            Dao<OrmInsight, Integer> insightsDao = databaseHelper.getInsightDao();
            Dao<OrmInsightMetaData, Integer> insightMetaDataDao = databaseHelper.getInsightMetaDataDao();

            Dao<OrmDCSync, Integer> dcSyncDao = databaseHelper.getDCSyncDao();
            OrmSaving saving = new OrmSaving(momentDao, momentDetailDao, measurementDao, measurementDetailDao,
                    synchronisationDataDao,consentDetailsDao, measurementGroup, measurementGroupDetails,
                    characteristicsesDao, settingsDao, insightsDao, insightMetaDataDao, dcSyncDao);


            OrmUpdating updating = new OrmUpdating(momentDao, momentDetailDao, measurementDao, measurementDetailDao, settingsDao,
                    consentDetailsDao, dcSyncDao, measurementGroup, synchronisationDataDao, measurementGroupDetails);
            OrmFetchingInterfaceImpl fetching = new OrmFetchingInterfaceImpl(momentDao, synchronisationDataDao, consentDetailsDao, characteristicsesDao,
                    settingsDao, dcSyncDao, insightsDao);
            OrmDeleting deleting = new OrmDeleting(momentDao, momentDetailDao, measurementDao,
                    measurementDetailDao, synchronisationDataDao, measurementGroupDetails, measurementGroup, consentDetailsDao, characteristicsesDao, settingsDao, dcSyncDao, insightsDao, insightMetaDataDao);


            BaseAppDateTime uGrowDateTime = new BaseAppDateTime();
            ORMSavingInterfaceImpl ORMSavingInterfaceImpl = new ORMSavingInterfaceImpl(saving, updating, fetching, deleting, uGrowDateTime);
            OrmDeletingInterfaceImpl ORMDeletingInterfaceImpl = new OrmDeletingInterfaceImpl(deleting, saving, fetching);
            ORMUpdatingInterfaceImpl dbInterfaceOrmUpdatingInterface = new ORMUpdatingInterfaceImpl(saving, updating, fetching, deleting);
            OrmFetchingInterfaceImpl dbInterfaceOrmFetchingInterface = new OrmFetchingInterfaceImpl(momentDao, synchronisationDataDao, consentDetailsDao,
                    characteristicsesDao, settingsDao, dcSyncDao, insightsDao);

            mDataServicesManager.initializeDatabaseMonitor(this, ORMDeletingInterfaceImpl, dbInterfaceOrmFetchingInterface, ORMSavingInterfaceImpl, dbInterfaceOrmUpdatingInterface);
        } catch (SQLException exception) {
            Toast.makeText(this, "db injection failed to dataservices", Toast.LENGTH_SHORT).show();
        }
    }*/

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

        initAppIdentity(configuration);
        URDependancies urDependancies = new URDependancies(gAppInfra);
        URSettings urSettings = new URSettings(this);
        URInterface urInterface = new URInterface();
        urInterface.init(urDependancies, urSettings);

    }

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


    public void initHSDP() {
        gAppInfra.
                getConfigInterface().setPropertyForKey(
                "HSDPConfiguration.ApplicationName",
                "UserRegistration",
                "OneBackend",
                configError);

        gAppInfra.
                getConfigInterface().setPropertyForKey(
                "HSDPConfiguration.Secret",
                "UserRegistration",
                "ad3d0618-be4d-4958-adc9-f6bcd01fde16",
                configError);

        gAppInfra.
                getConfigInterface().setPropertyForKey(
                "HSDPConfiguration.Shared",
                "UserRegistration",
                "ba404af2-ee41-4e7c-9157-fd20663f2a6c",
                configError);

        gAppInfra.
                getConfigInterface().setPropertyForKey(
                "HSDPConfiguration.BaseURL",
                "UserRegistration",
                "https://platforminfra-ds-platforminfrastaging.cloud.pcftest.com",
                configError);
    }

   /* void scheduleSync() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                try {
                    mScheduleSyncReceiver.onReceive(getApplicationContext());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    handler.postDelayed(this, ScheduleSyncReceiver.DATA_FETCH_FREQUENCY);
                }
            }
        };
        runnable.run();
    }*/
}
