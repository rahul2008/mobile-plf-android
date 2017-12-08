/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.abtestclient;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;

import com.adobe.mobile.Config;
import com.adobe.mobile.Target;
import com.adobe.mobile.TargetLocationRequest;
import com.google.gson.Gson;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraLogEventID;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * The ABTest Client Manager
 */

public class ABTestClientManager implements ABTestClientInterface {

    private static final String ABTEST_PRREFERENCE = "philips.appinfra.abtest.precache";
    private AppInfra mAppInfra;
    private String mExperience = null;
    private HashMap<String, CacheModel.ValueModel> mCacheStatusValue = new HashMap<>();
    protected CACHESTATUSVALUES mCachestatusvalues;
    private boolean isAppRestarted = false;
    private String previousVersion;
    private SharedPreferences mSharedPreferences;
    private CacheModel mCacheModel;
    private String cacheToPreference;
    private SharedPreferences.Editor editor;

    public ABTestClientManager(AppInfra appInfra) {
        mAppInfra = appInfra;
        Context mContext = appInfra.getAppInfraContext();
        isAppRestarted = true;
        init(mContext);
    }


	private void init(final Context mContext) {
		Config.setContext(mContext.getApplicationContext());
		mCacheModel = new CacheModel();
		loadfromDisk();
		mSharedPreferences = mAppInfra.getAppInfraContext().getSharedPreferences(ABTEST_PRREFERENCE,
				Context.MODE_PRIVATE);
		editor = mSharedPreferences.edit();
	}

