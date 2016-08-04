/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.modularui.statecontroller;

import android.content.Context;

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
