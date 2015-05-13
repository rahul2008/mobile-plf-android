package com.pins.philips.shinelib.datatypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by 310188215 on 07/05/15.
 */
public class SHNLog {
    private final Calendar startDate;
    private final Calendar endDate;
    private final String associatedDeviceAddress;
    private final List<SHNLogItem> logItems;
    private final Set<SHNDataType> containedDataTypes;

    public SHNLog(Calendar startDate, Calendar endDate, String associatedDeviceAddress, List<SHNLogItem> logItems, Set<SHNDataType> containedDataTypes) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.associatedDeviceAddress = associatedDeviceAddress;
        this.logItems = logItems;
        this.containedDataTypes = containedDataTypes;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public long getDurationMS() { // milli seconds between end and start
        return endDate.getTimeInMillis() - startDate.getTimeInMillis();
    }

    public String getAssociatedDeviceAddress() {
        return associatedDeviceAddress;
    }

    public List<SHNLogItem> getLogItems() {
        return logItems;
    }

    public Set<SHNDataType> getContainedDataTypes() {
        return containedDataTypes;
    }

    public List<SHNLogItem> getLogItems(SHNDataType shnDataType) {
        List<SHNLogItem> result = new ArrayList<>();

        for (SHNLogItem logItem: logItems) {
            if (logItem.getContainedDataTypes().contains(shnDataType)) {
                result.add(logItem);
            }
        }

        return result;
    }

    public SHNLogItem getLogItemAtTimeOffset(long offset) {
        Comparator<? super Object> comparator = new Comparator<Object>() {
            @Override
            public int compare(Object lhs, Object rhs) {
                long diff = ((SHNLogItem)rhs).getTimestamp().getTimeInMillis() - ((SHNLogItem)lhs).getTimestamp().getTimeInMillis();
                if (diff > 0) {
                    return 1;
                } else if (diff < 0) {
                    return -1;
                }
                return 0;
            }
        };
        return logItems.get(Collections.binarySearch(logItems, offset, comparator));
    }
}
