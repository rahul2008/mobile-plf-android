package com.philips.cl.di.dev.pa.purifier;

import java.util.Hashtable;

import com.philips.cl.di.dev.pa.constant.AppConstants.Port;
import com.philips.cl.di.dev.pa.newpurifier.ConnectionState;
import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.JSONBuilder;
import com.philips.cl.di.dicomm.communication.Error;
import com.philips.cl.di.dicomm.communication.Request;
import com.philips.cl.di.dicomm.communication.RequestType;
import com.philips.cl.di.dicomm.communication.ResponseHandler;

public class RoutingStrategy {
	public static Request getConnection(NetworkNode networkNode, Hashtable<String, String> airPortDetailsTable) {
		ALog.i("UIUX","State: "+networkNode.getConnectionState()) ;
		String dataToSend = "" ;
		if( networkNode.getConnectionState() == ConnectionState.CONNECTED_LOCALLY) {
			dataToSend = JSONBuilder.getAirporDICommBuilder(airPortDetailsTable, networkNode) ;
			return new LocalRequest(networkNode, Port.AIR.urlPart,Port.AIR.port,RequestType.PUT,airPortDetailsTable, new ResponseHandler() {
				
				@Override
				public void onSuccess(String data) {
					// TODO DICOMM Refactor, remove using request architecture is added
					
				}
				
				@Override
				public void onError(Error error) {
					// TODO DICOMM Refactor, remove using request architecture is added
					
				}
			}) ;
		}
		else if( networkNode.getConnectionState() == ConnectionState.CONNECTED_REMOTELY) {
			dataToSend = JSONBuilder.getAirPortPublishEventBuilder(airPortDetailsTable) ;
			return new RemoteRequest(networkNode,dataToSend) ;
		}
		else {
			return null;
		}
	}
}
