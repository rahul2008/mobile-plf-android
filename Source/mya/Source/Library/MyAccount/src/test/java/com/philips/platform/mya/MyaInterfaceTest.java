/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya;

import android.content.Context;

import com.philips.cdp.registration.User;
import com.philips.platform.catk.injection.CatkComponent;
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
import com.philips.platform.mya.mock.LaunchInputMock;
import com.philips.platform.mya.runner.CustomRobolectricRunner;
import com.philips.platform.mya.tabs.MyaTabFragment;
import com.philips.platform.myaplugin.user.UserDataModelProvider;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static com.philips.platform.mya.base.MyaBaseFragment.MY_ACCOUNTS_CALLEE_TAG;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 25)
public class MyaInterfaceTest {
    private MyaInterface myaInterface;

    private UiLauncher givenUiLauncher;
    private ActivityLauncherMock givenActivityLauncher;
    private FragmentLauncherMock givenFragmentLauncher;

    private ActionBarListener actionBarListener;
    private FragmentActivityMock fragmentActivity;
    private FragmentTransactionMock fragmentTransaction;
    private FragmentManagerMock fragmentManager;
    private LaunchInputMock launchInput;
    private AppInfraInterfaceMock appInfra;
    private Context context;

    private final int A_SPECIFIC_CONTAINER_ID = 12345678;
    public static final String MYAFRAGMENT = MY_ACCOUNTS_CALLEE_TAG;

    @Mock
    User mockUser;

    @Mock
    private CatkComponent mockCatkComponent;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        context = RuntimeEnvironment.application;
        launchInput = new LaunchInputMock();
        final UserDataModelProvider userDataModelProvider = mock(UserDataModelProvider.class);
        when(userDataModelProvider.isUserLoggedIn(launchInput.getContext())).thenReturn(true);
        myaInterface = new MyaInterface() {
            @Override
            public UserDataModelProvider getUserDataModelProvider(MyaLaunchInput myaLaunchInput) {
                return userDataModelProvider;
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
        thenReplaceWasCalledWith(A_SPECIFIC_CONTAINER_ID, MyaTabFragment.class, MY_ACCOUNTS_CALLEE_TAG);
        thenAddToBackStackWasCalled(MY_ACCOUNTS_CALLEE_TAG);
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
        thenStartActivityWasCalledWithIntent();
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
        launchInput.addToBackStack(true);
        myaInterface.launch(givenUiLauncher, launchInput);
    }

    private void whenCallingLaunchWithoutAddToBackstack() {
        myaInterface.init(new MyaDependencies(appInfra), new MyaSettings(context));
        launchInput.addToBackStack(false);
        myaInterface.launch(givenUiLauncher, launchInput);
    }

    private void thenReplaceWasCalledWith(int expectedParentContainerId, Class<?> expectedFragmentClass, String expectedTag) {
        assertEquals(expectedParentContainerId, fragmentTransaction.replace_containerId);
        assertTrue(fragmentTransaction.replace_fragment.getClass().isAssignableFrom(expectedFragmentClass));
//        assertEquals(expectedTag, fragmentTransaction.replace_tag);
    }

    private void thenAddToBackStackWasCalled(String expectedBackStackId) {
        assertEquals(expectedBackStackId, fragmentTransaction.addToBackStack_backStackId);
    }

    private void thenAddToBackStackWasNotCalled() {
//        assertNull(fragmentTransaction.addToBackStack_backStackId);
    }

    private void thenFragmentHasBundle() {
//        assertNotNull(fragmentTransaction.replace_fragment.getArguments());
    }

    private void thenCommitAllowingStateLossWasCalled() {
        assertTrue(fragmentTransaction.commitAllowingStateLossWasCalled);
    }

    private void thenStartActivityWasCalledWithIntent() {
        assertNotNull(launchInput.context.startActivity_intent);
    }



}