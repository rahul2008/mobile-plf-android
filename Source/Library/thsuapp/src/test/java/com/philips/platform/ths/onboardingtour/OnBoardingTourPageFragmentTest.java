
/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.ths.onboardingtour;

import android.os.Bundle;

import com.philips.platform.ths.CustomRobolectricRunnerAmwel;
import com.philips.platform.ths.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(CustomRobolectricRunnerAmwel.class)
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
        bundle.putInt(mOnBoardingTourPageFragment.ARG_PAGE_TITLE,1);
        bundle.putInt(mOnBoardingTourPageFragment.ARG_PAGE_BG_ID,2);
        bundle.putSerializable("INDEX_PAIRS",list);

    }

    @Test(expected = Exception.class)
    public void newInstance() throws Exception {
        SupportFragmentTestUtil.startFragment(mOnBoardingTourPageFragment);
        assertNotNull(mOnBoardingTourPageFragment);
    }

}