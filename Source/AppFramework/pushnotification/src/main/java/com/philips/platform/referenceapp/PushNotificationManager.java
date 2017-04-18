/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.referenceapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.philips.cdp.registration.User;
import com.philips.platform.core.listeners.RegisterDeviceTokenListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DataServicesError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Set;

/**
 * Class to manage push notification related operations
 */
public class PushNotificationManager {

    private static final String TAG = "PushNotification";
    private static PushNotificationManager pushNotificationManager;

    public interface DeregisterTokenListener {
        void onSuccess();

        void onError();
    }

    private PushNotificationManager() {
    }

    public static PushNotificationManager getInstance() {
        if (pushNotificationManager == null) {
            pushNotificationManager = new PushNotificationManager();
        }
        return pushNotificationManager;
    }

    /**
     * Check status of GCM token and registration with data core
     * @param context
     */
    public void startPushNotificationRegistration(Context context){
        if(TextUtils.isEmpty(pushNotificationManager.getToken(context))){
            Log.d(TAG,"Token is empty. Starting GCM registration....");
            pushNotificationManager.startGCMRegistrationService(context);
        }else if(new User(context).isUserSignIn()){
            Log.d(TAG,"User is signed in");
            if(!isTokenRegistered(context)){
                Log.d(TAG,"Token is not registered. Registering with datacore");
                registerTokenWithBackend(context);
            }else{
                //No need to do any registration stuff.
                Log.d(TAG,"User is signed in and token is registered");
            }
        }else{
            Log.d(TAG,"user is not signed in.");
            saveTokenRegistrationState(context,false);
        }
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
    private void registerTokenWithBackend(final Context applicationContext) {
        Log.d(TAG, "registerTokenWithBackend");
        if (TextUtils.isEmpty(getToken(applicationContext))) {
            Log.d(TAG, "Token is empty. Trying to regiter device with GCM server.....");
            startGCMRegistrationService(applicationContext);
        } else {
            Log.d(TAG, "Registering token with backend");
            DataServicesManager.getInstance().registerDeviceToken(getToken(applicationContext), PushNotificationConstants.APP_VARIANT, PushNotificationConstants.PUSH_GCMA, new RegisterDeviceTokenListener() {
                @Override
                public void onResponse(boolean isRegistered) {
                    Log.d(TAG, "registerTokenWithBackend reponse isregistered:" + isRegistered);
                    saveTokenRegistrationState(applicationContext, isRegistered);
                }

                @Override
                public void onError(DataServicesError dataServicesError) {
                    Log.d(TAG, "Register token error: code::" + dataServicesError.getErrorCode() + "message::" + dataServicesError.getErrorMessage());
                }
            });
        }
    }


    /**
     * Registration of token with datacore or backend
     */
    public void deregisterTokenWithBackend(final Context applicationContext, final DeregisterTokenListener deregisterTokenListener) {
        Log.d(TAG, "deregistering token with data core");
        if (TextUtils.isEmpty(getToken(applicationContext))) {
            Log.d(TAG, "Something went wrong. Token should not be empty");
        } else {
            DataServicesManager.getInstance().unRegisterDeviceToken(getToken(applicationContext), PushNotificationConstants.APP_VARIANT, new RegisterDeviceTokenListener() {
                @Override
                public void onResponse(boolean isDeRegistered) {
                    Log.d(TAG, "deregisterTokenWithBackend isDergistered:" + isDeRegistered);
                    saveTokenRegistrationState(applicationContext, !isDeRegistered);
                    if (isDeRegistered) {
                        deregisterTokenListener.onSuccess();
                    } else {
                        deregisterTokenListener.onError();
                    }

                }

                @Override
                public void onError(DataServicesError dataServicesError) {
                    deregisterTokenListener.onError();
                    Log.d(TAG, "Register token error: code::" + dataServicesError.getErrorCode() + "message::" + dataServicesError.getErrorMessage());
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
        Log.d(TAG, "Saving token in preferences");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PushNotificationConstants.GCM_TOKEN, token);
        editor.commit();
    }

    /**
     * Save the state of registration with data core
     *
     * @param applicationContext
     * @param state
     */
    public void saveTokenRegistrationState(Context applicationContext, boolean state) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PushNotificationConstants.IS_TOKEN_REGISTERED, state);
        editor.commit();
    }

    /**
     * Returns saved token
     *
     * @param applicationContext
     * @return
     */
    public String getToken(Context applicationContext) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        Log.d(TAG, "GCM token:" + sharedPreferences.getString(PushNotificationConstants.GCM_TOKEN, ""));
        return sharedPreferences.getString(PushNotificationConstants.GCM_TOKEN, "");
    }

    /**
     * Register app instance with GCM to get token
     *
     * @param context
     */
    public void startGCMRegistrationService(Context context) {
        Log.d(TAG, "Starting GCM registration. Getting token from server");
        //Remove registration state
        saveTokenRegistrationState(context, false);
        //Start registration service
        Intent intent = new Intent(context, RegistrationIntentService.class);
        context.startService(intent);
    }

    /**
     * Send message to desired component
     *
     * @param data
     */
    public void sendPayloadToCoCo(Context context, Bundle data) {
        Set<String> set = data.keySet();
        if (set.contains(PushNotificationConstants.PLATFORM_KEY)) {
            try {
                JSONObject jsonObject = new JSONObject(data.getString(PushNotificationConstants.PLATFORM_KEY));
                Iterator iterator = jsonObject.keys();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    switch (key) {
                        case PushNotificationConstants.DSC:
                            JSONObject dscobject = jsonObject.getJSONObject(key);
                            DataServicesManager.getInstance().handlePushNotificationPayload(dscobject);
//                            DataServicesState dataServicesState = ((AppFrameworkApplication) context.getApplicationContext()).getDataServiceState();
//                            dataServicesState.sendPayloadMessageToDSC(dscobject);
                            break;
                        default:
                            Log.d(TAG, "Common component is not designed for handling this key");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            Log.d(TAG,"Data sync key is absent in payload");
        }
    }
}
