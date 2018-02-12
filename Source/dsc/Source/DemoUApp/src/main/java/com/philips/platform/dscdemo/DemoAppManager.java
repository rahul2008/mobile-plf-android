package com.philips.platform.dscdemo;

import android.content.Context;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.philips.cdp.registration.User;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.mya.catk.ConsentsClient;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.UuidGenerator;
import com.philips.platform.dscdemo.database.DatabaseHelper;
import com.philips.platform.dscdemo.database.ORMSavingInterfaceImpl;
import com.philips.platform.dscdemo.database.ORMUpdatingInterfaceImpl;
import com.philips.platform.dscdemo.database.OrmCreator;
import com.philips.platform.dscdemo.database.OrmDeleting;
import com.philips.platform.dscdemo.database.OrmDeletingInterfaceImpl;
import com.philips.platform.dscdemo.database.OrmFetchingInterfaceImpl;
import com.philips.platform.dscdemo.database.OrmSaving;
import com.philips.platform.dscdemo.database.OrmUpdating;
import com.philips.platform.dscdemo.database.table.OrmCharacteristics;
import com.philips.platform.dscdemo.database.table.OrmConsentDetail;
import com.philips.platform.dscdemo.database.table.OrmDCSync;
import com.philips.platform.dscdemo.database.table.OrmInsight;
import com.philips.platform.dscdemo.database.table.OrmInsightMetaData;
import com.philips.platform.dscdemo.database.table.OrmMeasurement;
import com.philips.platform.dscdemo.database.table.OrmMeasurementDetail;
import com.philips.platform.dscdemo.database.table.OrmMeasurementGroup;
import com.philips.platform.dscdemo.database.table.OrmMeasurementGroupDetail;
import com.philips.platform.dscdemo.database.table.OrmMoment;
import com.philips.platform.dscdemo.database.table.OrmMomentDetail;
import com.philips.platform.dscdemo.database.table.OrmSettings;
import com.philips.platform.dscdemo.database.table.OrmSynchronisationData;
import com.philips.platform.dscdemo.utility.DSErrorHandler;
import com.philips.platform.dscdemo.utility.DemoUAppLog;
import com.philips.platform.dscdemo.utility.UserRegistrationHandler;

import java.sql.SQLException;

public class DemoAppManager {
    private DatabaseHelper mDatabaseHelper;
    private AppInfraInterface mAppInfra;
    private DataServicesManager mDataServicesManager;
    private UserRegistrationHandler userRegistrationHandler;
    private static DemoAppManager sDemoAppManager;

    private DemoAppManager() {
    }

    public static synchronized DemoAppManager getInstance() {
        if (sDemoAppManager == null) {
            return sDemoAppManager = new DemoAppManager();
        }
        return sDemoAppManager;
    }

    void initPreRequisite(Context context, AppInfraInterface appInfra) {
        this.mAppInfra = appInfra;
        mDatabaseHelper = DatabaseHelper.getInstance(context, mAppInfra);
        initLogger();
        initDataServicesLibrary(context);
    }

    private void initLogger() {
        DemoUAppLog.init();
        DemoUAppLog.enableLogging();
    }

    private void initDataServicesLibrary(Context context) {

        if(ConsentsClient.getInstance().getConsentDefinitions() == null){
            throw new RuntimeException("Consent Access Toolkit must be initialized first ->\n\n" +
                    "ConsentsClient.getInstance().init(new CatkInputs.Builder()\n" +
                    "                .setContext(context)\n" +
                    "                .setAppInfraInterface(app.getAppInfra())\n" +
                    "                .setConsentDefinitions(<list of your consentDefinitions>).build());");
        }

        mDataServicesManager = DataServicesManager.getInstance();

        OrmCreator creator = new OrmCreator(new UuidGenerator());
        DSErrorHandler dsErrorHandler = new DSErrorHandler();
        userRegistrationHandler = new UserRegistrationHandler(context, new User(context));

        mDataServicesManager.initializeDataServices(context, creator, userRegistrationHandler, dsErrorHandler, mAppInfra);
        injectDBInterfacesToCore(context);
        mDataServicesManager.initializeSyncMonitors(context, null, null);
    }

