package com.philips.platform.appframework.modularui.statecontroller;

import android.content.Context;

/**
 * Created by 310240027 on 6/16/2016.
 */
public interface UIBaseNavigation {
    UIStateBase onClick(int componentID, Context context);

    UIStateBase onPageLoad(Context context);

    void setState();

}
