package com.philips.cl.di.dicomm.port;

import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;

public abstract class DICommPort {
	
	protected final NetworkNode mNetworkNode;
	
	public DICommPort(NetworkNode networkNode){
		mNetworkNode = networkNode;
	}
	
	public abstract boolean isResponseForThisPort(String response);
	
	public abstract void processResponse(String response);
	
	public abstract String getDICommPortName();
	
	public abstract int getDICommProductId();

}
