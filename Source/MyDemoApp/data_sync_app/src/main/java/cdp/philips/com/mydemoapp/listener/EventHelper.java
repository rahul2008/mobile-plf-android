package cdp.philips.com.mydemoapp.listener;

import com.philips.platform.core.listeners.DBRequestListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EventHelper {

    private static EventHelper eventHelper;
    public static final int MOMENT = 1;
    public static final int CONSENT = 2;

    private Map<Integer, ArrayList<DBRequestListener>> eventMap;

    //Key is the unique tag for UI .

    private EventHelper() {
        eventMap = new HashMap<>();
    }

    public Map<Integer, ArrayList<DBRequestListener>> getEventMap() {
        return eventMap;
    }

    public static EventHelper getInstance() {
        synchronized (EventHelper.class) {
            if (eventHelper == null) {
                eventHelper = new EventHelper();
            }
        }
        return eventHelper;
    }

    public void registerEventNotification(Integer tag, DBRequestListener observer) {
        ArrayList<DBRequestListener> dbChangeListeners = eventMap.get(tag);
        if(dbChangeListeners==null)
            dbChangeListeners = new ArrayList<>();

        dbChangeListeners.add(observer);
        eventMap.put(tag, dbChangeListeners);
    }

    public void unregisterEventNotification(Integer tag, DBRequestListener pObserver) {

            ArrayList<DBRequestListener> listnerList = eventMap.get(tag);

            if (listnerList != null) {
                listnerList.remove(pObserver);
                eventMap.put(tag, listnerList);
            }
    }


}
