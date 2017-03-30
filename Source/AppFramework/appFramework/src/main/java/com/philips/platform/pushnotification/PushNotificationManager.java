package com.philips.platform.pushnotification;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.philips.cdp.registration.User;
import com.philips.platform.core.listeners.RegisterDeviceTokenListener;
import com.philips.platform.core.trackers.DataServicesManager;

/**
 * Created by philips on 29/03/17.
 */

public class PushNotificationManager {

    private static PushNotificationManager pushNotificationManager;

    private PushNotificationManager() {
    }

    public static PushNotificationManager getInstance() {
        if (pushNotificationManager == null) {
            pushNotificationManager = new PushNotificationManager();
        }
        return pushNotificationManager;
    }

    /**
     * This method will return true if token is registered with backend/app server
     *
     * @param applicationContext
     * @return
     */
    public boolean isTokenRegisteredWithBackend(Context applicationContext) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        boolean isTokenRegistered = sharedPreferences.getBoolean(PushNotificationConstants.SENT_TOKEN_TO_SERVER, false);
        return isTokenRegistered;
    }

    /**
     * Registration of token with datacore or backend
     */
    public void registerTokenWithBackend(final Context applicationContext) {
        if (TextUtils.isEmpty(getToken(applicationContext))) {
            startGCMRegistrationService(applicationContext);
        } else {
            DataServicesManager.getInstance().registerDeviceToken(getToken(applicationContext), PushNotificationConstants.APP_VARIANT, new RegisterDeviceTokenListener() {
                @Override
                public void onResponse(boolean isRegistered) {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(PushNotificationConstants.SENT_TOKEN_TO_SERVER, isRegistered);
                    editor.commit();
                }
            });
        }
    }


    /**
     * Registration of token with datacore or backend
     */
    public void deregisterTokenWithBackend(final Context applicationContext) {
        DataServicesManager.getInstance().unRegisterDeviceToken(getToken(applicationContext), PushNotificationConstants.APP_VARIANT, new RegisterDeviceTokenListener() {
            @Override
            public void onResponse(boolean isDeRegistered) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(PushNotificationConstants.SENT_TOKEN_TO_SERVER, !isDeRegistered);
                editor.commit();
            }
        });
    }

    /**
     * This method saves token in preferences
     *
     * @param token
     * @param applicationContext
     */
    public void saveToken(String token, Context applicationContext) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PushNotificationConstants.GCM_TOKEN, token);
        editor.commit();
        if (new User(applicationContext).isUserSignIn()) {
            registerTokenWithBackend(applicationContext);
        }
    }

    public String getToken(Context applicationContext) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        return sharedPreferences.getString(PushNotificationConstants.GCM_TOKEN, "");
    }

    public void startGCMRegistrationService(Context context) {
        Intent intent = new Intent(context, RegistrationIntentService.class);
        context.startService(intent);
    }
}
