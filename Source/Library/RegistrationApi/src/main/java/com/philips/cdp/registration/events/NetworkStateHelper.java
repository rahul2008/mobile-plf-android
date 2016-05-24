
package com.philips.cdp.registration.events;

import java.util.concurrent.CopyOnWriteArrayList;

public class NetworkStateHelper {

	private static NetworkStateHelper eventHelper;

	private CopyOnWriteArrayList<NetworStateListener> networStateListeners;

	private NetworkStateHelper() {
		networStateListeners = new CopyOnWriteArrayList<NetworStateListener>();
	}

	public static synchronized NetworkStateHelper getInstance() {
		if (eventHelper == null) {
			eventHelper = new NetworkStateHelper();
		}
		return eventHelper;
	}

	public synchronized void registerEventNotification(NetworStateListener observer) {
		synchronized (networStateListeners) {
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
	}

	public synchronized void unregisterEventNotification(NetworStateListener observer) {
		synchronized (networStateListeners) {
			if (networStateListeners != null && observer != null) {
				for (int i = 0; i < networStateListeners.size(); i++) {
					NetworStateListener tmp = networStateListeners.get(i);
					if (tmp.getClass() == observer.getClass()) {
						networStateListeners.remove(tmp);
					}
				}
			}
		}
	}

	public synchronized void notifyEventOccurred(boolean isOnline) {
		synchronized (networStateListeners) {
			if (networStateListeners != null) {
				for (NetworStateListener eventListener : networStateListeners) {
					if (eventListener != null) {
						eventListener.onNetWorkStateReceived(isOnline);
					}
				}
			}
		}
	}
}
