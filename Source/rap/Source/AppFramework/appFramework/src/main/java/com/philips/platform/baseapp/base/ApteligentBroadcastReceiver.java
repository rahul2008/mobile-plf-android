/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.baseapp.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.philips.platform.baseapp.screens.utility.RALog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/*
 * This class is listening to tag events, sent from AppInfra, and then after
 *  Apteligent will process those tags further.
 */
public class ApteligentBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = ApteligentBroadcastReceiver.class.getSimpleName();
    protected static final String TAGGING_DATA = "TAGGING_DATA";
    protected static final String ACTION_TAGGING_DATA = "ACTION_TAGGING_DATA";
    private static final String pageKeyName = "trackPage: ";
    private static final String actionKeyName = "trackAction: ";
    protected static final String PAGE_NAME = "ailPageName";
    protected static final String ACTION_NAME = "ailActionName";
    private Context mContext = null;

        protected ApteligentBroadcastReceiver(Context context) {
            mContext = context;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {

                JSONObject jsonObject = new JSONObject();

                if(intent.getAction() == ACTION_TAGGING_DATA) {
                    Map<String, String> textExtraTaggingData;
                    String eventPageValue;

                    textExtraTaggingData = getSerializableExtra(intent);
                    eventPageValue = getTaggingData(textExtraTaggingData, PAGE_NAME);

                    if(eventPageValue == null) {
                        String eventActionValue = getTaggingData(textExtraTaggingData, ACTION_NAME);
                        if(eventActionValue == null) {
                            return;
                        }
                        jsonObject = getJsonFormat(jsonObject, actionKeyName, eventActionValue);
                    }
                    else {
                        jsonObject = getJsonFormat(jsonObject, pageKeyName, eventPageValue);
                    }

                    RALog.d(TAG, "pageName or actionName -> " + jsonObject.toString());

                    /*
                        A breadcrumb is a developer-defined text string (up to 140 characters)
                        that allows developers to capture app run-time information.
                        Example breadcrumbs may include variable values, progress through
                        the code, user actions, or low memory warnings. For an introduction
                     */
//                    Crittercism.leaveBreadcrumb(jsonObject.toString());
                }
            }
        }

        /*
         * Converting String to Json format.
         */
    private JSONObject getJsonFormat (JSONObject jsonObject, String key, String value) {
        try {
            jsonObject.put(key, value);
        } catch (JSONException e) {
            RALog.e(TAG, "Found some issues in getJsonFormat");
        }

        return jsonObject;
    }

    private String getTaggingData(Map<String, String> textExtraTaggingData, String key) {
        return textExtraTaggingData.get(key);
    }

    @SuppressWarnings("unchecked")
    protected HashMap<String, String> getSerializableExtra(Intent intent) {
        return (HashMap<String, String>) intent.getSerializableExtra(TAGGING_DATA);
    }
}
