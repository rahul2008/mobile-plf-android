package com.philips.cdp.appframework.modularui;

import android.content.Context;

/**
 * Created by 310240027 on 6/16/2016.
 */
public interface UIBaseNavigation {
    public void onClick(int componentID,Context context);
    public void onSwipe(int componentID,Context context);
    public void onLongPress(int componentID,Context context);
}
