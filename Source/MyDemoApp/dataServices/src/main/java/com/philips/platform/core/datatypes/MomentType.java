/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.core.datatypes;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public enum MomentType {

    UNKNOWN(-1, "UNKNOWN"),
    TREATMENT(20,"TREATMENT"),
    USER_INFO(21, "USER_INFO"),
    PHOTO(21, "PHOTO"),
    TEMPERATURE(25,"TEMPERATURE"),;

    private final int id;
    private final String description;

    MomentType(final int id, final String description) {
        this.id = id;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public static MomentType fromId(final int id) {
        final MomentType[] values = MomentType.values();

        for (MomentType item : values) {
            if (id == item.getId()) {
                return item;
            }
        }

        return UNKNOWN;
    }
}
