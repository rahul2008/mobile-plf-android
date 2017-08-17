/* Copyright (c) Koninklijke Philips N.V., 2017
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

import com.philips.cdp.registration.User;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseState;
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


/**
 * @author ...
 *         This class has UI extended from UIKIT about screen , It shows the current version of the app
 */
public class DemoDataServicesState extends BaseState
        implements HandleNotificationPayloadInterface, PushNotificationTokenRegistrationInterface, PushNotificationUserRegistationWrapperInterface {

    public static final String TAG = DemoDataServicesState.class.getSimpleName();
    private Context mContext;
    private DSDemoAppuAppInterface dsDemoAppuAppInterface;

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
        int DEFAULT_THEME = R.style.Theme_Philips_DarkBlue_WhiteBackground;
        dsDemoAppuAppInterface.launch(new ActivityLauncher
                (ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_PORTRAIT, DEFAULT_THEME), null);
    }

    @Override
    public void init(Context context) {
        mContext = context;
        DSDemoAppuAppSettings dsDemoAppuAppSettings = new DSDemoAppuAppSettings(context.getApplicationContext());
        dsDemoAppuAppInterface = new DSDemoAppuAppInterface();
        dsDemoAppuAppInterface.init(new DSDemoAppuAppDependencies(((AppFrameworkApplication) context.getApplicationContext()).getAppInfra()), dsDemoAppuAppSettings);

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
        Intent intent = new Intent(mContext, LaunchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Bitmap icon = BitmapFactory.decodeResource(mContext.getResources(),
                R.mipmap.app_icon);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.mipmap.app_icon)
                .setLargeIcon(icon)
                .setContentTitle("Reference App ")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
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


