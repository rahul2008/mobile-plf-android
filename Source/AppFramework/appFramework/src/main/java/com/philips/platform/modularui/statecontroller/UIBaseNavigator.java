package com.philips.platform.modularui.statecontroller;

import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * Created by 310240027 on 7/4/2016.
 */
public interface UIBaseNavigator  {
    void loadScreen(Context context);
    Fragment loadFragment();
}
