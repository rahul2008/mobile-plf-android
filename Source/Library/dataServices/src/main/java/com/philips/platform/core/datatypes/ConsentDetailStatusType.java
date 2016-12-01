package com.philips.platform.core.datatypes;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

public enum ConsentDetailStatusType {
    UNKNOWN(200, "Unknown"),
    ACCEPTED(201, "accepted"),
    REFUSED(202, "refused");

    private final int id;
    private final String description;

    ConsentDetailStatusType(final int id, final String description) {
        this.id = id;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public static ConsentDetailStatusType fromId(final int id) {
        final ConsentDetailStatusType[] values = ConsentDetailStatusType.values();

        for (ConsentDetailStatusType item : values) {
            if (id == item.getId()) {
                return item;
            }
        }

        return UNKNOWN;
    }
}
