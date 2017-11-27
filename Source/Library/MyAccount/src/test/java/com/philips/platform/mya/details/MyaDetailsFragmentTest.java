package com.philips.platform.mya.details;

import android.content.Context;
import android.view.InflateException;

import com.philips.platform.mya.BuildConfig;
import com.philips.platform.mya.R;
import com.philips.platform.mya.mock.FragmentActivityMock;
import com.philips.platform.mya.mock.FragmentManagerMock;
import com.philips.platform.mya.mock.FragmentTransactionMock;
import com.philips.platform.mya.mock.LayoutInflatorMock;
import com.philips.platform.mya.runner.CustomRobolectricRunner;
import com.philips.platform.mya.wrapper.MyaFragmentWrapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 25)
public class MyaDetailsFragmentTest {

    MyaDetailsFragment myaDetailsFragment;
    private MyaFragmentWrapper myaFragment;
    private LayoutInflatorMock mockLayoutInflater = LayoutInflatorMock.createMock();
    private FragmentTransactionMock fragmentTransaction = new FragmentTransactionMock();
    private FragmentManagerMock fragmentManager = new FragmentManagerMock(fragmentTransaction);
    private FragmentActivityMock mockFragmentActivity = new FragmentActivityMock(fragmentManager);
    private Context mContext;

    @Before
    public void setUp() throws Exception{
        initMocks(this);
        mContext = RuntimeEnvironment.application;
        myaDetailsFragment = new MyaDetailsFragment();
    }

    @Test(expected = InflateException.class)
    public void testNotNull_getActionbarTitleResId() throws Exception{
        SupportFragmentTestUtil.startFragment(myaDetailsFragment);
        assertNotNull(myaDetailsFragment.getActionbarTitleResId());
    }

    @Test
    public void testEquals_getActionbarTitleResId() throws Exception{
        assertEquals(R.string.MYA_My_account,myaDetailsFragment.getActionbarTitleResId());
    }

    @Test
    public void testNotNull_getActionbarTitle() throws Exception{
        assertNotNull(myaDetailsFragment.getActionbarTitle(mContext));
    }

    @Test
    public void notNullgetBackButtonState() throws Exception{
        assertNotNull(myaDetailsFragment.getBackButtonState());
    }




}