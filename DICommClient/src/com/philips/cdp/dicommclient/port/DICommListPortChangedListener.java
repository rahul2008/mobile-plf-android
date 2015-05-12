package com.philips.cdp.dicommclient.port;

import com.philips.cdp.dicomm.util.ListenerRegistration;

public interface DICommListPortChangedListener {

	public ListenerRegistration onListEntryPortAdded(DICommListEntryPort<?> listEntryPort);
	public ListenerRegistration onListEntryPortRemoved(DICommListEntryPort<?> listEntryPort);

}
