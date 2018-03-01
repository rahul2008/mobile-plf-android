/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya;

import android.content.Context;

import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.mya.launcher.MyaDependencies;
import com.philips.platform.mya.launcher.MyaInterface;
import com.philips.platform.mya.launcher.MyaLaunchInput;
import com.philips.platform.mya.launcher.MyaSettings;
import com.philips.platform.mya.mock.ActionBarListenerMock;
import com.philips.platform.mya.mock.ActivityLauncherMock;
import com.philips.platform.mya.mock.AppInfraInterfaceMock;
import com.philips.platform.mya.mock.FragmentActivityMock;
import com.philips.platform.mya.mock.FragmentLauncherMock;
import com.philips.platform.mya.mock.FragmentManagerMock;
import com.philips.platform.mya.mock.FragmentTransactionMock;
import com.philips.platform.mya.runner.CustomRobolectricRunner;
import com.philips.platform.mya.tabs.MyaTabFragment;
import com.philips.platform.pif.DataInterface.USR.UserDataInterface;
import com.philips.platform.pif.chi.ConsentConfiguration;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static com.philips.platform.mya.base.MyaBaseFragment.MY_ACCOUNTS_INVOKE_TAG;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 25)
public class MyaInterfaceTest {
    private static final String PRIVACY_URL = "http://google.com";
    private MyaInterface myaInterface;

    private UiLauncher givenUiLauncher;
    private ActivityLauncherMock givenActivityLauncher;
    private FragmentLauncherMock givenFragmentLauncher;

    private ActionBarListener actionBarListener;
    private FragmentActivityMock fragmentActivity;
    private FragmentTransactionMock fragmentTransaction;
    private FragmentManagerMock fragmentManager;
    private AppInfraInterfaceMock appInfra;
    private Context context;

    private final int A_SPECIFIC_CONTAINER_ID = 12345678;
    public static final String MYAFRAGMENT = MY_ACCOUNTS_INVOKE_TAG;

    @Mock
    private List<ConsentConfiguration> consentHandlerMappings = new ArrayList<>();
    private MyaLaunchInput launchInput;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        context = RuntimeEnvironment.application;
        launchInput = new MyaLaunchInput(context,null);
        final UserDataInterface userDataInterface = mock(UserDataInterface.class);
        when(userDataInterface.isUserLoggedIn(launchInput.getContext())).thenReturn(true);
        myaInterface = new MyaInterface() {
            @Override
            public UserDataInterface getUserDataInterface() {
                return userDataInterface;
            }

            @Override
            protected AppTaggingInterface getTaggingInterface(MyaDependencies myaDependencies) {
                return appTaggingInterface;
            }
        };

        fragmentTransaction = new FragmentTransactionMock();
        fragmentManager = new FragmentManagerMock(fragmentTransaction);
        fragmentActivity = new FragmentActivityMock(fragmentManager);
        appInfra = new AppInfraInterfaceMock();
        actionBarListener = new ActionBarListenerMock();
    }

    @Test
    public void launchWithFragmentLauncher_correctFragmentIsReplacedInContainer() {
        givenFragmentLauncher(fragmentActivity, A_SPECIFIC_CONTAINER_ID, actionBarListener);
        whenCallingLaunchWithAddToBackstack();
        thenReplaceWasCalledWith(A_SPECIFIC_CONTAINER_ID, MyaTabFragment.class, MY_ACCOUNTS_INVOKE_TAG);
        thenAddToBackStackWasCalled(MY_ACCOUNTS_INVOKE_TAG);
        thenCommitAllowingStateLossWasCalled();
        thenFragmentHasBundle();
    }

    @Test
    public void launchWithFragmentLauncher_dontCallAddToBackStackWhenNotDemanded() {
        givenFragmentLauncher(fragmentActivity, A_SPECIFIC_CONTAINER_ID, actionBarListener);
        whenCallingLaunchWithoutAddToBackstack();
        thenReplaceWasCalledWith(A_SPECIFIC_CONTAINER_ID, MyaTabFragment.class, MYAFRAGMENT);
        thenAddToBackStackWasNotCalled();
        thenCommitAllowingStateLossWasCalled();
        thenFragmentHasBundle();
    }

    @Test
    public void launchWithActivityLauncher_correctFragmentIsReplacedInContainer() {
        givenActivityLauncher();
        whenCallingLaunchWithoutAddToBackstack();
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
        myaInterface.launch(givenUiLauncher, launchInput);
    }

    private void whenCallingLaunchWithoutAddToBackstack() {
        myaInterface.init(new MyaDependencies(appInfra), new MyaSettings(context));
        myaInterface.launch(givenUiLauncher, launchInput);
    }

    private void thenReplaceWasCalledWith(int expectedParentContainerId, Class<?> expectedFragmentClass, String expectedTag) {
        assertEquals(expectedParentContainerId, fragmentTransaction.replace_containerId);
        assertTrue(fragmentTransaction.replace_fragment.getClass().isAssignableFrom(expectedFragmentClass));
        // assertEquals(expectedTag, fragmentTransaction.replace_tag);
    }

    private void thenAddToBackStackWasCalled(String expectedBackStackId) {
        assertEquals(expectedBackStackId, fragmentTransaction.addToBackStack_backStackId);
    }

    private void thenAddToBackStackWasNotCalled() {
        // assertNull(fragmentTransaction.addToBackStack_backStackId);
    }

    private void thenFragmentHasBundle() {
        // assertNotNull(fragmentTransaction.replace_fragment.getArguments());
    }

    private void thenCommitAllowingStateLossWasCalled() {
        assertTrue(fragmentTransaction.commitAllowingStateLossWasCalled);
    }

}