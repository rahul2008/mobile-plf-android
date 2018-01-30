/*
 *  Copyright (c) Koninklijke Philips N.V., 2017
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 */
package com.philips.platform.appframework.flowmanager;

import com.philips.platform.appframework.flowmanager.base.BaseCondition;
import com.philips.platform.appframework.flowmanager.base.BaseFlowManager;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.appframework.stateimpl.DemoAILState;
import com.philips.platform.appframework.stateimpl.DemoBLLState;
import com.philips.platform.appframework.stateimpl.DemoCMLState;
import com.philips.platform.appframework.stateimpl.DemoDCCState;
import com.philips.platform.appframework.stateimpl.DemoDataServicesState;
import com.philips.platform.appframework.stateimpl.DemoDevicePairingState;
import com.philips.platform.appframework.stateimpl.DemoDlsState;
import com.philips.platform.appframework.stateimpl.DemoIAPState;
import com.philips.platform.appframework.stateimpl.DemoPRGState;
import com.philips.platform.appframework.stateimpl.DemoThsState;
import com.philips.platform.appframework.stateimpl.DemoUFWState;
import com.philips.platform.appframework.stateimpl.DemoUSRState;
import com.philips.platform.appframework.stateimpl.HamburgerActivityState;
import com.philips.platform.appframework.stateimpl.TestFragmentState;
import com.philips.platform.baseapp.FlowManagerUtil;
import com.philips.platform.baseapp.condition.ConditionAppLaunch;
import com.philips.platform.baseapp.condition.ConditionIsDonePressed;
import com.philips.platform.baseapp.condition.ConditionIsLoggedIn;
import com.philips.platform.baseapp.screens.telehealthservices.TeleHealthServicesState;
import com.philips.platform.modularui.stateimpl.EWSFragmentState;

import java.util.Map;

public class FlowManager extends BaseFlowManager {

    public void populateStateMap(final Map<String, BaseState> uiStateMap) {
        new FlowManagerUtil().addValuesToMap(uiStateMap);
        uiStateMap.put(AppStates.HAMBURGER_HOME, new HamburgerActivityState());
        uiStateMap.put(AppStates.TEST_MICROAPP, new TestFragmentState());
        uiStateMap.put(AppStates.TESTAPPINFRA, new DemoAILState());
        uiStateMap.put(AppStates.TESTUR, new DemoUSRState());
        uiStateMap.put(AppStates.TESTPR, new DemoPRGState());
        uiStateMap.put(AppStates.TESTIAP, new DemoIAPState());
        uiStateMap.put(AppStates.TELEHEALTHSERVICES, new DemoThsState());
        uiStateMap.put(AppStates.TESTUAPP, new DemoUFWState());
        uiStateMap.put(AppStates.TESTDATASERVICE, new DemoDataServicesState());
        uiStateMap.put(AppStates.TESTCC, new DemoDCCState());
        uiStateMap.put(AppStates.TESTDICOMM, new DemoCMLState());
        uiStateMap.put(AppStates.TESTBLUELIB, new DemoBLLState());
        uiStateMap.put(AppStates.EWS, new EWSFragmentState());
        uiStateMap.put(AppStates.TEST_DEVICE_PAIRING, new DemoDevicePairingState());
        uiStateMap.put(AppStates.TESTDLS, new DemoDlsState());
        uiStateMap.put(AppStates.TELEHEALTHSERVICES, new TeleHealthServicesState());
    }

    public void populateConditionMap(final Map<String, BaseCondition> baseConditionMap) {
        baseConditionMap.put(AppConditions.IS_LOGGED_IN, new ConditionIsLoggedIn());
        baseConditionMap.put(AppConditions.IS_DONE_PRESSED, new ConditionIsDonePressed());
        baseConditionMap.put(AppConditions.CONDITION_APP_LAUNCH, new ConditionAppLaunch());
    }

}
