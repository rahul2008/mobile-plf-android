package com.philips.platform.core.events;

import com.philips.platform.core.datatypes.SyncType;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class SyncBitUpdateRequest extends Event {

    private final SyncType tableType;
    private final boolean isSynced;
    public SyncBitUpdateRequest(SyncType tableType, boolean isSynced) {

        this.tableType = tableType;
        this.isSynced = isSynced;
    }

    public boolean isSynced() {
        return isSynced;
    }

    public SyncType getTableType() {
        return tableType;
    }
}
