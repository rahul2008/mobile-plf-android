package com.philips.platform.appinfra.abtestclient;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by 310243577 on 10/4/2016.
 */

public class ABTestClientManager implements ABTestClientInterface {

    private AppInfra mAppInfra;
    private Context mContext;
    private String exp = null;
    private HashMap<String, ArrayList<String>> cacheMboxVal = new HashMap<>();
    private CACHESTATUSVALUES cachestatusvalues;
    private CacheModel cacheModel;
    private static final String ABTEST_PRREFERENCE = "philips.appinfra.abtest.precache";
    private boolean isAppRestarted = false;


    public ABTestClientManager(AppInfra appInfra) {
        mAppInfra = appInfra;
        mContext = appInfra.getAppInfraContext();
        isAppRestarted = true;
        Config.setContext(mContext.getApplicationContext());
        Config.setDebugLogging(true);
        cacheModel = new CacheModel();
        loadfromCache();
    }


    private void loadfromCache() {
        if (getCache() != null && getCache().getTestValues() != null && getCache().getTestValues().size() > 0) {
            cachestatusvalues = CACHESTATUSVALUES.EXPERIENCES_UPDATED;
        } else {
            cachestatusvalues = CACHESTATUSVALUES.NO_CACHED_EXPERIENCES;
        }
    }


    private void precacheExperience(OnRefreshListener listener) {
        boolean shouldRefresh = false;
        loadfromCache();
        //if there are mbox name present in app config and not in cache
        //add the mbox name so that value will be filled during refresh
        ArrayList<String> testList = getTestNameFromConfig();
        if (testList != null) {
            for (String test : testList) {
                if (cacheMboxVal != null && cacheMboxVal.containsKey(test)) {
                    shouldRefresh = false;
                } else {
                    ArrayList<String> val = new ArrayList<>();
                    val.add(null);
                    val.add(UPDATETYPES.ONLY_AT_APP_UPDATE.name());
                    cacheMboxVal.put(test, val);
                    shouldRefresh = true;
                }
            }
        } else {
            cachestatusvalues = CACHESTATUSVALUES.NO_TESTS_DEFINED;
        }

        if (shouldRefresh) {
            cachestatusvalues = CACHESTATUSVALUES.EXPERIENCES_NOT_UPDATED;
        }
        refreshForVariableType(getVariableType(), listener);
    }


    private void refreshForVariableType(int variableType, OnRefreshListener listener) {
        String defaultType = null;
        mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "ABTESTCLIENT",
                "Refreshing cache upto" + variableType);
        cachestatusvalues = CACHESTATUSVALUES.EXPERIENCES_PARTIALLY_UPDATED;

