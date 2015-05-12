package com.philips.cl.di.dicomm.port;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cl.di.dicomm.communication.CommunicationStrategy;

public abstract class DICommListEntryPort<T> extends DICommPort<T> {

	private String mParentPortName;
	private int mParentPortProductId;
	private String mIdentifier;

	public static final String BASE_ENTRY_PORT_NAME = "%s/%s";

	public DICommListEntryPort(NetworkNode networkNode,
			CommunicationStrategy communicationStrategy, String parentPortName, int parentPortProductId, String identifier) {
		super(networkNode, communicationStrategy);
		mParentPortName = parentPortName;
		mParentPortProductId = parentPortProductId;
		mIdentifier = identifier;
	}

	@Override
	protected final String getDICommPortName(){
		return String.format(BASE_ENTRY_PORT_NAME, mParentPortName, mIdentifier);
	}

	@Override
	protected final int getDICommProductId() {
		return mParentPortProductId;
	}
}
