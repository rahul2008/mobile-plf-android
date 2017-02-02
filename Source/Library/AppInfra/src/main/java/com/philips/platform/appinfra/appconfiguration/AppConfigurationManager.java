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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by 310238114 on 7/25/2016.
 */
public class AppConfigurationManager implements AppConfigurationInterface {

    private AppInfra mAppInfra;
    private Context mContext;
    private JSONObject configJsonCache;
    private JSONObject staticconfigJsonCache;
    private static final String mAppConfig_SecureStoreKey = "ail.app_config";

    private SecureStorageInterface ssi;

    public AppConfigurationManager(AppInfra appInfra) {
        mAppInfra = appInfra;
        mContext = appInfra.getAppInfraContext();

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
            result = makeKeyUppercase(result); // converting all Group and child key Uppercase
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.VERBOSE, "Json",
                    result.toString());

        } catch (Exception e) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "AppConfiguration exception",
                    Log.getStackTraceString(e));
        }
        return result;
    }

    private JSONObject getjSONFromCache() {
        if (null == configJsonCache) {
            configJsonCache = getjSONFromDevice();
        }
        return configJsonCache;

    }

    private JSONObject getStaticConfigJsonCache() {
        if (staticconfigJsonCache == null) {
            staticconfigJsonCache = getMasterConfigFromApp();
        }
        return staticconfigJsonCache;
    }


    private JSONObject getjSONFromDevice() {
        ssi = mAppInfra.getSecureStorage();
        JSONObject jObj = null;
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        String jsonString = ssi.fetchValueForKey(mAppConfig_SecureStoreKey, sse);
        if (null == jsonString || null == sse) {
            jObj = getMasterConfigFromApp();// reads from Application asset
        } else {

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
    public Object getPropertyForKey(String key, String group, AppConfigurationError configError)
            throws IllegalArgumentException {
        Object object = null;
        if (null == group || null == key || group.isEmpty() || key.isEmpty() || !group.matches("[a-zA-Z0-9_.-]+") || !key.matches("[a-zA-Z0-9_.-]+")) {
            configError.setErrorCode(AppConfigurationError.AppConfigErrorEnum.InvalidKey);
            throw new IllegalArgumentException("Invalid Argument Exception");
        } else {
            getjSONFromCache(); // fetch from cache

            //configJsonCache is initialized//
            try {
                object = getKey(key, group, configError, configJsonCache);
            } catch (Exception e) {
                mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "AppConfiguration exception",
                        Log.getStackTraceString(e));
            }
        }
        return object;
    }

    @Override
    public boolean setPropertyForKey(String key, String group, Object object, AppConfigurationError
            configError) throws IllegalArgumentException {
        boolean setOperation = false;
        if (null == key || null == group || group.isEmpty() || !group.matches("[a-zA-Z0-9_.-]+") ||
                !key.matches("[a-zA-Z0-9_.-]+")) {
            configError.setErrorCode(AppConfigurationError.AppConfigErrorEnum.InvalidKey);
            throw new IllegalArgumentException("Invalid Argument Exception");
        } else {
            getjSONFromCache(); // fetch from cache
            key = key.toUpperCase();
            group = group.toUpperCase();
            try {
                boolean isCocoPresent = configJsonCache.has(group);
                JSONObject cocoJSONobject;
                if (!isCocoPresent) { // if request coco  does not exist
                    // configError.setErrorCode(ConfigError.ConfigErrorEnum.GroupNotExists);
                    cocoJSONobject = new JSONObject();
                    configJsonCache.put(group, cocoJSONobject);
                } else {
                    cocoJSONobject = configJsonCache.optJSONObject(group);
                }
                if (null == cocoJSONobject) { // invalid Coco JSON
                    configError.setErrorCode(AppConfigurationError.AppConfigErrorEnum.FatalError);
                } else {
                    // boolean isKeyPresent = cocoJSONobject.has(key);
                    if (object instanceof ArrayList) {
                        if (((ArrayList) object).get(0) instanceof ArrayList) {
                            throw new IllegalArgumentException("Invalid Argument Exception");
                        } else if (((ArrayList) object).get(0) instanceof Integer || ((ArrayList) object).get(0) instanceof String) {

                            JSONArray jsonArray = new JSONArray(((ArrayList) object).toArray());
                            cocoJSONobject.put(key, jsonArray);

                        } else {
                            throw new IllegalArgumentException("Invalid Argument Exception");
                        }
                    } else if (object instanceof HashMap) { // if object is MAP
                        Map<?,?> map = (Map) object;
                        Set<?> keyset = map.keySet();
                        Iterator<?> keyItr = keyset.iterator();
                        Object objectKey = keyItr.next();
                        Object value = map.get(objectKey); // value for key:objectKey
                        if (null == value) {
                            throw new IllegalArgumentException("Invalid Argument Exception");
                        } else {

                            if (objectKey instanceof String) { // if keys are String
                                if (value instanceof String || value instanceof Integer) { // if value are Integer OR String
                                    JSONObject jsonObject = new JSONObject(object.toString());
                                    cocoJSONobject.put(key, jsonObject);
                                } else {
                                    throw new IllegalArgumentException("Invalid Argument Exception");
                                }
                            } else {
                                throw new IllegalArgumentException("Invalid Argument Exception");
                            }
                        }
                    } else if (object instanceof Integer || object instanceof String || null == object) {

                        cocoJSONobject.put(key, object);
                    } else {
                        throw new IllegalArgumentException("Invalid Argument Exception");
                    }
                    SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
                    ssi.storeValueForKey(mAppConfig_SecureStoreKey, configJsonCache.toString(), sse);
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


    @Override
    public Object getDefaultPropertyForKey(String key, String group, AppConfigurationError configError) throws IllegalArgumentException {

        Object object = null;
        if (null == group || null == key || group.isEmpty() || key.isEmpty() || !group.matches("[a-zA-Z0-9_.-]+") || !key.matches("[a-zA-Z0-9_.-]+")) {
            configError.setErrorCode(AppConfigurationError.AppConfigErrorEnum.InvalidKey);
            throw new IllegalArgumentException("Invalid Argument Exception");
        } else {
            //configJsonCache is initialized//
            getStaticConfigJsonCache();
            try {
                object = getKey(key, group, configError, staticconfigJsonCache);
            } catch (Exception e) {
                mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "AppConfiguration exception",
                        Log.getStackTraceString(e));
            }
        }
        return object;
    }

    private Object getKey(String key, String group, AppConfigurationError configError, JSONObject jsonObject) {
        key = key.toUpperCase();
        group = group.toUpperCase();
        Object object = null;
        boolean isCocoPresent = jsonObject.has(group);
        if (!isCocoPresent) { // if request coco does not exist
            configError.setErrorCode(AppConfigurationError.AppConfigErrorEnum.GroupNotExists);
        } else {
            JSONObject cocoJSONobject = jsonObject.optJSONObject(group);
            if (null == cocoJSONobject) { // invalid Coco JSON
                configError.setErrorCode(AppConfigurationError.AppConfigErrorEnum.FatalError);
            } else {
                boolean isKeyPresent = cocoJSONobject.has(key);
                if (!isKeyPresent) { // if key is not found inside coco
                    configError.setErrorCode(AppConfigurationError.AppConfigErrorEnum.KeyNotExists);
                } else {
                    object = cocoJSONobject.opt(key); // Returns the value mapped by name, or null if no such mapping exists
                    if (null == object) {
                        //configError.setErrorCode(AppConfigurationError.AppConfigErrorEnum.NoDataFoundForKey);
                    } else {
                        //  KEY FOUND SUCCESS
                        if (cocoJSONobject.opt(key) instanceof JSONArray) {
                            JSONArray jsonArray = cocoJSONobject.optJSONArray(key);
                            List<Object> list = new ArrayList<Object>();
                            for (int iCount = 0; iCount < jsonArray.length(); iCount++) {
                                list.add(jsonArray.opt(iCount));
                            }
                            object = list;
                        } else if (cocoJSONobject.opt(key) instanceof JSONObject) {
                            try {
                                object = jsonToMap(cocoJSONobject.opt(key));
                            } catch (JSONException e) {
                                mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "AppConfiguration exception",
                                        Log.getStackTraceString(e));
                            }
                        }
                    }
                }
            }
        }
        return object;
    }

    private Map<String,?> jsonToMap(Object JSON) throws JSONException {
        HashMap<String, Object> map = new HashMap<String, Object>();
        JSONObject jObject = new JSONObject(JSON.toString());
        Iterator<?> keys = jObject.keys();

        while (keys.hasNext()) {
            String key = (String) keys.next();
            Object value = jObject.get(key);
            map.put(key, value);
        }
        return map;
    }

    private JSONObject makeKeyUppercase(JSONObject json) {
        JSONObject newJsonGroup = new JSONObject();
        Iterator<String> iteratorGroup = json.keys();
        while (iteratorGroup.hasNext()) {
            String keyGroup = iteratorGroup.next();
            try {
                JSONObject objectGroup = json.optJSONObject(keyGroup);


                JSONObject newJsonChildObject = new JSONObject();
                Iterator<String> iteratorKey = objectGroup.keys();
                while (iteratorKey.hasNext()) {
                    String key = iteratorKey.next();
                    try {
                        Object objectKey = objectGroup.opt(key);
                        newJsonChildObject.put(key.toUpperCase(), objectKey);
                    } catch (JSONException e) {
                        // Something went wrong!
                    }

                }
                newJsonGroup.put(keyGroup.toUpperCase(), newJsonChildObject);

            } catch (JSONException e) {
                // Something went wrong!
            }
        }
        return newJsonGroup;
    }
}
