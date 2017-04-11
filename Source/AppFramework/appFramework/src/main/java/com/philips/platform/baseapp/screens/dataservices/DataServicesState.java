/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.dataservices;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.philips.cdp.registration.User;
import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.base.AppFrameworkBaseActivity;
import com.philips.platform.baseapp.screens.dataservices.database.DatabaseHelper;
import com.philips.platform.baseapp.screens.dataservices.database.ORMSavingInterfaceImpl;
import com.philips.platform.baseapp.screens.dataservices.database.ORMUpdatingInterfaceImpl;
import com.philips.platform.baseapp.screens.dataservices.database.OrmCreator;
import com.philips.platform.baseapp.screens.dataservices.database.OrmDeleting;
import com.philips.platform.baseapp.screens.dataservices.database.OrmDeletingInterfaceImpl;
import com.philips.platform.baseapp.screens.dataservices.database.OrmFetchingInterfaceImpl;
import com.philips.platform.baseapp.screens.dataservices.database.OrmSaving;
import com.philips.platform.baseapp.screens.dataservices.database.OrmUpdating;
import com.philips.platform.baseapp.screens.dataservices.database.table.BaseAppDateTime;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmCharacteristics;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmConsentDetail;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmDCSync;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmInsight;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmInsightMetaData;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmMeasurement;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmMeasurementDetail;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmMeasurementGroup;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmMeasurementGroupDetail;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmMoment;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmMomentDetail;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmSettings;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmSynchronisationData;
import com.philips.platform.baseapp.screens.dataservices.error.ErrorHandlerInterfaceImpl;
import com.philips.platform.baseapp.screens.dataservices.reciever.ScheduleSyncReceiver;
import com.philips.platform.baseapp.screens.dataservices.registration.UserRegistrationInterfaceImpl;
import com.philips.platform.baseapp.screens.dataservices.temperature.TemperatureTimeLineFragment;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DSLog;
import com.philips.platform.datasync.userprofile.UserRegistrationInterface;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

import java.sql.SQLException;

import static com.philips.platform.baseapp.screens.utility.Constants.SERVER_SYNC_ERROR;


/**
 * This class has UI extended from UIKIT about screen , It shows the current version of the app
 */
public class DataServicesState extends BaseState {
    public static final String TAG = DataServicesState.class.getSimpleName();
    FragmentLauncher fragmentLauncher;
    ScheduleSyncReceiver mScheduleSyncReceiver;

    private DatabaseHelper databaseHelper;

    public DataServicesState() {
        super(AppStates.DATA_SYNC);
    }

    /**
     * Navigating to AboutScreenFragment
     *
     * @param uiLauncher requires UiLauncher
     */
    @Override
    public void navigate(UiLauncher uiLauncher) {
        fragmentLauncher = (FragmentLauncher) uiLauncher;
        ((AppFrameworkBaseActivity) fragmentLauncher.getFragmentActivity()).
                handleFragmentBackStack(new TemperatureTimeLineFragment(), TemperatureTimeLineFragment.TAG, getUiStateData().getFragmentLaunchState());
    }

    @Override
    public void init(Context context) {
        mScheduleSyncReceiver = new ScheduleSyncReceiver();
        //OrmCreator creator = new OrmCreator(new UuidGenerator());

        OrmCreator creator = new OrmCreator();
        UserRegistrationInterface userRegistrationInterface = new UserRegistrationInterfaceImpl(context, new User(context));
        ErrorHandlerInterfaceImpl errorHandlerInterface = new ErrorHandlerInterfaceImpl();
        DataServicesManager.getInstance().initializeDataServices(context, creator, userRegistrationInterface, errorHandlerInterface);
        injectDBInterfacesToCore(context);
        DataServicesManager.getInstance().initializeSyncMonitors(context, null, null);
        DSLog.enableLogging(true);
        DSLog.i(DSLog.LOG, "Before Setting up Synchronization Loop");
        scheduleSync(context);

        //Stetho.initializeWithDefaults(context);
    }

    void scheduleSync(final Context context) {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                try {
                    mScheduleSyncReceiver.onReceive(context);
                } catch (Exception e) {
                    AppFrameworkApplication.loggingInterface.log(LoggingInterface.LogLevel.DEBUG, SERVER_SYNC_ERROR,
                            e.getMessage());                } finally {
                    //also call the same runnable to call it at regular interval
                    handler.postDelayed(this, ScheduleSyncReceiver.DATA_FETCH_FREQUENCY);
                }
            }
        };
        runnable.run();
    }

    void injectDBInterfacesToCore(Context context) {
        databaseHelper = DatabaseHelper.getInstance(context);
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
            //  Dao<OrmCharacteristics, Integer> characteristicsDetailsDao = databaseHelper.getCharacteristicsDetailsDao();

            Dao<OrmSettings, Integer> settingsDao = databaseHelper.getSettingsDao();
            Dao<OrmInsight, Integer> insightsDao = databaseHelper.getInsightDao();
            Dao<OrmInsightMetaData, Integer> insightMetaDataDao = databaseHelper.getInsightMetaDataDao();


            OrmSaving saving = new OrmSaving(momentDao, momentDetailDao, measurementDao, measurementDetailDao,
                    synchronisationDataDao,consentDetailsDao, measurementGroup, measurementGroupDetails,
                    characteristicsesDao, settingsDao, insightsDao, insightMetaDataDao);

            Dao<OrmDCSync, Integer> dcSyncDao = databaseHelper.getDCSyncDao();
            OrmUpdating updating = new OrmUpdating(momentDao, momentDetailDao, measurementDao, measurementDetailDao, settingsDao,
                     consentDetailsDao, dcSyncDao, measurementGroup, synchronisationDataDao, measurementGroupDetails);
            OrmFetchingInterfaceImpl fetching = new OrmFetchingInterfaceImpl(momentDao, synchronisationDataDao,consentDetailsDao, characteristicsesDao,
                    settingsDao, dcSyncDao, insightsDao);
            OrmDeleting deleting = new OrmDeleting(momentDao, momentDetailDao, measurementDao,
                    measurementDetailDao, synchronisationDataDao, measurementGroupDetails, measurementGroup, consentDetailsDao, characteristicsesDao, settingsDao, dcSyncDao ,insightsDao, insightMetaDataDao);



            BaseAppDateTime uGrowDateTime = new BaseAppDateTime();
            ORMSavingInterfaceImpl ORMSavingInterfaceImpl = new ORMSavingInterfaceImpl(saving, updating, fetching, deleting, uGrowDateTime);
            OrmDeletingInterfaceImpl ORMDeletingInterfaceImpl = new OrmDeletingInterfaceImpl(deleting, saving, fetching);
            ORMUpdatingInterfaceImpl dbInterfaceOrmUpdatingInterface = new ORMUpdatingInterfaceImpl(saving, updating, fetching, deleting);
            OrmFetchingInterfaceImpl dbInterfaceOrmFetchingInterface = new OrmFetchingInterfaceImpl(momentDao, synchronisationDataDao,consentDetailsDao,
                    characteristicsesDao, settingsDao, dcSyncDao, insightsDao);

            DataServicesManager.getInstance().initializeDatabaseMonitor(context, ORMDeletingInterfaceImpl, dbInterfaceOrmFetchingInterface, ORMSavingInterfaceImpl, dbInterfaceOrmUpdatingInterface);
        } catch (SQLException exception) {
            Toast.makeText(context, "db injection failed to dataservices", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void updateDataModel() {

    }
}

