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
public enum MomentDetailType {

    UNKNOWN(-1, "UNKNOWN"),
    NOTE(50, "Note"),
    PHOTO(51, "Photo"),
    STICKER(52, "Sticker"),
    VIDEO(53, "Video"),
    TAGGING_ID(54, "Tagging ID"),
    PHASE(55, "PHASE"),;

    private final int id;
    private final String description;

    MomentDetailType(final int id, final String description) {
        this.id = id;
        this.description = description;
    }

    public static MomentDetailType fromId(final int id) {
        final MomentDetailType[] values = MomentDetailType.values();

        for (MomentDetailType item : values) {
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
