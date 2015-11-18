/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.datatypes;

import android.support.annotation.NonNull;

import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * Created by 310188215 on 07/05/15.
 */
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
        return containedDataTypes;
    }

    @NonNull
    public Map<SHNDataType, SHNData> getDataByDataTypeMap() {
        return dataByDataTypeMap;

    }
}
