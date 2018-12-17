/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.platform.mya.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by philips on 11/24/17.
 */
@RunWith(RobolectricTestRunner.class)
public class MyaViewPagerTest {

    private MyaViewPager myaViewPager, myaViewPagerWithAttr;

    @Mock
    private AttributeSet attributeSetMock;

    @Mock
    private MotionEvent motionEventMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        Context contextMock = RuntimeEnvironment.application;
        myaViewPager = new MyaViewPager(contextMock);
        myaViewPagerWithAttr = new MyaViewPager(contextMock, attributeSetMock);
    }

    @Test
    public void notNull_onInterceptTouchEvent() throws Exception {
        myaViewPagerWithAttr.onInterceptTouchEvent(motionEventMock);
        myaViewPager.onInterceptTouchEvent(motionEventMock);
    }
}