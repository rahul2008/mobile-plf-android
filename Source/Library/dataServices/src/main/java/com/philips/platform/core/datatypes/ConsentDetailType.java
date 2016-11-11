package com.philips.platform.core.datatypes;

import java.util.ArrayList;
import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public enum ConsentDetailType {
    WEIGHT(100, "Weight"),
    HEIGHT(101, "Height"),
    TEMPERATURE(102, "Temperature"),
    BREAST_FEEDING_SESSION(103, "BreastFeedingSession"),
    BOTTLE_FEEDING_SESSION(104, "BottleFeedingSession"),
    PUMPING_SESSION(105, "PumpingSession"),
    SLEEP(106, "Sleep"),
    CRY_DURATION(108, "CryDuration"),
    ROOM_TEMPERATURE(109, "RoomTemperature"),
    RELATIVE_HUMIDITY(110, "RelativeHumidity");

    private final int id;
    private final String description;

    ConsentDetailType(final int id, final String description) {
        this.id = id;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public static ConsentDetailType fromId(final int id) {
        final ConsentDetailType[] values = ConsentDetailType.values();

        for (ConsentDetailType item : values) {
            if (id == item.getId()) {
                return item;
            }
        }

        return WEIGHT;
    }

    public static ConsentDetailType fromDescription(final String description) {
        final ConsentDetailType[] values = ConsentDetailType.values();

        for (ConsentDetailType item : values) {
            if (description.equals(item.getDescription())) {
                return item;
            }
        }
        return BREAST_FEEDING_SESSION;
    }

    public static List<String> getDescriptionAsList() {
        final ConsentDetailType[] values = ConsentDetailType.values();
        List<String> descriptionList = new ArrayList<>(values.length);

        for (ConsentDetailType item : values) {
            descriptionList.add(item.description);
        }
        return descriptionList;
    }

}

