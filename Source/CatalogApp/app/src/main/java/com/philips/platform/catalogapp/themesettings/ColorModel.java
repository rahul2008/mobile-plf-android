/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */

package com.philips.platform.catalogapp.themesettings;

public class ColorModel {
    private int startColor = -1;
    private int endColor = -1;
    private String title;
    private String name;
    private int textColor = -1;
    private int tickColor = -1;
    private int backgroundColor;

    public ColorModel(final String title, final String name, final int textColor, final int tickColor, final int startColor, final int endColor) {
        this.title = title;
        this.name = name;
        this.textColor = textColor;
        this.tickColor = tickColor;
        this.startColor = startColor;
        this.endColor = endColor;
    }

    public ColorModel(final String title, final int textColor, final int tickColor, final int startColor, final int endColor) {
        this.title = title;
        this.textColor = textColor;
        this.tickColor = tickColor;
        this.startColor = startColor;
        this.endColor = endColor;
    }

    public ColorModel(final String title, final String colorRange, final int backgroundColor, final int textColor) {
        this.title = title;
        this.name = colorRange;
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
        this.tickColor = textColor;
    }

    public String getTitle() {
        return title;
    }

    public int getTextColor() {
        return textColor;
    }

    public String getName() {
        return name;
    }

    public int getStartColor() {
        return startColor;
    }

    public int getEndColor() {
        return endColor;
    }

    public int getTickColor() {
        return tickColor;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }
}
