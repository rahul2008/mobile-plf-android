package com.philips.platform.flowmanagerjson;

import android.content.Context;

import com.philips.platform.flowmanagerjson.condition.AppConditions;
import com.philips.platform.flowmanagerjson.condition.BaseCondition;
import com.philips.platform.flowmanagerjson.condition.ConditionFactory;
import com.philips.platform.flowmanagerjson.jsonstates.AppStates;
import com.philips.platform.flowmanagerjson.pojo.AppFlowModel;
import com.philips.platform.flowmanagerjson.pojo.AppFlowNextState;
import com.philips.platform.modularui.statecontroller.UIState;

import java.util.Map;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class FlowManagerJson {

    //Singleton instance of FlowManager class
    private static FlowManagerJson flowManagerInstance;
    //Map of states and nex tSates which decides the Application flow
    private Map<AppStates, AppFlowNextState[]> mAppFlowMap;
    //Object to hold first state of the app flow.
    private AppStates mFirstState;
    //Object to hold UI state factory instance
    private UIStateFactory mUIStateFactory;
    //Object to hold UI Condition factory instance
    private ConditionFactory mConditionFactory;
    //current context
    private Context mContext;

    /**
     * Private constructor of Flow manager class. Initialises objects and get the Application Flow.
     *
     * @param context The context to use.  Usually your {@link android.app.Application}
     *                or {@link android.app.Activity} object.
     */
    private FlowManagerJson(Context context) {
        mAppFlowMap = null;
        mFirstState = null;
        setCurrentContext(context);
        mUIStateFactory = new UIStateFactory();
        mConditionFactory = new ConditionFactory();
        getAppFlowStates();
    }

    /**
     * Method which create and returns the singleton instance of the FlowManager class.
     *
     * @param context The context to use.  Usually your {@link android.app.Application}
     *                or {@link android.app.Activity} object.
     * @return singleton instance of 'FlowManager' class
     */
    synchronized public static FlowManagerJson getInstance(Context context) {
        if (flowManagerInstance == null) {
            flowManagerInstance = new FlowManagerJson(context);
        }
        flowManagerInstance.setCurrentContext(context);
        return flowManagerInstance;
    }

    /**
     * Method to set current context for flow manager.
     *
     * @param context The context to use.  Usually your {@link android.app.Application}
     *                or {@link android.app.Activity} object.
     */
    private void setCurrentContext(Context context) {
        mContext = context;
    }

    /**
     * This Method gets and stores FirstState and State to NextStates map from AppFlowDataParser.
     */
    private void getAppFlowStates() {
        final AppFlowModel appFlowModel = AppFlowDataParser.getAppFlow(mContext);
        if (appFlowModel != null && appFlowModel.getAppFlow() != null) {
            mFirstState = AppStates.get(appFlowModel.getAppFlow().getFirstState());
            mAppFlowMap = AppFlowDataParser.getAppFlowMap(appFlowModel.getAppFlow());
        }
    }

    /**
     * This method will return first state of the app.
     *
     * @return First state of the app.
     */
    public AppStates getFirstState() {
        return mFirstState;
    }

    /**
     * Method to get object of next UIState based on the current state of the App.
     *
     * @param currentState current state of the app.
     * @return Object to next UIState if available or 'null'.
     */
    public UIState getNextState(AppStates currentState) {
        //Getting the list of all possible next state for the give 'currentState'.
        final AppFlowNextState[] appFlowNextStates = mAppFlowMap.get(currentState);
        if (appFlowNextStates != null) {
            //Looping through all the possible next states and returning the state which satisfies
            // any entry condition.
            for (final AppFlowNextState appFlowNextState : appFlowNextStates) {
                //boolean to hold the status of entry condition for the 'appFlowNextState'.
                boolean isConditionSatisfies = true;
                //Getting list of all possible entry conditions
                String[] conditionsTypes = appFlowNextState.getCondition();
                //Looping through all the possible condition.
                //Set 'isConditionSatisfies' to 'true' is any of the condition satisfies.
                //If any of condition satisfies other set it to 'false'.
                if (conditionsTypes != null && conditionsTypes.length > 0) {
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
                    return getUIState(AppStates.get(appFlowNextState.getState()));
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
}
