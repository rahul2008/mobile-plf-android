package com.philips.cl.di.dev.pa.scheduler;

import java.net.HttpURLConnection;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.constant.AppConstants.Port;
import com.philips.cl.di.dev.pa.cpp.CPPController;
import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;
import com.philips.cl.di.dev.pa.purifier.TaskPutDeviceDetails;
import com.philips.cl.di.dev.pa.scheduler.SchedulerConstants.SCHEDULE_TYPE;
import com.philips.cl.di.dev.pa.security.DISecurity;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.JSONBuilder;
import com.philips.cl.di.dev.pa.util.ServerResponseListener;
import com.philips.cl.di.dev.pa.util.Utils;
import com.philips.cl.di.dicomm.communication.Error;
import com.philips.cl.di.dicomm.communication.ResponseHandler;
import com.philips.cl.di.dicomm.communication.Error.PurifierEvent;

public class SchedulerHandler implements ServerResponseListener {
	public static final int DEFAULT_ERROR = 999;
	public static final int MAX_SCHEDULES_REACHED = 1;

	private ResponseHandler mListener;
	private NetworkNode mNetworkNode;

	public SchedulerHandler(ResponseHandler listener) {
		mListener = listener;
	}

	public void setScheduleDetails(String dataToSend, NetworkNode networkNode,
			SCHEDULE_TYPE scheduleType, int scheduleNumber) {
		this.mNetworkNode = networkNode;
		switch (networkNode.getConnectionState()) {
		case CONNECTED_LOCALLY:
			sendScheduleDetailsLocally(dataToSend, scheduleType, networkNode,	scheduleNumber);
			break;
		case CONNECTED_REMOTELY:
			sendScheduleDetailsViaCPP(dataToSend, scheduleType,	networkNode.getCppId(), scheduleNumber);
			break;
		case DISCONNECTED:
			break;
		}

	}

	private void sendScheduleDetailsLocally(String dataToSend,
			SCHEDULE_TYPE scheduleType, NetworkNode networkNode,
			int scheduleNumber) {
		String requestType = AppConstants.REQUEST_METHOD_GET;
		String url = Utils.getPortUrl(Port.SCHEDULES, networkNode.getIpAddress());
		switch (scheduleType) {
		case ADD:
			requestType = AppConstants.REQUEST_METHOD_POST;
			break;
		case DELETE:
			requestType = AppConstants.REQUEST_METHOD_DELETE;
			url = Utils.getScheduleDetailsUrl(networkNode.getIpAddress(),
					scheduleNumber);
			break;
		case GET_SCHEDULE_DETAILS:
			url = Utils.getScheduleDetailsUrl(networkNode.getIpAddress(),
					scheduleNumber);
			break;
		case EDIT:
			requestType = AppConstants.REQUEST_METHOD_PUT;
			url = Utils.getScheduleDetailsUrl(networkNode.getIpAddress(), scheduleNumber);
			break;
		default:
			break;
		}
		TaskPutDeviceDetails addSchedulerTask = new TaskPutDeviceDetails(
				new DISecurity(null).encryptData(dataToSend, networkNode), url, this, requestType);
		Thread addSchedulerThread = new Thread(addSchedulerTask);
		addSchedulerThread.start();
	}

	private void sendScheduleDetailsViaCPP(String dataToSend,
			SCHEDULE_TYPE scheduleType, String purifierEUI64, int scheduleNumber) {
		switch (scheduleType) {
		case GET:
			CPPController.getInstance(PurAirApplication.getAppContext())
					.publishEvent(
							JSONBuilder.getPublishEventBuilderForScheduler("",
									"{}"), AppConstants.DI_COMM_REQUEST,
							AppConstants.GET_PROPS,
							"", 20, 120,
							purifierEUI64);
			break;
		case ADD:
			CPPController
					.getInstance(PurAirApplication.getAppContext())
					.publishEvent(
							JSONBuilder
									.getPublishEventBuilderForAddScheduler(dataToSend),
							AppConstants.DI_COMM_REQUEST,
							AppConstants.ADD_PROPS,
							"", 20, 120,
							purifierEUI64);
			break;
		case DELETE:
			CPPController
					.getInstance(PurAirApplication.getAppContext())
					.publishEvent(
							JSONBuilder
									.getPublishEventBuilderForDeleteScheduler(scheduleNumber),
							AppConstants.DI_COMM_REQUEST,
							AppConstants.DEL_PROPS,
							"", 20, 120,
							purifierEUI64);
			break;
		case GET_SCHEDULE_DETAILS:
			CPPController
					.getInstance(PurAirApplication.getAppContext())
					.publishEvent(
							JSONBuilder
									.getPublishEventBuilderForGetSchedulerDetails(scheduleNumber),
							AppConstants.DI_COMM_REQUEST,
							AppConstants.GET_PROPS,
							"", 20, 120,
							purifierEUI64);
			break;
		case EDIT:
			CPPController.getInstance(PurAirApplication.getAppContext())
					.publishEvent(
							JSONBuilder.getPublishEventBuilderForEditScheduler(
									dataToSend, scheduleNumber),
							AppConstants.DI_COMM_REQUEST,
							AppConstants.PUT_PROPS,
							"", 20, 120,
							purifierEUI64);
			break;
		default:
			break;
		}
	}

	@Override
	public void receiveServerResponse(int responseCode, String responseData,
			String fromIp) {
		notifyListener(responseCode, responseData);
	}
	
	@Override
	public void receiveServerResponse(int responseCode, String responseData, String type, String areaId) {/**NOP*/}

	private void notifyListener(int responseCode, String data) {
		ALog.i(ALog.SCHEDULER, "Response Code: " + responseCode);
		if (mListener == null)	return;
		
		if (responseCode == HttpURLConnection.HTTP_OK) {
			DISecurity diSecurity = new DISecurity(null);
			String decryptedData = diSecurity.decryptData(data, mNetworkNode) ;
			if (decryptedData == null ) {
				ALog.d(ALog.SCHEDULER, "Unable to decrypt data for : " + mNetworkNode.getIpAddress());
				return;
			}			
			mListener.onSuccess(decryptedData);
		} else if (responseCode == HttpURLConnection.HTTP_INTERNAL_ERROR) {
			//String encryptedData = new DISecurity(null).encryptData(data, mNetworkNode);
			//TODO: DICOMM Refactor, check if decryption is required			
			mListener.onSuccess(data);
		} else {
			Error error = new Error(PurifierEvent.SCHEDULER.ordinal(),"");
			mListener.onError(error) ; 
		}
	}
}
