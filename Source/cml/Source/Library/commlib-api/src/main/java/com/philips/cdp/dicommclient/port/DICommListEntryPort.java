/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.port;

import android.support.annotation.NonNull;

import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.core.port.PortProperties;

abstract class DICommListEntryPort<T extends PortProperties> extends DICommPort<T> {

	private String mParentPortName;
	private int mParentPortProductId;
	private String mIdentifier;

	public static final String BASE_ENTRY_PORT_NAME = "%s/%s";

	public DICommListEntryPort(final @NonNull CommunicationStrategy communicationStrategy, String parentPortName, int parentPortProductId, String identifier) {
		super(communicationStrategy);
		mParentPortName = parentPortName;
		mParentPortProductId = parentPortProductId;
		mIdentifier = identifier;
	}

	@Override
	public final String getDICommPortName(){
		return String.format(BASE_ENTRY_PORT_NAME, mParentPortName, mIdentifier);
	}

	@Override
	protected final int getDICommProductId() {
		return mParentPortProductId;
	}
}
