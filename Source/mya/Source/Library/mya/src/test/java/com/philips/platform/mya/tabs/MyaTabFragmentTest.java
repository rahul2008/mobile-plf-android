/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.platform.mya.tabs;

import android.content.Context;
import android.view.InflateException;

import com.philips.platform.mya.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class MyaTabFragmentTest {

    private Context mContext;

    private MyaTabFragment myaTabFragment;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mContext = RuntimeEnvironment.application;
        myaTabFragment = new MyaTabFragment();
    }

    @Test(expected = InflateException.class)
    public void testStartFragment_ShouldNotNul() {
//        SupportFragmentTestUtil.startFragment(myaTabFragment);
    }

    @Test
    public void testEquals_getActionbarTitleResId() throws Exception {
        assertEquals(R.string.MYA_My_account, myaTabFragment.getActionbarTitleResId());
    }

    @Test
    public void testNotNull_getActionbarTitle() throws Exception {
        assertNotNull(myaTabFragment.getActionbarTitle(mContext));
    }

    @Test
    public void notNullgetBackButtonState() throws Exception {
        assertNotNull(myaTabFragment.getBackButtonState());
        assertFalse(myaTabFragment.getBackButtonState());
    }
}