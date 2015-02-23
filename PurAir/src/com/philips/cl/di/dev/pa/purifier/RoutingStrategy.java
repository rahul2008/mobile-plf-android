package com.philips.cl.di.dev.pa.purifier;

import java.util.Hashtable;

import com.philips.cl.di.dev.pa.newpurifier.ConnectionState;
import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.JSONBuilder;

public class RoutingStrategy {
	public static DeviceConnection getConnection(NetworkNode networkNode, Hashtable<String, String> airPortDetailsTable) {
		ALog.i("UIUX","State: "+networkNode.getConnectionState()) ;
		String dataToSend = "" ;
		if( networkNode.getConnectionState() == ConnectionState.CONNECTED_LOCALLY) {
			dataToSend = JSONBuilder.getAirporDICommBuilder(airPortDetailsTable, networkNode) ;
			return new LocalConnection(networkNode, dataToSend) ;
		}
		else if( networkNode.getConnectionState() == ConnectionState.CONNECTED_REMOTELY) {
			dataToSend = JSONBuilder.getAirPortPublishEventBuilder(airPortDetailsTable) ;
			return new RemoteConnection(networkNode,dataToSend) ;
		}
		else {
			return null;
		}
	}
}
