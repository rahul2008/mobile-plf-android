/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.core.utils;

import com.philips.platform.core.listeners.DBChangeListener;
import com.philips.platform.core.trackers.DataServicesManager;

public class NotifyDbChangeListener {

    public void notifyDbChangeSuccess(DBChangeListener dbChangeListener){
        if (dbChangeListener == null) {
            dbChangeListener = DataServicesManager.getInstance().getDbChangeListener();
        }
        if (dbChangeListener == null) {
            return;
        }
        dbChangeListener.dBChangeSuccess();
    }

    public void notifyDbChangeFailed(DBChangeListener dbChangeListener, Exception e){
        dbChangeListener.dBChangeFailed(e);
    }
}
