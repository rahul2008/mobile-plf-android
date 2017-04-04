package com.philips.platform.pushnotification;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.philips.cdp.registration.User;
import com.philips.platform.core.listeners.RegisterDeviceTokenListener;
import com.philips.platform.core.trackers.DataServicesManager;

/**
 * Created by philips on 29/03/17.
 */

public class PushNotificationManager {

    private static final String TAG = PushNotificationManager.class.getSimpleName();
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
    public boolean isTokenRegistered(Context applicationContext) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        boolean isTokenRegistered = sharedPreferences.getBoolean(PushNotificationConstants.IS_TOKEN_REGISTERED, false);
        return isTokenRegistered;
    }

    /**
     * Registration of token with datacore or backend
     */
    public void registerTokenWithBackend(final Context applicationContext) {
        Log.d(TAG,"registerTokenWithBackend");
        if (TextUtils.isEmpty(getToken(applicationContext))) {
            Log.d(TAG,"Token is empty. Trying to regiter device with GCM server.....");
            startGCMRegistrationService(applicationContext);
        } else {
            Log.d(TAG,"Registering token with backend");
            DataServicesManager.getInstance().registerDeviceToken(getToken(applicationContext), PushNotificationConstants.APP_VARIANT,PushNotificationConstants.PUSH_GCMA, new RegisterDeviceTokenListener() {
                @Override
                public void onResponse(boolean isRegistered) {
                    Log.d(TAG,"registerTokenWithBackend reponse isregistered:"+isRegistered);
                    saveTokenRegistrationState(applicationContext,isRegistered);
                }
            });
        }
    }


    /**
     * Registration of token with datacore or backend
     */
    public void deregisterTokenWithBackend(final Context applicationContext) {
        Log.d(TAG,"deregistering token with data core");
        if(TextUtils.isEmpty(getToken(applicationContext))){
            Log.d(TAG,"Something went wrong. Token should not be empty");
        }else{
            DataServicesManager.getInstance().unRegisterDeviceToken(getToken(applicationContext), PushNotificationConstants.APP_VARIANT, new RegisterDeviceTokenListener() {
                @Override
                public void onResponse(boolean isDeRegistered) {
                    Log.d(TAG,"deregisterTokenWithBackend isDergistered:"+isDeRegistered);
                    saveTokenRegistrationState(applicationContext,!isDeRegistered);

                }
            });
        }

    }

    /**
     * This method saves token in preferences
     *
     * @param token
     * @param applicationContext
     */
    public void saveToken(String token, Context applicationContext) {
        Log.d(TAG,"Saving token in preferences");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PushNotificationConstants.GCM_TOKEN, token);
        editor.commit();
        if (new User(applicationContext).isUserSignIn()) {
            Log.d(TAG,"User is already signed. Register token with data core");
            registerTokenWithBackend(applicationContext);
        }
    }

    /**
     * Save the state of registration with data core
     * @param applicationContext
     * @param state
     */
    public void saveTokenRegistrationState(Context applicationContext,boolean state){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PushNotificationConstants.IS_TOKEN_REGISTERED, state);
        editor.commit();
    }

    /**
     * Returns saved token
     * @param applicationContext
     * @return
     */
    public String getToken(Context applicationContext) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        return sharedPreferences.getString(PushNotificationConstants.GCM_TOKEN, "");
    }

    /**
     * Register app instance with GCM to get token
     * @param context
     */
    public void startGCMRegistrationService(Context context) {
        Log.d(TAG,"Starting GCM registration. Getting token from server");
        //Remove registration state
        saveTokenRegistrationState(context,false);

        //Start registration service
        Intent intent = new Intent(context, RegistrationIntentService.class);
        context.startService(intent);
    }
}
