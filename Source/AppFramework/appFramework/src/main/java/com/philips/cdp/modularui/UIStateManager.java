package com.philips.cdp.modularui;

import android.content.Context;

import com.philips.cdp.appframework.utility.SharedPreferenceUtility;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 310240027 on 6/16/2016.
 */
public class UIStateManager {

    public static UIStateManager instance = new UIStateManager();

    private UIStateManager() {

    }

    public static UIStateManager getInstance() {
        if (null == instance) {
            instance = new UIStateManager();
        }
        return instance;
    }

    public Map<Integer, UIStateBase> uiStateMap;

    public UIStateBase startState;

    public UIStateBase getCurrentState() {
        return currentState;
    }

    public void setCurrentState(UIStateBase currentState) {
        this.currentState = currentState;
    }

    public UIStateBase currentState;

    public void initAppStartState(Context mContext) {
        uiStateMap = new HashMap<Integer, UIStateBase>();
        if (!SharedPreferenceUtility.getInstance().contains(UIConstants.UI_START_STATUS)) {
            setCurrentState(UIStateFactory.getInstance().createUIState(UIConstants.UI_SPLASH_UNREGISTERED_STATE));
            SharedPreferenceUtility.getInstance().writePreferenceInt(UIConstants.UI_START_STATUS, UIConstants.UI_SPLASH_UNREGISTERED_STATE);
        }else {
            setCurrentState(UIStateFactory.getInstance().createUIState(SharedPreferenceUtility.getInstance().getPreferenceInt(UIConstants.UI_START_STATUS)));
        }

    }

    public void addToStateList(UIStateBase uiStateBase) {
        uiStateMap.put(uiStateBase.getStateID(), uiStateBase);
    }

    public Map<Integer, UIStateBase> getUiStateMap() {
        return uiStateMap;
    }

    public void setUiStateMap(Map<Integer, UIStateBase> uiStateMap) {
        this.uiStateMap = uiStateMap;
    }

    public UIStateBase getStateMap(@UIConstants.UIStateDef int stateID) {
        return uiStateMap.get(stateID);
    }

}
