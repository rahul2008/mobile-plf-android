package com.philips.platform.referenceapp.interfaces;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by philips on 20/04/17.
 */

public interface HandleNotificationPayloadInterface {
    void handlePayload(JSONObject payloadObject) throws JSONException;

    void handlePushNotification(String message);
}