        HashMap<String, ArrayList<String>> val = cacheMboxVal;
        for (String key : val.keySet()) {
            ArrayList<String> valType = val.get(key);
            UPDATETYPES updateType = UPDATETYPES.valueOf(valType.get(1));
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "ABTESTCLIENT",
                    "update TYPE" + updateType);
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "ABTESTCLIENT",
                    "varialbe TYPE" + variableType);
            if (valType.get(0) != null) {
                defaultType = valType.get(0);
            }
            if (updateType.ordinal() <= variableType) {
                getTestValue(key, defaultType, updateType, null);
            }
        }
    }

    private ArrayList<String> getTestNameFromConfig() {
        AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface
                .AppConfigurationError();

        Object mbox = mAppInfra.getConfigInterface().getDefaultPropertyForKey
                ("abtest.precache", "appinfra", configError);
        if (mbox instanceof ArrayList) {
            ArrayList<String> mBoxList = (ArrayList<String>) mbox;
            return mBoxList;
        } else {
            throw new IllegalArgumentException("Test Names for AB testing should be array of strings" +
                    " in AppConfig.json file");
        }
    }


    public CACHESTATUSVALUES getCacheStatus() {
        return cachestatusvalues;
    }

    @Override
    public String getTestValue(final String testName, final String defaultValue,
                               final UPDATETYPES updateType, Map<String, Object> parameters) {
        String testValue;

        testValue = getTestValueFromCache(testName);
        if (testValue != null && updateType != UPDATETYPES.UPDATE_ALWAYS) {
            return testValue;    // memory cache
        } else {
            if (getCache() != null && getCache().getTestValues() != null && getCache().getTestValues().size() > 0
                    && updateType != UPDATETYPES.UPDATE_ALWAYS) {
                ArrayList<String> updatedList = new ArrayList<>();
                HashMap<String, ArrayList<String>> val = getCache().getTestValues(); // persistent cache
                if (val.containsKey(testName)) {
                    testValue = val.get(testName).get(0);
                    String valueType = val.get(testName).get(1);
                    if (!valueType.equalsIgnoreCase(updateType.name())) {
                        valueType = updateType.name();
                        updatedList.add(testValue);
                        updatedList.add(valueType);

                        val.put(testName, updatedList);
                        cacheModel.setTestValues(val);
                        saveCache(cacheModel);
                    }
                    mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "ABTESTCLIENT",
                            "from persistent cache");
                    if (testValue != null)
                        return testValue;
                }
            } else {
                mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "ABTESTCLIENT",
                        "from server");
                testValue = getTestValueFromServer(testName, defaultValue, updateType, parameters); // from server
            }
            return testValue;
        }
    }

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
                    exp = content;
                    Log.e("ABTESTING", content);
                    mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "ABTESTCLIENT",
                            content);
                    cachestatusvalues = CACHESTATUSVALUES.EXPERIENCES_UPDATED;
                    // exp.notifyAll();
                    ArrayList<String> val = new ArrayList<String>();
                    val.add(content);
                    val.add(updatetypes.name());
                    cacheMboxVal.put(requestName, val);
                    cacheModel.setTestValues(cacheMboxVal);
                    saveCache(cacheModel);
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
        return exp;
    }

    private String getTestValueFromCache(String requestName) {
        String exp = null;
        if (cacheMboxVal.size() == 0) {
            cachestatusvalues = CACHESTATUSVALUES.EXPERIENCES_NOT_UPDATED;
        } else if (cacheMboxVal.containsKey(requestName)) {
            ArrayList value = cacheMboxVal.get(requestName);
            exp = (String) value.get(0);
        }
        return exp;
    }

    @Override
    public void updateCache(OnRefreshListener listener) {
        if (listener != null) {
            if (!isOnline()) {
                listener.onError(OnRefreshListener.ERRORVALUES.NO_NETWORK, "NO INTERNET");
            } else if (cachestatusvalues.equals(CACHESTATUSVALUES.EXPERIENCES_PARTIALLY_UPDATED)) {
                listener.onError(OnRefreshListener.ERRORVALUES.EXPERIENCES_PARTIALLY_DOWNLOADED,
                        "Partially Updated");
            } else {
                precacheExperience(listener);
                listener.onSuccess();
            }
        }


    }

    private boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                mAppInfra.getAppInfraContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private int getVariableType() {
        if (isAppUpdated()) {
            return UPDATETYPES.ONLY_AT_APP_UPDATE.ordinal();
        } else if (isAppRestarted) {
            return UPDATETYPES.EVERY_APP_START.ordinal();
        } else {
            return UPDATETYPES.UPDATE_ALWAYS.ordinal();
        }
    }

    private boolean isAppUpdated() {
        try {
            if (getCache() == null) {
                return true;
            }
            String appVersion = mAppInfra.getAppIdentity().getAppVersion();
            String previousVersion = getCache().getAppVersion();
            if (previousVersion == null) {
                cacheModel.setAppVersion(appVersion); // first launch
                saveCache(cacheModel);
                return true;
            } else if (previousVersion.equalsIgnoreCase(appVersion)) {
                return false; // same version.
            } else {
                getCache().setAppVersion(appVersion); // other version
                return true;
            }
        } catch (IllegalArgumentException exception) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "ABTESTCLIENT",
                    exception.getMessage());
        }

        return false;
    }

    private void saveCache(CacheModel model) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences
                (ABTEST_PRREFERENCE, MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(model);
        mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "ABTESTCLIENT",
                json);
        editor.putString("cacheobject", json);
        editor.commit();
    }

    private CacheModel getCache() {
        SharedPreferences prefs = mContext.getSharedPreferences(ABTEST_PRREFERENCE, MODE_PRIVATE);
        String json = prefs.getString("cacheobject", "");
        Gson gson = new Gson();
        CacheModel obj = gson.fromJson(json, CacheModel.class);
        return obj;
    }
}
