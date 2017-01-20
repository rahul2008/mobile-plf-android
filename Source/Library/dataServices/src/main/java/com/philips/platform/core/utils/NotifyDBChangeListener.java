package com.philips.platform.core.utils;

import com.philips.platform.core.listeners.DBChangeListener;
import com.philips.platform.core.trackers.DataServicesManager;

/**
 * Created by sangamesh on 18/01/17.
 */

public class NotifyDBChangeListener {

    public void notifyDBChangeSuccess(DBChangeListener dbChangeListener){
        if(dbChangeListener==null)dbChangeListener= DataServicesManager.getInstance().getDbChangeListener();
        if(dbChangeListener==null)return;
        dbChangeListener.dBChangeSuccess();
    }

    public void notifyDBChangeFailed(DBChangeListener dbChangeListener,Exception e){
        dbChangeListener.dBChangeFailed(e);
    }
}
