/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package cdp.philips.com.demoapp;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
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
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.UuidGenerator;
import com.philips.platform.datasync.userprofile.UserRegistrationInterface;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import cdp.philips.com.demoapp.database.DatabaseHelper;
import cdp.philips.com.demoapp.database.ORMSavingInterfaceImpl;
import cdp.philips.com.demoapp.database.ORMUpdatingInterfaceImpl;
import cdp.philips.com.demoapp.database.OrmCreator;
import cdp.philips.com.demoapp.database.OrmDeleting;
import cdp.philips.com.demoapp.database.OrmDeletingInterfaceImpl;
import cdp.philips.com.demoapp.database.OrmFetchingInterfaceImpl;
import cdp.philips.com.demoapp.database.OrmSaving;
import cdp.philips.com.demoapp.database.OrmUpdating;
import cdp.philips.com.demoapp.database.table.BaseAppDateTime;
import cdp.philips.com.demoapp.database.table.OrmConsentDetail;
import cdp.philips.com.demoapp.database.table.OrmDCSync;
import cdp.philips.com.demoapp.error.ErrorHandlerInterfaceImpl;
import cdp.philips.com.demoapp.registration.UserRegistrationInterfaceImpl;

public class DemoApplication extends Application {

    final String AI = "appinfra";
    private AppInfra gAppInfra;
    private DataServicesManager mDataServicesManager;
    private DatabaseHelper mDatabaseHelper;
    private Context mContext;
    private AppConfigurationInterface.AppConfigurationError configError;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();

        initAppInfra();

        initializeUserRegistrationLibrary(Configuration.STAGING);
        initHSDP();

        initPreRequisite(getApplicationContext(), gAppInfra);
    }

    private void initAppInfra() {
        gAppInfra = new AppInfra.Builder().build(getApplicationContext());
        configError = new
                AppConfigurationInterface.AppConfigurationError();
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

    private void initPreRequisite(Context context, AppInfraInterface appInfra) {
        mDatabaseHelper = DatabaseHelper.getInstance(context, new UuidGenerator());
        appInfra.getLogging().createInstanceForComponent("DevicePairing", "DevicePairing");
        init();
    }

    private void init() {
        mDataServicesManager = DataServicesManager.getInstance();

        OrmCreator creator = new OrmCreator(new UuidGenerator());
        UserRegistrationInterface userRegistrationInterface = new UserRegistrationInterfaceImpl(mContext, new User(mContext));
        ErrorHandlerInterfaceImpl errorHandlerInterface = new ErrorHandlerInterfaceImpl();

        Set<String> fetchers = new HashSet<>(2);
        fetchers.add(SyncType.MOMENT.getDescription());
        fetchers.add(SyncType.CONSENT.getDescription());
        mDataServicesManager.configureSyncDataType(fetchers);

        DataServicesManager.getInstance().initializeDataServices(mContext, creator, userRegistrationInterface, errorHandlerInterface);
        injectDBInterfacesToCore();
        DataServicesManager.getInstance().initializeSyncMonitors(mContext, null, null);

        User user = new User(mContext);
        if (user.isUserSignIn()) {
            SyncScheduler.getInstance().scheduleSync();
        }
    }

    private void injectDBInterfacesToCore() {
        try {
            Dao<OrmConsentDetail, Integer> consentDetailsDao = mDatabaseHelper.getConsentDetailsDao();
            Dao<OrmDCSync, Integer> dcSyncDao = mDatabaseHelper.getDCSyncDao();

            OrmSaving saving = new OrmSaving(consentDetailsDao, dcSyncDao);
            OrmUpdating updating = new OrmUpdating(consentDetailsDao, dcSyncDao);
            OrmFetchingInterfaceImpl fetching = new OrmFetchingInterfaceImpl(consentDetailsDao, dcSyncDao);
            OrmDeleting deleting = new OrmDeleting(consentDetailsDao, dcSyncDao);

            BaseAppDateTime baseAppDateTime = new BaseAppDateTime();
            ORMSavingInterfaceImpl ORMSavingInterfaceImpl = new ORMSavingInterfaceImpl(saving, updating, fetching, deleting, baseAppDateTime);
            OrmDeletingInterfaceImpl ORMDeletingInterfaceImpl = new OrmDeletingInterfaceImpl(deleting, saving, fetching);
            ORMUpdatingInterfaceImpl dbInterfaceOrmUpdatingInterface = new ORMUpdatingInterfaceImpl(saving, updating, fetching, deleting);
            OrmFetchingInterfaceImpl dbInterfaceOrmFetchingInterface = new OrmFetchingInterfaceImpl(consentDetailsDao, dcSyncDao);

            mDataServicesManager.initializeDatabaseMonitor(mContext, ORMDeletingInterfaceImpl, dbInterfaceOrmFetchingInterface, ORMSavingInterfaceImpl, dbInterfaceOrmUpdatingInterface);
        } catch (SQLException exception) {
            Toast.makeText(mContext, "db injection failed to dataservices", Toast.LENGTH_SHORT).show();
        }
    }
}
