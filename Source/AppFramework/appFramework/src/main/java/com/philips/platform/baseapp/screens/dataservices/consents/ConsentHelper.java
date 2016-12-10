package com.philips.platform.baseapp.screens.dataservices.consents;

import com.philips.platform.baseapp.screens.dataservices.listener.DBChangeListener;
import com.philips.platform.baseapp.screens.dataservices.listener.EventHelper;
import com.philips.platform.core.datatypes.Consent;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

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
