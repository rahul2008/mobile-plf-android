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
import java.util.concurrent.CountDownLatch;

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
    private SharedPreferences mSharedPreferences;


    public ABTestClientManager(AppInfra appInfra) {
        mAppInfra = appInfra;
        mContext = appInfra.getAppInfraContext();
        isAppRestarted = true;
        Config.setContext(mContext.getApplicationContext());
        Config.setDebugLogging(true);
        mCacheModel = new CacheModel();
        loadfromDisk();
        mSharedPreferences = mAppInfra.getAppInfraContext().getSharedPreferences(ABTEST_PRREFERENCE,
                Context.MODE_PRIVATE);

    }

    private void loadfromDisk() {
        ArrayList<String> testList = new ArrayList<>();
        if (getCachefromPreference() != null) {
            mCacheModel = getCachefromPreference();
            if(mCacheModel.getTestValues() != null
                    && mCacheModel.getTestValues().size() > 0){
                mCacheStatusValue = mCacheModel.getTestValues();
            }
        }

        //if there are mbox name present in app config and not in cache
        //add the mbox name so that value will be filled during refresh
        try {
            testList = getTestNameFromConfig();
        } catch (Exception e) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "ABTESTCLIENT",
                    e.toString());
        }
        if (testList != null && testList.size() > 0) {
            for (String test : testList) {
                if (mCacheStatusValue != null && mCacheStatusValue.containsKey(test)) {
                    // shouldRefresh = false;
                } else {
                    CacheModel.ValueModel valueModel = new CacheModel.ValueModel();
                    valueModel.setTestValue(null);
                    valueModel.setUpdateType(UPDATETYPES.EVERY_APP_START.name());
                    valueModel.setAppVersion(getAppVersion());
                    mCacheStatusValue.put(test, valueModel);
                    mCacheModel.setTestValues(mCacheStatusValue);

                    //shouldRefresh = true;
                }
            }
        }

        if (testList != null && testList.size() == 0) {
            mCachestatusvalues = CACHESTATUSVALUES.NO_TESTS_DEFINED;
        } else {
            mCachestatusvalues = CACHESTATUSVALUES.EXPERIENCES_NOT_UPDATED;
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
                        ArrayList<String> mBoxList = new ArrayList<>();
                        ArrayList list = (ArrayList) mbox;
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i) instanceof String) {
                                mBoxList.add((String) list.get(i));
                            } else {
                                throw new IllegalArgumentException("Test Names for AB testing should be array of strings" +
                                        " in AppConfig.json file");
                            }
                        }
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
        mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "ABTESTCLIENT",
                "testName" + testName);
        testValue = getTestValueFromMemoryCache(testName);

        if (testValue == null) {
            if (getCachefromPreference() != null && updateType.name().equals
                    (UPDATETYPES.ONLY_AT_APP_UPDATE.name())) {
                HashMap<String, CacheModel.ValueModel> model = getCachefromPreference().getTestValues();

                if (model != null && model.get(testName) != null && model.get(testName).getTestValue() != null) {
                    testValue = model.get(testName).getTestValue();
                } else {
                    testValue = defaultValue;
                }
            } else {
                testValue = defaultValue;
            }
        }


        updateMemorycacheForTestName(testName, testValue, updateType);
        if (updateType.name().equals
                (UPDATETYPES.ONLY_AT_APP_UPDATE.name())) {
            saveCachetoPreference(mCacheModel);
        }
        mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "ABTESTCLIENT",
                "testValue" + testValue);

        return testValue;
    }

    private void updateMemorycacheForTestName(String testName, String content, UPDATETYPES updateType) {

        if (mCachestatusvalues != null && mCacheStatusValue.containsKey(testName)) {
            CacheModel.ValueModel val = mCacheStatusValue.get(testName);
            if (val.getTestValue() != null && updateType.name().equalsIgnoreCase(UPDATETYPES.EVERY_APP_START.name())) {
                //value is already there in cache ignoring the new value
            } else {
                CacheModel.ValueModel updatedVal = new CacheModel.ValueModel();
                updatedVal.setTestValue(content);
                updatedVal.setUpdateType(updateType.name());
                mCacheStatusValue.put(testName, updatedVal);
                mCacheModel.setTestValues(mCacheStatusValue);
            }
        }
        //remove from disk if it is already saved as appupdate variable
        if (updateType.equals(UPDATETYPES.EVERY_APP_START)) {
            removeCacheforTestName(testName);
        }
    }


    private void removeCacheforTestName(String testName) {
        CacheModel model = getCachefromPreference();
        if (model != null) {
            HashMap<String, CacheModel.ValueModel> cModel = model.getTestValues();
            if (cModel != null && cModel.containsKey(testName)) {
                cModel.remove(testName);
            }
        }
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
            mCachestatusvalues = CACHESTATUSVALUES.NO_TESTS_DEFINED;
        } else if (mCacheStatusValue.containsKey(requestName)) {
            CacheModel.ValueModel value = mCacheStatusValue.get(requestName);
            exp = value.getTestValue();
            String valueType = value.getUpdateType();
           // mCacheModel.setTestValues(mCacheStatusValue);
//            if (valueType.equalsIgnoreCase("ONLY_AT_APP_UPDATE")) {
//                saveCachetoPreference(mCacheModel);
//            }
        }
        return exp;
    }


    /**
     * Method to return the valueType based on appstart/update.
     *
     * @return update type int value
     */
    private int getVariableType() {
        if (isAppUpdated()) {
            return UPDATETYPES.ONLY_AT_APP_UPDATE.getValue();
        } else if (isAppRestarted) {
            return UPDATETYPES.EVERY_APP_START.getValue();
        } else {
            return 0;
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
            previousVersion = getAppVerionfromPref();
            if (previousVersion.isEmpty()) {
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


    /**
     * Method to update the cache based on the valueType.
     *
     * @param listener listener
     */
    @Override
    public void updateCache(final OnRefreshListener listener) {
        if (!isOnline()) {
            mCachestatusvalues = CACHESTATUSVALUES.EXPERIENCES_NOT_UPDATED;
            if (listener != null)
                listener.onError(OnRefreshListener.ERRORVALUES.NO_NETWORK, "NO INTERNET");
        } else if (mCachestatusvalues != null &&
                mCachestatusvalues.equals(CACHESTATUSVALUES.EXPERIENCES_PARTIALLY_UPDATED)) {
            if (listener != null)
                listener.onError(OnRefreshListener.ERRORVALUES.EXPERIENCES_PARTIALLY_DOWNLOADED,
                        "Partially Updated");
        } else {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    refreshForVariableType(getVariableType());
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if (listener != null) {
                                listener.onSuccess();
                            }
                        }
                    });
                }
            }).start();
        }
    }


    private void refreshForVariableType(int variableType) {
        String defaultValue = null;
        mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "ABTESTCLIENT",
                "Refreshing cache upto" + variableType);
        mCachestatusvalues = CACHESTATUSVALUES.EXPERIENCES_PARTIALLY_UPDATED;
        HashMap<String, CacheModel.ValueModel> val = mCacheStatusValue;
        if (val.size() > 0) {
            for (String key : val.keySet()) {
                CacheModel.ValueModel valModel = val.get(key);
                UPDATETYPES updateType = UPDATETYPES.valueOf(valModel.getUpdateType());
                mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "ABTESTCLIENT",
                        "update TYPE" + updateType.getValue());
                mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "ABTESTCLIENT",
                        "varialbe TYPE" + variableType);

                if (updateType.getValue() <= variableType) {
                    boolean isalreadyRead = false;

                    if (valModel.getTestValue() != null && !valModel.getTestValue().isEmpty()) {
                        defaultValue = valModel.getTestValue();
                        if (valModel.getAppVersion() != null && valModel.getAppVersion().equalsIgnoreCase
                                (getAppVersion())) {
                            isalreadyRead = true;
                        }
                    }

                    //no need to refresh if value is already read from cache

                    if (!isalreadyRead) {
                        getTestValueFromServer(key, defaultValue, updateType, null);
                    }

                } else if (valModel.getAppVersion() != null && !valModel.getAppVersion().equalsIgnoreCase
                        (getAppVersion())) {
                    if (valModel.getTestValue() != null && !valModel.getTestValue().isEmpty()) {
                        defaultValue = valModel.getTestValue();
                    }
                    getTestValueFromServer(key, defaultValue, updateType, null);
                }
            }
            mCachestatusvalues = CACHESTATUSVALUES.EXPERIENCES_UPDATED;
        } else {
            mCachestatusvalues = CACHESTATUSVALUES.EXPERIENCES_NOT_UPDATED;
        }

        previousVersion = getAppVersion();
        if (variableType == 2) {
            saveAppVeriontoPref(previousVersion);
        } else if (variableType >= 1) {
            isAppRestarted = false;
        }

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

        final CountDownLatch done = new CountDownLatch(1);
        Target.loadRequest(locationRequest, new Target.TargetCallback<String>() {
            @Override
            public void call(final String content) {
                if (content != null) {
                    mExperience = content;
                    updateMemorycacheForTestName(requestName, content, updatetypes);
                    mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "ABTESTCLIENT",
                            content);
                }
                done.countDown();
            }
        });
        try {
            // done.await(10, TimeUnit.SECONDS);
            done.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mExperience;
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
     * method to save cachemodel object in preference.
     *
     * @param model cachemodel object
     */
    private void saveCachetoPreference(CacheModel model) {
        final SharedPreferences.Editor editor = mSharedPreferences.edit();
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
        try {
            String json = mSharedPreferences.getString("cacheobject", "");
            Gson gson = new Gson();
            return gson.fromJson(json, CacheModel.class);
        } catch (Exception e) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "ABTESTCLIENT",
                    e.getMessage());
        }
        return null;
    }

    private void saveAppVeriontoPref(String mAppVerion) {
        final SharedPreferences.Editor editor = mSharedPreferences.edit();
        mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "ABTESTCLIENT",
                mAppVerion);
        editor.putString("APPVERSION", mAppVerion);
        editor.commit();
    }

    private String getAppVerionfromPref() {
        return mSharedPreferences.getString("APPVERSION", "");
    }

}
