package com.philips.platform.appframework.flowmanager;

import android.content.Context;

import com.philips.platform.flowmanager.AppFrameworkDataParser;
import com.philips.platform.flowmanager.condition.AppConditions;
import com.philips.platform.flowmanager.condition.BaseCondition;
import com.philips.platform.flowmanager.condition.ConditionFactory;
import com.philips.platform.flowmanager.jsonstates.AppStates;
import com.philips.platform.flowmanager.jsonstates.EventStates;
import com.philips.platform.flowmanager.pojo.AppFlowModel;
import com.philips.platform.flowmanager.pojo.Event;
import com.philips.platform.flowmanager.pojo.NextState;
import com.philips.platform.modularui.statecontroller.BaseUiFlowManager;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.uappframework.launcher.UiLauncher;

import java.util.List;
import java.util.Map;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

public class FlowManager extends BaseUiFlowManager {

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

    private Context mContext;

    private FlowManager(Context context) {
        mContext = context;
        mUIStateFactory = new UIStateFactory();
        mConditionFactory = new ConditionFactory();
        getAppFlowStates(context);
    }

    public static FlowManager getInstance(Context context) {
        if (flowManager == null)
            flowManager = new FlowManager(context);

        return flowManager;
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
        return getUIState(mFirstState);
    }

    @Override
    public void navigateToNextState(String eventID, UiLauncher uiLauncher) {
    }

    @Override
    public UIState getState(final String eventID) {
        return null;
    }

    /**
     * Method to get object of next UIState based on the current state of the App.
     *
     * @param currentState current state of the app.
     * @return Object to next UIState if available or 'null'.
     */
    public UIState getNextState(AppStates currentState, EventStates eventData) {
        //Getting the list of all possible next state for the give 'currentState'.
        final List<Event> events = mAppFlowMap.get(currentState);

        if (events != null) {
            //Looping through all the possible next states and returning the state which satisfies
            // any entry condition.
            for (final Event event : events) {
                //boolean to hold the status of entry condition for the 'event'.

                final EventStates eventStates = EventStates.get(event.getEventId());
                if (event.getEventId() != null && eventStates == eventData) {
                    final List<NextState> nextStates = event.getNextStates();
                    //Getting list of all possible entry conditions
                    for (NextState nextState : nextStates) {
                        boolean isConditionSatisfies = true;
                        List<String> conditionsTypes = nextState.getCondition();
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
     * Method to return the Object to UIState based on AppFlowState ID.
     *
     * @param state state ID.
     * @return Object to UIState if available or 'null'.
     */
    private UIState getUIState(AppStates state) {
        UIState uiState = null;
        if (state != null) {
            uiState = mUIStateFactory.getUIStateState(state);
        }
        return uiState;
    }
}
