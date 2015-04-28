package com.philips.cl.di.dicomm.port;

public interface AddEntryPortListener {
	
	public void onAddEntryPortSuccess(DICommListEntryPort<?> listEntryPort);
	public void onAddEntryPortFailed();

}
