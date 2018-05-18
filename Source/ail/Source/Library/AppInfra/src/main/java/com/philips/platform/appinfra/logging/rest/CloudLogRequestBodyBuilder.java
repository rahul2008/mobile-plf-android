package com.philips.platform.appinfra.logging.rest;

import android.util.Log;

import com.google.gson.Gson;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.logging.AppInfraLogging;
import com.philips.platform.appinfra.logging.database.AILCloudLogData;
import com.philips.platform.appinfra.logging.model.AILCloudLogMetaData;
import com.philips.platform.appinfra.logging.rest.model.CloudLogs;
import com.philips.platform.appinfra.logging.rest.model.Entry;
import com.philips.platform.appinfra.logging.rest.model.LogMetaDataModel;
import com.philips.platform.appinfra.logging.rest.model.Resource;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhishek on 5/16/18.
 */

public class CloudLogRequestBodyBuilder {

    private AppInfra appInfra;


    private AILCloudLogMetaData ailCloudLogMetaData;

    public CloudLogRequestBodyBuilder(AppInfra appInfra) {
        if (appInfra.getLogging() instanceof AppInfraLogging) {
            this.ailCloudLogMetaData = ((AppInfraLogging) appInfra.getLogging()).getAilCloudLogMetaData();
        }
    }

    public JSONObject getCloudLogRequestBody(List<AILCloudLogData> ailCloudLogDataList) {
        CloudLogs cloudLogs = new CloudLogs();
        List<Entry> entryList = new ArrayList<Entry>();
        for (AILCloudLogData ailCloudLogData : ailCloudLogDataList) {
            Entry entry = new Entry();
            Resource resource = new Resource();
            resource.setApplicationName(ailCloudLogMetaData.getAppName());
            resource.setApplicationVersion(ailCloudLogData.appVersion);
            resource.setApplicationInstance(ailCloudLogMetaData.getAppsId());
            resource.setCategory("TraceLog");
            resource.setComponent(ailCloudLogData.component);
            resource.setEventId(ailCloudLogData.eventId);
            resource.setId(ailCloudLogData.logId);
            resource.setLogTime("" + ailCloudLogData.logTime);
            resource.setOriginatingUser(ailCloudLogData.userUUID);
            resource.setResourceType("LogEvent");
            resource.setServerName(ailCloudLogData.serverName);
            resource.setSeverity(ailCloudLogData.severity);
            resource.setTransactionId(ailCloudLogData.logId);
            LogMetaDataModel logMetaDataModel=new LogMetaDataModel();
            logMetaDataModel.setAppsid(ailCloudLogData.appsId);
            logMetaDataModel.setAppstate(ailCloudLogData.appState);
            logMetaDataModel.setDescription(ailCloudLogData.logDescription);
            logMetaDataModel.setDetails(ailCloudLogData.details);
            logMetaDataModel.setDevicetype(ailCloudLogData.serverName);
            logMetaDataModel.setHomecountry(ailCloudLogMetaData.getHomeCountry());
            logMetaDataModel.setLocale(ailCloudLogMetaData.getLocale());
            logMetaDataModel.setLocaltime(""+ailCloudLogData.localtime);
            logMetaDataModel.setNetworktype(ailCloudLogData.networktype);
            Gson gson = new Gson();
            String jsonString = gson.toJson(logMetaDataModel);
            resource.setLogData(jsonString);
            entry.setResource(resource);
            entryList.add(entry);
        }
        cloudLogs.setEntry(entryList);

        Gson gson = new Gson();
        String jsonString = gson.toJson(cloudLogs);

        try {
            return new JSONObject(jsonString);
        } catch (JSONException e) {
            Log.d("SyncTesting", "Exception while creating JSON object from json string");
            return null;
        }
    }


}
