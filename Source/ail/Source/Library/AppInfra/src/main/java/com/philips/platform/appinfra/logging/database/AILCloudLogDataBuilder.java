package com.philips.platform.appinfra.logging.database;

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

    public AILCloudLogDataBuilder(AppInfra appInfra) {
        this.appInfra = appInfra;
    }

    public AILCloudLogData buildCloudLogModel(LogRecord logRecord){
        AILCloudLogData ailCloudLogData=new AILCloudLogData();
        ailCloudLogData.id=LoggingUtils.getUUID();
        ailCloudLogData.severity= LoggingUtils.getAILLogLevel(logRecord.getLevel().toString());
        ailCloudLogData.eventId=logRecord.getMessage();
        if(logRecord.getParameters()!=null) {
            List<Object> parameters = Arrays.asList(logRecord.getParameters());
            for (Object object : parameters) {
                if (object instanceof String) {
                    String message = (String) object;
                    ailCloudLogData.logDescription = message;
                }
            }
        }
        return ailCloudLogData;
    }
}
