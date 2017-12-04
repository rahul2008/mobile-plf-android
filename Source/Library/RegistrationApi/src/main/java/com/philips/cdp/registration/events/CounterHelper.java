
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.events;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Event helper
 */
public class CounterHelper {

	private static CounterHelper eventHelper;

	/**
	 * class constructor
	 */
	private CounterHelper() {
		eventMap = new ConcurrentHashMap<String, List<CounterListener>>();

	}


	/**
	 * {@code getInstance} method get instance of Event helper
	 * @return eventHelper instance of event helper
     */
	public static synchronized CounterHelper getInstance() {
		if (eventHelper == null) {
			eventHelper = new CounterHelper();
		}
		return eventHelper;
	}

	private ConcurrentHashMap <String, List<CounterListener>> eventMap;

	/**
	 * {@code registerEventNotification} method to register event notification
	 * @param list list
	 * @param observer observer
     */
	public void registerCounterEventNotification(List<String> list, CounterListener observer) {
		if (eventMap != null && observer != null) {
			for (String event : list) {
				registerCounterEventNotification(event, observer);
			}
		}

	}

	/**
	 * {@code registerEventNotification}method to register event notification
	 * @param evenName even name
	 * @param observer observer
     */
    public void registerCounterEventNotification(String evenName, CounterListener observer) {
		if (eventMap != null && observer != null) {
			CopyOnWriteArrayList<CounterListener> listnerList = (CopyOnWriteArrayList<CounterListener>) eventMap
			        .get(evenName);
			if (listnerList == null) {
				listnerList = new CopyOnWriteArrayList<CounterListener>();
			}
			// Removing existing instance of same observer if exist
			for (int i = 0; i < listnerList.size(); i++) {
				CounterListener tmp = listnerList.get(i);
				if (tmp.getClass() == observer.getClass()) {
					listnerList.remove(tmp);
				}
			}
			listnerList.add(observer);
			eventMap.put(evenName, listnerList);
		}
	}

	/**
	 * {@code unregisterEventNotification} method to unregister event notification
	 * @param pEventName event name
	 * @param pObserver observer
     */
	public void unregisterCounterEventNotification(String pEventName, CounterListener pObserver) {
		if (eventMap != null && pObserver != null) {
			List<CounterListener> listnerList = eventMap.get(pEventName);
			if (listnerList != null) {
				listnerList.remove(pObserver);
				eventMap.put(pEventName, listnerList);
			}
		}
	}

	/**
	 * {@code notifyEventOccurred} method to notify event occurred
	 * @param pEventName event name
	 */
	public void notifyCounterEventOccurred(String pEventName, long time) {
		if (eventMap != null) {
			List<CounterListener> listnerList = eventMap.get(pEventName);
			if (listnerList != null) {
				for (CounterListener eventListener : listnerList) {
					if (eventListener != null) {
						eventListener.onCounterEventReceived(pEventName,time);
					}
				}
			}
		}
	}
}
