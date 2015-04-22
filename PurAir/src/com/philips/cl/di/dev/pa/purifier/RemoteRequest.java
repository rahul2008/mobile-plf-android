package com.philips.cl.di.dev.pa.purifier;

import java.util.Map;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.cpp.CPPController;
import com.philips.cl.di.dev.pa.cpp.DCSResponseListener;
import com.philips.cl.di.dev.pa.cpp.PublishEventListener;
import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dicomm.communication.Error;
import com.philips.cl.di.dicomm.communication.RemoteRequestType;
import com.philips.cl.di.dicomm.communication.Request;
import com.philips.cl.di.dicomm.communication.Response;
import com.philips.cl.di.dicomm.communication.ResponseHandler;
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

	private CPPController mCppController ;
	private final RemoteRequestType mRequestType;
	
	public RemoteRequest(NetworkNode networkNode, String portName, int productId, RemoteRequestType requestType,Map<String,Object> dataMap,ResponseHandler responseHandler) {
		super(networkNode, dataMap, responseHandler);
	    mCppController = CPPController.getInstance(PurAirApplication.getAppContext());
		mEventData = createDataToSend(networkNode,portName,productId,dataMap);
		mRequestType = requestType;
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
		mMessageId = mCppController.publishEvent(mEventData,DICOMM_REQUEST, mRequestType.getMethod(),
				"", REQUEST_PRIORITY, REQUEST_TTL, mNetworkNode.getCppId()) ;
		try {
			ALog.i(ALog.REMOTEREQUEST, "wait for "+ CPP_DEVICE_CONTROL_TIMEOUT/1000 + "seconds") ;
			synchronized (this) {
				wait(CPP_DEVICE_CONTROL_TIMEOUT) ;
			}
			ALog.e(ALog.REMOTEREQUEST, "Timeout occured");
		} catch (InterruptedException e) {
			// NOP
		}
		ALog.d(ALog.REMOTEREQUEST, "Stop request REMOTE");

		mCppController.removePublishEventListener(this);
		mCppController.removeDCSResponseListener(this);

		if (mResponse == null) {
			ALog.e(ALog.REMOTEREQUEST, "Request failed - null reponse, failed to publish event or request timeout");
			return new Response(null, Error.REQUESTFAILED, mResponseHandler) ;
		}
		return new Response(mResponse, null, mResponseHandler) ;
	}

	@Override
	public void onDCSResponseReceived(String dcsResponse, String conversationId) {
		if(mConversationId!=null && mConversationId.equals(conversationId)){
			ALog.i(ALog.REMOTEREQUEST,"DCSEvent received from the right request");
			mResponse = dcsResponse ;
			synchronized (this) {
				ALog.i(ALog.REMOTEREQUEST, "Notified on DCS Response") ;
				ALog.d(ALog.REMOTEREQUEST, "Stop request REMOTE - dcsevent");
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
