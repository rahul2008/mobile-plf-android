package com.philips.platform.dprdemo;

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
import com.philips.platform.dprdemo.database.DatabaseHelper;
import com.philips.platform.dprdemo.database.ORMSavingInterfaceImpl;
import com.philips.platform.dprdemo.database.ORMUpdatingInterfaceImpl;
import com.philips.platform.dprdemo.database.OrmCreator;
import com.philips.platform.dprdemo.database.OrmDeleting;
import com.philips.platform.dprdemo.database.OrmDeletingInterfaceImpl;
import com.philips.platform.dprdemo.database.OrmFetchingInterfaceImpl;
import com.philips.platform.dprdemo.database.OrmSaving;
import com.philips.platform.dprdemo.database.OrmUpdating;
import com.philips.platform.dprdemo.database.table.BaseAppDateTime;
import com.philips.platform.dprdemo.database.table.OrmConsentDetail;
import com.philips.platform.dprdemo.database.table.OrmDCSync;
import com.philips.platform.dprdemo.error.ErrorHandlerInterfaceImpl;
import com.philips.platform.dprdemo.registration.UserRegistrationInterfaceImpl;

import java.sql.SQLException;


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

    private DemoAppManager() {

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


            Dao<OrmConsentDetail, Integer> consentDetailsDao = databaseHelper.getConsentDetailsDao();
            Dao<OrmDCSync, Integer> dcSyncDao = databaseHelper.getDCSyncDao();
            OrmSaving saving = new OrmSaving(consentDetailsDao, dcSyncDao);


            OrmUpdating updating = new OrmUpdating(consentDetailsDao, dcSyncDao);
            OrmFetchingInterfaceImpl fetching = new OrmFetchingInterfaceImpl(consentDetailsDao, dcSyncDao);
            OrmDeleting deleting = new OrmDeleting(consentDetailsDao, dcSyncDao);


            BaseAppDateTime uGrowDateTime = new BaseAppDateTime();
            ORMSavingInterfaceImpl ORMSavingInterfaceImpl = new ORMSavingInterfaceImpl(saving, updating, fetching, deleting, uGrowDateTime);
            OrmDeletingInterfaceImpl ORMDeletingInterfaceImpl = new OrmDeletingInterfaceImpl(deleting, saving, fetching);
            ORMUpdatingInterfaceImpl dbInterfaceOrmUpdatingInterface = new ORMUpdatingInterfaceImpl(saving, updating, fetching, deleting);
            OrmFetchingInterfaceImpl dbInterfaceOrmFetchingInterface = new OrmFetchingInterfaceImpl(consentDetailsDao, dcSyncDao);

            mDataServicesManager.initializeDatabaseMonitor(mContext, ORMDeletingInterfaceImpl, dbInterfaceOrmFetchingInterface, ORMSavingInterfaceImpl, dbInterfaceOrmUpdatingInterface);
        } catch (SQLException exception) {
            Toast.makeText(mContext, "db injection failed to dataservices", Toast.LENGTH_SHORT).show();
        }
    }

    public DatabaseHelper getDatabaseHelper() {
        return databaseHelper;
    }

    public void initPreRequisite(Context context, AppInfraInterface appInfra) {
        this.mContext = context;
        this.mAppInfra = appInfra;
        configError = new
                AppConfigurationInterface.AppConfigurationError();
        databaseHelper = DatabaseHelper.getInstance(context, new UuidGenerator());
        initAppInfra(mAppInfra);
        init();
    }

}