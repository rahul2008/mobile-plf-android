/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.ecs.demouapp.ui.eventhelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventHelper {

    private static EventHelper eventHelper;
    private Map<String, List<EventListener>> eventMap;

    private EventHelper() {
        eventMap = new HashMap<>();
    }

    public static EventHelper getInstance() {
        synchronized (EventHelper.class) {
            if (eventHelper == null) {
                eventHelper = new EventHelper();
            }
        }
        return eventHelper;
    }

    public void registerEventNotification(List<String> list, EventListener observer) {
        if (eventMap != null && observer != null) {
            for (String event : list) {
                registerEventNotification(event, observer);
            }
        }
    }

    public void registerEventNotification(String evenName, EventListener observer) {
        if (eventMap != null && observer != null) {
            ArrayList<EventListener> listenerList = (ArrayList<EventListener>) eventMap
                    .get(evenName);
            if (listenerList == null) {
                listenerList = new ArrayList<>();
            }
            // Removing existing instance of same observer if exist
            for (int i = 0; i < listenerList.size(); i++) {
                EventListener tmp = listenerList.get(i);
                if (tmp.getClass() == observer.getClass()) {
                    listenerList.remove(tmp);
                }
            }
            listenerList.add(observer);
            eventMap.put(evenName, listenerList);
        }
    }

    public void unregisterEventNotification(String pEventName, EventListener pObserver) {
        if (eventMap != null && pObserver != null) {
            List<EventListener> listenerList = eventMap.get(pEventName);
            if (listenerList != null) {
                listenerList.remove(pObserver);
                eventMap.put(pEventName, listenerList);
            }
        }
    }

    public void notifyEventOccurred(String pEventName) {
        if (eventMap != null) {
            List<EventListener> listenerList = eventMap.get(pEventName);
            if (listenerList != null) {
                for (EventListener eventListener : listenerList) {
                    if (eventListener != null) {
                        eventListener.onEventReceived(pEventName);
                    }
                }
            }
        }
    }
}
