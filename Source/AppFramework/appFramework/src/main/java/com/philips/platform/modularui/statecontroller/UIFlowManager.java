package com.philips.platform.modularui.statecontroller;

import android.content.Context;

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
    public void navigateToState(UIState uiState, Context context) {
        uiState.navigate(context);
        setCurrentState(uiState);
    }
}
