/*
 * © Koninklijke Philips N.V., 2015, 2016, 2017.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.port;

import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.core.port.PortProperties;

abstract class DICommListEntryPort<T extends PortProperties> extends DICommPort<T> {

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
