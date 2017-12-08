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
    private int contentColor = 0;
    private int backgroundColor;

    public ColorModel(final String title, final String name, final int contentColor, final int startColor, final int endColor) {
        this.title = title;
        this.name = name;
        this.contentColor = contentColor;
        this.startColor = startColor;
        this.endColor = endColor;
    }

    public ColorModel(final String title, final String colorRange, final int backgroundColor, final int contentColor) {
        this.title = title;
        this.name = colorRange;
        this.backgroundColor = backgroundColor;
        this.contentColor = contentColor;
    }

    public String getTitle() {
        return title;
    }

    public int getContentColor() {
        return contentColor;
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

    public int getBackgroundColor() {
        return backgroundColor;
    }

    @Override
    public String toString() {
        return "ColorModel{" +
                "startColor=" + startColor +
                ", endColor=" + endColor +
                ", title='" + title + '\'' +
                ", name='" + name + '\'' +
                ", contentColor=" + contentColor +
                ", backgroundColor=" + backgroundColor +
                '}';
    }
}
