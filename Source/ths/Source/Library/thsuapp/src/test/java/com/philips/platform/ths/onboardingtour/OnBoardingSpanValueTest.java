/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.onboardingtour;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

public class OnBoardingSpanValueTest {

    OnBoardingSpanValue mOnBoardingSpanValue;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mOnBoardingSpanValue = new OnBoardingSpanValue(1,2, OnBoardingSpanValue.OnBoardingTypeface.BOLD);
    }

    @Test
    public void getStartIndex() throws Exception {
        final int startIndex = mOnBoardingSpanValue.getStartIndex();
        assert startIndex == 1;
    }

    @Test
    public void getEndIndex() throws Exception {
        final int endIndex = mOnBoardingSpanValue.getEndIndex();
        assert endIndex == 2;
    }

    @Test
    public void getOnBoardingTypeface() throws Exception {
        final OnBoardingSpanValue.OnBoardingTypeface onBoardingTypeface = mOnBoardingSpanValue.getOnBoardingTypeface();
        assert onBoardingTypeface == OnBoardingSpanValue.OnBoardingTypeface.BOLD;
    }

}