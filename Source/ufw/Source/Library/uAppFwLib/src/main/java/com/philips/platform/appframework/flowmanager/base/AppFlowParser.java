/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.flowmanager.base;

import android.content.Context;
import androidx.annotation.AnyRes;
import androidx.annotation.NonNull;
import androidx.annotation.RawRes;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.philips.platform.appframework.flowmanager.exceptions.JsonFileNotFoundException;
import com.philips.platform.appframework.flowmanager.exceptions.JsonStructureException;
import com.philips.platform.appframework.flowmanager.models.AppFlow;
import com.philips.platform.appframework.flowmanager.models.AppFlowEvent;
import com.philips.platform.appframework.flowmanager.models.AppFlowModel;
import com.philips.platform.appframework.flowmanager.models.AppFlowState;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class AppFlowParser {

    private Context mContext;

    AppFlowParser(Context mContext) {
        this.mContext = mContext;
    }

    AppFlowParser() {
    }

    /**
     * This method will return the object of AppFlow class or 'null'.
     * It request 'getJsonFromURL' to download the AppFlow json by sending the server URL.
     * it also send the path of prepackaged AppFlow Json file to handle the offline/error scenarios.
     * <p>
     * or {@link android.app.Activity} object.
     *
     * @return Object to 'AppFlowModel' class or 'null'
     */
    AppFlowModel getAppFlow(String jsonPath) throws JsonFileNotFoundException, JsonStructureException {
        AppFlowModel appFlow = null;
        if (isEmpty(jsonPath)) {
            throw new JsonFileNotFoundException();
        } else {
            try {
                final InputStreamReader inputStreamReader = getInputStreamReader(jsonPath);
                appFlow = new Gson().fromJson(inputStreamReader, AppFlowModel.class);
                inputStreamReader.close();
            } catch (JsonSyntaxException | FileNotFoundException e) {
                if (e instanceof JsonSyntaxException) {
                    throw new JsonStructureException();
                } else {
                    throw new JsonFileNotFoundException();
                }
            } catch (IOException e) {
                Log.e("IO-Exception", " error while reading appflow.json");
            }
        }
        return appFlow;
    }

    AppFlowModel getAppFlow(@RawRes int resId) throws JsonFileNotFoundException, JsonStructureException {
        AppFlowModel appFlow = null;
        if (resId == 0) {
            throw new JsonFileNotFoundException();
        } else {
            try {
                final InputStreamReader inputStreamReader = getInputStreamReader(resId);
                appFlow = new Gson().fromJson(inputStreamReader, AppFlowModel.class);
                inputStreamReader.close();
            } catch (JsonSyntaxException | FileNotFoundException e) {
                if (e instanceof JsonSyntaxException) {
                    throw new JsonStructureException();
                } else {
                    throw new JsonFileNotFoundException();
                }
            } catch (IOException e) {
                Log.e("IO-Exception", " error while reading appflow.json");
            }
        }
        return appFlow;
    }

    @NonNull
    private InputStreamReader getInputStreamReader(final String jsonPath) throws FileNotFoundException {
        InputStream is = getFileInputStream(jsonPath);
        return new InputStreamReader(is);
    }

    @NonNull
    private InputStreamReader getInputStreamReader(@AnyRes final int resId) throws FileNotFoundException {
        InputStream is = mContext.getResources().openRawResource(resId);
        return new InputStreamReader(is);
    }

    @NonNull
    InputStream getFileInputStream(final String jsonPath) throws FileNotFoundException {
        return new FileInputStream(jsonPath);
    }

    boolean isEmpty(final String jsonPath) {
        return jsonPath == null || jsonPath.length() == 0;
    }

    /**
     * This method will return a Map of state to array of next states.
     *
     * @param appFlow Object to AppFlow class which defines the app flow.
     * @return Map of state to array of next states.
     */
    Map<String, List<AppFlowEvent>> getAppFlowMap(AppFlow appFlow) {
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
