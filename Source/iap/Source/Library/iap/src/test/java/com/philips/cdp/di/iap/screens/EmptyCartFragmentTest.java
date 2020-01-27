/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.screens;

import android.os.Bundle;

import com.philips.cdp.di.iap.TestUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class EmptyCartFragmentTest {

    private EmptyCartFragment emptyCartFragment;

    @Before
    public void setUp() {
        initMocks(this);

        TestUtils.getStubbedStore();
        TestUtils.getStubbedHybrisDelegate();
    }

    @Test
    public void shouldDisplayAddressSelectionFragment() {
        emptyCartFragment = EmptyCartFragment.createInstance(new Bundle(), InAppBaseFragment.AnimationType.NONE);
//        SupportFragmentTestUtil.startFragment(emptyCartFragment);
    }

}