    protected void loadfromDisk() {
        ArrayList<String> testList = new ArrayList<>();
        CacheModel cacheModel = getCachefromPreference();
        if (cacheModel != null) {
            mCacheModel = cacheModel;
            if (mCacheModel.getTestValues() != null && mCacheModel.getTestValues().size() > 0) {
                mCacheStatusValue = mCacheModel.getTestValues();
            }
        }

        //if there are mbox name present in app config and not in cache
        //add the mbox name so that value will be filled during refresh
        try {
            testList = getTestNameFromConfig();
        } catch (Exception e) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_ABTEST_CLIENT,
                   "Error in Load from Disk "+e.getMessage());
        }
        if (testList != null && testList.size() > 0) {
            for (String test : testList) {
                if (mCacheStatusValue != null && mCacheStatusValue.containsKey(test)) {
                    // shouldRefresh = false;
                } else {
                    cacheModel(null, UPDATETYPES.EVERY_APP_START.name(), test);
                    //shouldRefresh = true;
                }
            }
            mCachestatusvalues = CACHESTATUSVALUES.EXPERIENCES_NOT_UPDATED;
           /* mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, AppInfraLogEventID.AI_ABTEST_CLIENT,
                    "Cache Status values EXPERIENCES_NOT_UPDATED");*/
        }  else {
            mCachestatusvalues = CACHESTATUSVALUES.NO_TESTS_DEFINED;
            /*mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, AppInfraLogEventID.AI_ABTEST_CLIENT,
                    "Cache Status values NO_TESTS_DEFINED");*/
        }
    }

	/**
	 * Method to fetch the testNames from the config.
	 *
	 * @return Arraylist list of testNames.
	 */
	private ArrayList<String> getTestNameFromConfig() {
		Object mbox = getAbtestConfig(mAppInfra.getConfigInterface(), mAppInfra);
		if (mbox != null) {
			if (mbox instanceof ArrayList<?>) {
				final ArrayList<String> mBoxList = new ArrayList<>();
				final ArrayList<?> list = (ArrayList<?>) mbox;
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i) instanceof String) {
						mBoxList.add((String) list.get(i));
					} else {
						throw new IllegalArgumentException("Test Names for AB testing should be array of strings" +
								" in AppConfig.json file");
					}
				}
			/*	mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, AppInfraLogEventID.AI_ABTEST_CLIENT,
						"fetch the testNames from the config");*/
				return mBoxList;
			} else {
				throw new IllegalArgumentException("Test Names for AB testing should be array of strings" +
						" in AppConfig.json file");
			}
		} else {
			mCachestatusvalues = CACHESTATUSVALUES.NO_TESTS_DEFINED;
		}
		return null;
	}

    /**
     * Method to return the cachestatus.
     *
     * @return cacheStauts
     */
    public CACHESTATUSVALUES getCacheStatus() {
        mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_ABTEST_CLIENT,
                "CacheStatus "+mCachestatusvalues);
        return mCachestatusvalues;
    }

    /**
     * Method to fetch testValue from memory cache/ persistent cache / server.
     *
     * @param requestNameKey     name of the test for which the value is to be provided
     * @param defaultValue value to use if no cached value is available
     * @param updateType   ValueType.
     * @param parameters   Parameters
     * @return String  testValue.
     */
    @Override
    public String getTestValue(final String requestNameKey, final String defaultValue,
                               final UPDATETYPES updateType, Map<String, Object> parameters) {

        /*mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, AppInfraLogEventID.AI_ABTEST_CLIENT,
                "testName " + requestNameKey);*/
        String requestName=mappedRequestName(requestNameKey);
        String testValue = getTestValueFromMemoryCache(requestName);
        if (testValue == null) {
            if (getCachefromPreference() != null && updateType.name().equals
                    (UPDATETYPES.ONLY_AT_APP_UPDATE.name())) {
                final HashMap<String, CacheModel.ValueModel> model = getCachefromPreference().getTestValues();
                if (model != null && model.get(requestName) != null && model.get(requestName).getTestValue() != null) {
                    testValue = model.get(requestName).getTestValue();
                } else {
                    testValue = defaultValue;
                }
            } else {
                testValue = defaultValue;
            }
        }


        updateMemorycacheForTestName(requestName, testValue, updateType);
        if (updateType.name().equals
                (UPDATETYPES.ONLY_AT_APP_UPDATE.name())) {
            saveCachetoPreference(mCacheModel);
        }
       /* mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, AppInfraLogEventID.AI_ABTEST_CLIENT,
                "testValue " + testValue);*/

        return testValue;
    }

    private void updateMemorycacheForTestName(String testName, String content, UPDATETYPES updateType) {

        if (mCachestatusvalues != null && mCacheStatusValue.containsKey(testName)) {
            final CacheModel.ValueModel val = mCacheStatusValue.get(testName);
            if (val.getTestValue() != null && updateType.name().equalsIgnoreCase(UPDATETYPES.EVERY_APP_START.name())) {
                //value is already there in cache ignoring the new value
            } else {
                cacheModel(content, updateType.name(), testName);

            }
        }
        //remove from disk if it is already saved as appupdate variable
        if (updateType.equals(UPDATETYPES.EVERY_APP_START)) {
            removeCacheforTestName(testName);
        }
       /* mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_ABTEST_CLIENT,
                "update memory cache for TestName");*/
    }


    private void cacheModel(String testValue, String updateType, String cacheStatusKey) {
        final CacheModel.ValueModel valueModel = new CacheModel.ValueModel();
        valueModel.setTestValue(testValue);
        valueModel.setUpdateType(updateType);
        valueModel.setAppVersion(getAppVersion());
        mCacheStatusValue.put(cacheStatusKey, valueModel);
        mCacheModel.setTestValues(mCacheStatusValue);
    }


    private void removeCacheforTestName(String testName) {
        final CacheModel model = getCachefromPreference();
        if (model != null) {
            final HashMap<String, CacheModel.ValueModel> cModel = model.getTestValues();
            if (cModel != null && cModel.containsKey(testName)) {
                cModel.remove(testName);
            }
        }
       /* mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_ABTEST_CLIENT,
                "remove cache for TestName");*/
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
            final CacheModel.ValueModel value = mCacheStatusValue.get(requestName);
            exp = value.getTestValue();
           // final String valueType = value.getUpdateType();
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
            final String appVersion = getAppVersion();
            previousVersion = getAppVerionfromPref();
            if(previousVersion != null){
                return previousVersion.isEmpty() || !previousVersion.equalsIgnoreCase(appVersion);
            }
        } catch (IllegalArgumentException exception) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_ABTEST_CLIENT,
                   "IllegalArgumentException in isAppUpdated "+exception.getMessage());
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
        if (null != mAppInfra.getRestClient() && !mAppInfra.getRestClient().isInternetReachable()) {
            /*mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, AppInfraLogEventID.AI_ABTEST_CLIENT,
                    "update Cache");*/
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
        loadfromDisk();
        mCachestatusvalues = CACHESTATUSVALUES.EXPERIENCES_PARTIALLY_UPDATED;
        final HashMap<String, CacheModel.ValueModel> val = mCacheStatusValue;
        if (val.size() > 0) {
            for (String key : val.keySet()) {
                final CacheModel.ValueModel valModel = val.get(key);
                final UPDATETYPES updateType = UPDATETYPES.valueOf(valModel.getUpdateType());
                mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_ABTEST_CLIENT,
                        "update TYPE" + updateType.getValue());
                mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_ABTEST_CLIENT,
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
        final TargetLocationRequest locationRequest = Target.createRequest(requestName,
                defaultContent, parameters);

        final CountDownLatch done = new CountDownLatch(1);
        Target.loadRequest(locationRequest, new Target.TargetCallback<String>() {
            @Override
            public void call(final String content) {
                if (content != null) {
                    mExperience = content;
                    updateMemorycacheForTestName(requestName, content, updatetypes);
                    mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_ABTEST_CLIENT,
                            "get Test Value From Server location request"+ content);
                }
                done.countDown();
            }
        });
        try {
            // done.await(10, TimeUnit.SECONDS);
            done.await();
        } catch (InterruptedException exception) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_ABTEST_CLIENT,
                    "Interrupted while test Value from server"+exception.getMessage());
        }
        return mExperience;
    }


    private String getAppVersion() {
        try {
            if (mAppInfra.getAppIdentity() != null) {
                return mAppInfra.getAppIdentity().getAppVersion();
            }
        } catch (IllegalArgumentException exception) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_ABTEST_CLIENT,
                    "Error in getAppVersion "+exception.getMessage());
        }
        return null;
    }


    /**
     * method to save cachemodel object in preference.
     *
     * @param model cachemodel object
     */
    private void saveCachetoPreference(final CacheModel model) {
        final Gson gson = new Gson();
        new Thread(new Runnable() {
            @Override
            public void run() {
                cacheToPreference= gson.toJson(model);
                /*mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_ABTEST_CLIENT,
                        "save Cache to Preference "+cacheToPreference);*/
                editor.putString("cacheobject", cacheToPreference);
                editor.commit();
            }
        }).start();

    }

    /**
     * method to fetch from the shared preference.
     *
     * @return cachemodel object
     */
    private CacheModel getCachefromPreference() {
        try {
            if(mSharedPreferences != null) {
                final String json = mSharedPreferences.getString("cacheobject", "");
                final Gson gson = new Gson();
                return gson.fromJson(json, CacheModel.class);
            }
        } catch (Exception e) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_ABTEST_CLIENT,
                    "Error in getCachefromPreference "+e.getMessage());
        }
        return null;
    }

    private void saveAppVeriontoPref(String mAppVerion) {
        /*mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, AppInfraLogEventID.AI_ABTEST_CLIENT,
                "save AppVerion to Pref"+mAppVerion);*/
        editor.putString("APPVERSION", mAppVerion);
        editor.commit();
    }

    private String getAppVerionfromPref() {
        if(mSharedPreferences != null) {
            final String getAppVerionfromPref=mSharedPreferences.getString("APPVERSION", "");
            /*mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, AppInfraLogEventID.AI_ABTEST_CLIENT,
                    "get AppVerion from Pref"+getAppVerionfromPref);*/
            return getAppVerionfromPref;
        }
        return null;
    }

    public static Object getAbtestConfig(AppConfigurationInterface appConfigurationManager, AppInfra ai) {
        try {
            final AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface
                    .AppConfigurationError();
            final Object mbox = appConfigurationManager.getPropertyForKey
                    ("abtest.precache", "appinfra", configError);
            return mbox;

        } catch (IllegalArgumentException exception) {
            ai.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,
                    AppInfraLogEventID.AI_APPINFRA,"Error in reading Abtesting  Config "+exception.getMessage());
        }
        return null;
    }

    public String mappedRequestName(String requestNameKey){
         String requestName=requestNameKey;
         HashMap<String,Object> mappConfig= getAbtestMapConfig(mAppInfra.getConfigInterface(), mAppInfra);
         if(mappConfig!=null && mappConfig instanceof HashMap<?,?>){
             String mappedRequestName=(String) mappConfig.get(requestNameKey);
             if(mappedRequestName!=null && !mappedRequestName.isEmpty()){
                 requestName=mappedRequestName;
             }

         }

        return requestName;
    }

    HashMap<String,Object> getAbtestMapConfig(AppConfigurationInterface appConfigurationManager, AppInfra ai) {
        try {

            final AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface
                    .AppConfigurationError();

            return (HashMap<String,Object>)appConfigurationManager.getPropertyForKey
                    ("abtest.mapping", "appinfra", configError);

        } catch (IllegalArgumentException exception) {
            ai.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,
                    AppInfraLogEventID.AI_APPINFRA,"Error in reading Abtesting Map Config "+exception.getMessage());
        }
        return null;
    }

}
