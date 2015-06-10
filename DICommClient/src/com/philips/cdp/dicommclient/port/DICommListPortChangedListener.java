package com.philips.cdp.dicommclient.port;

import com.philips.cdp.dicommclient.util.ListenerRegistration;

public interface DICommListPortChangedListener {

	public ListenerRegistration onListEntryPortAdded(DICommListEntryPort<?> listEntryPort);
	public ListenerRegistration onListEntryPortRemoved(DICommListEntryPort<?> listEntryPort);

}
