package com.philips.cdp.ui.catalog;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public enum ColorType {

    UNKNOWN(-1, "unknown"),
    BLUE(0, "blue"),
    ORANGE(1, "orange"),
    AQUA(2, "aqua"),
    BRIGHT_GREEN(3,"bright_green"),
    BRIGHT_PINK(4,"bright_pink"),
    DARK_PURPLE(5,"dark_purple"),
    DARK_AQUA(6, "dark_aqua"),
    DARK_GREEN(7, "dark_green"),
    DARK_ORANGE(8, "dark_orange"),
    DARK_PINK(9, "dark_pink"),
    BRIGHT_BLUE(10, "bright_blue"),
    BRIGHT_PURPLE(11, "bright_purple"),
    LIGHT_BLUE(12, "light_blue"),
    LIGHT_ORANGE(13, "light_orange"),
    LIGHT_AQUA(14, "light_aqua"),
    LIGHT_GREEN(15, "light_green"),
    LIGHT_PINK(16, "light_pink"),
    LIGHT_PURPLE(17, "light_purple");

    private final int id;
    private final String description;

    ColorType(final int id, final String description) {
        this.id = id;
        this.description = description;
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

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
