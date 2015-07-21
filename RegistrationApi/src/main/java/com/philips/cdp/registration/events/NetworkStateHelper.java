
package com.philips.cdp.registration.events;

import java.util.ArrayList;

public class NetworkStateHelper {

	private static NetworkStateHelper eventHelper;

	private ArrayList<NetworStateListener> networStateListeners;

	private NetworkStateHelper() {
		networStateListeners = new ArrayList<NetworStateListener>();
	}

	public static synchronized NetworkStateHelper getInstance() {
		if (eventHelper == null) {
			eventHelper = new NetworkStateHelper();
		}
		return eventHelper;
	}

	public void registerEventNotification(NetworStateListener observer) {
		if (networStateListeners != null && observer != null) {
			for (int i = 0; i < networStateListeners.size(); i++) {
				NetworStateListener tmp = networStateListeners.get(i);
				if (tmp.getClass() == observer.getClass()) {
					networStateListeners.remove(tmp);
				}
			}
			networStateListeners.add(observer);
		}
	}

	public void unregisterEventNotification(NetworStateListener observer) {
		if (networStateListeners != null && observer != null) {
			for (int i = 0; i < networStateListeners.size(); i++) {
				NetworStateListener tmp = networStateListeners.get(i);
				if (tmp.getClass() == observer.getClass()) {
					networStateListeners.remove(tmp);
				}
			}
		}
	}

	public void notifyEventOccurred(boolean isOnline) {
		if (networStateListeners != null) {
			for (NetworStateListener eventListener : networStateListeners) {
				if (eventListener != null) {
					eventListener.onNetWorkStateReceived(isOnline);
				}
			}
		}
	}
}
