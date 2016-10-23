
/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.flowmanager;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.philips.platform.appframework.R;
import com.philips.platform.flowmanager.jsonstates.AppStates;
import com.philips.platform.flowmanager.pojo.AppFlow;
import com.philips.platform.flowmanager.pojo.AppFlowEvent;
import com.philips.platform.flowmanager.pojo.AppFlowModel;
import com.philips.platform.flowmanager.pojo.AppFlowState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppFrameworkDataParser {

    private static AppFlowModel appFlow;
    private static HashMap<AppStates, List<AppFlowEvent>> appFlowMap;

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
        String appFlowResponse;
        final JSONHelper jsonHelper = new JSONHelper(context);
        try {
            appFlowResponse = jsonHelper.getJsonForAppFlow(R.string.com_philips_app_fmwk_app_flow_url);
            appFlow = new Gson().fromJson(appFlowResponse, AppFlowModel.class);
        } catch (JsonSyntaxException e) {
            // This code has been added to handle the cases of JSON parsing error/exception
            appFlowResponse = jsonHelper.readJsonFromFile
                    (R.string.com_philips_app_fmwk_app_flow_url, context);
            appFlow = new Gson().fromJson(appFlowResponse, AppFlowModel.class);
        }
        return appFlow;
    }

    /**
     * This method will return a Map of state to array of next states.
     *
     * @param appFlow Object to AppFlow class which defines the app flow.
     * @return Map of state to array of next states.
     */
    public static Map<AppStates, List<AppFlowEvent>> getAppFlowMap(AppFlow appFlow) {
        if (appFlow.getAppFlowStates() != null) {
            appFlowMap = new HashMap<>();
            for (final AppFlowState states : appFlow.getAppFlowStates()) {
                appFlowMap.put(AppStates.get(states.getState()), states.getAppFlowEvents());
            }
        }
        return appFlowMap;
    }
}
