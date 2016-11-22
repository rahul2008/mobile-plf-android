/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.modularui.statecontroller;

import android.support.v4.app.FragmentActivity;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.JUnitFlowManager;
import com.philips.platform.appframework.R;
import com.philips.platform.uappframework.launcher.UiLauncher;

import junit.framework.TestCase;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Flow manager class is used for navigating from one state to other state
 */

public class UIFlowManagerTest extends TestCase {

    private JUnitFlowManager uiFlowManager;
    private FragmentActivity fragmentActivityMock;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        fragmentActivityMock = mock(FragmentActivity.class);

    }

    public void testNavigateToState() {
        final AppFrameworkApplication appFrameworkApplicationMock = mock(AppFrameworkApplication.class);
        when(fragmentActivityMock.getApplicationContext()).thenReturn(appFrameworkApplicationMock);
        uiFlowManager = new JUnitFlowManager(appFrameworkApplicationMock, R.string.com_philips_app_fmwk_app_flow_url);
        BaseState baseState = mock(BaseState.class);
        when(baseState.getStateID()).thenReturn(BaseAppState.WELCOME);
        UiLauncher uiLauncher = mock(UiLauncher.class);
        baseState.navigate(uiLauncher);
        assertEquals(baseState.getStateID(),BaseAppState.WELCOME);
    }
}

