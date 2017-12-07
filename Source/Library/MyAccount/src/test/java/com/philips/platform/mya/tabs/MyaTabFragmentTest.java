package com.philips.platform.mya.tabs;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.InflateException;
import android.view.View;

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
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.MockitoAnnotations.initMocks;

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
    public void testEquals_getActionbarTitleResId() throws Exception {
        assertEquals(R.string.MYA_My_account, myaTabFragment.getActionbarTitleResId());
    }

    @Test
    public void testNotNull_getActionbarTitle() throws Exception {
        assertNotNull(myaTabFragment.getActionbarTitle(mContext));
    }

    @Test
    public void notNullgetBackButtonState() throws Exception {
        assertNotNull(myaTabFragment.getBackButtonState());
        assertFalse(myaTabFragment.getBackButtonState());
    }

    @Test
    public void testTabCount() {
        SupportFragmentTestUtil.startFragment(myaTabFragment);
        TabLayout tabLayout = myaTabFragment.getView().findViewById(R.id.tab_layout);
        ViewPager viewPager = myaTabFragment.getView().findViewById(R.id.pager);
        assertEquals(tabLayout.getTabCount(), 2);
        assertEquals(viewPager.getVisibility(), View.VISIBLE);
        assertEquals(viewPager.getCurrentItem(), 0);
    }

}