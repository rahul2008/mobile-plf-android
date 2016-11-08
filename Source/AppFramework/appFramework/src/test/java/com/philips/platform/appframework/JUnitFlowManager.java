package com.philips.platform.appframework;

import android.content.Context;
import android.support.annotation.IdRes;

import com.philips.platform.appframework.flowmanager.HamburgerAppCondition;
import com.philips.platform.appframework.flowmanager.HamburgerAppState;
import com.philips.platform.modularui.statecontroller.BaseUiFlowManager;

/**
 * Created by 310240027 on 11/7/2016.
 */

public class JUnitFlowManager extends BaseUiFlowManager {

    public JUnitFlowManager(Context context, int jsonPath) {
        super(context, jsonPath);
        baseAppState = new HamburgerAppState();
        baseAppCondition = new HamburgerAppCondition();
    }


}
