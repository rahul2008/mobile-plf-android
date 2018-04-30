package com.philips.platform.appinfra.logging.database;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.logging.LoggingConfiguration;
import com.philips.platform.appinfra.logging.LoggingUtils;
import com.philips.platform.appinfra.logging.model.AILCloudLogMetaData;

import java.util.Arrays;
import java.util.List;
import java.util.logging.LogRecord;

/**
 * Created by abhishek on 4/26/18.
 */

public class AILCloudLogDataBuilder {

    private final LoggingConfiguration loggingConfiguration;

    private AppInfra appInfra;

    public AILCloudLogDataBuilder(AppInfra appInfra, LoggingConfiguration loggingConfiguration) {
        this.appInfra = appInfra;
        this.loggingConfiguration = loggingConfiguration;
    }

    public AILCloudLogData buildCloudLogModel(LogRecord logRecord) {
        AILCloudLogData ailCloudLogData = new AILCloudLogData();
        ailCloudLogData.logId = LoggingUtils.getUUID();
        ailCloudLogData.severity = LoggingUtils.getAILLogLevel(logRecord.getLevel().toString());
        ailCloudLogData.eventId = logRecord.getMessage();
        if (logRecord.getParameters() != null) {
            List<Object> parameters = Arrays.asList(logRecord.getParameters());
            if (parameters.size() == 2) {
                if (parameters.get(1) != null) {
                    ailCloudLogData.details = convertMapToJsonString(parameters.get(1));
                }
            }
            ailCloudLogData.logDescription = (String) parameters.get(0);
        }
        AILCloudLogMetaData ailCloudLogMetaData=appInfra.getAilCloudLogMetaData();
        ailCloudLogData.homecountry = ailCloudLogMetaData.getHomeCountry();
        ailCloudLogData.locale = ailCloudLogMetaData.getLocale();
        if (appInfra.getTime() != null && appInfra.getTime().getUTCTime() != null) {
            ailCloudLogData.logTime = appInfra.getTime().getUTCTime().getTime();
        }
        if (appInfra.getRestClient() != null && appInfra.getRestClient().getNetworkReachabilityStatus() != null) {
            ailCloudLogData.networktype = appInfra.getRestClient().getNetworkReachabilityStatus().toString();
        }
        ailCloudLogData.localtime = System.currentTimeMillis();
        ailCloudLogData.appState=ailCloudLogMetaData.getAppState();
        ailCloudLogData.appVersion=ailCloudLogMetaData.getAppVersion();
        ailCloudLogData.appsId=ailCloudLogMetaData.getAppsId();
        if (loggingConfiguration.getComponentID() != null && !loggingConfiguration.getComponentID().isEmpty()
                && loggingConfiguration.getComponentVersion() != null && !loggingConfiguration.getComponentVersion().isEmpty()) {
            ailCloudLogData.component = loggingConfiguration.getComponentID() + "/" + loggingConfiguration.getComponentVersion();
        }
        Log.d("test", "After adding heavy params:" + System.currentTimeMillis());
        return ailCloudLogData;
    }

    private String convertMapToJsonString(Object param) {
        if(param != null) {
            Gson gson = new GsonBuilder().create();
            return gson.toJson(param);
        }
        else
            return null;
    }
}
