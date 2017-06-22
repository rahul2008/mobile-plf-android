/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.flowmanager;

import com.philips.platform.appframework.flowmanager.base.BaseCondition;
import com.philips.platform.appframework.flowmanager.base.BaseFlowManager;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.appframework.stateimpl.DemoAppInfraState;
import com.philips.platform.appframework.stateimpl.DemoCCState;
import com.philips.platform.appframework.stateimpl.DemoDSState;
import com.philips.platform.appframework.stateimpl.DemoIAPState;
import com.philips.platform.appframework.stateimpl.DemoPRState;
import com.philips.platform.appframework.stateimpl.DemoURState;
import com.philips.platform.appframework.stateimpl.DemoUappState;
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
		uiStateMap.put(AppStates.TESTAPPINFRA,new DemoAppInfraState());
        uiStateMap.put(AppStates.TESTUR,new DemoURState());
        uiStateMap.put(AppStates.TESTPR,new DemoPRState());
        uiStateMap.put(AppStates.TESTIAP,new DemoIAPState());
        uiStateMap.put(AppStates.TESTUAPP,new DemoUappState());
        uiStateMap.put(AppStates.TESTDATASERVICE,new DemoDSState());
        uiStateMap.put(AppStates.TESTCC,new DemoCCState());
    }

    public void populateConditionMap(final Map<String, BaseCondition> baseConditionMap) {
        baseConditionMap.put(AppConditions.IS_LOGGED_IN, new ConditionIsLoggedIn());
        baseConditionMap.put(AppConditions.IS_DONE_PRESSED, new ConditionIsDonePressed());
        baseConditionMap.put(AppConditions.CONDITION_APP_LAUNCH, new ConditionAppLaunch());
    }

}
