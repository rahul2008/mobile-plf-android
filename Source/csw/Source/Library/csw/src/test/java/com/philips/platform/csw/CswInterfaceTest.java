/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.platform.csw;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.philips.platform.csw.injection.CswComponent;
import com.philips.platform.csw.mock.AppInfraInterfaceMock;
import com.philips.platform.csw.mock.LaunchInputMock;
import com.philips.platform.csw.permission.PermissionFragment;
import com.philips.platform.csw.utils.CswLogger;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CswInterface.class, Intent.class, CswLogger.class})
public class CswInterfaceTest {

    @Mock
    private Intent intentMock;
    @Mock
    private Bundle bundle;
    @Mock
    private FragmentLauncher fragmentLauncher;
    @Mock
    private ActivityLauncher activityLauncher;
    @Mock
    private FragmentActivity activityMock;
    @Mock
    private FragmentManager fragmentManagerMock;
    @Mock
    private FragmentTransaction transactionMock;
    @Mock
    private Context contextMock;


    private LaunchInputMock givenLaunchInput;
    private CswInterface cswInterface;
    private static final int A_SPECIFIC_CONTAINER_ID = 938462837;

    @Before
    public void setup() throws Exception {
        initMocks(this);

        PowerMockito.mockStatic(CswLogger.class);
        PowerMockito.whenNew(Intent.class).withAnyArguments().thenReturn(intentMock);
        PowerMockito.whenNew(Bundle.class).withAnyArguments().thenReturn(bundle);

        when(fragmentLauncher.getFragmentActivity()).thenReturn(activityMock);
        when(activityMock.getSupportFragmentManager()).thenReturn(fragmentManagerMock);
        when(fragmentManagerMock.beginTransaction()).thenReturn(transactionMock);

        cswInterface = new CswInterface();
        AppInfraInterfaceMock appInfraInterface = new AppInfraInterfaceMock();
        CswDependencies cswDependencies = new CswDependencies(appInfraInterface);
        CswSettings cswSettings = new CswSettings(contextMock);
        cswInterface.init(cswDependencies, cswSettings);
    }

    @Test
    public void launchReplacesWithPermissionViewOnParentContainer() {
        when(fragmentLauncher.getParentContainerResourceID()).thenReturn(A_SPECIFIC_CONTAINER_ID);
        givenLaunchInput();
        whenCallingLaunchWithAddToBackstack(fragmentLauncher);
        thenReplaceWasCalledWith(A_SPECIFIC_CONTAINER_ID, PermissionFragment.class);
        thenAddToBackStackWasCalled(PermissionFragment.TAG);
        thenCommitAllowingStateLossWasCalled();
    }

    @Test
    public void launchWithActivityLauncher_correctFragmentIsReplacedInContainer() {
        givenLaunchInput();
        whenCallingLaunchWithoutAddToBackstack(activityLauncher);
        thenStartActivityWasCalledWithIntent();
    }

    @Test(expected = IllegalStateException.class)
    public void givenInterfaceCreated_andWrongDependenciesClass_whenInit_thenShouldThrowException() {
        UappDependencies dep = new UappDependencies(null);

        cswInterface.init(dep, new CswSettings(contextMock));
    }

    public void givenInterfaceCreated_andCorrectDependenciesClass_whenInit_thenShouldThrowException() {
        UappDependencies dep = new UappDependencies(null);

        cswInterface.init(dep, new CswSettings(contextMock));

        assertNotNull(cswInterface);
    }

    @Test
    public void givenInterfaceCreated_whenInit_thenShouldCallInitOnLogger() {
        verifyStatic(CswLogger.class);
        CswLogger.init();
    }

    @Test
    public void givenInterfaceCreated_whenInit_thenShouldCallEnableOnLogger() {
        verifyStatic(CswLogger.class);
        CswLogger.enableLogging();
    }

    @Test
    public void givenInterfaceCreated_whenGet_thenShouldAlwaysReturnObject() {
        CswInterface get = CswInterface.get();

        assertNotNull(get);
    }

    @Test
    public void givenInterfaceInitialized_whenGetCswComponent_thenShouldAlwaysReturnObject() {
        CswComponent component = CswInterface.getCswComponent();

        assertNotNull(component);
    }

    @Test
    public void givenInterfaceInitialized_whenLaunchAsFragment_thenShouldCallGetFragmentActivity() {
        givenLaunchInput();

        cswInterface.launch(fragmentLauncher, givenLaunchInput);

        verify(fragmentLauncher).getFragmentActivity();
    }

    public void givenInterfaceInitialized_whenLaunchAsActivity_thenShouldCallStartActivity() {
        givenLaunchInput();

        cswInterface.launch(activityLauncher, givenLaunchInput);

        verify(contextMock).startActivity((Intent) any());
    }

    private void givenLaunchInput() {
        givenLaunchInput = new LaunchInputMock();
    }

    private void whenCallingLaunchWithAddToBackstack(UiLauncher givenUiLauncher) {
        givenLaunchInput.addToBackStack(true);
        cswInterface.launch(givenUiLauncher, givenLaunchInput);
    }

    private void whenCallingLaunchWithoutAddToBackstack(UiLauncher givenUiLauncher) {
        givenLaunchInput.addToBackStack(false);
        cswInterface.launch(givenUiLauncher, givenLaunchInput);
    }

    private void thenReplaceWasCalledWith(int expectedParentContainerId, Class<? extends Fragment> expectedFragmentClass) {
        verify(transactionMock).replace(eq(expectedParentContainerId), isA(expectedFragmentClass), anyString());
    }

    private void thenAddToBackStackWasCalled(String expectedBackStackId) {
        verify(transactionMock).addToBackStack(eq(expectedBackStackId));
    }

    private void thenCommitAllowingStateLossWasCalled() {
        verify(transactionMock).commitAllowingStateLoss();
    }

    private void thenStartActivityWasCalledWithIntent() {
        assertNotNull(givenLaunchInput.context.startActivity_intent);
    }
}