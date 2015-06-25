package com.philips.cdp.dicommclient.port;


interface DICommListPortChangedListener {

	public void onListEntryPortAdded(DICommListEntryPort<?> listEntryPort);
	public void onListEntryPortRemoved(DICommListEntryPort<?> listEntryPort);

}
