/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.flowmanager;

import com.philips.platform.appframework.flowmanager.base.BaseCondition;
import com.philips.platform.appframework.flowmanager.base.BaseFlowManager;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.appframework.stateimpl.HamburgerActivityState;
import com.philips.platform.appframework.testmicroappfw.stateimpl.TestFragmentState;
import com.philips.platform.baseapp.FlowManagerUtil;
import com.philips.platform.baseapp.condition.ConditionAppLaunch;
import com.philips.platform.baseapp.condition.ConditionIsDonePressed;
import com.philips.platform.baseapp.condition.ConditionIsLoggedIn;

import java.util.Map;

public class FlowManager extends BaseFlowManager {

    public void populateStateMap(final Map<String, BaseState> uiStateMap) {
        new FlowManagerUtil().addValuesToMap(uiStateMap);
        uiStateMap.put(AppStates.HAMBURGER_HOME, new HamburgerActivityState());
        uiStateMap.put(AppStates.TEST_MICROAPP,new TestFragmentState());
    }

    public void populateConditionMap(final Map<String, BaseCondition> baseConditionMap) {
        baseConditionMap.put(AppConditions.IS_LOGGED_IN, new ConditionIsLoggedIn());
        baseConditionMap.put(AppConditions.IS_DONE_PRESSED, new ConditionIsDonePressed());
        baseConditionMap.put(AppConditions.CONDITION_APP_LAUNCH, new ConditionAppLaunch());
    }

}
