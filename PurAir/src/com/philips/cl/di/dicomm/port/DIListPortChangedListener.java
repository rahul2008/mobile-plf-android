package com.philips.cl.di.dicomm.port;

public interface DIListPortChangedListener {

	public ListenerRegistration onListEntryPortAdded(DICommListEntryPort<?> listEntryPort);
	public ListenerRegistration onListEntryPortRemoved(DICommListEntryPort<?> listEntryPort);

}
