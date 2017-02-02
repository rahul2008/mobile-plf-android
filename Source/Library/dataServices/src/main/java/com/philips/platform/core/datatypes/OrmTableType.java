package com.philips.platform.core.datatypes;

/**
 * Created by sangamesh on 23/01/17.
 */

public enum OrmTableType {

    UNKNOWN(-1, "UNKNOWN"),
    SETTINGS(100, "Settings"),
    CHARACTERISTICS(101, "characteristics");

    private final int id;
    private final String description;

    OrmTableType(final int id, final String description) {
        this.id = id;
        this.description = description;
    }

    public static OrmTableType fromId(final int id) {
        final OrmTableType[] values = OrmTableType.values();

        for (OrmTableType item : values) {
            if (id == item.getId()) {
                return item;
            }
        }

        return UNKNOWN;
    }


    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
