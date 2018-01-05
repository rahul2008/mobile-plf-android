package com.philips.platform.mya.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.philips.platform.mya.BuildConfig;
import com.philips.platform.mya.runner.CustomRobolectricRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by philips on 11/24/17.
 */
@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 25)
public class MyaViewPagerTest {

    MyaViewPager myaViewPager, myaViewPagerWithAttr;

    Context contextMock;

    @Mock
    AttributeSet attributeSetMock;

    @Mock
    MotionEvent motionEventMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        contextMock = RuntimeEnvironment.application;
        myaViewPager = new MyaViewPager(contextMock);
        myaViewPagerWithAttr = new MyaViewPager(contextMock, attributeSetMock);

    }

    @Test
    public void notNull_onInterceptTouchEvent() throws Exception {
        assertNotNull(myaViewPagerWithAttr.onInterceptTouchEvent(motionEventMock));
        assertNotNull(myaViewPager.onInterceptTouchEvent(motionEventMock));
    }

}