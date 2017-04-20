package com.philips.platform.core.datatypes;

public enum SyncType {
    INSIGHT(104,"insight"),
    MOMENT(103,"moment"),
    UNKNOWN(-1, "UNKNOWN"),
    SETTINGS(100, "Settings"),
    CHARACTERISTICS(101, "characteristics"),
    CONSENT(102, "consent");

    private final int id;
    private final String description;

    SyncType(final int id, final String description) {
        this.id = id;
        this.description = description;
    }

    public static SyncType fromId(final int id) {
        final SyncType[] values = SyncType.values();

        for (SyncType item : values) {
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
