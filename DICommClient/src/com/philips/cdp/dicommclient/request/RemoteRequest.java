package com.philips.cdp.dicommclient.request;

import java.util.Map;

import com.philips.cdp.dicomm.cpp.CPPController;
import com.philips.cdp.dicomm.cpp.DCSResponseListener;
import com.philips.cdp.dicomm.cpp.PublishEventListener;
import com.philips.cdp.dicomm.util.ALog;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.icpinterface.data.Errors;

public class RemoteRequest extends Request implements DCSResponseListener, PublishEventListener {

	private static final int CPP_DEVICE_CONTROL_TIMEOUT = 30000;
	private static String BASEDATA_PORTS = "{ \"product\":\"%d\",\"port\":\"%s\",\"data\":%s}";
	private static final String DICOMM_REQUEST = "DICOMM-REQUEST" ;
	private static int REQUEST_PRIORITY = 20;
	private static int REQUEST_TTL = 5;

	private String mEventData ;
	private String mResponse ;
	private int mMessageId ;
	private String mConversationId;
	private String mPortName;
	private int mProductId;

	private CPPController mCppController ;
	private final RemoteRequestType mRequestType;

	public RemoteRequest(NetworkNode networkNode, String portName, int productId, RemoteRequestType requestType,Map<String,Object> dataMap,ResponseHandler responseHandler) {
		super(networkNode, dataMap, responseHandler);
	    mCppController = CPPController.getInstance();
		mRequestType = requestType;
		mPortName = portName;
		mProductId = productId;
	}

	private String createDataToSend(NetworkNode networkNode, String portName, int productId, Map<String,Object> dataMap){
		String data = convertKeyValuesToJson(dataMap);
		String dataToSend = String.format(BASEDATA_PORTS, productId, portName, data);

		ALog.i(ALog.REMOTEREQUEST, "Data to send: "+ dataToSend);
		return dataToSend;
	}

	@Override
	public Response execute() {
		ALog.d(ALog.REMOTEREQUEST, "Start request REMOTE");
		//TODO - Add publish event listener for handling error cases
		mCppController.addDCSResponseListener(this) ;
		mCppController.addPublishEventListener(this) ;

		mEventData = createDataToSend(mNetworkNode, mPortName, mProductId, mDataMap);
		mMessageId = mCppController.publishEvent(mEventData,DICOMM_REQUEST, mRequestType.getMethod(),
				"", REQUEST_PRIORITY, REQUEST_TTL, mNetworkNode.getCppId()) ;
		try {
			long startTime = System.currentTimeMillis();
			synchronized (this) {
				wait(CPP_DEVICE_CONTROL_TIMEOUT) ;
			}
			if ((System.currentTimeMillis() - startTime) > CPP_DEVICE_CONTROL_TIMEOUT) {
				ALog.e(ALog.REMOTEREQUEST, "Timeout occured");
			}
		} catch (InterruptedException e) {
			// NOP
		}

		mCppController.removePublishEventListener(this);
		mCppController.removeDCSResponseListener(this);

		if (mResponse == null) {
			ALog.e(ALog.REMOTEREQUEST, "Request failed - null reponse, failed to publish event or request timeout");
			ALog.d(ALog.REMOTEREQUEST, "Stop request REMOTE - Failure");
			return new Response(null, Error.REQUESTFAILED, mResponseHandler) ;
		}

		ALog.i(ALog.REMOTEREQUEST, "Received data: " + mResponse);
		ALog.d(ALog.REMOTEREQUEST, "Stop request REMOTE - Success");
		return new Response(mResponse, null, mResponseHandler) ;
	}

	@Override
	public void onDCSResponseReceived(String dcsResponse, String conversationId) {
		if(mConversationId!=null && mConversationId.equals(conversationId)){
			ALog.i(ALog.REMOTEREQUEST,"DCSEvent received from the right request");
			mResponse = dcsResponse ;
			synchronized (this) {
				ALog.i(ALog.REMOTEREQUEST, "Notified on DCS Response") ;
				notify() ;
			}
		}else{
			ALog.i(ALog.REMOTEREQUEST,"DCSEvent received from different request - ignoring");
		}


	}

	@Override
	public void onPublishEventReceived(int status, int messageId, String conversationId) {
		if( mMessageId == messageId) {
			ALog.i(ALog.REMOTEREQUEST,"Publish event received from the right request - status: " + status);
			if ( status == Errors.SUCCESS){
				mConversationId = conversationId;
			}else {
				synchronized (this) {
					ALog.e(ALog.REMOTEREQUEST, "Publish Event Failed") ;
					notify() ;
				}
			}
		} else {
			ALog.i(ALog.REMOTEREQUEST,"Publish event received from different request - ignoring");
		}
	}

}
