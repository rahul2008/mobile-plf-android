package com.pins.philips.shinelib.datatypes;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by 310188215 on 07/05/15.
 */
public abstract class SHNLog {
    public abstract Date getStartDate();
    public abstract Date getEndDate();
    public abstract long getDurationMS(); // In MSseconds? between end and start?
    public abstract String getAssociatedDeviceAddress();
    public abstract List<SHNLogItem> getLogItems();
    public abstract Set<SHNDataType> getContainedDataTypes();

    public abstract List<SHNLogItem> getLogItems(SHNDataType shnDataType);
    public abstract SHNLogItem getLogItemAtTimeOffset(long offset);
}
