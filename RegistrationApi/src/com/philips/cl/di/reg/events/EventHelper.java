package com.philips.cl.di.reg.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventHelper {

	private static EventHelper eventHelper;

	private EventHelper() {
		eventMap = new HashMap<String, List<EventListener>>();
	}

	/**
	 * Returns singleton instance of eventHelper
	 * 
	 * @return EventHelper
	 */
	public static synchronized EventHelper getInstance() {
		if (eventHelper == null) {
			eventHelper = new EventHelper();
		}
		return eventHelper;
	}

	private Map<String, List<EventListener>> eventMap;

	/**
	 * This API is for Registering list of events for change notification
	 * 
	 * @param list
	 * @param observer
	 */
	public void registerEventNotification(List<String> list,
			EventListener observer) {
		if (eventMap != null && observer != null) {
			for (String event : list) {
				registerEventNotification(event, observer);
			}
		}

	}

	/**
	 * This API is for Registering for notification of event
	 * 
	 * @param evenName
	 * @param observer
	 */
	public void registerEventNotification(String evenName,
			EventListener observer) {
		if (eventMap != null && observer != null) {
			ArrayList<EventListener> listnerList = (ArrayList<EventListener>) eventMap
					.get(evenName);
			if (listnerList == null) {
				listnerList = new ArrayList<EventListener>();
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

	/**
	 * This API is for Registering for notification of event
	 * 
	 * @param pEventName
	 * @param pObserver
	 */
	public void unregisterEventNotification(String pEventName,
			EventListener pObserver) {
		if (eventMap != null && pObserver != null) {
			List<EventListener> listnerList = eventMap.get(pEventName);
			if (listnerList != null) {
				listnerList.remove(pObserver);
				eventMap.put(pEventName, listnerList);
			}
		}
	}

	/**
	 * @param pObserver
	 */
	// public void unregisterforAllEvents(EventListener pObserver) {
	// if (eventMap != null && pObserver != null) {
	// Set<Entry<String, Set<EventListener>>> entrySet = eventMap.entrySet();
	// for (Entry<String, Set<EventListener>> entry : entrySet) {
	// Set<EventListener> eventListeners = entry.getValue();
	// if (eventListeners != null) {
	// eventListeners.remove(pObserver);
	// eventMap.put(entry.getKey(), eventListeners);
	// }
	// }
	// }
	// }

	/**
	 * @param pEventName
	 */
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
