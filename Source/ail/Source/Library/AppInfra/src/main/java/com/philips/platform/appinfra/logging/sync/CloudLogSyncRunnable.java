package com.philips.platform.appinfra.logging.sync;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.apisigning.HSDPLoggingV2ApiSigning;
import com.philips.platform.appinfra.logging.CloudLoggingConstants;
import com.philips.platform.appinfra.logging.LoggingUtils;
import com.philips.platform.appinfra.logging.database.AILCloudLogDBManager;
import com.philips.platform.appinfra.logging.database.AILCloudLogData;
import com.philips.platform.appinfra.logging.rest.CloudLogRequestBodyBuilder;
import com.philips.platform.appinfra.rest.ServiceIDUrlFormatting;
import com.philips.platform.appinfra.rest.request.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by abhishek on 5/14/18.
 */

public class CloudLogSyncRunnable implements Runnable {


    public static final int STATUS_CODE_CREATED = 201;
    public static final int STATUS_CODE_BAD_REQUEST = 400;
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
            //Log.d("SyncTesting", "About to sync records" + ailCloudLogDataList.size());
            //2. Build rest api call template

            //3. Make rest api call
            JSONObject jsonBody = new CloudLogRequestBodyBuilder(appInfra, prouctKey).getCloudLogRequestBody(ailCloudLogDataList);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "appinfra.cloudLogging", ServiceIDUrlFormatting.SERVICEPREFERENCE.BYCOUNTRY, null, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    //Log.d("SyncTesting", "Inside onResponse");
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ailCloudLogDBManager.updateAILCloudLogList(ailCloudLogDataList, AILCloudLogDBManager.DBLogState.ERROR);
                            //Log.d("SyncTesting", "Inside onErrorResponse" + error.getMessage());
                        }
                    }).start();

                }
            }) {
                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {

                    if (response.statusCode == STATUS_CODE_CREATED || response.statusCode == STATUS_CODE_BAD_REQUEST) {
                        //4. Based on status delete data from db
                        ailCloudLogDBManager.deleteLogRecords(ailCloudLogDataList);
                        //Log.d("SyncTesting", "Deleted records" + ailCloudLogDataList.size());
                        return Response.success(new JSONObject(),
                                HttpHeaderParser.parseCacheHeaders(response));

                    } else {
                        ailCloudLogDBManager.updateAILCloudLogListToNewState(ailCloudLogDataList);
                        return Response.error(new VolleyError("Log request failed due to error::" + response.statusCode));
                    }
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return getCloudLoggingHeaders();
                }
            };
            appInfra.getRestClient().getRequestQueue().add(request);


        }
    }

    private Map<String, String> getCloudLoggingHeaders() {
        String signingDate = LoggingUtils.getCurrentDateAndTime(CloudLoggingConstants.CLOUD_LOGGING_DATE_TIME_FORMAT, appInfra);
        HSDPLoggingV2ApiSigning hsdpLoggingV2ApiSigning = new HSDPLoggingV2ApiSigning(secretKey);
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("api-version", "1");//int value?
        header.put("SignedDate", signingDate);
        header.put("hsdp-api-signature", "HmacSHA256;Credential:"
                + sharedKey + ";SignedHeaders:SignedDate;Signature:"
                + hsdpLoggingV2ApiSigning.createSignatureForCloudLogging(signingDate));
        return header;
    }

}
