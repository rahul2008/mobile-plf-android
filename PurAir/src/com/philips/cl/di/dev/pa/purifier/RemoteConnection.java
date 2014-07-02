package com.philips.cl.di.dev.pa.purifier;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.cpp.CPPController;
import com.philips.cl.di.dev.pa.cpp.DCSResponseListener;
import com.philips.cl.di.dev.pa.datamodel.SessionDto;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;
import com.philips.cl.di.dev.pa.util.ALog;

public class RemoteConnection extends RoutingStrategy implements DCSResponseListener {
	private PurAirDevice purifier ;
	private String eventData ;
	
	private String response ;
	
	public RemoteConnection(PurAirDevice purifier, String eventData) {
		this.purifier = purifier ;
		this.eventData = eventData ;
	}
	@Override
	public String setPurifierDetails() {
		// Send the CPP publish event from here
		CPPController.getInstance(PurAirApplication.getAppContext()).setDCSResponseListener(this) ;
		CPPController.getInstance(PurAirApplication.getAppContext()).publishEvent(eventData,AppConstants.DI_COMM_REQUEST, AppConstants.PUT_PROPS, SessionDto.getInstance().getAppEui64(), "", 20, 120, purifier.getEui64()) ;
		// wait here for 30secs
		try {
			ALog.i(ALog.DEVICEHANDLER, "wait for 30 secs") ;
			synchronized (this) {
				wait(30000) ;
			}			
		} catch (InterruptedException e) {
			ALog.e(ALog.DEVICEHANDLER, "interupted exception") ;
		}
		ALog.i(ALog.DEVICEHANDLER, "Wait over") ;
		return response ;
	}
	
	@Override
	public void onDCSResponseReceived(String dcsResponse) {
		// TODO Auto-generated method stub
		response = dcsResponse ;
		synchronized (this) {
			ALog.i(ALog.DEVICEHANDLER, "Notified") ;
			notify() ;
		}		
	}

}
