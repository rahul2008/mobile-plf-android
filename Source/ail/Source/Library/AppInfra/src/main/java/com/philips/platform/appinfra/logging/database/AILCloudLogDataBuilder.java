package com.philips.platform.appinfra.logging.database;

import android.util.Log;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.logging.LoggingUtils;

import java.util.Arrays;
import java.util.List;
import java.util.logging.LogRecord;

/**
 * Created by abhishek on 4/26/18.
 */

public class AILCloudLogDataBuilder {
    private AppInfra appInfra;

    private String homeCountry=null;

    private String locale=null;

    public AILCloudLogDataBuilder(AppInfra appInfra) {
        this.appInfra = appInfra;
    }

    public AILCloudLogData buildCloudLogModel(LogRecord logRecord) {
        AILCloudLogData ailCloudLogData = new AILCloudLogData();
        ailCloudLogData.id = LoggingUtils.getUUID();
        ailCloudLogData.severity = LoggingUtils.getAILLogLevel(logRecord.getLevel().toString());
        ailCloudLogData.eventId = logRecord.getMessage();
        if (logRecord.getParameters() != null) {
            List<Object> parameters = Arrays.asList(logRecord.getParameters());
            for (Object object : parameters) {
                if (object instanceof String) {
                    String message = (String) object;
                    ailCloudLogData.logDescription = message;
                }
            }
        }
        Log.d("test", "Before adding heavy params:" + System.currentTimeMillis());
        if (appInfra.getServiceDiscovery() != null) {
            if(homeCountry==null){
                homeCountry=appInfra.getServiceDiscovery().getHomeCountry();
            }
            ailCloudLogData.homecountry = homeCountry;
        }
        if (appInfra.getInternationalization() != null) {
            if(locale==null){
                locale= appInfra.getInternationalization().getUILocaleString();
            }
            ailCloudLogData.locale =locale;
        }
        if (appInfra.getTime() != null && appInfra.getTime().getUTCTime() != null) {
            ailCloudLogData.logTime = appInfra.getTime().getUTCTime().getTime();
        }
        if (appInfra.getRestClient() != null && appInfra.getRestClient().getNetworkReachabilityStatus() != null) {
            ailCloudLogData.networktype = appInfra.getRestClient().getNetworkReachabilityStatus().toString();
        }
        ailCloudLogData.localtime = System.currentTimeMillis();
        Log.d("test", "After adding heavy params:" + System.currentTimeMillis());
        return ailCloudLogData;
    }
}
