package com.philips.cdp.digitalcare.util;

import androidx.annotation.StringRes;

/**
 * Created by arbin on 11/04/2017.
 */

public class MenuItem {
    public final int mIcon;
    @StringRes
    public final int mText;

    public MenuItem(int icon, int text) {
        this.mIcon = icon;
        this.mText = text;
    }
}
