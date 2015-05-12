package com.philips.cl.di.dicomm.port;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cl.di.dicomm.communication.CommunicationStrategy;

public abstract class DICommListPort<T extends DICommListEntryPort<?>> extends DICommPort<Object>{

	private Map<String, T> mListEntryPorts;
	private List<DIListPortChangedListener> mListPortChangedListeners;

	public DICommListPort(NetworkNode networkNode,
			CommunicationStrategy communicationStrategy) {
		super(networkNode, communicationStrategy);
		mListEntryPorts = new HashMap<String, T>();
		mListPortChangedListeners = new ArrayList<DIListPortChangedListener>();
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

	public void addNewListEntryPort() {
		// TODO:DICOMM Refactor, check how to add entry port
	}

	public void removeListEntryPort(String identifier) {
		// TODO:DICOMM Refactor, check how to remove entry port
	}

	@Override
	public boolean isResponseForThisPort(String response) {
		// TODO DIComm refacor - implement method
		return false;
	}

	@Override
	protected void processResponse(String response) {
		// TODO DIComm refactor - implement method

		// Create a hashmap (key value) of processed ports

		// Loop over all JSON elements
		// if property -> update port property
		// if json -> put into hashmap

		// Loop over Hashmap of existing ports
		// if port not in new hashmap -> call port removed listener

		// Loop over hashmap of new ports
		// if port in existing hashmap -> update port
		// if port in new hashmap -> create new port and call add listener
	}

    public void registerListPortChangedListener(DIListPortChangedListener listener) {
		mListPortChangedListeners.add(listener);
    }

    public void unRegisterListPortChangedListener(DIListPortChangedListener listener) {
    	mListPortChangedListeners.remove(listener);
    }

    private void notifyListenersOnEntryPortAdded(DICommListEntryPort<?> addedEntryPort) {
        ArrayList<DIListPortChangedListener> copyListeners = new ArrayList<DIListPortChangedListener>(mListPortChangedListeners);
		for (DIListPortChangedListener listener : copyListeners) {
			ListenerRegistration registration = listener.onListEntryPortAdded(addedEntryPort);
			if (registration == ListenerRegistration.UNREGISTER) {
			    mListPortChangedListeners.remove(listener);
			}
		}
    }

    private void notifyListenersOnEntryPortRemoved(DICommListEntryPort<?> removedEntryPort) {
    	ArrayList<DIListPortChangedListener> copyListeners = new ArrayList<DIListPortChangedListener>(mListPortChangedListeners);
		for (DIListPortChangedListener listener : copyListeners) {
			ListenerRegistration registration = listener.onListEntryPortRemoved(removedEntryPort);
			if (registration == ListenerRegistration.UNREGISTER) {
                mListPortChangedListeners.remove(listener);
            }
		}
    }
}