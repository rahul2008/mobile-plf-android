/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */

package com.philips.platform.catalogapp.themesettings;

public class ColorModel {
    private String title;

    private String name;

    private int color;

    public ColorModel(final String title, final String name, final int color) {
        this.title = title;
        this.name = name;
        this.color = color;
    }

    public ColorModel(final String title, final int color) {
        this.title = title;
        this.name = null;
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
