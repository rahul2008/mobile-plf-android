/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.uid;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ThemeValue {

    @SerializedName("color")
    @Expose
    private String color;
    @SerializedName("color-code")
    @Expose
    private String colorCode;
    @SerializedName("color-range")
    @Expose
    private String colorRange;
    @SerializedName("opacity")
    @Expose
    private String opacity;
    @SerializedName("reference")
    @Expose
    private String reference;
    @SerializedName("offset")
    @Expose
    private String offset;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getColorRange() {
        return colorRange;
    }

    public void setColorRange(String colorRange) {
        this.colorRange = colorRange;
    }

    public String getOpacity() {
        return opacity;
    }

    public void setOpacity(String opacity) {
        this.opacity = opacity;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    @Override
    public String toString() {
        return "ThemeValue{" +
                "color='" + color + '\'' +
                ", colorCode='" + colorCode + '\'' +
                ", colorRange='" + colorRange + '\'' +
                ", opacity='" + opacity + '\'' +
                ", reference='" + reference + '\'' +
                ", offset='" + offset + '\'' +
                '}';
    }
}
