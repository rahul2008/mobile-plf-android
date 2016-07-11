package com.philips.platform.modularui.statecontroller;

import android.content.Context;

/**
 * Created by 310240027 on 7/4/2016.
 */
public interface UIBasePresenter {
    void onClick(int componentID, Context context);
    void onLoad(Context context);
}
