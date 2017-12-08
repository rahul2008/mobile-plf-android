/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.onboardingtour;

import android.graphics.Typeface;
import android.text.TextPaint;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

public class OnBoardingTypefaceSpanTest {

    OnBoardingTypefaceSpan mOnBoardingTypefaceSpan;

    @Mock
    Typeface typefaceMock;

    @Mock
    TextPaint textPaintMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mOnBoardingTypefaceSpan = new OnBoardingTypefaceSpan(typefaceMock);
    }

    @Test
    public void updateDrawState() throws Exception {
        mOnBoardingTypefaceSpan.updateDrawState(textPaintMock);
        verify(textPaintMock).setTypeface(any(Typeface.class));
    }

    @Test
    public void updateMeasureState() throws Exception {
        mOnBoardingTypefaceSpan.updateDrawState(textPaintMock);
        verify(textPaintMock).setTypeface(any(Typeface.class));
    }

}