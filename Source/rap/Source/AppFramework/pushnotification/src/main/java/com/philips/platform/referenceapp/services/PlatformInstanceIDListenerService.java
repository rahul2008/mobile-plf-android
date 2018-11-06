/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.referenceapp.services;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.philips.platform.referenceapp.PushNotificationManager;
import com.philips.platform.referenceapp.utils.PNLog;

/**
 * Created by philips on 08/03/17.
 */

public class PlatformInstanceIDListenerService extends FirebaseMessagingService {
    private static final String TAG = "PushNotification";

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        PNLog.d(TAG, token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        PNLog.d(TAG,"Push Notification received");
        PushNotificationManager.getInstance().sendPayloadToCoCo(remoteMessage.getData());
    }
}
