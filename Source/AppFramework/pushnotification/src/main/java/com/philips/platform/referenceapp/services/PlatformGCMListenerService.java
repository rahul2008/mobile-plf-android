/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.referenceapp.services;

import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;
import com.philips.platform.referenceapp.PushNotificationManager;
import com.philips.platform.referenceapp.utils.PNLog;

public class PlatformGCMListenerService extends GcmListenerService {

    private static final String TAG = "PushNotification";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        PNLog.d(TAG,"Push Notification received");
        PushNotificationManager.getInstance().sendPayloadToCoCo(data);
    }
}
