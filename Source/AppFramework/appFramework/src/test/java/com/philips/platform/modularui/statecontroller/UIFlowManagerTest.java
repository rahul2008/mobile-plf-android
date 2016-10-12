/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.modularui.statecontroller;

import com.philips.platform.uappframework.launcher.UiLauncher;

import junit.framework.TestCase;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Flow manager class is used for navigating from one state to other state
 */
public class UIFlowManagerTest extends TestCase {

    private UIFlowManager uiFlowManager;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        uiFlowManager = new UIFlowManager();
    }

    public void testSetCurrentState() {
        UIState uiState = mock(UIState.class);
        uiFlowManager.setCurrentState(uiState);
        assertEquals(uiState, uiFlowManager.getCurrentState());
    }

    public void testNavigateToState() {
        UIState uiState = mock(UIState.class);
        UiLauncher uiLauncher = mock(UiLauncher.class);
        uiFlowManager.navigateToState(uiState, uiLauncher);
        verify(uiState).navigate(uiLauncher);
        assertEquals(uiState, uiFlowManager.getCurrentState());
    }
}
