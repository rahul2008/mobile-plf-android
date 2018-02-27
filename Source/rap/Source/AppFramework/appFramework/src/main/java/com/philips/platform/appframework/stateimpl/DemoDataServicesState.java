/*
 * Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appframework.stateimpl;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import com.philips.cdp.registration.User;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.screens.introscreen.LaunchActivity;
import com.philips.platform.baseapp.screens.utility.BaseAppUtil;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.core.listeners.RegisterDeviceTokenListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DataServicesError;
import com.philips.platform.dscdemo.DSDemoAppuAppDependencies;
import com.philips.platform.dscdemo.DSDemoAppuAppInterface;
import com.philips.platform.dscdemo.DSDemoAppuAppSettings;
import com.philips.platform.dscdemo.utility.SyncScheduler;
import com.philips.platform.pif.chi.ConsentDefinitionRegistry;
import com.philips.platform.referenceapp.PushNotificationManager;
import com.philips.platform.referenceapp.PushNotificationUserRegistationWrapperInterface;
import com.philips.platform.referenceapp.interfaces.HandleNotificationPayloadInterface;
import com.philips.platform.referenceapp.interfaces.PushNotificationTokenRegistrationInterface;
import com.philips.platform.referenceapp.interfaces.RegistrationCallbacks;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import static com.philips.platform.mya.csw.justintime.JustInTimeConsentDependencies.textResources;

public class DemoDataServicesState extends DemoBaseState
        implements HandleNotificationPayloadInterface, PushNotificationTokenRegistrationInterface, PushNotificationUserRegistationWrapperInterface {

    public static final String TAG = DemoDataServicesState.class.getSimpleName();
    private Context mContext;
    private DSDemoAppuAppInterface dsDemoAppuAppInterface;

    private static final String PRIMARY_CHANNEL_ID = "ra_default_channel";

    private static final String PRIMARY_CHANNEL_NAME = "Primary Channel";

    public DemoDataServicesState() {
        super(AppStates.TESTDATASERVICE);
    }

    /**
     * Navigating to DemoDataService Screen
     *
     * @param uiLauncher requires UiLauncher
     */
    @Override
    public void navigate(UiLauncher uiLauncher) {
        dsDemoAppuAppInterface.launch(new ActivityLauncher(ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_PORTRAIT,
                getDLSThemeConfiguration(mContext.getApplicationContext()), 0, null), null);
    }

    @Override
    public void init(Context context) {
        mContext = context;
        DSDemoAppuAppSettings dsDemoAppuAppSettings = new DSDemoAppuAppSettings(context.getApplicationContext());
        dsDemoAppuAppInterface = getDsDemoAppuAppInterface();
        dsDemoAppuAppInterface.init(getUappDependencies(context), dsDemoAppuAppSettings);

        if (BaseAppUtil.isDSPollingEnabled(context)) {
            User user = new User(context);
            if (user.isUserSignIn()) {
                SyncScheduler.getInstance().scheduleSync();
            }
        } else {
            if (new User(context).isUserSignIn()) {
                registerDSForRegisteringToken();
                registerForReceivingPayload();
            } else {
                deregisterDSForRegisteringToken();
                deregisterForReceivingPayload();
            }
        }
    }

    @NonNull
    protected DSDemoAppuAppDependencies getUappDependencies(Context context) {
        return new DSDemoAppuAppDependencies(((AppFrameworkApplication) context.getApplicationContext()).getAppInfra(),
                ((AppFrameworkApplication) context.getApplicationContext()).getConsentRegistryInterface(),
                ConsentDefinitionRegistry.getDefinitionByConsentType("moment"), textResources);
    }

    @NonNull
    protected DSDemoAppuAppInterface getDsDemoAppuAppInterface() {
        return new DSDemoAppuAppInterface();
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
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID,
                    PRIMARY_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setLightColor(Color.GREEN);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Intent intent = new Intent(mContext, LaunchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Bitmap icon = BitmapFactory.decodeResource(mContext.getResources(),
                R.mipmap.app_icon);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext, PRIMARY_CHANNEL_ID)
                .setSmallIcon(R.mipmap.app_icon)
                .setLargeIcon(icon)
                .setContentTitle(mContext.getString(R.string.RA_DLS_home_page_text))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        Random r = new Random();
        int i1 = r.nextInt(80 - 65) + 65;
        notificationManager.notify(i1 /* ID of notification */, notificationBuilder.build());
        RALog.d(TAG, "creating notification number  " + i1);
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
