package com.philips.platform.mya.csw;

import android.content.Intent;
import android.test.mock.MockContext;

import com.philips.platform.mya.csw.mock.ActivityLauncherMock;
import com.philips.platform.mya.csw.mock.AppInfraInterfaceMock;
import com.philips.platform.mya.csw.mock.FragmentActivityMock;
import com.philips.platform.mya.csw.mock.FragmentLauncherMock;
import com.philips.platform.mya.csw.mock.FragmentManagerMock;
import com.philips.platform.mya.csw.mock.FragmentTransactionMock;
import com.philips.platform.mya.csw.mock.LaunchInputMock;
import com.philips.platform.mya.csw.permission.PermissionView;
import com.philips.platform.pif.chi.ConsentConfiguration;
import com.philips.platform.uappframework.launcher.UiLauncher;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@Config(constants = BuildConfig.class, sdk = 25)
@PrepareForTest({ CswInterface.class, Intent.class})
public class CswInterfaceTest {

    @Mock
    private List<ConsentConfiguration> consentConfigurations;
    @Mock
    private Intent intentMock;

    @Before
    public void setup() throws Exception {
        initMocks(this);

        PowerMockito.whenNew(Intent.class).withAnyArguments().thenReturn(intentMock);

        fragmentTransaction = new FragmentTransactionMock();
        FragmentManagerMock fragmentManager = new FragmentManagerMock(fragmentTransaction);
        fragmentActivity = new FragmentActivityMock(fragmentManager);
        cswInterface = new CswInterface();
        AppInfraInterfaceMock appInfraInterface = new AppInfraInterfaceMock();
        MockContext context = new MockContext();
        CswDependencies cswDependencies = new CswDependencies(appInfraInterface, consentConfigurations);
        CswSettings cswSettings = new CswSettings(context);
        cswInterface.init(cswDependencies, cswSettings);
    }

    @Test
    public void launchReplacesWithPermissionViewOnParentContainer() throws InterruptedException {
        givenFragmentLauncherWithParentContainerId(A_SPECIFIC_CONTAINER_ID);
        givenLaunchInput();
        whenCallingLaunchWithAddToBackstack();
        thenReplaceWasCalledWith(A_SPECIFIC_CONTAINER_ID, PermissionView.class);
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
        givenUiLauncher = new ActivityLauncherMock(null, null, 0, null);
    }

    public void givenFragmentLauncherWithParentContainerId(int parentContainerId) {
        givenUiLauncher = new FragmentLauncherMock(fragmentActivity, parentContainerId, null);
    }

    private void whenCallingLaunchWithAddToBackstack() {
        givenLaunchInput.addToBackStack(true);
        cswInterface.launch(givenUiLauncher, givenLaunchInput);
    }

    private void whenCallingLaunchWithoutAddToBackstack() {
        givenLaunchInput.addToBackStack(false);
        cswInterface.launch(givenUiLauncher, givenLaunchInput);
    }

    private void thenReplaceWasCalledWith(int expectedParentContainerId, Class<?> expectedFragmentClass) {
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
    private LaunchInputMock givenLaunchInput;
    private FragmentActivityMock fragmentActivity;
    private FragmentTransactionMock fragmentTransaction;
    private CswInterface cswInterface;
    private static final int A_SPECIFIC_CONTAINER_ID = 938462837;

}