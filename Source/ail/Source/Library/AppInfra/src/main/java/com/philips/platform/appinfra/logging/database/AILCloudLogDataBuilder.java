package com.philips.platform.appinfra.logging.database;

import android.util.Log;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.logging.LoggingConfiguration;
import com.philips.platform.appinfra.logging.LoggingUtils;

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
        this.loggingConfiguration=loggingConfiguration;
    }

    public AILCloudLogData buildCloudLogModel(LogRecord logRecord) {
        AILCloudLogData ailCloudLogData = new AILCloudLogData();
        ailCloudLogData.logId = LoggingUtils.getUUID();
        ailCloudLogData.severity = LoggingUtils.getAILLogLevel(logRecord.getLevel().toString());
        ailCloudLogData.eventId = logRecord.getMessage();
        if (logRecord.getParameters() != null) {
            List<Object> parameters = Arrays.asList(logRecord.getParameters());
            for (Object object : parameters) {
                if (object instanceof String) {
                    ailCloudLogData.logDescription = (String) object;
                }
            }
        }
            ailCloudLogData.homecountry = appInfra.getHomeCountry();
            ailCloudLogData.locale =appInfra.getLocale();
        if (appInfra.getTime() != null && appInfra.getTime().getUTCTime() != null) {
            ailCloudLogData.logTime = appInfra.getTime().getUTCTime().getTime();
        }
        if (appInfra.getRestClient() != null && appInfra.getRestClient().getNetworkReachabilityStatus() != null) {
            ailCloudLogData.networktype = appInfra.getRestClient().getNetworkReachabilityStatus().toString();
        }
        ailCloudLogData.localtime = System.currentTimeMillis();
        if(loggingConfiguration.getComponentID()!=null && loggingConfiguration.getComponentVersion()!=null) {
            ailCloudLogData.component = loggingConfiguration.getComponentID() + "/" + loggingConfiguration.getComponentVersion();
        }
        Log.d("test", "After adding heavy params:" + System.currentTimeMillis());
        return ailCloudLogData;
    }
}
