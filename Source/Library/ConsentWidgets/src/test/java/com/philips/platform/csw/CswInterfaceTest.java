package com.philips.platform.csw;

import android.test.mock.MockContext;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.catk.ConsentAccessToolKitEmulator;
import com.philips.platform.catk.CswConsentAccessToolKitManipulator;
import com.philips.platform.catk.model.ConsentDefinition;
import com.philips.platform.csw.mock.ActivityLauncherMock;
import com.philips.platform.csw.mock.AppInfraInterfaceMock;
import com.philips.platform.csw.mock.FragmentActivityMock;
import com.philips.platform.csw.mock.FragmentLauncherMock;
import com.philips.platform.csw.mock.FragmentManagerMock;
import com.philips.platform.csw.mock.FragmentTransactionMock;
import com.philips.platform.csw.mock.LaunchInputMock;
import com.philips.platform.csw.utils.CustomRobolectricRunner;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(CustomRobolectricRunner.class)
@Config(constants = com.philips.platform.mya.consentaccesstoolkit.BuildConfig.class, sdk = 25)
public class CswInterfaceTest {


    @Before
    public void setup() {
        fragmentTransaction = new FragmentTransactionMock();
        fragmentManager = new FragmentManagerMock(fragmentTransaction);
        fragmentActivity = new FragmentActivityMock(fragmentManager);
        consentAccessToolKit = new ConsentAccessToolKitEmulator();
        cswInterface = new CswInterface();
        appInfraInterface = new AppInfraInterfaceMock();
        context = new MockContext();
        CswConsentAccessToolKitManipulator.setInstance(consentAccessToolKit);
        CswDependencies cswDependencies = new CswDependencies(appInfraInterface);
        CswSettings cswSettings = new CswSettings(context);
        cswInterface.init(cswDependencies, cswSettings);
    }

    @Test
    public void launchReplacesWithCswFragmentOnParentContainer() throws InterruptedException {
        givenFragmentLauncherWithParentContainerId(A_SPECIFIC_CONTAINER_ID);
        givenConsentBundleConfig(new ConsentBundleConfig(new ArrayList<ConsentDefinition>()));
        givenLaunchInput();
        whenCallingLaunchWithAddToBackstack();
        thenReplaceWasCalledWith(A_SPECIFIC_CONTAINER_ID, CswFragment.class, CSWFRAGMENT);
        thenAddToBackStackWasCalled(CSWFRAGMENT);
        thenCommitAllowingStateLossWasCalled();
    }

    @Test
    public void launchWithActivityLauncher_correctFragmentIsReplacedInContainer() {
        givenActivityLauncher();
        givenConsentBundleConfig(new ConsentBundleConfig(new ArrayList<ConsentDefinition>()));
        givenLaunchInput();
        whenCallingLaunchWithoutAddToBackstack();
        thenStartActivityWasCalledWithIntent();
    }

    private void givenLaunchInput() {
        givenLaunchInput = new LaunchInputMock(givenConfig);
    }

    private void givenConsentBundleConfig(ConsentBundleConfig config) {
        this.givenConfig = config;
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
    private ConsentAccessToolKitEmulator consentAccessToolKit;

    private static final String CSWFRAGMENT = "CSWFRAGMENT";

    private CswInterface cswInterface;
    private ConsentBundleConfig givenConfig;

    private static final int A_SPECIFIC_CONTAINER_ID = 938462837;

}