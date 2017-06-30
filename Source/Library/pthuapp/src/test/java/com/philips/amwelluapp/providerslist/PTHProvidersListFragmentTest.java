package com.philips.amwelluapp.providerslist;


import com.americanwell.sdk.AWSDK;
import com.philips.amwelluapp.CustomRobolectricRunnerAmwel;
import com.philips.amwelluapp.activity.PTHLaunchActivity;
import com.philips.amwelluapp.utility.PTHManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.junit.Assert.assertNotNull;

@RunWith(CustomRobolectricRunnerAmwel.class)
public class PTHProvidersListFragmentTest {

    private PTHLaunchActivity mActivity;
    private PTHProvidersListFragment pthProvidersListFragment;

    @Mock
    AWSDK awsdkMock;

    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);
        ShadowLog.stream = System.out;
        PTHManager.getInstance().setAwsdk(awsdkMock);
        mActivity = Robolectric.buildActivity(PTHLaunchActivity.class).create().get();
        assertNotNull(mActivity);
        pthProvidersListFragment = new PTHProvidersListFragment();
    }

    @Test
    public  void testFragment(){
        assertNotNull(pthProvidersListFragment);
    }

    @Test
    public void testFragmentActionBarName(){
        SupportFragmentTestUtil.startFragment(pthProvidersListFragment);
    }
}
