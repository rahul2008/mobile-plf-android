package com.philips.platform.core.listeners;

import com.philips.platform.core.datatypes.SyncType;

/**
 * Created by sangamesh on 18/01/17.
 */

public interface DBChangeListener {

    void dBChangeSuccess(SyncType type);
    void dBChangeFailed(Exception e);
}
