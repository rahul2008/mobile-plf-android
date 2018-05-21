package com.philips.platform.appinfra.logging.database;

import android.util.Log;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.logging.AppInfraLogging;
import com.philips.platform.appinfra.logging.CloudLoggingConstants;
import com.philips.platform.appinfra.logging.LoggingUtils;
import com.philips.platform.appinfra.logging.MessageSizeExceedsException;
import com.philips.platform.appinfra.logging.model.AILCloudLogMetaData;

import java.util.Arrays;
import java.util.List;
import java.util.logging.LogRecord;

/**
 * Created by abhishek on 4/26/18.
 */

public class AILCloudLogDataBuilder {


    private AppInfra appInfra;

    public AILCloudLogDataBuilder(AppInfra appInfra) {
        this.appInfra = appInfra;
    }

    public AILCloudLogData buildCloudLogModel(LogRecord logRecord) throws MessageSizeExceedsException {
        AILCloudLogData ailCloudLogData = new AILCloudLogData();

        if (logRecord.getParameters() != null) {
            List<Object> parameters = Arrays.asList(logRecord.getParameters());
            if (parameters.size() == 4) {
                if (parameters.get(3) != null) {
                    ailCloudLogData.details = LoggingUtils.convertObjectToJsonString(parameters.get(3));
                }
            }
            ailCloudLogData.logDescription = (String) parameters.get(0);
            ailCloudLogData.component = (String) parameters.get(1) + "/" + (String) parameters.get(2);
            if (LoggingUtils.getStringLengthInBytes(ailCloudLogData.logDescription + ailCloudLogData.details) > CloudLoggingConstants.MAX_LOG_SIZE) {
                throw new MessageSizeExceedsException();
            }
        }
        ailCloudLogData.logId = LoggingUtils.getUUID();
        ailCloudLogData.severity = LoggingUtils.getAILLogLevel(logRecord.getLevel().toString());
        ailCloudLogData.eventId = logRecord.getMessage();
        if (appInfra.getLogging() != null) {
            AILCloudLogMetaData ailCloudLogMetaData = ((AppInfraLogging) appInfra.getLogging()).getAilCloudLogMetaData();
            if (ailCloudLogMetaData != null) {
                ailCloudLogData.homecountry = ailCloudLogMetaData.getHomeCountry();
                ailCloudLogData.locale = ailCloudLogMetaData.getLocale();
                ailCloudLogData.appState = ailCloudLogMetaData.getAppState();
                ailCloudLogData.appVersion = ailCloudLogMetaData.getAppVersion();
                ailCloudLogData.appsId = ailCloudLogMetaData.getAppId();
                ailCloudLogData.userUUID = ailCloudLogMetaData.getUserUUID();
            }
        }

        if (appInfra.getTime() != null && appInfra.getTime().getUTCTime() != null) {
            ailCloudLogData.logTime = appInfra.getTime().getUTCTime().getTime();
        }
        if (appInfra.getRestClient() != null && appInfra.getRestClient().getNetworkReachabilityStatus() != null) {
            ailCloudLogData.networktype = appInfra.getRestClient().getNetworkReachabilityStatus().toString();
        }
        ailCloudLogData.localtime = System.currentTimeMillis();

        ailCloudLogData.serverName = "Android/" + LoggingUtils.getOSVersion();
        ailCloudLogData.status= AILCloudLogDBManager.DBLogState.NEW.getState();
        Log.v("test", "After adding heavy params:" + System.currentTimeMillis());
        return ailCloudLogData;
    }


}
