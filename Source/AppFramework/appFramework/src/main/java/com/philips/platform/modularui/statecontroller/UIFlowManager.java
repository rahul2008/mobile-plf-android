package com.philips.platform.modularui.statecontroller;

import android.content.Context;

import com.philips.platform.modularui.factorymanager.StateCreator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 310240027 on 7/6/2016.
 */
public class UIFlowManager {
    // TODO: Array of state objects
    List<UIState> stateList;
    UIState currentState;

    public UIState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(UIState currentState) {
        this.currentState = currentState;
    }

    // TODO: Presenter should be represented as an interface
    public void navigateToState(@UIState.UIStateDef int stateID, Context context, UIBasePresenter uiBasePresenter) {
        UIState uiState = null;
        if(containsState((ArrayList<UIState>) stateList,stateID)){
            uiState = getStateFromStateList(stateID);
            uiState.setPresenter(uiBasePresenter);

        }else {
            uiState = StateCreator.getInstance().getState(stateID,context);
            uiState.setPresenter(uiBasePresenter);
            addToStateList(uiState);
        }

        uiState.navigate(context);
        setCurrentState(uiState);
    }

    private boolean containsState(ArrayList<UIState> stateList, @UIState.UIStateDef int stateID) {
        for(UIState uiStateObject : stateList) {
            if(uiStateObject != null && uiStateObject.getStateID() == stateID) {
                return true;
            }
        }
        return false;
    }

    private void addToStateList(UIState uiState) {
        stateList.add(uiState);
    }

    private UIState getStateFromStateList(@UIState.UIStateDef int stateID) {
        for(UIState uiStateObject : stateList) {
            if(uiStateObject != null && uiStateObject.getStateID() == stateID) {
                return uiStateObject;
            }
        }
        return null;
    }
}
