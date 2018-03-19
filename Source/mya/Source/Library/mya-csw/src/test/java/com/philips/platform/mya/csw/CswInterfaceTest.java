package com.philips.platform.mya.csw;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;


import com.philips.platform.pif.chi.ConsentConfiguration;
import com.philips.platform.mya.csw.mock.ActivityLauncherMock;
import com.philips.platform.mya.csw.mock.AppInfraInterfaceMock;
import com.philips.platform.mya.csw.mock.FragmentActivityMock;
import com.philips.platform.mya.csw.mock.FragmentLauncherMock;
import com.philips.platform.mya.csw.mock.FragmentManagerMock;
import com.philips.platform.mya.csw.mock.FragmentTransactionMock;
import com.philips.platform.mya.csw.mock.LaunchInputMock;
import com.philips.platform.mya.csw.permission.PermissionView;
import com.philips.platform.uappframework.launcher.UiLauncher;

import android.test.mock.MockContext;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 25)
public class CswInterfaceTest {

    @Mock
    private List<ConsentConfiguration> consentConfigurations;

    @Before
    public void setup() {
        fragmentTransaction = new FragmentTransactionMock();
        fragmentManager = new FragmentManagerMock(fragmentTransaction);
        fragmentActivity = new FragmentActivityMock(fragmentManager);
        cswInterface = new CswInterface();
        appInfraInterface = new AppInfraInterfaceMock();
        context = new MockContext();
        CswDependencies cswDependencies = new CswDependencies(appInfraInterface, consentConfigurations);
        CswSettings cswSettings = new CswSettings(context);
        cswInterface.init(cswDependencies, cswSettings);
    }

    @Test
    public void launchReplacesWithPermissionViewOnParentContainer() throws InterruptedException {
        givenFragmentLauncherWithParentContainerId(A_SPECIFIC_CONTAINER_ID);
        givenLaunchInput();
        whenCallingLaunchWithAddToBackstack();
        thenReplaceWasCalledWith(A_SPECIFIC_CONTAINER_ID, PermissionView.class, PermissionView.TAG);
        thenAddToBackStackWasCalled(PermissionView.TAG);
        thenCommitAllowingStateLossWasCalled();
    }

    @Test
    public void launchWithActivityLauncher_correctFragmentIsReplacedInContainer() {
        givenActivityLauncher();
        givenLaunchInput();
        whenCallingLaunchWithoutAddToBackstack();
        thenStartActivityWasCalledWithIntent();
    }

    private void givenLaunchInput() {
        givenLaunchInput = new LaunchInputMock();
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
    }

    private void thenAddToBackStackWasCalled(String expectedBackStackId) {
        assertEquals(expectedBackStackId, fragmentTransaction.addToBackStack_backStackId);
    }

    private void thenCommitAllowingStateLossWasCalled() {
        assertTrue(fragmentTransaction.commitAllowingStateLossWasCalled);
    }

    private void thenStartActivityWasCalledWithIntent() {
        assertNotNull(givenLaunchInput.context.startActivity_intent);
    }

    private UiLauncher givenUiLauncher;

    private ActivityLauncherMock givenActivityLauncher;
    private FragmentLauncherMock givenFragmentLauncher;
    private LaunchInputMock givenLaunchInput;
    private AppInfraInterfaceMock appInfraInterface;
    private MockContext context;

    private FragmentActivityMock fragmentActivity;
    private FragmentTransactionMock fragmentTransaction;
    private FragmentManagerMock fragmentManager;

    private static final String CSWFRAGMENT = "CSWFRAGMENT";

    private CswInterface cswInterface;

    private static final int A_SPECIFIC_CONTAINER_ID = 938462837;

}