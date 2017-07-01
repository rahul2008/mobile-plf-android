package com.philips.amwelluapp.practice;

import com.americanwell.sdk.AWSDK;
import com.philips.amwelluapp.ApplicationTestClass;
import com.philips.amwelluapp.BuildConfig;
import com.philips.amwelluapp.CustomRobolectricRunnerAmwel;
import com.philips.amwelluapp.activity.PTHLaunchActivity;
import com.philips.amwelluapp.utility.PTHManager;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.matchers.NotNull;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

/**
 * Created by philips on 6/27/17.
 */
@RunWith(CustomRobolectricRunnerAmwel.class)
//@Config(constants = BuildConfig.class, sdk = 21, application = ApplicationTestClass.class, libraries = "../../../../../../../build/intermediates/exploded-aar/com.philips.cdp/uid/0.1.1-SNAPSHOT.20170623170225")

public class PTHPracticeFragmentTest {
    private PTHLaunchActivity mActivity;
    private PTHPracticeFragment mPTHPracticeFragment;

    @Mock
    AWSDK awsdkMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ShadowLog.stream = System.out;
        PTHManager.getInstance().setAwsdk(awsdkMock);
        mActivity = Robolectric.buildActivity(PTHLaunchActivity.class).create().get();
        Assert.assertNotNull(mActivity);
        mPTHPracticeFragment = new PTHPracticeFragment();
        Assert.assertNotNull(mPTHPracticeFragment);
    }


    @Test
    public void getFragmentActivity() throws Exception {
        SupportFragmentTestUtil.startFragment(mPTHPracticeFragment);
        Assert.assertNotNull(mPTHPracticeFragment.getFragmentActivity());

    }

}

