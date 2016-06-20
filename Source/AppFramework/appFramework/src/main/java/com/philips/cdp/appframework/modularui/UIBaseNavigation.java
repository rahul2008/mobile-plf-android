package com.philips.cdp.appframework.modularui;

import android.content.Context;

/**
 * Created by 310240027 on 6/16/2016.
 */
public interface UIBaseNavigation {
     public @UIFlowManager.UIStateDef int onClick(int componentID, Context context);
    public @UIFlowManager.UIStateDef int onSwipe(int componentID,Context context);
    public @UIFlowManager.UIStateDef int onLongPress(int componentID,Context context);
}
