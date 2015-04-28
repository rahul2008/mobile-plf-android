package com.philips.cl.di.dicomm.port;

import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;
import com.philips.cl.di.dicomm.communication.CommunicationStrategy;

public abstract class DICommListEntryPort<T> extends DICommPort<T> {
	
	private String mParentPortName;
	private String mIdentifier;
	
	public static final String BASE_ENTRY_PORT_NAME = "%s/%s";

	public DICommListEntryPort(NetworkNode networkNode,
			CommunicationStrategy communicationStrategy, String parentPortName, String identifier) {
		super(networkNode, communicationStrategy);
		mParentPortName = parentPortName;
		mIdentifier = identifier;
	}
	
	protected final String getDICommPortName(){
		return String.format(BASE_ENTRY_PORT_NAME, mParentPortName, mIdentifier);
	}
	

}
