
/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.platform.ths.onboardingtour;

import android.os.Bundle;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.ArrayList;

import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
public class OnBoardingTourPageFragmentTest {

    OnBoardingTourPageFragment mOnBoardingTourPageFragment;

    @Mock
    OnBoardingSpanValue onBoardingSpanValueMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mOnBoardingTourPageFragment = new OnBoardingTourPageFragmentMock();

        ArrayList list = new ArrayList();
        list.add(onBoardingSpanValueMock);

        Bundle bundle = new Bundle();
        bundle.putInt(mOnBoardingTourPageFragment.ARG_PAGE_TEXT,1);
        bundle.putInt(mOnBoardingTourPageFragment.ARG_PAGE_BG_ID,2);
        bundle.putSerializable("INDEX_PAIRS",list);

    }

    @Test(expected = Exception.class)
    public void newInstance() throws Exception {
        SupportFragmentTestUtil.startFragment(mOnBoardingTourPageFragment);
        assertNotNull(mOnBoardingTourPageFragment);
    }

}