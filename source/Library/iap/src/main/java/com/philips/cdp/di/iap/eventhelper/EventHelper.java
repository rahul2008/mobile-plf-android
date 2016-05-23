/*----------------------------------------------------------------------------
Copyright(c) Philips Electronics India Ltd
All rights reserved. Reproduction in whole or in part is prohibited without
the written consent of the copyright holder.

Project           : SaecoAvanti

File Name         : EventHelper.java

Description       : EventHelper
Revision History: version 1:
    Date: Jul 5, 2014
    Original author: Maruti Kutre
    Description: Initial version
----------------------------------------------------------------------------*/

package com.philips.cdp.di.iap.eventhelper;

import com.philips.cdp.di.iap.utils.IAPLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventHelper {

    private static EventHelper eventHelper;

    private EventHelper() {
        eventMap = new HashMap<String, List<EventListener>>();
    }

    public static EventHelper getInstance() {
        synchronized (EventHelper.class) {
            if (eventHelper == null) {
                eventHelper = new EventHelper();
            }
        }
        return eventHelper;
    }

    private Map<String, List<EventListener>> eventMap;

    public void registerEventNotification(List<String> list, EventListener observer) {
        if (eventMap != null && observer != null) {
            for (String event : list) {
                registerEventNotification(event, observer);
            }
        }

    }

    public void registerEventNotification(String evenName, EventListener observer) {
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
            IAPLog.i(IAPLog.LOG, "Events = " + eventMap.toString());
        }
    }

    public void unregisterEventNotification(String pEventName, EventListener pObserver) {
        if (eventMap != null && pObserver != null) {
            List<EventListener> listnerList = eventMap.get(pEventName);
            if (listnerList != null) {
                listnerList.remove(pObserver);
                eventMap.put(pEventName, listnerList);
                IAPLog.i(IAPLog.LOG, " unregister Events = " + eventMap.toString());
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
