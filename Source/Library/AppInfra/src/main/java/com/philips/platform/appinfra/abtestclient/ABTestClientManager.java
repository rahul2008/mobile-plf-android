/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.abtestclient;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.adobe.mobile.Config;
import com.adobe.mobile.Target;
import com.adobe.mobile.TargetLocationRequest;
import com.google.gson.Gson;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by 310243577 on 10/4/2016.
 */

public class ABTestClientManager implements ABTestClientInterface {

    private AppInfra mAppInfra;
    private Context mContext;
    private String mExperience = null;
    private HashMap<String, CacheModel.ValueModel> mCacheStatusValue = new HashMap<>();
    private CACHESTATUSVALUES mCachestatusvalues;
    private CacheModel mCacheModel;
    private static final String ABTEST_PRREFERENCE = "philips.appinfra.abtest.precache";
    private boolean isAppRestarted = false;
    private String previousVersion;
    //  CacheModel.ValueModel valueModel;


    public ABTestClientManager(AppInfra appInfra) {
        mAppInfra = appInfra;
        mContext = appInfra.getAppInfraContext();
        isAppRestarted = true;
        Config.setContext(mContext.getApplicationContext());
        Config.setDebugLogging(true);
        mCacheModel = new CacheModel();
        loadfromDisk();
    }

    private void loadfromDisk() {
        boolean shouldRefresh = false;

        if (getCachefromPreference() != null && getCachefromPreference().getTestValues() != null
                && getCachefromPreference().getTestValues().size() > 0) {
            mCacheModel = getCachefromPreference();
            mCacheStatusValue = mCacheModel.getTestValues();
        }

        //if there are mbox name present in app config and not in cache
        //add the mbox name so that value will be filled during refresh
        ArrayList<String> testList = getTestNameFromConfig();
        if (testList != null) {
            for (String test : testList) {
                if (mCacheStatusValue != null && mCacheStatusValue.containsKey(test)) {
                    shouldRefresh = false;
                } else {
                    CacheModel.ValueModel valueModel = new CacheModel.ValueModel();
                    valueModel.setTestValue(null);
                    valueModel.setUpdateType(UPDATETYPES.EVERY_APP_START.name());
                    mCacheStatusValue.put(test, valueModel);
                    shouldRefresh = true;
                }
            }
        } else {
            mCachestatusvalues = CACHESTATUSVALUES.NO_TESTS_DEFINED;
        }

        if (shouldRefresh) {
            mCachestatusvalues = CACHESTATUSVALUES.EXPERIENCES_NOT_UPDATED;
        }
    }


    /**
     * Method to refresh the testValue based on the valueType.
     *
     * @param variableType valuType/UpdateType.
     * @param listener
     */
    private void refreshForVariableType(UPDATETYPES variableType, OnRefreshListener listener) {
        String defaultValue = null;
        mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "ABTESTCLIENT",
                "Refreshing cache upto" + variableType);
        mCachestatusvalues = CACHESTATUSVALUES.EXPERIENCES_PARTIALLY_UPDATED;

