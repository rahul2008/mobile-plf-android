/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.datatypes;

import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Set;

public class SHNLogItem {

    @NonNull
    private final Date timestamp;

    @NonNull
    private final Set<SHNDataType> containedDataTypes;

    @NonNull
    private final Map<SHNDataType, SHNData> dataByDataTypeMap;

    public SHNLogItem(@NonNull final Date timestamp, @NonNull final Set<SHNDataType> containedDataTypes, @NonNull final Map<SHNDataType, SHNData> dataByDataTypeMap) {
        this.timestamp = timestamp;
        this.containedDataTypes = containedDataTypes;
        this.dataByDataTypeMap = dataByDataTypeMap;
    }

    @NonNull
    public Date getTimestamp() {
        return timestamp;
    }

    @NonNull
    public Set<SHNDataType> getContainedDataTypes() {
        return Collections.unmodifiableSet(containedDataTypes);
    }

    @NonNull
    public Map<SHNDataType, SHNData> getDataByDataTypeMap() {
        return Collections.unmodifiableMap(dataByDataTypeMap);

    }
}
