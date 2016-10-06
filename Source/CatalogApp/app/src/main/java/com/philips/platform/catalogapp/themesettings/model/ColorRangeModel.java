package com.philips.platform.catalogapp.themesettings.model;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ColorRangeModel {
    private String title;

    private String name;

    private int color;

    public ColorRangeModel(final String title, final String name, final int color) {
        this.title = title;
        this.name = name;
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public int getColor() {
        return color;
    }

    public String getName() {
        return name;
    }
}
