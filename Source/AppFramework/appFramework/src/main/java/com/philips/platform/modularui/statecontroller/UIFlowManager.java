/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.modularui.statecontroller;

import com.philips.platform.uappframework.launcher.UiLauncher;

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

    /**
     * For naviating to next state
     * @param uiState requires Uistate object
     * @param uiLauncher requires UiLauncher object
     */
    public void navigateToState(UIState uiState, UiLauncher uiLauncher) {
        uiState.navigate(uiLauncher);
        setCurrentState(uiState);
    }
}
