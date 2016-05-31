
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

public class EventHelper {

	private static EventHelper eventHelper;

	private EventHelper() {
		eventMap = new ConcurrentHashMap<String, List<EventListener>>();

	}


	public static synchronized EventHelper getInstance() {
		if (eventHelper == null) {
			eventHelper = new EventHelper();
		}
		return eventHelper;
	}

	private ConcurrentHashMap <String, List<EventListener>> eventMap;

	public void registerEventNotification(List<String> list, EventListener observer) {
		if (eventMap != null && observer != null) {
			for (String event : list) {
				registerEventNotification(event, observer);
			}
		}

	}

	public void registerEventNotification(String evenName, EventListener observer) {
		if (eventMap != null && observer != null) {
			CopyOnWriteArrayList<EventListener> listnerList = (CopyOnWriteArrayList<EventListener>) eventMap
			        .get(evenName);
			if (listnerList == null) {
				listnerList = new CopyOnWriteArrayList<EventListener>();
			}
			// Removing existing instance of same observer if exist
			for (int i = 0; i < listnerList.size(); i++) {
				EventListener tmp = listnerList.get(i);
				if (tmp.getClass() == observer.getClass()) {
					listnerList.remove(tmp);
				}
			}
			listnerList.add(observer);
			eventMap.put(evenName, listnerList);
		}
	}

	public void unregisterEventNotification(String pEventName, EventListener pObserver) {
		if (eventMap != null && pObserver != null) {
			List<EventListener> listnerList = eventMap.get(pEventName);
			if (listnerList != null) {
				listnerList.remove(pObserver);
				eventMap.put(pEventName, listnerList);
			}
		}
	}

	public void notifyEventOccurred(String pEventName) {
		if (eventMap != null) {
			List<EventListener> listnerList = eventMap.get(pEventName);
			if (listnerList != null) {
				for (EventListener eventListener : listnerList) {
					if (eventListener != null) {
						eventListener.onEventReceived(pEventName);
					}
				}
			}
		}
	}
}
