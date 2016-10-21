package com.philips.platform.flowmanagerjson;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.philips.platform.appframework.R;
import com.philips.platform.flowmanagerjson.jsonstates.AppStates;
import com.philips.platform.flowmanagerjson.pojo.AppFlow;
import com.philips.platform.flowmanagerjson.pojo.AppFlowModel;
import com.philips.platform.flowmanagerjson.pojo.AppFlowNextState;
import com.philips.platform.flowmanagerjson.pojo.AppFlowStates;

import java.util.HashMap;
import java.util.Map;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class AppFlowDataParser {

    /**
     * This method will return the object of AppFlow class or 'null'.
     * It request 'getJsonFromURL' to download the AppFlow json by sending the server URL.
     * it also send the path of prepackaged AppFlow Json file to handle the offline/error scenarios.
     *
     * @param context The context to use.  Usually your {@link android.app.Application}
     *                or {@link android.app.Activity} object.
     * @return Object to 'AppFlowModel' class or 'null'
     */
    public static AppFlowModel getAppFlow(Context context) {
        AppFlowModel appFlowModel = null;
        String response = null;
        final JSONHelper jsonHelper = new JSONHelper(context);
        try {
            response = jsonHelper.getJsonFromURL(R.string.com_philips_app_fmwk_app_flow_url
                    , R.string.com_philips_app_fwrk_app_flow_json_file_path);
            appFlowModel = new Gson().fromJson(response, AppFlowModel.class);
        } catch (JsonSyntaxException e) {
            appFlowModel = null;
            response = null;

            // This code has been added to handle the cases of JSON parsing error/exception
            response = jsonHelper.readJsonFromFile
                    (R.string.com_philips_app_fwrk_app_flow_json_file_path, context);
            appFlowModel = new Gson().fromJson(response, AppFlowModel.class);
        }
        return appFlowModel;
    }

    /**
     * This method will return a Map of state to array of next states.
     *
     * @param appFlow Object to AppFlow class which defines the app flow.
     * @return Map of state to array of next states.
     */
    public static Map<AppStates, AppFlowNextState[]> getAppFlowMap(AppFlow appFlow) {
        Map<AppStates, AppFlowNextState[]> appFlowMap = null;
        if (appFlow.getStates() != null) {
            appFlowMap = new HashMap<AppStates, AppFlowNextState[]>();
            for (final AppFlowStates states : appFlow.getStates()) {
                appFlowMap.put(AppStates.get(states.getState()), states.getNextState());
            }
        }
        return appFlowMap;
    }
}
