package com.philips.platform.appinfra.logging.rest;


import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.logging.AppInfraLogging;
import com.philips.platform.appinfra.logging.database.AILCloudLogData;
import com.philips.platform.appinfra.logging.model.AILCloudLogMetaData;
import com.philips.platform.appinfra.logging.rest.model.CloudLogs;
import com.philips.platform.appinfra.logging.rest.model.Resource;

import junit.framework.TestCase;

import org.json.JSONObject;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.mockito.Mockito.when;

/**
 * Created by Yogesh on 5/24/18.
 */
public class CloudLogRequestBodyBuilderTest extends TestCase {

    private CloudLogRequestBodyBuilder cloudLogRequestBodyBuilder;


    @Mock
    private AppInfra appInfra;

    @Mock
    private AppInfraLogging appInfraLogging;

    @Mock
    private AILCloudLogMetaData ailCloudLogMetaData;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        when(appInfra.getLogging()).thenReturn(appInfraLogging);
        when(appInfraLogging.getAilCloudLogMetaData()).thenReturn(ailCloudLogMetaData);
        cloudLogRequestBodyBuilder = new CloudLogRequestBodyBuilder(appInfra, "product_key");
    }

    public void testFormingCloudLogRequestBody() {
        when(ailCloudLogMetaData.getAppName()).thenReturn("AppInfra");
        AILCloudLogData ailCloudLogData = getAilCloudLogData();
        ArrayList<AILCloudLogData> ailCloudLogDataList = new ArrayList<>();
        ailCloudLogDataList.add(ailCloudLogData);
        JSONObject cloudLogRequestBody = cloudLogRequestBodyBuilder.getCloudLogRequestBody(ailCloudLogDataList);
        Gson gson = new Gson();
        CloudLogs cloudLogs = gson.fromJson(cloudLogRequestBody.toString(), CloudLogs.class);

        if (cloudLogs != null && cloudLogs.getEntry() != null) {
            Resource resource = cloudLogs.getEntry().get(0).getResource();
            assertEquals(resource.getApplicationName(), "AppInfra");
            assertEquals(resource.getApplicationVersion(), "version");
            assertEquals(resource.getEventId(), "eventId");
            assertEquals(resource.getServerName(), "serverName");
            assertEquals(resource.getOriginatingUser(), "userUUID");
        }


    }

    @NonNull
    private AILCloudLogData getAilCloudLogData() {
        AILCloudLogData ailCloudLogData = new AILCloudLogData();
        ailCloudLogData.status = "status";
        ailCloudLogData.appsId = "app_id";
        ailCloudLogData.appState = "appState";
        ailCloudLogData.appVersion = "version";
        ailCloudLogData.details = "details";
        ailCloudLogData.homecountry = "homeCountry";
        ailCloudLogData.component = "component";
        ailCloudLogData.userUUID = "userUUID";
        ailCloudLogData.logDescription = "description";
        ailCloudLogData.locale = "en";
        ailCloudLogData.localtime = 456789;
        ailCloudLogData.serverName = "serverName";
        ailCloudLogData.severity = "severity";
        ailCloudLogData.logId = "log_id";
        ailCloudLogData.eventId = "eventId";
        ailCloudLogData.networktype = "networkType";
        return ailCloudLogData;
    }
}