    private void injectDBInterfacesToCore(Context context) {
        try {
            Dao<OrmMoment, Integer> momentDao = mDatabaseHelper.getMomentDao();
            Dao<OrmMomentDetail, Integer> momentDetailDao = mDatabaseHelper.getMomentDetailDao();
            Dao<OrmMeasurement, Integer> measurementDao = mDatabaseHelper.getMeasurementDao();
            Dao<OrmMeasurementDetail, Integer> measurementDetailDao = mDatabaseHelper.getMeasurementDetailDao();
            Dao<OrmSynchronisationData, Integer> synchronisationDataDao = mDatabaseHelper.getSynchronisationDataDao();
            Dao<OrmMeasurementGroup, Integer> measurementGroup = mDatabaseHelper.getMeasurementGroupDao();
            Dao<OrmMeasurementGroupDetail, Integer> measurementGroupDetails = mDatabaseHelper.getMeasurementGroupDetailDao();

            Dao<OrmConsentDetail, Integer> consentDetailsDao = mDatabaseHelper.getConsentDetailsDao();
            Dao<OrmCharacteristics, Integer> characteristicsesDao = mDatabaseHelper.getCharacteristicsDao();

            Dao<OrmSettings, Integer> settingsDao = mDatabaseHelper.getSettingsDao();
            Dao<OrmInsight, Integer> insightsDao = mDatabaseHelper.getInsightDao();
            Dao<OrmInsightMetaData, Integer> insightMetaDataDao = mDatabaseHelper.getInsightMetaDataDao();

            Dao<OrmDCSync, Integer> dcSyncDao = mDatabaseHelper.getDCSyncDao();
            OrmSaving saving = new OrmSaving(momentDao, momentDetailDao, measurementDao, measurementDetailDao,
                    synchronisationDataDao, consentDetailsDao, measurementGroup, measurementGroupDetails,
                    characteristicsesDao, settingsDao, insightsDao, insightMetaDataDao, dcSyncDao);

            OrmUpdating updating = new OrmUpdating(momentDao, momentDetailDao, measurementDao, measurementDetailDao, settingsDao,
                    consentDetailsDao, dcSyncDao, measurementGroup, synchronisationDataDao, measurementGroupDetails);
            OrmFetchingInterfaceImpl fetching = new OrmFetchingInterfaceImpl(momentDao, synchronisationDataDao, consentDetailsDao, characteristicsesDao,
                    settingsDao, dcSyncDao, insightsDao);
            OrmDeleting deleting = new OrmDeleting(momentDao, momentDetailDao, measurementDao,
                    measurementDetailDao, synchronisationDataDao, measurementGroupDetails, measurementGroup, consentDetailsDao, characteristicsesDao, settingsDao, dcSyncDao, insightsDao, insightMetaDataDao);

            ORMSavingInterfaceImpl ORMSavingInterfaceImpl = new ORMSavingInterfaceImpl(saving, updating, deleting);
            OrmDeletingInterfaceImpl ORMDeletingInterfaceImpl = new OrmDeletingInterfaceImpl(deleting, saving, fetching);
            ORMUpdatingInterfaceImpl dbInterfaceOrmUpdatingInterface = new ORMUpdatingInterfaceImpl(saving, updating, fetching, deleting);
            OrmFetchingInterfaceImpl dbInterfaceOrmFetchingInterface = new OrmFetchingInterfaceImpl(momentDao, synchronisationDataDao, consentDetailsDao,
                    characteristicsesDao, settingsDao, dcSyncDao, insightsDao);

            mDataServicesManager.initializeDatabaseMonitor(context, ORMDeletingInterfaceImpl, dbInterfaceOrmFetchingInterface, ORMSavingInterfaceImpl, dbInterfaceOrmUpdatingInterface);
        } catch (SQLException exception) {
            Toast.makeText(context, "DB injection failed", Toast.LENGTH_SHORT).show();
        }
    }

    public UserRegistrationHandler getUserRegistrationHandler() {
        return userRegistrationHandler;
    }

    public AppInfraInterface getAppInfra() {
        return mAppInfra;
    }

    public DatabaseHelper getDatabaseHelper() {
        return mDatabaseHelper;
    }
}