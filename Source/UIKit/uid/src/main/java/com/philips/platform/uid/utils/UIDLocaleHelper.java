package com.philips.platform.uid.utils;


import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UIDLocaleHelper {

    private static final String TAG = "UIDLocaleHelper";

    private static UIDLocaleHelper uidLocaleHelper = new UIDLocaleHelper();
    private Map<String, String> stringMap = new HashMap<>();
    private boolean isLookUp = false;

    private UIDLocaleHelper() {
    }

    /**
     * This static API will help you get singleton instance of UIDLocaleHelper
     */
    public static UIDLocaleHelper getInstance() {
        return uidLocaleHelper;
    }

    /**
     * This API will help you to set the path of your Language pack JSON.<br> This path would be used by UIDLocaleHelper to parse the JSON.
     * <br>DLS will handle the string value mappings to set the corresponding string in your widgets or controls.
     * <br>Call with null to stop lookup and delegate all calls to native.
     *
     * <br>Must be called before setContentview.
     *
     * @param pathInput Absolute path of your JSON file in String format
     */
    public void setFilePath(String pathInput) {
        if (TextUtils.isEmpty(pathInput)) {
            resetAllValues();
        } else {
            uidLocaleHelper.isLookUp = parseJSON(pathInput);
        }
    }

    private void resetAllValues() {
        uidLocaleHelper.isLookUp = false;
        uidLocaleHelper.stringMap = null;
    }

    /**
     * This method is used by UIDResources to lookup strings present in your Language pack JSON and subsequently apply the string values.
     */
    public String lookUpString(String key) {
        return uidLocaleHelper.stringMap.get(key);
    }

    /**
     * This method is used by UIDResources to check if lookup is needed based a result of JSON parsing.
     */
    public boolean isLookUp() {
        return uidLocaleHelper.isLookUp;
    }

    private boolean parseJSON(String pathInput) {
        boolean parseSuccess = false;
        String jsonString = getJSONStringFromPath(pathInput);
        try {
            if (jsonString != null) {
                JSONObject jsonObject = new JSONObject(jsonString);
                Iterator<String> keys = jsonObject.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    String value = jsonObject.getString(key);
                    uidLocaleHelper.stringMap.put(key, value);
                }
                parseSuccess = true;
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return parseSuccess;
    }

    private String getJSONStringFromPath(String pathInput) {
        String jsonString = null;

        try {
            File file = new File(pathInput);
            int length = (int) file.length();
            byte[] bytes = new byte[length];
            FileInputStream in = new FileInputStream(file);
            in.read(bytes);
            in.close();
            jsonString = new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, e.getMessage());
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return jsonString;
    }
}