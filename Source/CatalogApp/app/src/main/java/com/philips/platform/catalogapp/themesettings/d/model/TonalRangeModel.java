package com.philips.platform.catalogapp.themesettings.d.model;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class TonalRangeModel {
    private String title;
    private int color;

    public TonalRangeModel(final String title, final int color) {
        this.title = title;
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public int getColor() {
        return color;
    }
}
