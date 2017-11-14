/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya;

import android.content.Context;

import com.philips.cdp.registration.User;
import com.philips.platform.catk.CatkConsentAccessToolKitManipulator;
import com.philips.platform.catk.injection.CatkComponent;
import com.philips.platform.mya.launcher.MyaInterface;
import com.philips.platform.mya.launcher.MyaSettings;
import com.philips.platform.mya.mock.ActionBarListenerMock;
import com.philips.platform.mya.mock.ActivityLauncherMock;
import com.philips.platform.mya.mock.AppInfraInterfaceMock;
import com.philips.platform.mya.mock.ContextMock;
import com.philips.platform.mya.mock.FragmentActivityMock;
import com.philips.platform.mya.mock.FragmentLauncherMock;
import com.philips.platform.mya.mock.FragmentManagerMock;
import com.philips.platform.mya.mock.FragmentTransactionMock;
import com.philips.platform.mya.mock.LaunchInputMock;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class MyaInterfaceTest {

    @Mock
    User mockUser;

    @Mock
    private CatkComponent mockCatkComponent;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        myaInterface = new MyaInterface();
        fragmentTransaction = new FragmentTransactionMock();
        fragmentManager = new FragmentManagerMock(fragmentTransaction);
        fragmentActivity = new FragmentActivityMock(fragmentManager);
        appInfra = new AppInfraInterfaceMock();
        launchInput = new LaunchInputMock();
        actionBarListener = new ActionBarListenerMock();
        context = new ContextMock();
        CatkConsentAccessToolKitManipulator.setCatkComponent(mockCatkComponent);
        when(mockCatkComponent.getUser()).thenReturn(mockUser);
    }

    @After
    public void tearDown() {
        CatkConsentAccessToolKitManipulator.clearCatkComponent();
    }

    @Test
    public void launchWithFragmentLauncher_correctFragmentIsReplacedInContainer() {
        givenFragmentLauncher(fragmentActivity, A_SPECIFIC_CONTAINER_ID, actionBarListener);
        givenLaunchInput();
        whenCallingLaunchWithAddToBackstack();
        thenReplaceWasCalledWith(A_SPECIFIC_CONTAINER_ID, MyaFragment.class, MYAFRAGMENT);
        thenAddToBackStackWasCalled(MYAFRAGMENT);
        thenCommitAllowingStateLossWasCalled();
        thenFragmentHasBundle();
    }

    @Test
    public void launchWithFragmentLauncher_dontCallAddToBackStackWhenNotDemanded() {
        givenFragmentLauncher(fragmentActivity, A_SPECIFIC_CONTAINER_ID, actionBarListener);
        givenLaunchInput();
        whenCallingLaunchWithoutAddToBackstack();
        thenReplaceWasCalledWith(A_SPECIFIC_CONTAINER_ID, MyaFragment.class, MYAFRAGMENT);
        thenAddToBackStackWasNotCalled();
        thenCommitAllowingStateLossWasCalled();
        thenFragmentHasBundle();
    }

    @Test
    public void launchWithActivityLauncher_correctFragmentIsReplacedInContainer() {
        givenActivityLauncher();
        givenLaunchInput(launchInput);
        whenCallingLaunchWithoutAddToBackstack();
        thenStartActivityWasCalledWithIntent();
    }

    private void givenLaunchInput() {
        givenLaunchInput = new MyaLaunchInput();
    }

    private void givenLaunchInput(MyaLaunchInput launchInput) {
        givenLaunchInput = launchInput;
    }

    private void givenFragmentLauncher(FragmentActivityMock fragmentActivity, int containerId, ActionBarListener actionBarListener) {
        givenFragmentLauncher = new FragmentLauncherMock(fragmentActivity, containerId, actionBarListener);
        givenUiLauncher = givenFragmentLauncher;
    }

    private void givenActivityLauncher() {
        givenActivityLauncher = new ActivityLauncherMock(null, null, 0, null);
        givenUiLauncher = givenActivityLauncher;
    }

    private void whenCallingLaunchWithAddToBackstack() {
        myaInterface.init(new MyaDependencies(appInfra), new MyaSettings(context));
        givenLaunchInput.addToBackStack(true);
        myaInterface.launch(givenUiLauncher, givenLaunchInput);
    }

    private void whenCallingLaunchWithoutAddToBackstack() {
        myaInterface.init(new MyaDependencies(appInfra), new MyaSettings(context));
        givenLaunchInput.addToBackStack(false);
        myaInterface.launch(givenUiLauncher, givenLaunchInput);
    }

    private void thenReplaceWasCalledWith(int expectedParentContainerId, Class<?> expectedFragmentClass, String expectedTag) {
        assertEquals(expectedParentContainerId, fragmentTransaction.replace_containerId);
        assertTrue(fragmentTransaction.replace_fragment.getClass().isAssignableFrom(expectedFragmentClass));
        assertEquals(expectedTag, fragmentTransaction.replace_tag);
    }

    private void thenAddToBackStackWasCalled(String expectedBackStackId) {
        assertEquals(expectedBackStackId, fragmentTransaction.addToBackStack_backStackId);
    }

    private void thenAddToBackStackWasNotCalled() {
        assertNull(fragmentTransaction.addToBackStack_backStackId);
    }

    private void thenFragmentHasBundle() {
        assertNotNull(fragmentTransaction.replace_fragment.getArguments());
    }

    private void thenCommitAllowingStateLossWasCalled() {
        assertTrue(fragmentTransaction.commitAllowingStateLossWasCalled);
    }

    private void thenStartActivityWasCalledWithIntent() {
        assertNotNull(launchInput.context.startActivity_intent);
    }

    private MyaInterface myaInterface;

    private UiLauncher givenUiLauncher;
    private ActivityLauncherMock givenActivityLauncher;
    private FragmentLauncherMock givenFragmentLauncher;
    private MyaLaunchInput givenLaunchInput;

    private ActionBarListener actionBarListener;
    private FragmentActivityMock fragmentActivity;
    private FragmentTransactionMock fragmentTransaction;
    private FragmentManagerMock fragmentManager;
    private LaunchInputMock launchInput;
    private AppInfraInterfaceMock appInfra;
    private Context context;

    private final int A_SPECIFIC_CONTAINER_ID = 12345678;
    public static final String MYAFRAGMENT = "MYAFRAGMENT";

}