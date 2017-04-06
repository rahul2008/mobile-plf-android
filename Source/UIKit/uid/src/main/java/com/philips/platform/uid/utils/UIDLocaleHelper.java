package com.philips.platform.uid.utils;


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

public class UIDLocaleHelper {

    public static final String TAG = "UIDLocaleHelper";

    private static String filePath = null;
    private static UIDLocaleHelper uidLocaleHelper;
    private static HashMap<String,String> stringHashMap = new HashMap<String, String>();
    private static boolean isLookUp = false;

    private UIDLocaleHelper() {
    }

    public static UIDLocaleHelper getUidLocaleHelper() {
        if(uidLocaleHelper == null) {
                uidLocaleHelper = new UIDLocaleHelper();
        }
        return uidLocaleHelper;
    }

    /**
     * This API will help you to set the path of your Language pack JSON.<br> This path would be used by UIDLocaleHelper to parse the JSON.
     *<br>DLS will handle the string value mappings to set the corresponding string in your widgets or controls.
     * @param filePath Absolute path of your JSON file in String format
     */
    public static void setFilePath(String filePath){
        uidLocaleHelper.filePath = filePath;
        UIDLocaleHelper.isLookUp = parseJSON();
    }

    public static String lookUpString(String key) {
        return stringHashMap.get(key);
    }

    public static boolean isLookUp() {
        return isLookUp;
    }

    private static boolean parseJSON() {
        boolean parseSuccess = false;
        String jsonString = getJSONStringFromPath();
            try {
                if(jsonString!= null){
                    JSONObject jsonObject = new JSONObject(jsonString);
                    Iterator<String> keys = jsonObject.keys();
                    while (keys.hasNext()){
                        String key = keys.next();
                        String value = jsonObject.getString(key);
                        stringHashMap.put(key,value);
                    }
                    parseSuccess = true;
                }
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        return parseSuccess;
    }

    private static String getJSONStringFromPath(){

        File file = new File(filePath);
        int length = (int) file.length();
        byte[] bytes = new byte[length];
        String jsonString = null;

        try {
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
