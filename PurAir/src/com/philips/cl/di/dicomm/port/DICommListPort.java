package com.philips.cl.di.dicomm.port;

import java.util.HashMap;
import java.util.Map;

import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;
import com.philips.cl.di.dicomm.communication.CommunicationStrategy;

public abstract class DICommListPort<T extends DICommListEntryPort<?>> extends DICommPort<Object>{
	
	private Map<String, T> mListEntryPorts;
	private AddEntryPortListener mAddEntryPortListener;

	public DICommListPort(NetworkNode networkNode,
			CommunicationStrategy communicationStrategy) {
		super(networkNode, communicationStrategy);
		mListEntryPorts = new HashMap<String, T>();
	}
	
	public abstract T createNewListEntryPort(String listPortName, String portKey);
	
	public int getNumberOfListEntryPorts(){
		return mListEntryPorts.size();
	}
	
	public T getListEntryPort(String identifier){
		if (mListEntryPorts.containsKey(identifier)){
			return mListEntryPorts.get(identifier);
		}
		return null;
	}
	
	public void addNewListEntryPort(AddEntryPortListener addEntryPortListener){
		mAddEntryPortListener = addEntryPortListener;
		// TODO:DICOMM Refactor, check how to add entry port
	}
	
	public void removeListEntryPort(String identifier){
		// TODO:DICOMM Refactor, check how to remove entry port
	}

	@Override
	public boolean isResponseForThisPort(String response) {
		// TODO DICOmm refacor - implement method
		return false;
	}

	@Override
	protected void processResponse(String response) {
		// TODO DIComm refactor - implement method
		
	}

	@Override
	public boolean supportsSubscription() {
		// TODO DIComm Refactor - check if list ports will also allow subscription
		return false;
	}
}