package cdp.philips.com.mydemoapp.consents;

import com.philips.platform.core.datatypes.Consent;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import cdp.philips.com.mydemoapp.listener.DBChangeListener;
import cdp.philips.com.mydemoapp.listener.EventHelper;

/**
 * Created by sangamesh on 06/12/16.
 */

public class ConsentHelper {
    public void notifyAllSuccess(Consent ormConsent) {
        Map<Integer, ArrayList<DBChangeListener>> eventMap = EventHelper.getInstance().getEventMap();
        Set<Integer> integers = eventMap.keySet();
        if (integers.contains(EventHelper.CONSENT)) {
            ArrayList<DBChangeListener> dbChangeListeners = EventHelper.getInstance().getEventMap().get(EventHelper.CONSENT);
            for (DBChangeListener listener : dbChangeListeners) {
                listener.onSuccess(ormConsent);
            }
        }
    }

    public void notifyFailConsent(Exception e) {
        Map<Integer, ArrayList<DBChangeListener>> eventMap = EventHelper.getInstance().getEventMap();
        Set<Integer> integers = eventMap.keySet();
        if (integers.contains(EventHelper.CONSENT)) {
            ArrayList<DBChangeListener> dbChangeListeners = EventHelper.getInstance().getEventMap().get(EventHelper.CONSENT);
            for (DBChangeListener listener : dbChangeListeners) {
                listener.onFailure(e);
            }
        }
    }



}
