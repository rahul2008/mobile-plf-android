package com.philips.cdp.uikit.utils;

import android.graphics.drawable.Drawable;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class RowItem {
    private int imageId = 0;
    private Drawable drawable = null;
    private int stringId = 0;
    private String desc = null;


    public int getImageId() {
        return imageId;
    }
    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(final Drawable drawable) {
        this.drawable = drawable;
    }

    public int getStringId() {
        return stringId;
    }

    public void setStringId(final int stringId) {
        this.stringId = stringId;
    }

    public RowItem() {

    }
}