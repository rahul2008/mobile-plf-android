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

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.referenceapp.interfaces.HandleNotificationPayloadInterface;
import com.philips.platform.referenceapp.interfaces.PushNotificationTokenRegistrationInterface;
import com.philips.platform.referenceapp.interfaces.RegistrationCallbacks;
import com.philips.platform.referenceapp.services.RegistrationIntentService;
import com.philips.platform.referenceapp.utils.PNLog;
import com.philips.platform.referenceapp.utils.PushNotificationConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Set;

/**
 * @author Abhishek Gadewar
 *
 * Class to manage push notification related operations
 */
public class PushNotificationManager {

    public static final String TAG = "PushNotification";
    private static PushNotificationManager pushNotificationManager;
    private HandleNotificationPayloadInterface payloadListener;
    private PushNotificationTokenRegistrationInterface tokenRegistrationListener;
    private PushNotificationUserRegistationWrapperInterface pushNotificationUserRegistationWrapperInterface;
    private AppInfraInterface appInfra;
    private RegistrationCallbacks.RegisterCallbackListener registerCallbackListener = null;

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


    public void init(AppInfraInterface appInfra,PushNotificationUserRegistationWrapperInterface pushNotificationUserRegistationWrapperInterface){
        PNLog.initialise(appInfra);
        PNLog.enablePNLogging();
        this.appInfra = appInfra;
        this.pushNotificationUserRegistationWrapperInterface=pushNotificationUserRegistationWrapperInterface;
    }

    public PushNotificationUserRegistationWrapperInterface getPushNotificationUserRegistationWrapperInterface () {
        return pushNotificationUserRegistationWrapperInterface;
    }

    /**
     * Register common component for payload
     * @param handleNotificationPayloadInterface
     */
    public void registerForPayload(HandleNotificationPayloadInterface handleNotificationPayloadInterface){
        payloadListener=handleNotificationPayloadInterface;
        PNLog.d(TAG,"Registering component for handling payload");
    }

    /**
     * Dergister common component for payload
     */
    public void deRegisterForPayload(){
        payloadListener=null;
        PNLog.d(TAG,"Removing component from handling payload");
    }
    /**
     * Register common component for payload
     * @param pushNotificationTokenRegistrationInterface
     */
    public void registerForTokenRegistration(PushNotificationTokenRegistrationInterface
                                                     pushNotificationTokenRegistrationInterface){
        tokenRegistrationListener=pushNotificationTokenRegistrationInterface;
        PNLog.d(TAG,"Registering component for token registration with backend");
    }

    /**
     * TESTING API. Dont use for production.
     * Register common component for payload
     * @param pushNotificationTokenRegistrationInterface
     */
    public void registerForTokenRegistration(PushNotificationTokenRegistrationInterface
                                                     pushNotificationTokenRegistrationInterface,
                                             RegistrationCallbacks.RegisterCallbackListener registerCallbackListener){
        tokenRegistrationListener=pushNotificationTokenRegistrationInterface;
        this.registerCallbackListener = registerCallbackListener;
        PNLog.d(TAG,"Registering component for token registration with backend");
    }

    /**
     * Dergister common component for payload
     */
    public void deregisterForTokenRegistration(){
        tokenRegistrationListener=null;
        PNLog.d(TAG,"Deregistering component for token registration with backend");
    }

