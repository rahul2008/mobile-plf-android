package com.philips.platform.appinfra.config;

import android.content.Context;

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
public class ConfigManager implements ConfigInterface {

    JSONObject jo;
    AppInfra mAppInfra;
    Context mContext;
    private static final String uAPP_CONFIG_FILE = "uAPP_CONFIG_FILE";

    SecureStorageInterface ssi;

    public ConfigManager(AppInfra appInfra) {
        mAppInfra = appInfra;
        mContext = appInfra.getAppInfraContext();
        ssi = mAppInfra.getSecureStorage();
    }

    public JSONObject getMasterConfigFromApp() {
        JSONObject result = null;
        try {
            InputStream mInputStream = mContext.getAssets().open("configuration.json");
            BufferedReader r = new BufferedReader(new InputStreamReader(mInputStream));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line).append('\n');
            }
            result = new JSONObject(total.toString());
            SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
            ssi.storeValueForKey(uAPP_CONFIG_FILE, result.toString(), sse);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public JSONObject getjSONFromDevice() {
        JSONObject jObj = null;
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        String jsonString = ssi.fetchValueForKey(uAPP_CONFIG_FILE, sse);
        if (null == jsonString || null == sse) {
            // do nothing
        } else {
            // save master json file to device memory
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, "uAPP_CONFIG", jsonString);
            try {
                jObj = new JSONObject(jsonString);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return jObj;
    }

    @Override
    public Object getPropertyForKey(String groupName, String key, ConfigError configError) {
        Object object = null;
        if (null == groupName || null == key || key.isEmpty() || key.isEmpty()) {
            configError.setErrorCode(ConfigError.ConfigErrorEnum.InvalidKey);
        } else {
            JSONObject deviceObject = getjSONFromDevice();
            if (null == deviceObject) {  // if master file is not yet saved into phone memory
                deviceObject = getMasterConfigFromApp();// reads from Application asset
            }
            try {
                boolean isCocoPresent = deviceObject.has(groupName);
                if (!isCocoPresent) { // if request coco does not exist
                    configError.setErrorCode(ConfigError.ConfigErrorEnum.GroupNotExists);
                } else {

                    JSONObject cocoJSONobject = deviceObject.optJSONObject(groupName);
                    if (null == cocoJSONobject) { // invalid Coco JSON
                        configError.setErrorCode(ConfigError.ConfigErrorEnum.FatalError);
                    } else {
                        boolean isKeyPresent = cocoJSONobject.has(key);
                        if (!isKeyPresent) { // if key is not found inside coco
                            configError.setErrorCode(ConfigError.ConfigErrorEnum.KeyNotExists);
                        } else {
                            object = cocoJSONobject.opt(key); // Returns the value mapped by name, or null if no such mapping exists
                            if (null == object) {
                                configError.setErrorCode(ConfigError.ConfigErrorEnum.NoDataFoundForKey);
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
                e.printStackTrace();
            }
        }
        return object;
    }

    @Override
    public boolean setPropertyForKey(String groupName, String key, Object object, ConfigError configError) {
        boolean setOperation = false;
        if (null == groupName || null == key || key.isEmpty() || key.isEmpty()) {
            configError.setErrorCode(ConfigError.ConfigErrorEnum.InvalidKey);
            return setOperation;
        } else {
            JSONObject deviceObject = getjSONFromDevice();
            if (null == deviceObject) {  // if master file is not yet saved into phone memory
                deviceObject = getMasterConfigFromApp();// reads from Application asset
            }
            try {
                boolean isCocoPresent = deviceObject.has(groupName);
                if (!isCocoPresent) { // if request coco  does not exist
                    configError.setErrorCode(ConfigError.ConfigErrorEnum.GroupNotExists);
                } else {
                    JSONObject cocoJSONobject = deviceObject.optJSONObject(groupName);
                    if (null == cocoJSONobject) { // invalid Coco JSON
                        configError.setErrorCode(ConfigError.ConfigErrorEnum.FatalError);
                    } else {
                        boolean isKeyPresent = cocoJSONobject.has(key);
                        cocoJSONobject.put(key, object);
                        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
                        ssi.storeValueForKey(uAPP_CONFIG_FILE, deviceObject.toString(), sse);
                        if (null == sse.getErrorCode()) {
                            setOperation = true;
                        } else {
                            setOperation = false;
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                setOperation = false;
            }
        }
        return setOperation;
    }


}
