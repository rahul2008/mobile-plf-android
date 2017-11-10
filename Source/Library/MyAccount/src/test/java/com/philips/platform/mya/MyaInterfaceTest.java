/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.mya.mock.ActivityLauncherMock;
import com.philips.platform.mya.mock.AppInfraInterfaceMock;
import com.philips.platform.mya.mock.FragmentActivityMock;
import com.philips.platform.mya.mock.FragmentLauncherMock;
import com.philips.platform.mya.mock.FragmentManagerMock;
import com.philips.platform.mya.mock.FragmentTransactionMock;
import com.philips.platform.mya.mock.LaunchInputMock;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class MyaInterfaceTest {

    //@Before
    public void setup() {
        myaInterface = new MyaInterface();
        fragmentTransaction = new FragmentTransactionMock();
        fragmentManager = new FragmentManagerMock(fragmentTransaction);
        fragmentActivity = new FragmentActivityMock(fragmentManager);
        appInfra = new AppInfraInterfaceMock();
        launchInput = new LaunchInputMock();
    }

    //@Test
    public void launchWithFragmentLauncher_correctFragmentIsReplacedInContainer() {
        givenFragmentLauncher(fragmentActivity, A_SPECIFIC_CONTAINER_ID, actionBarListener);
        givenLaunchInput("applicationName1", "propositionName");
        whenCallingLaunchWithAddToBackstack();
        thenReplaceWasCalledWith(A_SPECIFIC_CONTAINER_ID, MyaFragment.class, MYAFRAGMENT);
        thenAddToBackStackWasCalled(MYAFRAGMENT);
        thenCommitAllowingStateLossWasCalled();
        thenFragmentHasBundle();
    }

    //@Test
    public void launchWithFragmentLauncher_dontCallAddToBackStackWhenNotDemanded() {
        givenFragmentLauncher(fragmentActivity, A_SPECIFIC_CONTAINER_ID, actionBarListener);
        givenLaunchInput("applicationName1", "propositionName");
        whenCallingLaunchWithoutAddToBackstack();
        thenReplaceWasCalledWith(A_SPECIFIC_CONTAINER_ID, MyaFragment.class, MYAFRAGMENT);
        thenAddToBackStackWasNotCalled();
        thenCommitAllowingStateLossWasCalled();
        thenFragmentHasBundle();
    }

    //@Test
    public void launchWithActivityLauncher_correctFragmentIsReplacedInContainer() {
        givenActivityLauncher();
        givenLaunchInput(launchInput);
        whenCallingLaunchWithoutAddToBackstack();
        thenStartActivityWasCalledWithIntent();
    }

    private void givenLaunchInput(String applicationName, String propositionName) {
        givenLaunchInput = new MyaLaunchInput();
        givenLaunchInput.setApplicationName(applicationName);
        givenLaunchInput.setPropositionName(propositionName);
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
        myaInterface.init(new MyaDependencies(appInfra), new MyaSettings(null));
        givenLaunchInput.addToBackStack(true);
        myaInterface.launch(givenUiLauncher, givenLaunchInput);
    }

    private void whenCallingLaunchWithoutAddToBackstack() {
        myaInterface.init(new MyaDependencies(appInfra), new MyaSettings(null));
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
    private MyaSettings myaSettings;

    private int containerId = 12345678;
    private ActionBarListener actionBarListener;
    private FragmentActivityMock fragmentActivity;
    private FragmentTransactionMock fragmentTransaction;
    private FragmentManagerMock fragmentManager;
    private LaunchInputMock launchInput;
    private AppInfraInterface appInfra;

    private final int A_SPECIFIC_CONTAINER_ID = 12345678;
    public static final String MYAFRAGMENT = "MYAFRAGMENT";

}