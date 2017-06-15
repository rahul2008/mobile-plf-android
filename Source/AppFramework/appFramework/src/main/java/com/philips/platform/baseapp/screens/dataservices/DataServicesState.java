/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.dataservices;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.philips.cdp.registration.User;
import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.baseapp.base.AbstractAppFrameworkBaseActivity;
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
import com.philips.platform.baseapp.screens.dataservices.temperature.TemperatureTimeLineFragmentAbstract;
import com.philips.platform.baseapp.screens.dataservices.utility.SyncScheduler;
import com.philips.platform.baseapp.screens.introscreen.LaunchActivity;
import com.philips.platform.baseapp.screens.utility.BaseAppUtil;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.core.listeners.RegisterDeviceTokenListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DSLog;
import com.philips.platform.core.utils.DataServicesError;
import com.philips.platform.datasync.userprofile.UserRegistrationInterface;
import com.philips.platform.referenceapp.PushNotificationManager;
import com.philips.platform.referenceapp.PushNotificationUserRegistationWrapperInterface;
import com.philips.platform.referenceapp.interfaces.HandleNotificationPayloadInterface;
import com.philips.platform.referenceapp.interfaces.PushNotificationTokenRegistrationInterface;
import com.philips.platform.referenceapp.interfaces.RegistrationCallbacks;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.Random;


/**
 * @author ...
 * This class has UI extended from UIKIT about screen , It shows the current version of the app
 */
public class DataServicesState extends BaseState implements HandleNotificationPayloadInterface, PushNotificationTokenRegistrationInterface, PushNotificationUserRegistationWrapperInterface {
    public static final String TAG = DataServicesState.class.getSimpleName();
    FragmentLauncher fragmentLauncher;
    ScheduleSyncReceiver mScheduleSyncReceiver;
    Context mcontext;

    private DatabaseHelper databaseHelper;

    public DataServicesState() {
        super(AppStates.DATA_SYNC);
    }

    /**
     * Navigating to AboutScreenFragmentAbstract
     *
     * @param uiLauncher requires UiLauncher
     */
    @Override
    public void navigate(UiLauncher uiLauncher) {
        fragmentLauncher = (FragmentLauncher) uiLauncher;
        ((AbstractAppFrameworkBaseActivity) fragmentLauncher.getFragmentActivity()).
                handleFragmentBackStack(new TemperatureTimeLineFragmentAbstract(), TemperatureTimeLineFragmentAbstract.TAG, getUiStateData().getFragmentLaunchState());
    }

    @Override
    public void init(Context context) {
        mcontext=context;
        mScheduleSyncReceiver = new ScheduleSyncReceiver();
        //OrmCreator creator = new OrmCreator(new UuidGenerator());

        OrmCreator creator = new OrmCreator();
        UserRegistrationInterface userRegistrationInterface = new UserRegistrationInterfaceImpl(context, new User(context));
        ErrorHandlerInterfaceImpl errorHandlerInterface = new ErrorHandlerInterfaceImpl();
        DataServicesManager.getInstance().initializeDataServices(context, creator, userRegistrationInterface, errorHandlerInterface);
        injectDBInterfacesToCore(context);
        DataServicesManager.getInstance().initializeSyncMonitors(context, null, null);
        DSLog.enableLogging(true);
        if (BaseAppUtil.isDSPollingEnabled(context)) {
            DSLog.i(DSLog.LOG, "Before Setting up Synchronization Loop");
            User user = new User(context);
            if (user != null && user.isUserSignIn()) {
                SyncScheduler.getInstance().scheduleSync();
            }
        } else {
            if (new User(context).isUserSignIn()) {
                registerDSForRegisteringToken();
                registerForReceivingPayload();
            }else{
                deregisterDSForRegisteringToken();
                deregisterForReceivingPayload();
            }
        }
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

            DataServicesManager.getInstance().initializeDatabaseMonitor(context, ORMDeletingInterfaceImpl, dbInterfaceOrmFetchingInterface, ORMSavingInterfaceImpl, dbInterfaceOrmUpdatingInterface);
        } catch (SQLException exception) {
            Toast.makeText(context, "db injection failed to dataservices", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void updateDataModel() {

    }

    public void registerForReceivingPayload() {
        PushNotificationManager.getInstance().registerForPayload(this);
    }

    public void deregisterForReceivingPayload() {
        PushNotificationManager.getInstance().deRegisterForPayload();
    }

    public void registerDSForRegisteringToken() {
        PushNotificationManager.getInstance().registerForTokenRegistration(this);
    }

    public void deregisterDSForRegisteringToken() {
        PushNotificationManager.getInstance().deregisterForTokenRegistration();
    }

    @Override
    public void handlePayload(JSONObject payloadObject) throws JSONException {
        DataServicesManager.getInstance().handlePushNotificationPayload(payloadObject);
    }

    @Override
    public void handlePushNotification(String message) {
        sendNotification(message);
    }

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message) {
        Intent intent = new Intent(mcontext, LaunchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(mcontext, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Bitmap icon = BitmapFactory.decodeResource(mcontext.getResources(),
                R.mipmap.app_icon);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(mcontext)
                .setSmallIcon(R.mipmap.app_icon)
                .setLargeIcon(icon)
                .setContentTitle("Reference App ")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) mcontext.getSystemService(Context.NOTIFICATION_SERVICE);
        Random r = new Random();
        int i1 = r.nextInt(80 - 65) + 65;
        notificationManager.notify(i1 /* ID of notification */, notificationBuilder.build());
        RALog.d(TAG, "creating notification number  "+i1);
    }

    @Override
    public void registerToken(String deviceToken, String appVariant, String protocolProvider, final RegistrationCallbacks.RegisterCallbackListener registerCallbackListener) {
        DataServicesManager.getInstance().registerDeviceToken(deviceToken, appVariant, protocolProvider, new RegisterDeviceTokenListener() {
            @Override
            public void onResponse(boolean b) {
                registerCallbackListener.onResponse(b);
            }

            @Override
            public void onError(DataServicesError dataServicesError) {
                registerCallbackListener.onError(dataServicesError.getErrorCode(), dataServicesError.getErrorMessage());
            }
        });
    }

    @Override
    public void deregisterToken(String appToken, String appVariant, final RegistrationCallbacks.DergisterCallbackListener dergisterCallbackListener) {
        DataServicesManager.getInstance().unRegisterDeviceToken(appToken, appVariant, new RegisterDeviceTokenListener() {
            @Override
            public void onResponse(boolean isDregistered) {
                dergisterCallbackListener.onResponse(isDregistered);
            }

            @Override
            public void onError(DataServicesError dataServicesError) {
                dergisterCallbackListener.onError(dataServicesError.getErrorCode(), dataServicesError.getErrorMessage());
            }
        });
    }

    @Override
    public boolean isUserSignedIn(Context appContext) {
        return new User(appContext).isUserSignIn();
    }


}


