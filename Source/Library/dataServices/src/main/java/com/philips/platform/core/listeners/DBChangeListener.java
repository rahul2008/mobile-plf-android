package com.philips.platform.core.listeners;

import com.philips.platform.core.datatypes.SyncType;

public interface DBChangeListener {

    void dBChangeSuccess(SyncType type);

    void dBChangeFailed(Exception e);
}
