package com.philips.cdp.uikit.utils;


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

public class UikitLocaleHelper {
    public static final String TAG = "UikitLocaleHelper";

    private static UikitLocaleHelper uikitLocaleHelper = new UikitLocaleHelper();
    private String filePath = null;
    private HashMap<String, String> stringHashMap = new HashMap<>();
    private boolean isLookUp = false;

    private UikitLocaleHelper() {
    }

    /**
     * This static API will help you get singleton instance of UikitLocaleHelper
     *
     */
    public static UikitLocaleHelper getUikitLocaleHelper() {
        return uikitLocaleHelper;
    }

    /**
     * This API will help you to set the path of your Language pack JSON.<br> This path would be used by UikitLocaleHelper to parse the JSON.
     * <br>Uikit will handle the string value mappings to set the corresponding string in your widgets or controls.
     *
     * @param pathInput Absolute path of your JSON file in String format
     */
    public void setFilePath(String pathInput) {

        if (pathInput == null || pathInput.length() == 0){
            uikitLocaleHelper.stringHashMap = null;
            uikitLocaleHelper.isLookUp = false;
        }
        else{
            uikitLocaleHelper.filePath = pathInput;
            uikitLocaleHelper.isLookUp = parseJSON();
        }
    }

    /**
     * This method is used by UikitResources to lookup strings present in your Language pack JSON and subsequently apply the string values.
     *
     */
    public String lookUpString(String key) {
        return uikitLocaleHelper.stringHashMap.get(key);
    }

    /**
     * This method is used by UikitResources to check if lookup is needed based a result of JSON parsing.
     *
     */
    public boolean isLookUp() {
        return uikitLocaleHelper.isLookUp;
    }

    private boolean parseJSON() {
        boolean parseSuccess = false;
        String jsonString = getJSONStringFromPath();
        try {
            if (jsonString != null) {
                JSONObject jsonObject = new JSONObject(jsonString);
                Iterator<String> keys = jsonObject.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    String value = jsonObject.getString(key);
                    uikitLocaleHelper.stringHashMap.put(key, value);
                }
                parseSuccess = true;
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return parseSuccess;
    }

    private String getJSONStringFromPath() {

        String jsonString = null;

        try {
            File file = new File(uikitLocaleHelper.filePath);
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