    /**
     * Check status of GCM token and registration with data core
     * @param context
     */
    public void startPushNotificationRegistration(Context context){
        if(TextUtils.isEmpty(getToken(context))){
            PNLog.d(TAG,"Token is empty. Starting GCM registration....");
            startGCMRegistrationService(context);
        }else if(pushNotificationUserRegistationWrapperInterface.isUserSignedIn(context)){
            PNLog.d(TAG,"User is signed in");
            if(!isTokenRegistered(context)){
                PNLog.d(TAG,"Token is not registered. Registering with datacore");
                registerTokenWithBackend(context);
            }else{
                //No need to do any registration stuff.
                PNLog.d(TAG,"User is signed in and token is registered");
            }
        }else{
            PNLog.d(TAG,"user is not signed in.");
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
    void registerTokenWithBackend(final Context applicationContext) {
        PNLog.d(TAG, "registerTokenWithBackend");
        if (TextUtils.isEmpty(getToken(applicationContext))) {
            PNLog.d(TAG, "Token is empty. Trying to register device with GCM server.....");
            startGCMRegistrationService(applicationContext);
        } else if(tokenRegistrationListener!=null){
            PNLog.d(TAG, "Registering token with backend");
            tokenRegistrationListener.registerToken(getToken(applicationContext), PushNotificationConstants.APP_VARIANT, PushNotificationConstants.PUSH_GCMA, new RegistrationCallbacks.RegisterCallbackListener() {
                @Override
                public void onResponse(boolean isRegistered) {
                    PNLog.d(TAG, "registerTokenWithBackend reponse isregistered:" + isRegistered);
                    saveTokenRegistrationState(applicationContext, isRegistered);
                    if(registerCallbackListener != null) {
                        registerCallbackListener.onResponse(isRegistered);
                    }
                }

                @Override
                public void onError(int errorCode, String errorMessage) {
                    PNLog.d(TAG, "Register token error: code::" + errorCode + "message::" +errorMessage);
                    if(registerCallbackListener != null) {
                        registerCallbackListener.onError(errorCode, errorMessage);
                    }
                }
            });
        }else{
            PNLog.d(TAG,"No component registered for registering token with backend");
        }
    }

    /**
     * Deregistration of token with datacore or backend
     */
    public void deregisterTokenWithBackend(final Context applicationContext, final DeregisterTokenListener deregisterTokenListener) {
        PNLog.d(TAG, "deregistering token with data core");

        if (TextUtils.isEmpty(getToken(applicationContext))) {
            PNLog.d(TAG, "Something went wrong. Token should not be empty");
        }
        else if(!isNetworkAvailable()) {
            deregisterTokenListener.onError();
        }
        else if(tokenRegistrationListener!=null){
            tokenRegistrationListener.deregisterToken(getToken(applicationContext), PushNotificationConstants.APP_VARIANT, new RegistrationCallbacks.DergisterCallbackListener() {
                @Override
                public void onResponse(boolean isDeRegistered) {
                    PNLog.d(TAG, "deregisterTokenWithBackend isDergistered:" + isDeRegistered);
                    saveTokenRegistrationState(applicationContext, !isDeRegistered);
                    if (isDeRegistered) {
                        deregisterTokenListener.onSuccess();
                    } else {
                        deregisterTokenListener.onError();
                    }
                }

                @Override
                public void onError(int errorCode, String errorMessage) {
                    deregisterTokenListener.onError();
                    PNLog.d(TAG, "Register token error: code::" + errorCode+ "message::" + errorMessage);
                }
            });
        }else{
            PNLog.d(TAG,"No component registered for deregistering component with backend");
        }

    }

    /**
     * This method saves token in preferences
     *
     * @param token
     * @param applicationContext
     */
    public void saveToken(String token, Context applicationContext) {
        PNLog.d(TAG, "Saving token in preferences");
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
        PNLog.d(TAG, "Saving token registration state in preferences");
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
        PNLog.d(TAG, "GCM token:" + sharedPreferences.getString(PushNotificationConstants.GCM_TOKEN, ""));
        return sharedPreferences.getString(PushNotificationConstants.GCM_TOKEN, "");
    }

    /**
     * Register app instance with GCM to get token
     *
     * @param context
     */
    public void startGCMRegistrationService(Context context) {
        PNLog.d(TAG, "Starting GCM registration. Getting token from server");
        //Remove registration state
        saveTokenRegistrationState(context, false);
        //Start registration service
        Intent intent = new Intent(context, RegistrationIntentService.class);
        context.startService(intent);
    }

    public boolean isNetworkAvailable() {
        return appInfra.getRestClient().isInternetReachable();
    }

    /**
     * Send message to desired component
     *
     * @param data
     */
    public void sendPayloadToCoCo(Bundle data) {

        if (payloadListener != null) {

            for (String key : data.keySet()) {
                PNLog.d(TAG, key + " is a key in the bundle" + " value " + data.get(key));
            }
            Set<String> set = data.keySet();
            if (set.contains(PushNotificationConstants.PLATFORM_KEY) ) {
                try {
                    JSONObject jsonObject = new JSONObject(data.getString(PushNotificationConstants.PLATFORM_KEY));
                    Iterator<String> iterator = jsonObject.keys();
                    while (iterator.hasNext()) {
                        String key = iterator.next();
                        switch (key) {
                            case PushNotificationConstants.DSC:
                                JSONObject dscobject = jsonObject.getJSONObject(key);
                                payloadListener.handlePayload(dscobject);
                                PNLog.d(TAG, " THIS is a SILENT notification");
                                //  payloadListener.handlePushNotification(data.getString("message"),data.getString("Description is this "));
                                break;
                            default:
                                PNLog.d(TAG, "Common component is not designed for handling this key");
                        }
                    }
                } catch (JSONException e) {
                    PNLog.d(TAG, "Exception while parsing payload:" + e.getMessage());
                }
            } else {
                PNLog.d(TAG, " THIS is a NOT A silent  notification");

                payloadListener.handlePushNotification(data.getString("alert"));

            }
                /*else
                {
                PNLog.d(TAG, "Data sync key is absent in payload");
            }
        }else{
            PNLog.d(TAG, "No component registered for receiving push notification");
        }*/
        }
    }}

