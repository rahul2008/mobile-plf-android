package com.philips.platform.appframework.flowmanager;

import com.philips.platform.appframework.flows.SplashFlow;
import com.philips.platform.appframework.flows.WelcomeFlow;
import com.philips.platform.modularui.statecontroller.BaseFlow;
import com.philips.platform.modularui.statecontroller.BaseUiFlowManager;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.modularui.stateimpl.HomeActivityState;
import com.philips.platform.modularui.stateimpl.HomeFragmentState;
import com.philips.platform.modularui.stateimpl.UserRegistrationState;
import com.philips.platform.modularui.stateimpl.WelcomeState;
import com.philips.platform.uappframework.launcher.UiLauncher;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

public class FlowManager extends BaseUiFlowManager {

    private static final int WELCOME_FLOW = 1006;
    private static final int SPLASH_FLOW = 1007;
    private static Map<Integer, UIState> uiStateHashMap;
    private static Map<Integer, BaseFlow> baseFlowMap;
    private static Map<String, Integer> eventFlowMap;

    private static void initFlowMap() {
        baseFlowMap = new TreeMap<>();
        baseFlowMap.put(WELCOME_FLOW, new WelcomeFlow());
        baseFlowMap.put(SPLASH_FLOW, new SplashFlow());
    }

    private static void initEventFlowMap() {
        eventFlowMap = new HashMap<>();
        eventFlowMap.put("welcome_done", WELCOME_FLOW);
        eventFlowMap.put("welcome_skip", WELCOME_FLOW);
        eventFlowMap.put("welcome_fragment_home", WELCOME_FLOW);
        eventFlowMap.put("welcome_activity_home", WELCOME_FLOW);

        eventFlowMap.put("splash_navigate_home", SPLASH_FLOW);
        eventFlowMap.put("splash_navigate_welcome", SPLASH_FLOW);
    }

    private static void initStates() {
        uiStateHashMap = new TreeMap<>();
        uiStateHashMap.put(UIState.UI_WELCOME_STATE, new WelcomeState());
        uiStateHashMap.put(UIState.UI_HOME_FRAGMENT_STATE, new HomeFragmentState());
        uiStateHashMap.put(UIState.UI_HOME_STATE, new HomeActivityState());
        uiStateHashMap.put(UIState.UI_USER_REGISTRATION_STATE, new UserRegistrationState());
    }

    @Override
    public void initFlowManager() {

    }

    @Override
    public UIState getFirstState() {
        return null;
    }

    @Override
    public void navigateToNextState(String eventID, UiLauncher uiLauncher) {
        final Integer stateId = eventFlowMap.get(eventID);
        final BaseFlow baseFlow = baseFlowMap.get(stateId);
        final Integer state = baseFlow.getNextState(eventID);
        final UIState uiState = uiStateHashMap.get(state);
        uiState.navigate(uiLauncher);
    }

    @Override
    public UIState getState(final String eventID) {
        final Integer stateId = eventFlowMap.get(eventID);
        final BaseFlow baseFlow = baseFlowMap.get(stateId);
        final Integer state = baseFlow.getNextState(eventID);
        return uiStateHashMap.get(state);
    }

    static {
        initStates();
        initEventFlowMap();
        initFlowMap();
    }
}
