package com.pins.philips.shinelib.datatypes;

import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * Created by 310188215 on 07/05/15.
 */
public abstract class SHNLogItem {
    public abstract Date getTimestamp();
    public abstract Set<SHNDataType> getContainedDataTypes();
    public abstract Map<SHNDataType, SHNData> getDataByDataTypeMap();
}