        HashMap<String, CacheModel.ValueModel> val = mCacheStatusValue;
        for (String key : val.keySet()) {
            CacheModel.ValueModel valModel = val.get(key);
            UPDATETYPES updateType = UPDATETYPES.valueOf(valModel.getUpdateType());
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "ABTESTCLIENT",
                    "update TYPE" + updateType.ordinal());
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "ABTESTCLIENT",
                    "varialbe TYPE" + variableType.ordinal());
            if (valModel.getTestValue() != null) {
                defaultValue = valModel.getTestValue();
            }

            if (updateType.equals(variableType)) {
                getTestValueFromServer(key, defaultValue, updateType, null);
            } else {
                getTestValueFromServer(key, defaultValue, updateType, null);
            }
        }
        mCachestatusvalues = CACHESTATUSVALUES.EXPERIENCES_UPDATED;
        previousVersion = getAppVersion();
        mCacheModel.setAppVersion(previousVersion);
        if (variableType.equals(UPDATETYPES.ONLY_AT_APP_UPDATE)) {
            saveCachetoPreference(mCacheModel);
        } else if (variableType.equals(UPDATETYPES.EVERY_APP_START)) {
            isAppRestarted = false;
        }
        if (listener != null) {
            listener.onSuccess();
        }
    }

    /**
     * Method to fetch the testNames from the config.
     *
     * @return Arraylist list of testNames.
     */
    private ArrayList<String> getTestNameFromConfig() {
        AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface
                .AppConfigurationError();
        if (mAppInfra.getConfigInterface() != null) {
            try {
                Object mbox = mAppInfra.getConfigInterface().getPropertyForKey
                        ("abtest.precache", "appinfra", configError);
                if (mbox != null) {
                    if (mbox instanceof ArrayList) {
                        ArrayList<String> mBoxList = (ArrayList<String>) mbox;
                        return mBoxList;
                    } else {
                        throw new IllegalArgumentException("Test Names for AB testing should be array of strings" +
                                " in AppConfig.json file");
                    }
                } else {
                    mCachestatusvalues = CACHESTATUSVALUES.NO_TESTS_DEFINED;
                }
            } catch (IllegalArgumentException exception) {
                mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "ABTESTCLIENT",
                        exception.toString());
            }

        }
        return null;
    }

    /**
     * Method to return the cachestatus.
     *
     * @return cacheStauts
     */
    public CACHESTATUSVALUES getCacheStatus() {
        return mCachestatusvalues;
    }

    /**
     * Method to fetch testValue from memory cache/ persistent cache / server.
     *
     * @param testName     name of the test for which the value is to be provided
     * @param defaultValue value to use if no cached value is available
     * @param updateType   ValueType.
     * @param parameters   Parameters
     * @return String  testValue.
     */
    @Override
    public String getTestValue(final String testName, final String defaultValue,
                               final UPDATETYPES updateType, Map<String, Object> parameters) {
        String testValue;

        testValue = getTestValueFromMemoryCache(testName);
        if (testValue != null) {
            return testValue;    // memory cache
        }
        return defaultValue;
    }


    /**
     * MEthod to fetch the testValue from the server.
     *
     * @param requestName    testName
     * @param defaultContent defaultcontent
     * @param updatetypes    valueTypes
     * @param parameters     parameters
     * @return String testValue.
     */
    private String getTestValueFromServer(final String requestName, String defaultContent,
                                          final UPDATETYPES updatetypes, Map<String, Object> parameters) {
        Target.clearCookies();
        TargetLocationRequest locationRequest = Target.createRequest(requestName,
                defaultContent, parameters);

        Target.loadRequest(locationRequest, new Target.TargetCallback<String>() {
            @Override
            public void call(final String content) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (content != null) {
                            mExperience = content;
                            Log.e("ABTESTING", content);
                            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "ABTESTCLIENT",
                                    content);
                            CacheModel.ValueModel valueModel = new CacheModel.ValueModel();
                            valueModel.setTestValue(content);
                            valueModel.setUpdateType(updatetypes.name());
                            mCacheStatusValue.put(requestName, valueModel);
                            mCacheModel.setTestValues(mCacheStatusValue);
                            //  saveCachetoPreference(mCacheModel);
                        }
                    }
                });
            }
        });
        return mExperience;
    }

    /**
     * Method to testvalue from memory cache if present.
     *
     * @param requestName testName
     * @return String TestValue.
     */
    private String getTestValueFromMemoryCache(String requestName) {
        String exp = null;
        if (mCacheStatusValue.size() == 0) {
            mCachestatusvalues = CACHESTATUSVALUES.EXPERIENCES_NOT_UPDATED;
        } else if (mCacheStatusValue.containsKey(requestName)) {
            CacheModel.ValueModel value = mCacheStatusValue.get(requestName);
            exp = value.getTestValue();
            String valueType = value.getUpdateType();
            mCacheModel.setTestValues(mCacheStatusValue);
            if (valueType.equalsIgnoreCase("ONLY_AT_APP_UPDATE")) {
                saveCachetoPreference(mCacheModel);
            }
        }
        return exp;
    }


    /**
     * Method to update the cache based on the valueType.
     *
     * @param listener listener
     */
    @Override
    public void updateCache(OnRefreshListener listener) {
        if (!isOnline()) {
            if (listener != null)
                listener.onError(OnRefreshListener.ERRORVALUES.NO_NETWORK, "NO INTERNET");
        } else if (mCachestatusvalues != null &&
                mCachestatusvalues.equals(CACHESTATUSVALUES.EXPERIENCES_PARTIALLY_UPDATED)) {
            if (listener != null)
                listener.onError(OnRefreshListener.ERRORVALUES.EXPERIENCES_PARTIALLY_DOWNLOADED,
                        "Partially Updated");
        } else {
            refreshForVariableType(getVariableType(), listener);
            //            if (listener != null)
//                listener.onSuccess();
        }
    }

    /**
     * Method to check the network connectivity.
     *
     * @return boolean true/false.
     */
    private boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                mAppInfra.getAppInfraContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Method to return the valueType based on appstart/update.
     *
     * @return update type int value
     */
    private UPDATETYPES getVariableType() {
        if (isAppUpdated()) {
            return UPDATETYPES.ONLY_AT_APP_UPDATE;
        } else if (isAppRestarted) {
            return UPDATETYPES.EVERY_APP_START;
        } else {
            return UPDATETYPES.EVERY_APP_START;
        }
    }

    /**
     * method to check if app is updated or not.
     *
     * @return boolean true/false.
     */
    private boolean isAppUpdated() {
        try {
            if (mCacheModel == null) {
                return true;
            }
            String appVersion = getAppVersion();
            String previousVersion = mCacheModel.getAppVersion();
            if (previousVersion == null) {
                //mCacheModel.setAppVersion(appVersion); // first launch
                // saveCachetoPreference(mCacheModel);
                return true;
            } else if (previousVersion.equalsIgnoreCase(appVersion)) {
                return false; // same version.
            } else {
                // other version
                return true;
            }
        } catch (IllegalArgumentException exception) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "ABTESTCLIENT",
                    exception.getMessage());
        }

        return false;
    }


    private String getAppVersion() {
        try {
            if (mAppInfra.getAppIdentity() != null) {
                return mAppInfra.getAppIdentity().getAppVersion();
            }
        } catch (IllegalArgumentException exception) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "ABTESTCLIENT",
                    exception.getMessage());
        }
        return null;
    }

    /**
     * method to save cachemodel object in preference.
     *
     * @param model cachemodel object
     */
    private void saveCachetoPreference(CacheModel model) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences
                (ABTEST_PRREFERENCE, MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(model);
        mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "ABTESTCLIENT",
                json);
        editor.putString("cacheobject", json);
        editor.commit();
    }

    /**
     * method to fetch from the shared preference.
     *
     * @return cachemodel object
     */
    private CacheModel getCachefromPreference() {
        SharedPreferences prefs = mContext.getSharedPreferences(ABTEST_PRREFERENCE, MODE_PRIVATE);
        String json = prefs.getString("cacheobject", "");
        Gson gson = new Gson();
        CacheModel obj = gson.fromJson(json, CacheModel.class);
        return obj;
    }
}
