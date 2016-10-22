package com.philips.platform.appframework.flowmanager;

import android.content.Context;

import com.philips.platform.appframework.flows.SplashFlow;
import com.philips.platform.appframework.flows.WelcomeFlow;
import com.philips.platform.flowmanager.AppFrameworkDataParser;
import com.philips.platform.flowmanager.UIStateFactory;
import com.philips.platform.flowmanager.condition.AppConditions;
import com.philips.platform.flowmanager.condition.BaseCondition;
import com.philips.platform.flowmanager.condition.ConditionFactory;
import com.philips.platform.flowmanager.jsonstates.AppStates;
import com.philips.platform.flowmanager.pojo.AppFlowModel;
import com.philips.platform.flowmanager.pojo.Event;
import com.philips.platform.flowmanager.pojo.NextState;
import com.philips.platform.modularui.statecontroller.BaseFlow;
import com.philips.platform.modularui.statecontroller.BaseUiFlowManager;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.modularui.stateimpl.HomeFragmentState;
import com.philips.platform.modularui.stateimpl.HomeTabbedActivityState;
import com.philips.platform.modularui.stateimpl.SettingsFragmentState;
import com.philips.platform.modularui.stateimpl.UserRegistrationState;
import com.philips.platform.modularui.stateimpl.WelcomeState;
import com.philips.platform.uappframework.launcher.UiLauncher;

import java.util.HashMap;
import java.util.List;
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

    //Singleton instance of FlowManager class
    private static FlowManager flowManager;
    //Map of states and nex tSates which decides the Application flow
    private static Map<AppStates, List<Event>> mAppFlowMap;
    //Object to hold first state of the app flow.
    private static AppStates mFirstState;
    //Object to hold UI state factory instance
    private static UIStateFactory mUIStateFactory;
    //Object to hold UI Condition factory instance
    private static ConditionFactory mConditionFactory;

    private static Map<String, NextState> mEventNextStatesMap;
    private Context mContext;

    private static void initFlowMap() {
        baseFlowMap = new TreeMap<>();
        baseFlowMap.put(WELCOME_FLOW, new WelcomeFlow());
        baseFlowMap.put(SPLASH_FLOW, new SplashFlow());
    }

    private static void initEventFlowMap() {
        eventFlowMap = new HashMap<>();
        eventFlowMap.put("welcome_done", WELCOME_FLOW);
        eventFlowMap.put("welcome_skip", WELCOME_FLOW);
        eventFlowMap.put("welcome_ur", WELCOME_FLOW);
        eventFlowMap.put("welcome_fragment_home", WELCOME_FLOW);
        eventFlowMap.put("welcome_activity_home", WELCOME_FLOW);
        eventFlowMap.put("welcome_settings", WELCOME_FLOW);

        eventFlowMap.put("splash_navigate_home", SPLASH_FLOW);
        eventFlowMap.put("splash_navigate_welcome", SPLASH_FLOW);
    }

    private static void initStates() {
        uiStateHashMap = new TreeMap<>();
        uiStateHashMap.put(UIState.UI_WELCOME_STATE, new WelcomeState());
        uiStateHashMap.put(UIState.UI_HOME_FRAGMENT_STATE, new HomeFragmentState());
        uiStateHashMap.put(UIState.UI_HOME_STATE, new HomeTabbedActivityState());
        uiStateHashMap.put(UIState.UI_USER_REGISTRATION_STATE, new UserRegistrationState());
        uiStateHashMap.put(UIState.UI_SETTINGS_FRAGMENT_STATE, new SettingsFragmentState());
    }

    private static void getAppFlowStates(Context mContext) {
        final AppFlowModel appFlowModel = AppFrameworkDataParser.getAppFlow(mContext);
        if (appFlowModel != null && appFlowModel.getAppFlow() != null) {
            mFirstState = AppStates.get(appFlowModel.getAppFlow().getFirstState());
            mAppFlowMap = AppFrameworkDataParser.getAppFlowMap(appFlowModel.getAppFlow());
        }
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

    /**
     * Method to get object of next UIState based on the current state of the App.
     *
     * @param currentState current state of the app.
     * @return Object to next UIState if available or 'null'.
     */
    public UIState getNextState(AppStates currentState, String eventData) {
        //Getting the list of all possible next state for the give 'currentState'.
        final List<Event> events = mAppFlowMap.get(currentState);

        if (events != null) {
            //Looping through all the possible next states and returning the state which satisfies
            // any entry condition.
            for (final Event event : events) {
                //boolean to hold the status of entry condition for the 'event'.

                if (event.getEventId() != null && event.getEventId().equals(eventData)) {
                    final List<NextState> nextStates = event.getNextStates();
                    boolean isConditionSatisfies = true;
                    //Getting list of all possible entry conditions
                    for (NextState nextState :
                            nextStates) {
                        List<String> conditionsTypes = nextState.getCondition();
                        //Looping through all the possible condition.
                        //Set 'isConditionSatisfies' to 'true' is any of the condition satisfies.
                        //If any of condition satisfies other set it to 'false'.
                        if (conditionsTypes != null && conditionsTypes.size() > 0) {
                            for (final String conditionType : conditionsTypes) {
                                BaseCondition condition = mConditionFactory
                                        .getCondition(AppConditions.get(conditionType));
                                isConditionSatisfies = condition.isConditionSatisfies(mContext);
                                if (isConditionSatisfies)
                                    break;
                            }
                        }
                        //Return the UIState if the entry condition is satisfies.
                        if (isConditionSatisfies) {
                            return getUIState(AppStates.get(nextState.getNextState()));
                        }
                    }
                    break;
                }
            }
        }
        return null;
    }

    /**
     * Method to return the Object to UIState based on State ID.
     *
     * @param state state ID.
     * @return Object to UIState if available or 'null'.
     */
    public UIState getUIState(AppStates state) {
        UIState uiState = null;
        if (state != null) {
            uiState = mUIStateFactory.getUIStateState(state);
        }
        return uiState;
    }

    static {
        initStates();
        initEventFlowMap();
        initFlowMap();

        mUIStateFactory = new UIStateFactory();
        mConditionFactory = new ConditionFactory();
        getAppFlowStates(null);
    }
}
