/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.appinfra.appconfiguration;

import android.content.Context;
import android.util.Log;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 310238114 on 7/25/2016.
 */
public class AppConfigurationManager implements AppConfigurationInterface {

    AppInfra mAppInfra;
    Context mContext;
    private static final String mAppConfig_SecureStoreKey = "ail.app_config";

    SecureStorageInterface ssi;

    public AppConfigurationManager(AppInfra appInfra) {
        mAppInfra = appInfra;
        mContext = appInfra.getAppInfraContext();
        ssi = mAppInfra.getSecureStorage();
    }

    protected JSONObject getMasterConfigFromApp() {
        JSONObject result = null;
        try {
            InputStream mInputStream = mContext.getAssets().open("AppConfig.json");
            BufferedReader r = new BufferedReader(new InputStreamReader(mInputStream));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line).append('\n');
            }
            result = new JSONObject(total.toString());
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.VERBOSE, "Json",
                    result.toString());

        } catch (Exception e) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "AppConfiguration exception",
                    Log.getStackTraceString(e));
        }
        return result;
    }


    private JSONObject getjSONFromDevice() {
        JSONObject jObj = null;
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        String jsonString = ssi.fetchValueForKey(mAppConfig_SecureStoreKey, sse);
        if (null == jsonString || null == sse) {
            // do nothing
        } else {
            // save master json file to device memory
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, "uAPP_CONFIG", jsonString);
            try {
                jObj = new JSONObject(jsonString);
            } catch (Exception e) {
                mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "AppConfiguration exception",
                        Log.getStackTraceString(e));
            }
        }
        return jObj;
    }

    @Override
    public Object getPropertyForKey(String groupName, String key, AppConfigurationError configError) throws InvalidArgumentException {
        Object object = null;
        if (null == groupName || null == key || key.isEmpty() || key.isEmpty() || !key.matches("[a-zA-Z0-9_.-]+") || !groupName.matches("[a-zA-Z0-9_.-]+")) {
            configError.setErrorCode(AppConfigurationError.AppConfigErrorEnum.InvalidKey);
            throw new InvalidArgumentException("Invalid Argument Exception");
        } else {
            JSONObject deviceObject = getjSONFromDevice();
            if (null == deviceObject) {  // if master file is not yet saved into phone memory
                deviceObject = getMasterConfigFromApp();// reads from Application asset
                SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
                ssi.storeValueForKey(mAppConfig_SecureStoreKey, deviceObject.toString(), sse);// write json to device for further read
            }
            try {
                boolean isCocoPresent = deviceObject.has(groupName);
                if (!isCocoPresent) { // if request coco does not exist
                    configError.setErrorCode(AppConfigurationError.AppConfigErrorEnum.GroupNotExists);
                } else {
                    JSONObject cocoJSONobject = deviceObject.optJSONObject(groupName);
                    if (null == cocoJSONobject) { // invalid Coco JSON
                        configError.setErrorCode(AppConfigurationError.AppConfigErrorEnum.FatalError);
                    } else {
                        boolean isKeyPresent = cocoJSONobject.has(key);
                        if (!isKeyPresent) { // if key is not found inside coco
                            configError.setErrorCode(AppConfigurationError.AppConfigErrorEnum.KeyNotExists);
                        } else {
                            object = cocoJSONobject.opt(key); // Returns the value mapped by name, or null if no such mapping exists
                            if (null == object) {
                                configError.setErrorCode(AppConfigurationError.AppConfigErrorEnum.NoDataFoundForKey);
                            } else {
                                //  KEY FOUND SUCCESS
                                if (cocoJSONobject.opt(key) instanceof JSONArray) {
                                    JSONArray jsonArray = cocoJSONobject.optJSONArray(key);
                                    List<Object> list = new ArrayList<Object>();
                                    for (int iCount = 0; iCount < jsonArray.length(); iCount++) {
                                        list.add(jsonArray.opt(iCount));
                                    }
                                    object = list;
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "AppConfiguration exception",
                        Log.getStackTraceString(e));
            }
        }
        return object;
    }

    @Override
    public boolean setPropertyForKey(String groupName, String key, Object object, AppConfigurationError configError) throws InvalidArgumentException {
        boolean setOperation = false;
        if (null == groupName || null == key || key.isEmpty() || !key.matches("[a-zA-Z0-9_.-]+") ||
                !groupName.matches("[a-zA-Z0-9_.-]+") || object == null) {
            configError.setErrorCode(AppConfigurationError.AppConfigErrorEnum.InvalidKey);
            throw new InvalidArgumentException("Invalid Argument Exception");
        } else {
            JSONObject deviceObject = getjSONFromDevice();
            if (null == deviceObject) {  // if master file is not yet saved into phone memory
                deviceObject = getMasterConfigFromApp();// reads from Application asset
                SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
                ssi.storeValueForKey(mAppConfig_SecureStoreKey, deviceObject.toString(), sse);// write json to device for further read
            }
            try {
                boolean isCocoPresent = deviceObject.has(groupName);
                JSONObject cocoJSONobject;
                if (!isCocoPresent) { // if request coco  does not exist
                    // configError.setErrorCode(ConfigError.ConfigErrorEnum.GroupNotExists);
                    cocoJSONobject = new JSONObject();
                    deviceObject.put(groupName, cocoJSONobject);
                } else {
                    cocoJSONobject = deviceObject.optJSONObject(groupName);
                }
                if (null == cocoJSONobject) { // invalid Coco JSON
                    configError.setErrorCode(AppConfigurationError.AppConfigErrorEnum.FatalError);
                } else {
                    // boolean isKeyPresent = cocoJSONobject.has(key);
                    if (object instanceof ArrayList) {
                        JSONArray jsonArray = new JSONArray(((ArrayList) object).toArray());
                        cocoJSONobject.put(key, jsonArray);
                    } else {
                        cocoJSONobject.put(key, object);
                    }
                    SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
                    ssi.storeValueForKey(mAppConfig_SecureStoreKey, deviceObject.toString(), sse);
                    if (null == sse.getErrorCode()) {
                        setOperation = true;
                    } else {
                        configError.setErrorCode(AppConfigurationError.AppConfigErrorEnum.SecureStorageError);
                    }
                }
            } catch (Exception e) {
                mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "AppConfiguration exception",
                        Log.getStackTraceString(e));
                setOperation = false;
            }
        }
        return setOperation;
    }
}
