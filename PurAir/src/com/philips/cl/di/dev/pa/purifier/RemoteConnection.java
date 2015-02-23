package com.philips.cl.di.dev.pa.purifier;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.cpp.CPPController;
import com.philips.cl.di.dev.pa.cpp.DCSResponseListener;
import com.philips.cl.di.dev.pa.cpp.PublishEventListener;
import com.philips.cl.di.dev.pa.datamodel.AirPortInfo;
import com.philips.cl.di.dev.pa.datamodel.SessionDto;
import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.DataParser;
import com.philips.icpinterface.data.Errors;

public class RemoteConnection implements DeviceConnection, DCSResponseListener, PublishEventListener {
	private static final int CPP_DEVICE_CONTROL_TIMEOUT = 30000;
	private String eventData ;

	private String response ;
	private int messageId ;
	private CPPController cppController ;
	
	private String mCppId;

	public RemoteConnection(NetworkNode networkNode, String eventData) {
		ALog.d(ALog.DEVICEHANDLER, "Start request REMOTE");
		this.mCppId = networkNode.getCppId() ;
		this.eventData = eventData ;
		cppController = CPPController.getInstance(PurAirApplication.getAppContext());
	}

	@Override
	public String setPurifierDetails() {
		//TODO - Add publish event listener for handling error cases
		cppController.setDCSResponseListener(this) ;
		cppController.addPublishEventListener(this) ;
		messageId = cppController.publishEvent(eventData,AppConstants.DI_COMM_REQUEST, AppConstants.PUT_PROPS,
				SessionDto.getInstance().getAppEui64(), "", 20, 120, mCppId) ;
		try {
			ALog.i(ALog.DEVICEHANDLER, "wait for 30 secs") ;
			synchronized (this) {
				wait(CPP_DEVICE_CONTROL_TIMEOUT) ;
			}
		} catch (InterruptedException e) {
			ALog.e(ALog.DEVICEHANDLER, "interupted exception") ;
			ALog.d(ALog.DEVICEHANDLER, "Stop request REMOTE - timeout");
		}
		ALog.i(ALog.DEVICEHANDLER, "Wait over");
		return response ;
	}

	@Override
	public void onDCSResponseReceived(String dcsResponse) {
		//TODO - Check for Air Port Response
		AirPortInfo airPortInfo = DataParser.parseAirPurifierEventData(dcsResponse) ;
		if( airPortInfo == null ) {
			return ;
		}
		response = dcsResponse ;
		synchronized (this) {
			ALog.i(ALog.DEVICEHANDLER, "Notified on DCS Response") ;
			ALog.d(ALog.DEVICEHANDLER, "Stop request REMOTE - dcsevent");
			notify() ;
		}
	}

	@Override
	public void onPublishEventReceived(int status, int messageId) {
		ALog.i(ALog.DEVICEHANDLER,"Publish event message ID: " +messageId );
		if( this.messageId == messageId) {
			if( status != Errors.SUCCESS) {
				synchronized (this) {
					ALog.i(ALog.DEVICEHANDLER, "Notified on Publish Event Response") ;
					ALog.d(ALog.DEVICEHANDLER, "Stop request REMOTE - response");
					notify() ;
				}
			}
		}
	}

}
