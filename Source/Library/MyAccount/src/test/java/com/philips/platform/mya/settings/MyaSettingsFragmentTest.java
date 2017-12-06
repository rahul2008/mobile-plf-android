package com.philips.platform.mya.settings;

import android.content.Context;

import com.philips.platform.mya.BuildConfig;
import com.philips.platform.mya.R;
import com.philips.platform.mya.runner.CustomRobolectricRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 25)
public class MyaSettingsFragmentTest {
    private Context mContext;
    private MyaSettingsFragment myaSettingsFragment;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mContext = RuntimeEnvironment.application;
        myaSettingsFragment = new MyaSettingsFragment();
    }

/*
    @Test(expected = InflateException.class)
    public void testStartFragment_ShouldNotNul() {
        SupportFragmentTestUtil.startFragment(myaSettingsFragment);
    }*/

    @Test
    public void testEquals_getActionbarTitleResId() throws Exception{
        assertEquals(R.string.MYA_My_account,myaSettingsFragment.getActionbarTitleResId());
    }

    @Test
    public void testNotNull_getActionbarTitle() throws Exception {
        assertNotNull(myaSettingsFragment.getActionbarTitle(mContext));
    }

    @Test
    public void notNullgetBackButtonState() throws Exception {
        assertNotNull(myaSettingsFragment.getBackButtonState());
    }

}