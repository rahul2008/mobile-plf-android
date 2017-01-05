package cdp.philips.com.mydemoapp.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EventHelper {
    private static EventHelper eventHelper;
    public static final int MOMENT = 1;
    public static final int CONSENT = 2;
    public static final int UR = 1;
    public static final int USERCHARACTERISTICS = 3;

    private Map<Integer, ArrayList<DBChangeListener>> eventMap;
    private Map<Integer, ArrayList<UserRegistrationFailureListener>> urMap;

    //Key is the unique tag for UI .

    private EventHelper() {
        eventMap = new HashMap<>();
        urMap = new HashMap<>();
    }

    public Map<Integer, ArrayList<DBChangeListener>> getEventMap() {
        return eventMap;
    }

    public Map<Integer, ArrayList<UserRegistrationFailureListener>> getURMap() {
        return urMap;
    }

    public static EventHelper getInstance() {
        synchronized (EventHelper.class) {
            if (eventHelper == null) {
                eventHelper = new EventHelper();
            }
        }
        return eventHelper;
    }

    public void registerEventNotification(Integer tag, DBChangeListener observer) {
        ArrayList<DBChangeListener> dbChangeListeners = eventMap.get(tag);
        if (dbChangeListeners == null)
            dbChangeListeners = new ArrayList<>();

        dbChangeListeners.add(observer);
        eventMap.put(tag, dbChangeListeners);
    }

    public void registerURNotification(Integer tag, UserRegistrationFailureListener observer) {
        ArrayList<UserRegistrationFailureListener> userRegistrationFailureListeners = urMap.get(tag);
        if (userRegistrationFailureListeners == null)
            userRegistrationFailureListeners = new ArrayList<>();

        userRegistrationFailureListeners.add(observer);
        urMap.put(tag, userRegistrationFailureListeners);
    }

    public void unregisterEventNotification(Integer tag, DBChangeListener pObserver) {
        ArrayList<DBChangeListener> listenerList = eventMap.get(tag);
        if (listenerList != null) {
            listenerList.remove(pObserver);
            eventMap.put(tag, listenerList);
        }
    }
}
