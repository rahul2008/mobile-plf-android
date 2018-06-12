package com.philips.platform.appinfra.logging.rest;

import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.logging.AppInfraLogging;
import com.philips.platform.appinfra.logging.CloudLoggingConstants;
import com.philips.platform.appinfra.logging.LoggingUtils;
import com.philips.platform.appinfra.logging.database.AILCloudLogData;
import com.philips.platform.appinfra.logging.model.AILCloudLogMetaData;
import com.philips.platform.appinfra.logging.rest.model.CloudLogs;
import com.philips.platform.appinfra.logging.rest.model.Entry;
import com.philips.platform.appinfra.logging.rest.model.LogData;
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

    private  String productKey;

    private AILCloudLogMetaData ailCloudLogMetaData;


    public CloudLogRequestBodyBuilder(AppInfra appInfra,String productKey) {
        if (appInfra.getLogging() instanceof AppInfraLogging) {
            this.ailCloudLogMetaData = ((AppInfraLogging) appInfra.getLogging()).getAilCloudLogMetaData();
            this.productKey=productKey;
        }
    }

    public JSONObject getCloudLogRequestBody(List<AILCloudLogData> ailCloudLogDataList) {
        CloudLogs cloudLogs = new CloudLogs();
        List<Entry> entryList = new ArrayList<Entry>();
        for (AILCloudLogData ailCloudLogData : ailCloudLogDataList) {
            Entry entry = new Entry();
            Resource resource = new Resource();
            resource.setApplicationName(getFormattedString(ailCloudLogMetaData.getAppName()));
            resource.setApplicationVersion(getFormattedString(ailCloudLogData.appVersion));
            resource.setApplicationInstance(getFormattedString(ailCloudLogData.appsId));
            resource.setCategory("TraceLog");
            resource.setComponent(getFormattedString(ailCloudLogData.component));
            resource.setEventId(getFormattedString(ailCloudLogData.eventId));
            resource.setId(getValue(ailCloudLogData.logId));
            resource.setLogTime("" + LoggingUtils.getFormattedDateAndTime(ailCloudLogData.logTime, CloudLoggingConstants.CLOUD_LOGGING_DATE_TIME_FORMAT));
            resource.setOriginatingUser(getFormattedString(ailCloudLogData.userUUID));
            resource.setResourceType("LogEvent");
            resource.setServerName(getFormattedString(ailCloudLogData.serverName));
            resource.setSeverity(getFormattedString(ailCloudLogData.severity));
            resource.setTransactionId(getValue(ailCloudLogData.logId));
            LogMetaDataModel logMetaDataModel=new LogMetaDataModel();
            logMetaDataModel.setAppstate(ailCloudLogData.appState);
            logMetaDataModel.setDescription(ailCloudLogData.logDescription);
            Log.v("SyncTesting","Log::"+ailCloudLogData.logDescription);
            logMetaDataModel.setDetails(getValue(ailCloudLogData.details));
            logMetaDataModel.setDevicetype(Build.MANUFACTURER + " " + Build.MODEL);
            logMetaDataModel.setHomecountry(getValue(ailCloudLogData.homecountry));
            logMetaDataModel.setLocale(ailCloudLogData.locale);
            logMetaDataModel.setLocaltime("" + LoggingUtils.getFormattedDateAndTime(ailCloudLogData.localtime, CloudLoggingConstants.CLOUD_LOGGING_DATE_TIME_FORMAT));
            logMetaDataModel.setNetworktype(ailCloudLogData.networktype);
            Gson gson = new Gson();
            String jsonString = gson.toJson(logMetaDataModel);

            LogData logData=new LogData();
            logData.setMessage(Base64.encodeToString(jsonString.getBytes(),Base64.NO_WRAP));
            resource.setLogData(logData);
            entry.setResource(resource);
            entryList.add(entry);
        }
        cloudLogs.setEntry(entryList);
        cloudLogs.setProductKey(productKey);
        cloudLogs.setTotal(ailCloudLogDataList.size());
        cloudLogs.setResourceType("Bundle");
        cloudLogs.setType("transaction");
        Gson gson = new Gson();
        String jsonString = gson.toJson(cloudLogs);

        try {
            return new JSONObject(jsonString);
        } catch (JSONException e) {
            Log.v("SyncTesting", "Exception while creating JSON object from json string");
            return null;
        }
    }

    private String getValue(String value){
        if(TextUtils.isEmpty(value)){
            return CloudLoggingConstants.NA;
        }
        return value;
    }

    private String getFormattedString(String value){
        if(TextUtils.isEmpty(value)){
            return CloudLoggingConstants.NA;
        }else{
            return value.replaceAll("[$&+,:;=?@#|<>()\\\\\\[\\]]", "_");
        }
    }


}
