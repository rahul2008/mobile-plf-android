package com.philips.platform.core.events;

import com.philips.platform.core.datatypes.OrmTableType;
import com.philips.platform.core.datatypes.Settings;

import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class SyncBitUpdateRequest extends Event {

    private final OrmTableType tableType;
    private final boolean isSynced;
    public SyncBitUpdateRequest(OrmTableType tableType, boolean isSynced) {

        this.tableType = tableType;
        this.isSynced = isSynced;
    }

    public boolean isSynced() {
        return isSynced;
    }

    public OrmTableType getTableType() {
        return tableType;
    }
}
