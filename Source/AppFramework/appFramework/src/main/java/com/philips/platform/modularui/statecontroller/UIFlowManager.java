/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.modularui.statecontroller;

import android.content.Context;

/**
 * Flow manager class is used for navigating from one state to other state
 */
public class UIFlowManager {
    UIState currentState;

    /** getter for current state
     *
     * @return
     */
    public UIState getCurrentState() {
        return currentState;
    }

    /**
     * setter for current state
     * @param currentState
     */

    public void setCurrentState(UIState currentState) {
        this.currentState = currentState;
    }

    // TODO: Presenter should be represented as an interface

    /**
     * For naviating to next state
     * @param uiState requires Uistate object
     * @param context requires context
     */
    public void navigateToState(UIState uiState, Context context) {
        uiState.navigate(context);
        setCurrentState(uiState);
    }
}
