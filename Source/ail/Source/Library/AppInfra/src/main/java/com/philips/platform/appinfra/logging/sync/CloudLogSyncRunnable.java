package com.philips.platform.appinfra.logging.sync;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.apisigning.HSDPPHSApiSigning;
import com.philips.platform.appinfra.logging.CloudLoggingConstants;
import com.philips.platform.appinfra.logging.LoggingUtils;
import com.philips.platform.appinfra.logging.database.AILCloudLogDBManager;
import com.philips.platform.appinfra.logging.database.AILCloudLogData;
import com.philips.platform.appinfra.logging.rest.CloudLogRequestBodyBuilder;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by abhishek on 5/14/18.
 */

public class CloudLogSyncRunnable implements Runnable {


    private String prouctKey;
    private AILCloudLogDBManager ailCloudLogDBManager;

    private AppInfra appInfra;
    private String sharedKey;
    private String secretKey;

    public CloudLogSyncRunnable(AppInfra appInfra, String sharedKey, String secretKey, String productKey) {
        this.secretKey = secretKey;
        this.sharedKey = sharedKey;
        this.prouctKey = productKey;
        this.appInfra = appInfra;
        ailCloudLogDBManager = AILCloudLogDBManager.getInstance(appInfra);
    }

    @Override
    public void run() {
        //Check whether to start sync or not
        //1. Fetch oldest data from DB
        final List<AILCloudLogData> ailCloudLogDataList = ailCloudLogDBManager.getNewAILCloudLogRecords();
        if (ailCloudLogDataList != null && ailCloudLogDataList.size() > 0) {
            Log.v("SyncTesting", "About to sync records" + ailCloudLogDataList.size());
            //2. Build rest api call template

            //3. Make rest api call
            JSONObject jsonBody = new CloudLogRequestBodyBuilder(appInfra, prouctKey).getCloudLogRequestBody(ailCloudLogDataList);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            String cloudLoggingURL = "https://logingestor2-int.us-east.philips-healthsuite.com/core/log/LogEvent";
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, cloudLoggingURL, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    if (response.statusCode == 201) {
                        //4. Based on status delete data from db
                        ailCloudLogDBManager.deleteLogRecords(ailCloudLogDataList);
                        Log.v("SyncTesting", "Deleted records" + ailCloudLogDataList.size());
                    } else {
                        ailCloudLogDBManager.updateAILCloudLogListToNewState(ailCloudLogDataList);
                    }
                    return null;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return getCloudLoggingtHeaders();
                }
            };
            appInfra.getRestClient().getRequestQueue().add(request);


        }
    }

    public Map<String, String> getCloudLoggingtHeaders() {
        String signingDate = LoggingUtils.getCurrentDateAndTime(CloudLoggingConstants.CLOUD_LOGGING_DATE_TIME_FORMAT);
        HSDPPHSApiSigning hsdpphsApiSigning = new HSDPPHSApiSigning(sharedKey,secretKey);
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("api-version", "1");//int value?
        header.put("SignedDate", signingDate);
        header.put("hsdp-api-signature", "HmacSHA256;Credential:"
                + sharedKey + ";SignedHeaders:SignedDate;Signature:"
                + hsdpphsApiSigning.createSignatureForCloudLogging(signingDate));
        return header;
    }

    private String getUTCTime() {
        return new DateTime().toDateTime(DateTimeZone.UTC).toString();//format time
    }
}
