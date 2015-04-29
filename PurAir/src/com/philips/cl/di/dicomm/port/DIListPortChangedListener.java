package com.philips.cl.di.dicomm.port;

public interface DIListPortChangedListener {

	public DIRegistration onListEntryPortAdded(DICommListEntryPort<?> listEntryPort);
	public DIRegistration onListEntryPortRemoved(DICommListEntryPort<?> listEntryPort);

}
