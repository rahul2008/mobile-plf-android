package com.philips.platform.ths.intake;

import com.americanwell.sdk.AWSDK;
import com.philips.platform.ths.CustomRobolectricRunnerAmwel;
import com.philips.platform.ths.activity.THSLaunchActivity;
import com.philips.platform.ths.utility.THSManager;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;


@RunWith(CustomRobolectricRunnerAmwel.class)
public class THSMedicationFragmentTest {

    private THSLaunchActivity mActivity;
    private THSMedicationFragment mPTHMedicationFragment;

    @Mock
    AWSDK awsdkMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ShadowLog.stream = System.out;
        THSManager.getInstance().setAwsdk(awsdkMock);
        mActivity = Robolectric.buildActivity(THSLaunchActivity.class).create().get();
        Assert.assertNotNull(mActivity);
        mPTHMedicationFragment = new THSMedicationFragment();
        Assert.assertNotNull(mPTHMedicationFragment);
    }


    @Test
    public void getFragmentActivity() throws Exception {
//        SupportFragmentTestUtil.startFragment(mPTHMedicationFragment);
        Assert.assertNull(mPTHMedicationFragment.getFragmentActivity());

    }

}
