/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.request;

import android.util.Log;

import com.philips.cdp.dicommclient.cpp.CppController;
import com.philips.cdp.dicommclient.cpp.listener.DcsResponseListener;
import com.philips.cdp.dicommclient.cpp.listener.PublishEventListener;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.icpinterface.data.Errors;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class RemoteRequest extends Request implements DcsResponseListener, PublishEventListener {

    private static final String TAG = "RemoteRequest";

    private static final int CPP_DEVICE_CONTROL_TIMEOUT = 30000;
    private static String BASEDATA_PORTS = "{ \"product\":\"%d\",\"port\":\"%s\",\"data\":%s}";
    private static final String DICOMM_REQUEST = "DICOMM-REQUEST";
    private static int REQUEST_PRIORITY = 20;
    private static int REQUEST_TTL = 5;
    private final String cppId;

    private String mEventData;
    private String mResponse;
    private int mMessageId;
    private String mConversationId;
    private String mPortName;
    private int mProductId;

    private CppController mCppController;
    private final RemoteRequestType mRequestType;

    public RemoteRequest(String cppId, String portName, int productId, RemoteRequestType requestType, Map<String, Object> dataMap, ResponseHandler responseHandler) {
        super(dataMap, responseHandler);
        this.cppId = cppId;
        mCppController = CppController.getInstance();
        mRequestType = requestType;
        mPortName = portName;
        mProductId = productId;
    }

    private String createDataToSend(String portName, int productId, Map<String, Object> dataMap) {
        String data = Request.convertKeyValuesToJson(dataMap);
        String dataToSend = String.format(BASEDATA_PORTS, productId, portName, data);

        DICommLog.i(DICommLog.REMOTEREQUEST, "Data to send: " + dataToSend);
        return dataToSend;
    }

    @Override
    public Response execute() {
        DICommLog.d(DICommLog.REMOTEREQUEST, "Start request REMOTE");
        mCppController.addDCSResponseListener(this);
        mCppController.addPublishEventListener(this);

        mEventData = createDataToSend(mPortName, mProductId, mDataMap);
        mMessageId = mCppController.publishEvent(mEventData, DICOMM_REQUEST, mRequestType.getMethod(),
                "", REQUEST_PRIORITY, REQUEST_TTL, cppId);
        try {
            long startTime = System.currentTimeMillis();
            synchronized (this) {
                wait(CPP_DEVICE_CONTROL_TIMEOUT);
            }
            if ((System.currentTimeMillis() - startTime) > CPP_DEVICE_CONTROL_TIMEOUT) {
                DICommLog.e(DICommLog.REMOTEREQUEST, "Timeout occured");
            }
        } catch (InterruptedException e) {
            // NOP
        }

        mCppController.removePublishEventListener(this);
        mCppController.removeDCSResponseListener(this);

        if (mResponse == null) {
            DICommLog.e(DICommLog.REMOTEREQUEST, "Request failed - null reponse, failed to publish event or request timeout");
            DICommLog.d(DICommLog.REMOTEREQUEST, "Stop request REMOTE - Failure");
            return new Response(null, Error.REQUESTFAILED, mResponseHandler);
        }

        DICommLog.i(DICommLog.REMOTEREQUEST, "Received data: " + mResponse);
        DICommLog.d(DICommLog.REMOTEREQUEST, "Stop request REMOTE - Success");

        mResponse = extractData(mResponse);

        return new Response(mResponse, null, mResponseHandler);
    }

    @Override
    public void onDCSResponseReceived(String dcsResponse, String conversationId) {
        if (mConversationId != null && mConversationId.equals(conversationId)) {
            DICommLog.i(DICommLog.REMOTEREQUEST, "DCSEvent received from the right request");
            mResponse = dcsResponse;
            synchronized (this) {
                DICommLog.i(DICommLog.REMOTEREQUEST, "Notified on DCS Response");
                notify();
            }
        } else {
            DICommLog.i(DICommLog.REMOTEREQUEST, "DCSEvent received from different request - ignoring");
        }
    }

    @Override
    public void onPublishEventReceived(int status, int messageId, String conversationId) {
        if (mMessageId == messageId) {
            DICommLog.i(DICommLog.REMOTEREQUEST, "Publish event received from the right request - status: " + status);
            if (status == Errors.SUCCESS) {
                mConversationId = conversationId;
            } else {
                synchronized (this) {
                    DICommLog.e(DICommLog.REMOTEREQUEST, "Publish Event Failed");
                    notify();
                }
            }
        } else {
            DICommLog.i(DICommLog.REMOTEREQUEST, "Publish event received from different request - ignoring");
        }
    }

    private String extractData(final String data) {
        String res = data;

        try {
            JSONObject jsonObject = new JSONObject(data);
            int status = jsonObject.getInt("status");
            JSONObject dataObject = jsonObject.optJSONObject("data");

            if (status > 0) {
                Log.e(TAG, "extractData: code received: " + status + "");
            } else if (dataObject == null) {
                Log.e(TAG, "extractData: no data received: " + data + "");
            } else {
                res = dataObject.toString();
            }
        } catch (JSONException e) {
            DICommLog.i(DICommLog.REMOTEREQUEST, "JSONException: " + e.getMessage());
        }

        return res;
    }
}
