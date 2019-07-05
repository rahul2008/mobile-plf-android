/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.cdp.di.iap.Constants;

/**
 * Enum class for Getting the Sync DataTypes
 */
public enum OrderStatus {

    UNKNOWN(-1, "UNKNOWN"),
    PROCESSING(100, "processing"),
    PENDING(101, "pending"),
    COMPLETED(102, "completed");

    private final int id;
    private final String description;

    OrderStatus(final int id, final String description) {
        this.id = id;
        this.description = description;
    }

    /**
     * Get the OrderStatus from ID
     *
     * @param id ID for getting OrderStatus
     * @return returns the OrderStatus for the given ID
     */
    public static OrderStatus fromId(final int id) {
        final OrderStatus[] values = OrderStatus.values();

        for (OrderStatus item : values) {
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
     * Get the description of the OrderStatus
     *
     * @return returns the description of the syncType
     */
    public final String getDescription() {
        return description;
    }
}
