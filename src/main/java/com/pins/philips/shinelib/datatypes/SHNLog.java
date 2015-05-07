package com.pins.philips.shinelib.datatypes;

import com.pins.philips.shinelib.SHNDevice;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by 310188215 on 07/05/15.
 */
public abstract class SHNLog {
    public abstract Date getStartDate();
    public abstract Date getEndDate();
    public abstract long getDuration(); // In seconds? between end and start?
    public abstract SHNDevice getAssociatedDevice();
    public abstract List<SHNLogItem> getLogItems();
    public abstract Set<SHNLogItemDataType> getContainedDataTypes();

    public abstract List<SHNLogItem> getLogItems(SHNLogItemDataType shnLogItemDataType);
    public abstract SHNLogItem getLogItemAtTimeOffset(long offset);
}
