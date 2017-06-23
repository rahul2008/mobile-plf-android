package cdp.philips.com.utility;

import android.content.Context;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.philips.cdp.registration.User;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.UuidGenerator;
import com.philips.platform.datasync.userprofile.UserRegistrationInterface;


import java.sql.SQLException;

import cdp.philips.com.database.DatabaseHelper;
import cdp.philips.com.database.ORMSavingInterfaceImpl;
import cdp.philips.com.database.ORMUpdatingInterfaceImpl;
import cdp.philips.com.database.OrmCreator;
import cdp.philips.com.database.OrmDeleting;
import cdp.philips.com.database.OrmDeletingInterfaceImpl;
import cdp.philips.com.database.OrmFetchingInterfaceImpl;
import cdp.philips.com.database.OrmSaving;
import cdp.philips.com.database.OrmUpdating;
import cdp.philips.com.database.table.BaseAppDateTime;
import cdp.philips.com.database.table.OrmCharacteristics;
import cdp.philips.com.database.table.OrmConsentDetail;
import cdp.philips.com.database.table.OrmDCSync;
import cdp.philips.com.database.table.OrmInsight;
import cdp.philips.com.database.table.OrmInsightMetaData;
import cdp.philips.com.database.table.OrmMeasurement;
import cdp.philips.com.database.table.OrmMeasurementDetail;
import cdp.philips.com.database.table.OrmMeasurementGroup;
import cdp.philips.com.database.table.OrmMeasurementGroupDetail;
import cdp.philips.com.database.table.OrmMoment;
import cdp.philips.com.database.table.OrmMomentDetail;
import cdp.philips.com.database.table.OrmSettings;
import cdp.philips.com.database.table.OrmSynchronisationData;
import cdp.philips.com.error.ErrorHandlerInterfaceImpl;
import cdp.philips.com.registration.UserRegistrationInterfaceImpl;

public class DemoAppManager {

    public DatabaseHelper databaseHelper;
    public AppInfraInterface mAppInfra;
    public LoggingInterface loggingInterface;
    private Context mContext;
    private AppConfigurationInterface.AppConfigurationError configError;
    DataServicesManager mDataServicesManager;
    UserRegistrationInterfaceImpl userRegImple;
    final String AI = "appinfra";
    private static DemoAppManager sDemoAppManager;
    private DemoAppManager(){

    }

    public static synchronized DemoAppManager getInstance() {
        if (sDemoAppManager == null) {
            return sDemoAppManager = new DemoAppManager();
        }
        return sDemoAppManager;
    }
    public AppInfraInterface getAppInfra() {
        return mAppInfra;
    }

    public LoggingInterface getLoggingInterface() {
        return loggingInterface;
    }

    public Context getAppContext() {
        return mContext;
    }

    private void initAppInfra(AppInfraInterface gAppInfra) {
        loggingInterface = gAppInfra.getLogging().createInstanceForComponent("DataSync", "DataSync");
    }

    public UserRegistrationInterfaceImpl getUserRegImple() {
        return userRegImple;
    }

    private void init() {
        mDataServicesManager = DataServicesManager.getInstance();
        OrmCreator creator = new OrmCreator(new UuidGenerator());
        userRegImple = new UserRegistrationInterfaceImpl(mContext, new User(mContext));
        UserRegistrationInterface userRegistrationInterface = userRegImple;
        ErrorHandlerInterfaceImpl errorHandlerInterface = new ErrorHandlerInterfaceImpl();
        mDataServicesManager.initializeDataServices(mContext, creator, userRegistrationInterface, errorHandlerInterface);
        injectDBInterfacesToCore();
        mDataServicesManager.initializeSyncMonitors(mContext, null, null);
    }

    void injectDBInterfacesToCore() {
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
                    synchronisationDataDao, consentDetailsDao, measurementGroup, measurementGroupDetails,
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

            mDataServicesManager.initializeDatabaseMonitor(mContext, ORMDeletingInterfaceImpl, dbInterfaceOrmFetchingInterface, ORMSavingInterfaceImpl, dbInterfaceOrmUpdatingInterface);
        } catch (SQLException exception) {
            Toast.makeText(mContext, "db injection failed to dataservices", Toast.LENGTH_SHORT).show();
        }
    }

    public DatabaseHelper getDatabaseHelper() {
        return databaseHelper;
    }

    public void initPreRequisite(Context context, AppInfraInterface appInfra) {
        this.mContext=context;
        this.mAppInfra =appInfra;
        configError = new
                AppConfigurationInterface.AppConfigurationError();
        databaseHelper=DatabaseHelper.getInstance(context,new UuidGenerator());
        initAppInfra(mAppInfra);
        init();
    }

}