package com.pins.philips.shinelib.datatypes;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * Created by 310188215 on 07/05/15.
 */
public class SHNLogItem {
    private final Calendar timestamp;
    private final Set<SHNDataType> containedDataTypes;
    private final Map<SHNDataType, SHNData> dataByDataTypeMap;

    public SHNLogItem(Calendar timestamp, Set<SHNDataType> containedDataTypes, Map<SHNDataType, SHNData> dataByDataTypeMap) {
        this.timestamp = timestamp;
        this.containedDataTypes = containedDataTypes;
        this.dataByDataTypeMap = dataByDataTypeMap;
    }

    public Calendar getTimestamp() {
        return timestamp;
    }

    public Set<SHNDataType> getContainedDataTypes() {
        return containedDataTypes;
    }

    public Map<SHNDataType, SHNData> getDataByDataTypeMap() {
        return dataByDataTypeMap;

    }
}
