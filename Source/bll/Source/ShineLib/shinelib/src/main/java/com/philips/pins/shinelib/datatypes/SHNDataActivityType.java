/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.datatypes;

public class SHNDataActivityType extends SHNData {
    public enum SHNCM3ActivityType {
        SHNCM3ActivityTypeUnspecified,
        SHNCM3ActivityTypeOther,
        SHNCM3ActivityTypeWalk,
        SHNCM3ActivityTypeCycleIndoor,
        SHNCM3ActivityTypeRun,
        SHNCM3ActivityTypeCycleOutdoor,
        SHNCM3ActivityTypeCycle
    }

    private final SHNActivityType wearableActivityType;
    private final SHNActivityType manualActivityType;
    private final SHNCM3ActivityType cm3ActivityType;

    public SHNDataActivityType(SHNActivityType wearableActivityType, SHNActivityType manualActivityType, SHNCM3ActivityType cm3ActivityType) {
        this.wearableActivityType = wearableActivityType;
        this.manualActivityType = manualActivityType;
        this.cm3ActivityType = cm3ActivityType;
    }

    public SHNActivityType getWearableActivityType() {
        return wearableActivityType;
    }

    public SHNActivityType getManualActivityType() {
        return manualActivityType;
    }

    public SHNCM3ActivityType getCm3ActivityType() {
        return cm3ActivityType;
    }

    @Override
    public SHNDataType getSHNDataType() {
        return SHNDataType.ActivityTypeMoonshine;
    }

    @Override
    public String toString() {
        return "ActivityTypeMoonshine Wearable: " + getWearableActivityType().name() + " Manual: " + getManualActivityType().name() + " CM3: " + getCm3ActivityType().name();
    }
}
