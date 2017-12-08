/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.datatypes;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class SHNLog {

    @Nullable
    private final Date startDate;

    @Nullable
    private final Date endDate;

    @NonNull
    private final String associatedDeviceAddress;

    @NonNull
    private final List<SHNLogItem> logItems;

    @NonNull
    private final Set<SHNDataType> containedDataTypes;

    public SHNLog(@Nullable final Date startDate, @Nullable final Date endDate, @NonNull final String associatedDeviceAddress, @NonNull final List<SHNLogItem> logItems, @NonNull final Set<SHNDataType> containedDataTypes) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.associatedDeviceAddress = associatedDeviceAddress;
        this.logItems = logItems;
        this.containedDataTypes = containedDataTypes;
    }

    @Nullable
    public Date getStartDate() {
        return startDate;
    }

    @Nullable
    public Date getEndDate() {
        return endDate;
    }

    public long getDurationMS() { // milli seconds between end and start
        if (endDate != null && startDate != null) {
            return endDate.getTime() - startDate.getTime();
        }
        return 0;
    }

    @NonNull
    public String getAssociatedDeviceAddress() {
        return associatedDeviceAddress;
    }

    @NonNull
    public List<SHNLogItem> getLogItems() {
        return Collections.unmodifiableList(logItems);
    }

    @NonNull
    public Set<SHNDataType> getContainedDataTypes() {
        return Collections.unmodifiableSet(containedDataTypes);
    }

    @NonNull
    public List<SHNLogItem> getLogItems(SHNDataType shnDataType) {
        List<SHNLogItem> result = new ArrayList<>();

        for (SHNLogItem logItem : logItems) {
            if (logItem.getContainedDataTypes().contains(shnDataType)) {
                result.add(logItem);
            }
        }

        return Collections.unmodifiableList(result);
    }

    public SHNLogItem getLogItemAtTimeOffset(long offset) {
        Comparator<? super Object> comparator = new Comparator<Object>() {
            @Override
            public int compare(Object lhs, Object rhs) {
                long diff = ((SHNLogItem) rhs).getTimestamp().getTime() - ((SHNLogItem) lhs).getTimestamp().getTime();
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
