package com.philips.platform.mya.csw;

import android.content.Intent;
import android.os.Bundle;
import android.test.mock.MockContext;

import com.philips.platform.mya.csw.mock.ActivityLauncherMock;
import com.philips.platform.mya.csw.mock.AppInfraInterfaceMock;
import com.philips.platform.mya.csw.mock.FragmentActivityMock;
import com.philips.platform.mya.csw.mock.FragmentLauncherMock;
import com.philips.platform.mya.csw.mock.FragmentManagerMock;
import com.philips.platform.mya.csw.mock.FragmentTransactionMock;
import com.philips.platform.mya.csw.mock.LaunchInputMock;
import com.philips.platform.mya.csw.permission.PermissionFragment;
import com.philips.platform.mya.csw.utils.CswLogger;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappSettings;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@Config(constants = BuildConfig.class, sdk = 25)
@PrepareForTest({CswInterface.class, Intent.class, CswLogger.class})
public class CswInterfaceTest {

    @Mock
    private Intent intentMock;
    @Mock
    private Bundle bundle;
    private UiLauncher givenUiLauncher;
    private LaunchInputMock givenLaunchInput;
    private FragmentActivityMock fragmentActivity;
    private FragmentTransactionMock fragmentTransaction;
    private CswInterface cswInterface;
    private static final int A_SPECIFIC_CONTAINER_ID = 938462837;

    @Before
    public void setup() throws Exception {
        initMocks(this);

        PowerMockito.mockStatic(CswLogger.class);
        PowerMockito.whenNew(Intent.class).withAnyArguments().thenReturn(intentMock);
        PowerMockito.whenNew(Bundle.class).withAnyArguments().thenReturn(bundle);

        fragmentTransaction = new FragmentTransactionMock();
        FragmentManagerMock fragmentManager = new FragmentManagerMock(fragmentTransaction);
        fragmentActivity = new FragmentActivityMock(fragmentManager);
        cswInterface = new CswInterface();
        AppInfraInterfaceMock appInfraInterface = new AppInfraInterfaceMock();
        MockContext context = new MockContext();
        CswDependencies cswDependencies = new CswDependencies(appInfraInterface);
        CswSettings cswSettings = new CswSettings(context);
        cswInterface.init(cswDependencies, cswSettings);
    }

    @Test
    public void launchReplacesWithPermissionViewOnParentContainer() {
        givenFragmentLauncherWithParentContainerId(A_SPECIFIC_CONTAINER_ID);
        givenLaunchInput();
        whenCallingLaunchWithAddToBackstack();
        thenReplaceWasCalledWith(A_SPECIFIC_CONTAINER_ID, PermissionFragment.class);
        thenAddToBackStackWasCalled(PermissionFragment.TAG);
        thenCommitAllowingStateLossWasCalled();
    }

    @Test
    public void launchWithActivityLauncher_correctFragmentIsReplacedInContainer() {
        givenActivityLauncher();
        givenLaunchInput();
        whenCallingLaunchWithoutAddToBackstack();
        thenStartActivityWasCalledWithIntent();
    }

    @Test(expected = IllegalStateException.class)
    public void givenInterfaceCreated_andWrongDependenciesClass_whenInit_thenShouldThrowException() {
        UappDependencies dep = new UappDependencies(null);

        cswInterface.init(dep, new CswSettings(new MockContext()));
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
}