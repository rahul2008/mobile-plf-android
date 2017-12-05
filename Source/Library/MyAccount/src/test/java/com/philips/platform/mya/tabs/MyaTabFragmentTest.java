package com.philips.platform.mya.tabs;

import android.content.Context;
import android.view.InflateException;

import com.philips.platform.mya.BuildConfig;
import com.philips.platform.mya.R;
import com.philips.platform.mya.runner.CustomRobolectricRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by philips on 11/24/17.
 */
@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 25)
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
        SupportFragmentTestUtil.startFragment(myaTabFragment);
    }

    @Test
    public void testEquals_getActionbarTitleResId() throws Exception{
        assertEquals(R.string.MYA_My_account,myaTabFragment.getActionbarTitleResId());
    }

    @Test
    public void testNotNull_getActionbarTitle() throws Exception{
        assertNotNull(myaTabFragment.getActionbarTitle(mContext));
    }

    @Test
    public void notNullgetBackButtonState() throws Exception{
        assertNotNull(myaTabFragment.getBackButtonState());
    }

}