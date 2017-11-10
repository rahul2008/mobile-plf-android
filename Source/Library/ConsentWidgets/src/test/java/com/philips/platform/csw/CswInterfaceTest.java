package com.philips.platform.csw;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.csw.mock.ActivityLauncherMock;
import com.philips.platform.csw.mock.FragmentActivityMock;
import com.philips.platform.csw.mock.FragmentLauncherMock;
import com.philips.platform.csw.mock.FragmentManagerMock;
import com.philips.platform.csw.mock.FragmentTransactionMock;
import com.philips.platform.csw.mock.LaunchInputMock;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


public class CswInterfaceTest {

    @Before
    public void setup() {
        fragmentTransaction = new FragmentTransactionMock();
        fragmentManager = new FragmentManagerMock(fragmentTransaction);
        fragmentActivity = new FragmentActivityMock(fragmentManager);
        cswInterface = new CswInterface();
        launchInput = new LaunchInputMock();

    }

    @Test
    public void launchReplacesWithCswFragmentOnParentContainer() {
        givenFragmentLauncherWithParentContainerId(A_SPECIFIC_CONTAINER_ID);
        givenLaunchInput(launchInput);
        whenCallingLaunchWithAddToBackstack();
        thenReplaceWasCalledWith(A_SPECIFIC_CONTAINER_ID, CswFragment.class, CSWFRAGMENT);
        thenAddToBackStackWasCalled(CSWFRAGMENT);
        thenCommitAllowingStateLossWasCalled();
    }

    @Test
    public void launchWithActivityLauncher_correctFragmentIsReplacedInContainer() {
        givenActivityLauncher();
        givenLaunchInput(launchInput);
        whenCallingLaunchWithoutAddToBackstack();
        thenStartActivityWasCalledWithIntent();
    }

    private void givenLaunchInput(CswLaunchInput launchInput) {
        givenLaunchInput = launchInput;
    }

    private void givenActivityLauncher() {
        givenActivityLauncher = new ActivityLauncherMock(null, null, 0, null);
        givenUiLauncher = givenActivityLauncher;
    }

    public void givenFragmentLauncherWithParentContainerId(int parentContainerId) {
        givenFragmentLauncher = new FragmentLauncherMock(fragmentActivity, parentContainerId, null);
        givenUiLauncher = givenFragmentLauncher;
    }

    private void whenCallingLaunchWithAddToBackstack() {
        givenLaunchInput.addToBackStack(true);
        cswInterface.launch(givenUiLauncher, givenLaunchInput);
    }

    private void whenCallingLaunchWithoutAddToBackstack() {
        givenLaunchInput.addToBackStack(false);
        cswInterface.launch(givenUiLauncher, givenLaunchInput);
    }

    private void thenReplaceWasCalledWith(int expectedParentContainerId, Class<?> expectedFragmentClass, String expectedTag) {
        assertEquals(expectedParentContainerId, fragmentTransaction.replace_containerId);
        assertTrue(fragmentTransaction.replace_fragment.getClass().isAssignableFrom(expectedFragmentClass));
        assertEquals(expectedTag, fragmentTransaction.replace_tag);
    }

    private void thenAddToBackStackWasCalled(String expectedBackStackId) {
        assertEquals(expectedBackStackId, fragmentTransaction.addToBackStack_backStackId);
    }

    private void thenCommitAllowingStateLossWasCalled() {
        assertTrue(fragmentTransaction.commitAllowingStateLossWasCalled);
    }

    private void thenStartActivityWasCalledWithIntent() {
        assertNotNull(launchInput.context.startActivity_intent);
    }

    private UiLauncher givenUiLauncher;
    private ActivityLauncherMock givenActivityLauncher;
    private FragmentLauncherMock givenFragmentLauncher;
    private CswLaunchInput givenLaunchInput;
    private CswSettings myaSettings;

    private int containerId = 12345678;
    private ActionBarListener actionBarListener;
    private FragmentActivityMock fragmentActivity;
    private FragmentTransactionMock fragmentTransaction;
    private FragmentManagerMock fragmentManager;
    private LaunchInputMock launchInput;
    private AppInfraInterface appInfra;

    private static final String CSWFRAGMENT = "CSWFRAGMENT";

    private CswInterface cswInterface;

    private static final int A_SPECIFIC_CONTAINER_ID = 938462837;
}