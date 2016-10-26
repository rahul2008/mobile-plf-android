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
        BaseState baseState = mock(BaseState.class);
        uiFlowManager.setCurrentState(baseState);
        assertEquals(baseState, uiFlowManager.getCurrentState());
    }

    public void testNavigateToState() {
        BaseState baseState = mock(BaseState.class);
        UiLauncher uiLauncher = mock(UiLauncher.class);
        uiFlowManager.navigateToState(baseState, uiLauncher);
        verify(baseState).navigate(uiLauncher);
        assertEquals(baseState, uiFlowManager.getCurrentState());
    }
}
