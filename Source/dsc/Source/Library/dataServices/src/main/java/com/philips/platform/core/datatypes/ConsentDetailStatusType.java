/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.core.datatypes;

/**
 * Defined Enum for Consent Status
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

    /**
     * Get ID of the Consent Status
     *
     * @return returns the enum constant for the consent status
     */
    public int getId() {
        return id;
    }

    /**
     * Get the String description of the Consent Status
     *
     * @return returns the description for the consent status
     */
    public String getDescription() {
        return description;
    }

    /**
     * Retuns ConsentDetailStatusType from the ID provided
     *
     * @param id The ID for getting ConsentDetailStatusType
     * @return retuns ConsentDetailStatusType from the ID provided
     */
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
