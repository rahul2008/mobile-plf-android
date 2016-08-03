package com.philips.platform.modularui.statecontroller;

import android.content.Context;

import com.philips.platform.modularui.factorymanager.StateCreator;

/**
 * Created by 310240027 on 7/6/2016.
 */
public class UIFlowManager {
    UIState currentState;

    public UIState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(UIState currentState) {
        this.currentState = currentState;
    }

    // TODO: Presenter should be represented as an interface
    public void navigateToState(@UIState.UIStateDef int stateID, Context context, UIBasePresenter uiBasePresenter) {
            UIState uiState = StateCreator.getInstance().getState(stateID,context);
            uiState.setPresenter(uiBasePresenter);
            uiState.navigate(context);
            setCurrentState(uiState);
    }
}
