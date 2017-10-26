package com.philips.platform.mya;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.mya.mock.AppInfraInterfaceMock;
import com.philips.platform.mya.mock.FragmentActivityMock;
import com.philips.platform.mya.mock.FragmentLauncherMock;
import com.philips.platform.mya.mock.FragmentManagerMock;
import com.philips.platform.mya.mock.FragmentTransactionMock;
import com.philips.platform.uappframework.listener.ActionBarListener;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MyaInterfaceTest {

    @Before
    public void setup () {
        myaInterface = new MyaInterface();
        fragmentTransaction = new FragmentTransactionMock();
        fragmentManager = new FragmentManagerMock(fragmentTransaction);
        fragmentActivity = new FragmentActivityMock(fragmentManager);
        appInfra = new AppInfraInterfaceMock();
    }

    @Test
    public void launchWithFragmentLauncher_CorrectFragmentIsReplacedInContainer() {
        givenFragmentLauncher(fragmentActivity, A_SPECIFIC_CONTAINER_ID, actionBarListener);
        givenSettings("applicationName1", "propositionName");
        whenCallingLaunchWithAddToBackstack();
        thenReplaceWasCalledWith(A_SPECIFIC_CONTAINER_ID, MyaFragment.class, MYAFRAGMENT);
        thenAddToBackStackWasCalled(MYAFRAGMENT);
        thenCommitAllowingStateLossWasCalled();
        thenFragmentHasBundle();
    }

    @Test
    public void launchWithFragmentLauncher_dontCallAddToBackStackWhenNotDemanded() {
        givenFragmentLauncher(fragmentActivity, A_SPECIFIC_CONTAINER_ID, actionBarListener);
        givenSettings("applicationName1", "propositionName");
        whenCallingLaunchWithoutAddToBackstack();
        thenReplaceWasCalledWith(A_SPECIFIC_CONTAINER_ID, MyaFragment.class, MYAFRAGMENT);
        thenAddToBackStackWasNotCalled();
        thenCommitAllowingStateLossWasCalled();
        thenFragmentHasBundle();
    }

    private void thenFragmentHasBundles(String expectedApplicationName, String expectedPropositionName) {
        assertEquals(expectedApplicationName, fragmentTransaction.replace_fragment.getArguments().getString("appName"));
        assertEquals(expectedPropositionName, fragmentTransaction.replace_fragment.getArguments().getString("propName"));
    }

    private void givenSettings(String applicationName, String propositionName) {
        myaInterface.init(new MyaDependencies(appInfra), new MyaSettings(null, applicationName, propositionName));
    }

    private void givenFragmentLauncher(FragmentActivityMock fragmentActivity, int containerId, ActionBarListener actionBarListener) {
        givenFragmentLauncher = new FragmentLauncherMock(fragmentActivity, containerId, actionBarListener);
    }

    private void whenCallingLaunchWithAddToBackstack() {
        MyaLaunchInput myaLaunchInput = new MyaLaunchInput();
        myaLaunchInput.addToBackStack(true);
        myaInterface.launch(givenFragmentLauncher, myaLaunchInput);
    }

    private void whenCallingLaunchWithoutAddToBackstack() {
        MyaLaunchInput myaLaunchInput = new MyaLaunchInput();
        myaLaunchInput.addToBackStack(false);
        myaInterface.launch(givenFragmentLauncher, myaLaunchInput);
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

    private MyaInterface myaInterface;

    private FragmentLauncherMock givenFragmentLauncher;
    private MyaSettings myaSettings;

    private int containerId = 12345678;
    private ActionBarListener actionBarListener;
    private FragmentActivityMock fragmentActivity;
    private FragmentTransactionMock fragmentTransaction;
    private FragmentManagerMock fragmentManager;
    private AppInfraInterface appInfra;

    private final int A_SPECIFIC_CONTAINER_ID = 12345678;
    public static final String MYAFRAGMENT = "MYAFRAGMENT";

}