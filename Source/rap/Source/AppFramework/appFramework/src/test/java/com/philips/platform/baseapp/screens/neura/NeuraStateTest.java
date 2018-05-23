/* Copyright (c) Koninklijke Philips N.V., 2018
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.neura;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.philips.platform.uappframework.launcher.FragmentLauncher;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.junit.MockitoJUnitRunner.Silent.class)
public class NeuraStateTest {


    private NeuraState neuraState;
    @Mock
    private NeuraConsentManagerFragment neuraConsentManagerFragmentMock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        neuraState = new NeuraState() {
            @NonNull
            @Override
            NeuraConsentManagerFragment getNeuraConsentManagerFragment() {
                return neuraConsentManagerFragmentMock;
            }
        };
    }

    @Test
    public void testNavigate() {
        FragmentLauncher fragmentLauncherMock = mock(FragmentLauncher.class);
        FragmentActivity fragmentActivityMock = mock(FragmentActivity.class);
        FragmentManager fragmentManagerMock = mock(FragmentManager.class);
        FragmentTransaction fragmentTransactionMock = mock(FragmentTransaction.class);
        int parentContainerId = 123456;
        when(fragmentLauncherMock.getParentContainerResourceID()).thenReturn(parentContainerId);
        when(fragmentActivityMock.getSupportFragmentManager()).thenReturn(fragmentManagerMock);
        when(fragmentManagerMock.beginTransaction()).thenReturn(fragmentTransactionMock);
        when(fragmentLauncherMock.getFragmentActivity()).thenReturn(fragmentActivityMock);
        neuraState.navigate(fragmentLauncherMock);
        verify(fragmentTransactionMock).replace(parentContainerId,
                neuraConsentManagerFragmentMock,
                NeuraConsentManagerFragment.TAG);
        verify(fragmentTransactionMock).addToBackStack(NeuraConsentManagerFragment.TAG);
        verify(fragmentTransactionMock).commitAllowingStateLoss();
    }

}