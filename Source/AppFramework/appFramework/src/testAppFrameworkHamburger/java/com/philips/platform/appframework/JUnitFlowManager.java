/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework;

import android.content.Context;

import com.philips.platform.appframework.flowmanager.base.BaseCondition;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.appframework.flowmanager.base.BaseUiFlowManager;

import java.util.Map;


public class JUnitFlowManager extends BaseUiFlowManager {

    @Override
    public void populateStateMap(Map<String, BaseState> uiStateMap) {

    }

    @Override
    public void populateConditionMap(Map<String, BaseCondition> baseConditionMap) {

    }

    public JUnitFlowManager(Context context, String jsonPath) {
        super(context, jsonPath);
    }


}
