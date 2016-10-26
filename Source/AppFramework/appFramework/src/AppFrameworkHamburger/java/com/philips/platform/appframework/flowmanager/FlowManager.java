package com.philips.platform.appframework.flowmanager;

import android.content.Context;

import com.philips.platform.modularui.statecontroller.BaseUiFlowManager;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

public class FlowManager extends BaseUiFlowManager {

    private static FlowManager flowManager = null;

    private FlowManager(Context context, int jsonPath) {
        super(context, jsonPath);
        baseAppState = new HamburgerAppState();
        baseAppCondition = new HamburgerAppCondition();
    }

    public static synchronized FlowManager getInstance(Context context, int jsonPath) {
        if (flowManager == null) {
            synchronized (FlowManager.class) {
                if (flowManager == null)
                    flowManager = new FlowManager(context, jsonPath);
            }
        }
        return flowManager;
    }

}
