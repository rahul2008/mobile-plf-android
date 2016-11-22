/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.modularui.statecontroller;

import android.support.v4.app.FragmentActivity;

import com.philips.platform.appframework.flowmanager.FlowManager;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.appframework.JUnitFlowManager;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.baseapp.base.FileUtility;
import com.philips.platform.baseapp.screens.homefragment.HomeFragmentState;
import com.philips.platform.baseapp.screens.splash.SplashState;
import com.philips.platform.uappframework.launcher.UiLauncher;

import junit.framework.TestCase;

import java.io.File;
import java.io.InputStream;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Flow manager class is used for navigating from one state to other state
 */

public class FlowManagerTest extends TestCase {

    private JUnitFlowManager uiFlowManager;
    private FragmentActivity fragmentActivityMock;
    private AppFrameworkApplication appFrameworkApplication;
    private FileUtility fileUtility;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        fragmentActivityMock = mock(FragmentActivity.class);
        appFrameworkApplication = mock(AppFrameworkApplication.class);
        fileUtility = new FileUtility(appFrameworkApplication);

    }

    public void testGetNextState(){
        final SplashState splashState = mock(SplashState.class);
        final FlowManager flowManager = mock(FlowManager.class);
        when(flowManager.getNextState(AppStates.FIRST_STATE,"onAppLaunch")).thenReturn(splashState);
        assertTrue(splashState instanceof SplashState);
    }

    public void testCurrentState(){
        final HomeFragmentState homeFragmentState = mock(HomeFragmentState.class);
        final FlowManager flowManager = mock(FlowManager.class);
        when(flowManager.getNextState(AppStates.HAMBURGER_HOME,"home_fragment")).thenReturn(homeFragmentState);
        when(flowManager.getCurrentState()).thenReturn(homeFragmentState);
        assertEquals(flowManager.getCurrentState(),homeFragmentState);
    }

    public void testGetState(){
        final FileUtility fileUtility = mock(FileUtility.class);
        final File file = mock(File.class);
        when(fileUtility.createFileFromInputStream(R.string.com_philips_app_fmwk_app_flow_url)).thenReturn(file);
        FlowManager flowManager = new FlowManager(appFrameworkApplication,fileUtility.createFileFromInputStream(R.string.com_philips_app_fmwk_app_flow_url).getPath());
        assertTrue(flowManager.getState(AppStates.SPLASH) instanceof SplashState);
    }
}

