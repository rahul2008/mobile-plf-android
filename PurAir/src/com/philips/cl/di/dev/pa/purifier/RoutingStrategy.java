package com.philips.cl.di.dev.pa.purifier;

import java.util.Hashtable;

import com.philips.cl.di.dev.pa.constant.AppConstants.Port;
import com.philips.cl.di.dev.pa.newpurifier.ConnectionState;
import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;
import com.philips.cl.di.dev.pa.security.DISecurity;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dicomm.communication.Error;
import com.philips.cl.di.dicomm.communication.LocalRequestType;
import com.philips.cl.di.dicomm.communication.RemoteRequestType;
import com.philips.cl.di.dicomm.communication.Request;
import com.philips.cl.di.dicomm.communication.ResponseHandler;

public class RoutingStrategy {
	public static Request getConnection(NetworkNode networkNode, Hashtable<String, String> airPortDetailsTable) {
		ALog.i("UIUX","State: "+networkNode.getConnectionState()) ;
		if( networkNode.getConnectionState() == ConnectionState.CONNECTED_LOCALLY) {
			// TODO: DICOMM Refactor, generalise to all ports 
			// TODO: DICOMM Refactor, use DISecurity from AirPurifier
			return new LocalRequest(networkNode, Port.AIR.urlPart,Port.AIR.port,LocalRequestType.PUT,airPortDetailsTable, new ResponseHandler() {
				
				@Override
				public void onSuccess(String data) {
					// TODO DICOMM Refactor, remove using request architecture is added
					
				}
				
				@Override
				public void onError(Error error) {
					// TODO DICOMM Refactor, remove using request architecture is added
					
				}
			}, new DISecurity(null)) ;
		}
		else if( networkNode.getConnectionState() == ConnectionState.CONNECTED_REMOTELY) {
			return new RemoteRequest(networkNode,Port.AIR.urlPart,Port.AIR.port,RemoteRequestType.PUT_PROPS,airPortDetailsTable, new ResponseHandler() {
				
				@Override
				public void onSuccess(String data) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onError(Error error) {
					// TODO Auto-generated method stub
					
				}
			}) ;
		}
		else {
			return null;
		}
	}
}
