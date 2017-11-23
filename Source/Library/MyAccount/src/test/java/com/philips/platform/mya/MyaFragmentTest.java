/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya;

import android.view.ViewGroup;

import com.philips.platform.mya.mock.FragmentActivityMock;
import com.philips.platform.mya.mock.FragmentManagerMock;
import com.philips.platform.mya.mock.FragmentTransactionMock;
import com.philips.platform.mya.mock.LayoutInflatorMock;
import com.philips.platform.mya.runner.CustomRobolectricRunner;
import com.philips.platform.mya.wrapper.MyaFragmentWrapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 25)

// TODO: Deepthi, recheck sdk version whether it should be 25 or not.
// TODO: Deepthi, These test cases need refactoring since csw input is removed from My account fragment.

public class MyaFragmentTest {

    @Before
    public void setup() {
        myaFragment = new MyaFragmentWrapper();
        myaFragment.fragmentActivity = mockFragmentActivity;
    }

    @Test
    public void onCreate_InvokesInflatorWithRightParameters() throws Exception {
        whenCallingOnCreateView();
        thenInflatorIsCalledWith(R.layout.mya_fragment_my_account_root, null, false);
    }

    @Test
    public void onCreate_inflatesCorrectLayout() throws Exception {
        whenCallingOnCreateView();
        thenAccountViewIsInflatedWith(R.id.mya_frame_layout_view_container);
    }

    private void whenCallingOnCreateView() {
        myaFragment.onCreateView(mockLayoutInflater, null, null);
    }

    private void thenInflatorIsCalledWith(int layout, ViewGroup group, boolean attachToRoot) {
        assertEquals(layout, mockLayoutInflater.usedResource);
        assertEquals(group, mockLayoutInflater.usedViewGroup);
        assertEquals(attachToRoot, mockLayoutInflater.usedAttachToRoot);
    }

    private void thenAccountViewIsInflatedWith(int layout) {
        assertEquals(layout, fragmentTransaction.replace_containerId);
        assertNotNull(fragmentTransaction.replace_fragment);
        assertTrue(fragmentTransaction.commitAllowingStateLossWasCalled);
        assertTrue(fragmentManager.beginTransactionCalled);
    }

    private MyaFragmentWrapper myaFragment;
    private LayoutInflatorMock mockLayoutInflater = LayoutInflatorMock.createMock();
    private FragmentTransactionMock fragmentTransaction = new FragmentTransactionMock();
    private FragmentManagerMock fragmentManager = new FragmentManagerMock(fragmentTransaction);
    private FragmentActivityMock mockFragmentActivity = new FragmentActivityMock(fragmentManager);
}