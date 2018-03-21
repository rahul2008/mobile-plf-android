
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.events;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * class network state helper
 */
public class NetworkStateHelper {

	/**
	 * Network state helper
	 */
	private static NetworkStateHelper eventHelper;

	/**
	 * copy on write array list
	 */
	private CopyOnWriteArrayList<NetworkStateListener> networStateListeners;

	/**
	 * Class constructor
	 */
	private NetworkStateHelper() {
		networStateListeners = new CopyOnWriteArrayList<NetworkStateListener>();
	}

	/**
	 * {@code getInstance} method to get network state helper instance
	 * @return instance
     */
	public static synchronized NetworkStateHelper getInstance() {
		if (eventHelper == null) {
			eventHelper = new NetworkStateHelper();
		}
		return eventHelper;
	}

	/**
	 * {@code registerEventNotification} method to registerHandler event notification
	 * @param observer network state listener
     */
	public synchronized void registerEventNotification(NetworkStateListener observer) {
		synchronized (networStateListeners) {
			if (networStateListeners != null && observer != null) {
				for (int i = 0; i < networStateListeners.size(); i++) {
					NetworkStateListener tmp = networStateListeners.get(i);
					if (tmp.getClass() == observer.getClass()) {
						networStateListeners.remove(tmp);
					}
				}
				networStateListeners.add(observer);
			}
		}
	}

	/**
	 * {@code unregisterEventNotification} method to unregister event notification
	 * @param observer network state listener
     */
	public synchronized void unregisterEventNotification(NetworkStateListener observer) {
		synchronized (networStateListeners) {
			if (networStateListeners != null && observer != null) {
				for (int i = 0; i < networStateListeners.size(); i++) {
					NetworkStateListener tmp = networStateListeners.get(i);
					if (tmp.getClass() == observer.getClass()) {
						networStateListeners.remove(tmp);
					}
				}
			}
		}
	}

	/**
	 * {@code notifyEventOccurred} method to notify event occurred
	 * @param isOnline
     */
	public synchronized void notifyEventOccurred(boolean isOnline) {
		synchronized (networStateListeners) {
			if (networStateListeners != null) {
				for (NetworkStateListener eventListener : networStateListeners) {
					if (eventListener != null) {
						eventListener.onNetWorkStateReceived(isOnline);
					}
				}
			}
		}
	}
}
