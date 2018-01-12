package com.philips.platform.uid.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.philips.platform.uid.view.widget.EditText;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
     * @since 3.0.0
     */
    public static UIDLocaleHelper getInstance() {
        return uidLocaleHelper;
    }

    /**
     * This method is used by UID widgets to lookup strings in language pack JSON for all the strings which are set in xml layout files of the widgets.
     *
     * @param context Context of view to be updated
     * @param view    View object to be updated
     * @param attrs   Attribute set for the view
     *                @since 3.0.0
     */
    public static void setTextFromResourceID(Context context, View view, AttributeSet attrs) {
        if (view instanceof TextView) {
            TypedArray textArray = context.obtainStyledAttributes(attrs, new int[]{android.R.attr.text, android.R.attr.hint});
            int resourceId = textArray.getResourceId(0, -1);
            if (resourceId != -1) {
                ((TextView) view).setText(resourceId);
            }
            resourceId = textArray.getResourceId(1, -1);
            if (resourceId != -1) {
                ((EditText) view).setHint(resourceId);
            }
            textArray.recycle();
        }
    }

    /**
     * This API will help you to set the path of your Language pack JSON.<br> This path would be used by UIDLocaleHelper to parse the JSON.
     * <br>DLS will handle the string value mappings to set the corresponding string in your widgets or controls.
     * <br>Call with null to stop lookup and delegate all calls to native.
     *
     * @param pathInput Absolute path of your JSON file in String format
     *                  @since 3.0.0
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
        uidLocaleHelper.stringMap.clear();
    }

    /**
     * This method is used by UIDResources to lookup strings present in your Language pack JSON and subsequently apply the string values.
     * @since 3.0.0
     */
    public String lookUpString(String key) {
        return uidLocaleHelper.stringMap.get(key);
    }

    /**
     * This method is used by UIDResources to check if lookup is needed based a result of JSON parsing.
     * @since 3.0.0
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
            UIDLog.e(TAG, e.getMessage());
        }
        return parseSuccess;
    }

    private String getJSONStringFromPath(String pathInput) {
        String jsonString = null;
        FileInputStream fis = null;
        try {
            File file = new File(pathInput);
            int length = (int) file.length();
            byte[] bytes = new byte[length];
            fis = new FileInputStream(file);
            fis.read(bytes);
            jsonString = new String(bytes, "UTF-8");
        } catch (IOException e) {
            UIDLog.e(TAG, e.getMessage());
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    UIDLog.e(TAG, e.getMessage());
                }
            }
        }
        return jsonString;
    }
}