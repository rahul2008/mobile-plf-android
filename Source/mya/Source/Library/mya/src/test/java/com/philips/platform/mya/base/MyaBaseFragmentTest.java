/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.platform.mya.base;

import android.content.Context;
import android.content.res.Resources;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.mya.MyaHelper;
import com.philips.platform.mya.activity.MyaActivity;
import com.philips.platform.mya.launcher.MyaLaunchInput;
import com.philips.platform.mya.settings.MyaSettingsFragment;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static com.philips.platform.mya.base.MyaBaseFragment.MY_ACCOUNTS_INVOKE_TAG;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment;

@RunWith(RobolectricTestRunner.class)
public class MyaBaseFragmentTest {

    private MyaBaseFragment myaBaseFragment;

    private ActionBarListener actionBarListener = new ActionBarListener() {
        @Override
        public void updateActionBar(int i, boolean b) {
            assertEquals(RuntimeEnvironment.application.getString(i), "My account");
        }

        @Override
        public void updateActionBar(String s, boolean b) {
            assertEquals(s, "My account");
        }
    };

    @Before
    public void setup() {
        initMocks(this);

        Context mContext = RuntimeEnvironment.application;
        myaBaseFragment = new MyaSettingsFragment();
        AppInfra appInfra = new AppInfra.Builder().build(mContext);
        MyaHelper.getInstance().setAppInfra(appInfra);
        MyaLaunchInput myaLaunchInput = new MyaLaunchInput(mContext);
        MyaHelper.getInstance().setMyaLaunchInput(myaLaunchInput);
        AppTaggingInterface appTaggingInterfaceMock = mock(AppTaggingInterface.class);
        MyaHelper.getInstance().setAppTaggingInterface(appTaggingInterfaceMock);
//        SupportFragmentTestUtil.startFragment(myaBaseFragment);
        assertNotNull(myaBaseFragment.getFragmentActivity());
    }

    @Test
    public void testInvocations() {
        myaBaseFragment.setActionbarUpdateListener(actionBarListener);
        assertEquals(myaBaseFragment.getActionbarUpdateListener(),actionBarListener);
        assertFalse(myaBaseFragment.getBackButtonState());
    }

    @Test
    public void shouldExitMyAccounts() {
        FragmentLauncher fragmentLauncherMock = mock(FragmentLauncher.class);
        FragmentActivity fragmentActivity = mock(FragmentActivity.class);
        FragmentManager fragmentManagerMock = mock(FragmentManager.class);
        when(fragmentActivity.getSupportFragmentManager()).thenReturn(fragmentManagerMock);
        when(fragmentLauncherMock.getFragmentActivity()).thenReturn(fragmentActivity);
        myaBaseFragment.setFragmentLauncher(fragmentLauncherMock);
        myaBaseFragment.exitMyAccounts();
        verify(fragmentManagerMock).popBackStackImmediate(MY_ACCOUNTS_INVOKE_TAG,
                FragmentManager.POP_BACK_STACK_INCLUSIVE);
        assertEquals(myaBaseFragment.getFragmentLauncher(),fragmentLauncherMock);
        MyaActivity myaActivityMock = mock(MyaActivity.class);
        when(fragmentLauncherMock.getFragmentActivity()).thenReturn(myaActivityMock);
        myaBaseFragment.setFragmentLauncher(fragmentLauncherMock);
        myaBaseFragment.exitMyAccounts();
        verify(myaActivityMock).finish();
    }

    @Test
    public void shouldAddFragmentWhenRequired() {

        int mEnterAnimation = 500;
        int mExitAnimation = 700;
        final String startAnim = "start_anim";
        final String endAnim= "end_anim";
        FragmentLauncher fragmentLauncherMock = mock(FragmentLauncher.class);
        FragmentActivity fragmentActivity = mock(FragmentActivity.class);
        when(fragmentActivity.getPackageName()).thenReturn("some_package");
        FragmentManager fragmentManagerMock = mock(FragmentManager.class);
        MyaBaseFragment myaBaseFragmentMock = mock(MyaSettingsFragment.class);
        int value = 123456;
        when(myaBaseFragmentMock.getId()).thenReturn(value);
        when(fragmentActivity.getSupportFragmentManager()).thenReturn(fragmentManagerMock);
        when(fragmentLauncherMock.getFragmentActivity()).thenReturn(fragmentActivity);
        when(fragmentLauncherMock.getParentContainerResourceID()).thenReturn(value);
        when(fragmentLauncherMock.getEnterAnimation()).thenReturn(mEnterAnimation);
        when(fragmentLauncherMock.getExitAnimation()).thenReturn(mExitAnimation);
        Resources resourcesMock = mock(Resources.class);
        when(resourcesMock.getResourceName(mEnterAnimation)).thenReturn(startAnim);
        when(resourcesMock.getResourceName(mExitAnimation)).thenReturn(endAnim);
        when(resourcesMock.getIdentifier(startAnim,"anim","some_package")).thenReturn(mEnterAnimation);
        when(resourcesMock.getIdentifier(endAnim,"anim","some_package")).thenReturn(mExitAnimation);
        when(fragmentActivity.getResources()).thenReturn(resourcesMock);
        FragmentTransaction fragmentTransactionMock = mock(FragmentTransaction.class);
        when(fragmentManagerMock.beginTransaction()).thenReturn(fragmentTransactionMock);
        when(fragmentManagerMock.findFragmentById(myaBaseFragmentMock.getId())).thenReturn(myaBaseFragmentMock);
        myaBaseFragment.setFragmentLauncher(fragmentLauncherMock);
        myaBaseFragment.showFragment(myaBaseFragmentMock);

        verify(fragmentTransactionMock).setCustomAnimations(mEnterAnimation,
                mExitAnimation, mEnterAnimation, mExitAnimation);
        verify(fragmentTransactionMock).replace(myaBaseFragmentMock.getId(), myaBaseFragmentMock, myaBaseFragmentMock.getClass().getSimpleName());
        verify(fragmentTransactionMock).commitAllowingStateLoss();
        Fragment fragmentMock = mock(Fragment.class);
        when(fragmentMock.getId()).thenReturn(value);
        when(fragmentManagerMock.findFragmentById(value)).thenReturn(fragmentMock);
        myaBaseFragment.showFragment(myaBaseFragmentMock);
        when(fragmentManagerMock.findFragmentById(fragmentMock.getId())).thenReturn(myaBaseFragmentMock);
        verify(fragmentTransactionMock,atLeastOnce()).addToBackStack(MY_ACCOUNTS_INVOKE_TAG);
    }

    @Test
    public void shouldSetTitleWhenInvoked() {
        ActionBarListener actionBarListener = mock(ActionBarListener.class);
        myaBaseFragment = new MyaSettingsFragment();
        myaBaseFragment.setActionbarUpdateListener(actionBarListener);
//        startFragment(myaBaseFragment);
        verify(actionBarListener).updateActionBar(myaBaseFragment.getActionbarTitleResId(),myaBaseFragment.getBackButtonState());
        verify(actionBarListener).updateActionBar(myaBaseFragment.getActionbarTitle(RuntimeEnvironment.application),myaBaseFragment.getBackButtonState());
    }
}