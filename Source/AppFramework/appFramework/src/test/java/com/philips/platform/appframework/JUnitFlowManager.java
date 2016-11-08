/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework;

import android.content.Context;
import android.support.annotation.IdRes;

import com.philips.platform.appframework.flowmanager.HamburgerAppCondition;
import com.philips.platform.appframework.flowmanager.HamburgerAppState;
import com.philips.platform.modularui.statecontroller.BaseUiFlowManager;



public class JUnitFlowManager extends BaseUiFlowManager {

    public JUnitFlowManager(Context context, int jsonPath) {
        super(context, jsonPath);
        baseAppState = new HamburgerAppState();
        baseAppCondition = new HamburgerAppCondition();
    }


}
