package com.philips.platform.csw;

import android.test.mock.MockContext;

import com.philips.platform.consenthandlerinterface.BuildConfig;
import com.philips.platform.consenthandlerinterface.ConsentHandlerInterface;
import com.philips.platform.csw.mock.ActivityLauncherMock;
import com.philips.platform.csw.mock.AppInfraInterfaceMock;
import com.philips.platform.csw.mock.FragmentActivityMock;
import com.philips.platform.csw.mock.FragmentLauncherMock;
import com.philips.platform.csw.mock.FragmentManagerMock;
import com.philips.platform.csw.mock.FragmentTransactionMock;
import com.philips.platform.csw.mock.LaunchInputMock;
import com.philips.platform.csw.utils.CustomRobolectricRunner;
import com.philips.platform.uappframework.launcher.UiLauncher;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 25)
public class CswInterfaceTest {


    @Mock
    private ConsentHandlerInterface mockHandlerInterface;

    @Before
    public void setup() {
        fragmentTransaction = new FragmentTransactionMock();
        fragmentManager = new FragmentManagerMock(fragmentTransaction);
        fragmentActivity = new FragmentActivityMock(fragmentManager);
        cswInterface = new CswInterface();
        appInfraInterface = new AppInfraInterfaceMock();
        context = new MockContext();
        CswDependencies cswDependencies = new CswDependencies(appInfraInterface, mockHandlerInterface);
        CswSettings cswSettings = new CswSettings(context);
        cswInterface.init(cswDependencies, cswSettings);
    }

    @Test
    public void launchReplacesWithCswFragmentOnParentContainer() throws InterruptedException {
        givenFragmentLauncherWithParentContainerId(A_SPECIFIC_CONTAINER_ID);
        givenLaunchInput();
        whenCallingLaunchWithAddToBackstack();
        thenReplaceWasCalledWith(A_SPECIFIC_CONTAINER_ID, CswFragment.class, CSWFRAGMENT);
        thenAddToBackStackWasCalled(CSWFRAGMENT);
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
        assertEquals(expectedTag, fragmentTransaction.replace_tag);
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