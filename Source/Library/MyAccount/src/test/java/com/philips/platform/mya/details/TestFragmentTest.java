package com.philips.platform.mya.details;

import android.content.Context;

import com.philips.platform.mya.BuildConfig;
import com.philips.platform.mya.runner.CustomRobolectricRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by philips on 11/24/17.
 */

@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class TestFragmentTest {


    private Context mContext;
    private TestFragment myaTestFragment;

    @Before
    public void setUp() throws Exception{
        initMocks(this);
        mContext = RuntimeEnvironment.application;
        myaTestFragment = new TestFragment();
    }

    @Test
    public void testStartFragment_ShouldNotNul(){
        SupportFragmentTestUtil.startFragment(myaTestFragment);
    }
}