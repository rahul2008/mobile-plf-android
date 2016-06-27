package com.philips.cdp.appframework.modularui;

import android.content.Context;

/**
 * Created by 310240027 on 6/16/2016.
 */
public interface UIBaseNavigation {
    public
    @UIConstants.UIStateDef
    int onClick(int componentID, Context context);

    public
    @UIConstants.UIStateDef
    int onSwipe(int componentID, Context context);

    public
    @UIConstants.UIStateDef
    int onLongPress(int componentID, Context context);

    public
    @UIConstants.UIStateDef
    int onPageLoad(Context context);

    void setState();

}
