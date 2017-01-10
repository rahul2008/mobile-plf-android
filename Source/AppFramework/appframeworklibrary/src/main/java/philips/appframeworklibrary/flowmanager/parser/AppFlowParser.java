/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package philips.appframeworklibrary.flowmanager.parser;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import philips.appframeworklibrary.flowmanager.enums.AppFlowEnum;
import philips.appframeworklibrary.flowmanager.listeners.AppFlowJsonListener;
import philips.appframeworklibrary.flowmanager.models.AppFlow;
import philips.appframeworklibrary.flowmanager.models.AppFlowEvent;
import philips.appframeworklibrary.flowmanager.models.AppFlowModel;
import philips.appframeworklibrary.flowmanager.models.AppFlowState;

public class AppFlowParser {

    /**
     * This method will return the object of AppFlow class or 'null'.
     * It request 'getJsonFromURL' to download the AppFlow json by sending the server URL.
     * it also send the path of prepackaged AppFlow Json file to handle the offline/error scenarios.
     *
     *                or {@link android.app.Activity} object.
     * @return Object to 'AppFlowModel' class or 'null'
     */
    // TODO: Deepthi , need to be prepared for running in separate thread and handle scenarios , may not be in same APIs
    public static AppFlowModel getAppFlow(String jsonPath, AppFlowJsonListener appFlowJsonListener) {
        AppFlowModel appFlow = null;
        try {
            InputStream is = new FileInputStream(jsonPath);
            final InputStreamReader inputStreamReader = new InputStreamReader(is);
            appFlow = new Gson().fromJson(inputStreamReader, AppFlowModel.class);
        } catch (JsonSyntaxException | FileNotFoundException e) {
            if(e instanceof JsonSyntaxException){
                appFlowJsonListener.onError(AppFlowEnum.JSON_PARSE_EXCEPTION);
            }else {
                appFlowJsonListener.onError(AppFlowEnum.FILE_NOT_FOUND);
            }
        }
        return appFlow;
    }

    /**
     * This method will return a Map of state to array of next states.
     *
     * @param appFlow Object to AppFlow class which defines the app flow.
     * @return Map of state to array of next states.
     */
    public static Map<String, List<AppFlowEvent>> getAppFlowMap(AppFlow appFlow) {
        HashMap<String, List<AppFlowEvent>> appFlowMap = null;
        if (appFlow.getStates() != null) {
            appFlowMap = new HashMap<>();
            for (final AppFlowState states : appFlow.getStates()) {
                appFlowMap.put(states.getState(), states.getEvents());
            }
        }
        return appFlowMap;
    }
}
