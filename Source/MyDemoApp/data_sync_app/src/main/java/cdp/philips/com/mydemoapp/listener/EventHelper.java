package cdp.philips.com.mydemoapp.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EventHelper {

    private static EventHelper eventHelper;
    public static final int MOMENT = 1;
    public static final int CONSENT = 2;

    private Map<Integer, ArrayList<DBChangeListener>> eventMap;

    //Key is the unique tag for UI .

    private EventHelper() {
        eventMap = new HashMap<>();
    }

    public Map<Integer, ArrayList<DBChangeListener>> getEventMap() {
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

    public void registerEventNotification(Integer tag, DBChangeListener observer) {
        ArrayList<DBChangeListener> dbChangeListeners = eventMap.get(tag);
        if(dbChangeListeners==null)
            dbChangeListeners = new ArrayList<>();

        dbChangeListeners.add(observer);
        eventMap.put(tag, dbChangeListeners);
    }

    public void unregisterEventNotification(Integer tag, DBChangeListener pObserver) {

            ArrayList<DBChangeListener> listnerList = eventMap.get(tag);

            if (listnerList != null) {
                listnerList.remove(pObserver);
                eventMap.put(tag, listnerList);
            }
    }


}
