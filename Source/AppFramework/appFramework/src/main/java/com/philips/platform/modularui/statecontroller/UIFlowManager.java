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
    BaseState currentState;

    /** getter for current state
     *
     * @return
     */
    public BaseState getCurrentState() {
        return currentState;
    }

    /**
     * setter for current state
     * @param currentState
     */

    public void setCurrentState(BaseState currentState) {
        this.currentState = currentState;
    }

    /**
     * For naviating to next state
     * @param baseState requires Uistate object
     * @param uiLauncher requires UiLauncher object
     */
    public void navigateToState(BaseState baseState, UiLauncher uiLauncher) {
        baseState.navigate(uiLauncher);
        setCurrentState(baseState);
    }
}
