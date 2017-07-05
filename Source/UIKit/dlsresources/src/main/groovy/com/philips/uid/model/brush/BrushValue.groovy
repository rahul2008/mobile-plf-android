/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.uid.model.brush;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import groovy.transform.AutoClone;

@AutoClone
class BrushValue {

    @SerializedName("color")
    @Expose
    String color;
    @SerializedName("color-code")
    @Expose
    String colorCode;
    @SerializedName("color-range")
    @Expose
    String colorRange;
    @SerializedName("opacity")
    @Expose
    String opacity;
    @SerializedName("reference")
    @Expose
    String reference;
    @SerializedName("offset")
    @Expose
    String offset;

    String getColor() {
        return color;
    }

    void setColor(String color) {
        this.color = color;
    }

    String getColorCode() {
        return colorCode;
    }

    void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    String getColorRange() {
        return colorRange;
    }

    void setColorRange(String colorRange) {
        this.colorRange = colorRange;
    }

    String getOpacity() {
        return opacity;
    }

    void setOpacity(String opacity) {
        this.opacity = opacity;
    }

    String getReference() {
        return reference;
    }

    void setReference(String reference) {
        this.reference = reference;
    }

    String getOffset() {
        return offset;
    }

    void setOffset(String offset) {
        this.offset = offset;
    }

    @Override
    String toString() {
        return "BrushValue{" +
                "color='" + color + '\'' +
                ", colorCode='" + colorCode + '\'' +
                ", colorRange='" + colorRange + '\'' +
                ", opacity='" + opacity + '\'' +
                ", reference='" + reference + '\'' +
                ", offset='" + offset + '\'' +
                '}';
    }

    @Override
    protected BrushValue clone() throws CloneNotSupportedException {
        return (BrushValue) super.clone();
    }
}