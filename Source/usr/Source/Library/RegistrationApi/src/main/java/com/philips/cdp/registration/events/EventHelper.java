
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.events;

import com.philips.cdp.registration.ui.utils.RLog;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Event helper
 */
public class EventHelper {

    private final String TAG = "EventHelper";

    private static EventHelper eventHelper;

    /**
     * class constructor
     */
    private EventHelper() {
        eventMap = new ConcurrentHashMap<>();

    }


    /**
     * {@code getInstance} method get instance of Event helper
     *
     * @return eventHelper instance of event helper
     */
    public static synchronized EventHelper getInstance() {
        if (eventHelper == null) {
            eventHelper = new EventHelper();
        }
        return eventHelper;
    }

    private ConcurrentHashMap<String, List<EventListener>> eventMap;

    /**
     * {@code registerEventNotification} method to register event notification
     *
     * @param list     list
     * @param observer observer
     */
    public void registerEventNotification(List<String> list, EventListener observer) {
        RLog.d(TAG, "registerEventNotification " + list.toString());
        if (eventMap != null && observer != null) {
            for (String event : list) {
                registerEventNotification(event, observer);
            }
        }

    }

    /**
     * {@code registerEventNotification}method to register event notification
     *
     * @param evenName even name
     * @param observer observer
     */
    public void registerEventNotification(String evenName, EventListener observer) {
        RLog.d(TAG, "registerEventNotification : " + evenName);
        if (eventMap != null && observer != null) {
            CopyOnWriteArrayList<EventListener> listnerList = (CopyOnWriteArrayList<EventListener>) eventMap
                    .get(evenName);
            if (listnerList == null) {
                listnerList = new CopyOnWriteArrayList<>();
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
     * {@code unregisterEventNotification} method to unregister event notification
     *
     * @param pEventName event name
     * @param pObserver  observer
     */
    public void unregisterEventNotification(String pEventName, EventListener pObserver) {
        RLog.d(TAG, "unregisterEventNotification : " + pEventName);
        if (eventMap != null && pObserver != null) {
            List<EventListener> listnerList = eventMap.get(pEventName);
            if (listnerList != null) {
                listnerList.remove(pObserver);
                eventMap.put(pEventName, listnerList);
            }
        }
    }

    /**
     * {@code notifyEventOccurred} method to notify event occurred
     *
     * @param pEventName event name
     */
    public void notifyEventOccurred(String pEventName) {
        RLog.d(TAG, "notifyEventOccurred : " + pEventName);
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
