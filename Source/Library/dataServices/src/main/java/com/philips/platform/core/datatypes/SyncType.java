/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.core.datatypes;

/**
 * Enum class for Getting the Sync DataTypes
 */
public enum SyncType {
    UNKNOWN(-1, "UNKNOWN"),
    INSIGHT(104, "insight"),
    MOMENT(103, "moment"),
    SETTINGS(100, "Settings"),
    CHARACTERISTICS(101, "characteristics"),
    CONSENT(102, "consent");

    private final int id;
    private final String description;

    SyncType(final int id, final String description) {
        this.id = id;
        this.description = description;
    }

    /**
     * Get the SyncType from ID
     *
     * @param id ID for getting SyncType
     * @return returns the SyncType for the given ID
     */
    public static SyncType fromId(final int id) {
        final SyncType[] values = SyncType.values();

        for (SyncType item : values) {
            if (id == item.getId()) {
                return item;
            }
        }

        return UNKNOWN;
    }


    /**
     * Get the ID
     *
     * @return returns the ID
     */
    public int getId() {
        return id;
    }

    /**
     * Get the description of the SyncType
     *
     * @return returns the description of the syncType
     */
    public String getDescription() {
        return description;
    }
}
