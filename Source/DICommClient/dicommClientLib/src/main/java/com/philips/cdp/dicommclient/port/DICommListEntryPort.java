/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.port;

import com.philips.cdp.dicommclient.communication.CommunicationStrategy;
import com.philips.cdp.dicommclient.networknode.NetworkNode;

abstract class DICommListEntryPort<T> extends DICommPort<T> {

	private String mParentPortName;
	private int mParentPortProductId;
	private String mIdentifier;

	public static final String BASE_ENTRY_PORT_NAME = "%s/%s";

	public DICommListEntryPort(CommunicationStrategy communicationStrategy, String parentPortName, int parentPortProductId, String identifier) {
		super(communicationStrategy);
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
