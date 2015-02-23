package com.philips.cl.di.dev.pa.purifier;

import java.util.Hashtable;

import com.philips.cl.di.dev.pa.newpurifier.ConnectionState;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.JSONBuilder;

public class RoutingStrategy {
	public static DeviceConnection getConnection(PurAirDevice purifier, Hashtable<String, String> airPortDetailsTable) {
		ALog.i("UIUX","State: "+purifier.getConnectionState()) ;
		String dataToSend = "" ;
		if( purifier.getConnectionState() == ConnectionState.CONNECTED_LOCALLY) {
			dataToSend = JSONBuilder.getAirporDICommBuilder(airPortDetailsTable, purifier.getNetworkNode()) ;
			return new LocalConnection(purifier, dataToSend) ;
		}
		else if( purifier.getConnectionState() == ConnectionState.CONNECTED_REMOTELY) {
			dataToSend = JSONBuilder.getAirPortPublishEventBuilder(airPortDetailsTable) ;
			return new RemoteConnection(purifier,dataToSend) ;
		}
		else {
			return null;
		}
	}
}
