package com.philips.platform.ths.practice;

import com.americanwell.sdk.AWSDK;
import com.philips.platform.ths.CustomRobolectricRunnerAmwel;
import com.philips.platform.ths.activity.THSLaunchActivity;
import com.philips.platform.ths.utility.PTHManager;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

/**
 * Created by philips on 6/27/17.
 */
@RunWith(CustomRobolectricRunnerAmwel.class)
//@Config(constants = BuildConfig.class, sdk = 21, application = ApplicationTestClass.class, libraries = "../../../../../../../build/intermediates/exploded-aar/com.philips.cdp/uid/0.1.1-SNAPSHOT.20170623170225")

public class THSPracticeFragmentTest {
    private THSLaunchActivity mActivity;
    private THSPracticeFragment mPTHPracticeFragment;

    @Mock
    AWSDK awsdkMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ShadowLog.stream = System.out;
        PTHManager.getInstance().setAwsdk(awsdkMock);
        mActivity = Robolectric.buildActivity(THSLaunchActivity.class).create().get();
        Assert.assertNotNull(mActivity);
        mPTHPracticeFragment = new THSPracticeFragment();
        Assert.assertNotNull(mPTHPracticeFragment);
    }


    @Test
    public void getFragmentActivity() throws Exception {
        SupportFragmentTestUtil.startFragment(mPTHPracticeFragment);
        Assert.assertNotNull(mPTHPracticeFragment.getFragmentActivity());

    }

}

