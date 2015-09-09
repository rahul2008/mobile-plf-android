package com.philips.cdp.ui.catalog;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public enum ColorType {

    UNKNOWN(-1, "unknown"),
    BLUE(0, "blue"),
    ORANGE(1, "orange");


    private final int id;
    private final String description;

    ColorType(final int id, final String description) {
        this.id = id;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public static ColorType fromId(final int id) {
        final ColorType[] values = ColorType.values();

        for (ColorType item : values) {
            if (id == item.getId()) {
                return item;
            }
        }

        return UNKNOWN;
    }

    public static ColorType fromValue(final String value) {
        final ColorType[] values = ColorType.values();

        for (ColorType item : values) {
            if (value.equals(item.getDescription())) {
                return item;
            }
        }

        return UNKNOWN;
    }
}
