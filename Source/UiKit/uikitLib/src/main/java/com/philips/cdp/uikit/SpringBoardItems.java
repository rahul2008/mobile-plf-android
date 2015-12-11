package com.philips.cdp.uikit;

import android.graphics.drawable.Drawable;

/**
 * Created by 310213373 on 12/2/2015.
 */
public class SpringBoardItems {
    int mImageID;
    int mStringID;

    public int getmImageID() {
        return mImageID;
    }

    public void setmImageID(int mImageID) {
        this.mImageID = mImageID;
    }

    public int getmStringID() {
        return mStringID;
    }

    public void setmStringID(int mStringID) {
        this.mStringID = mStringID;
    }

    public Drawable getmImage() {
        return mImage;
    }

    public void setmImage(Drawable mImage) {
        this.mImage = mImage;
    }

    public String getmString() {
        return mString;
    }

    public void setmString(String mString) {
        this.mString = mString;
    }

    String mString;
    Drawable mImage;
}
