package com.pins.philips.shinelib.datatypes;

import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * Created by 310188215 on 07/05/15.
 */
public abstract class SHNLogItem {
    public abstract Date getTimestamp();
    public abstract Set<SHNLogItemDataType> getContainedDataTypes(); // Is this usefull. Its the same data that can be obtained from the Map...
    public abstract Map<SHNLogItemDataType, SHNLogItemData> getDataByDataTypeMap();
}
