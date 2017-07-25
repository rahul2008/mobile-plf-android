package com.philips.platform.ths.intake;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.manager.ConsumerManager;
import com.philips.platform.ths.CustomRobolectricRunnerAmwel;
import com.philips.platform.ths.activity.THSLaunchActivity;
import com.philips.platform.ths.registration.THSConsumer;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.listener.ActionBarListener;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;


@RunWith(CustomRobolectricRunnerAmwel.class)
public class THSMedicationFragmentTest {

    private THSLaunchActivity mActivity;
    private THSMedicationFragmentTestMock mPTHMedicationFragment;

    @Mock
    AWSDK awsdkMock;

    @Mock
    ActionBarListener actionBarListenerMock;

    @Mock
    THSConsumer pthConsumer;

    @Mock
    Consumer consumerMock;

    @Mock
    ConsumerManager consumerManagerMock;

    @Mock
    THSMedicationFragmentTest existingMedication;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ShadowLog.stream = System.out;
        THSManager.getInstance().setAwsdk(awsdkMock);
        mPTHMedicationFragment.setActionBarListener(actionBarListenerMock);


        THSManager.getInstance().setAwsdk(awsdkMock);
        THSManager.getInstance().setPTHConsumer(pthConsumer);
        when(pthConsumer.getConsumer()).thenReturn(consumerMock);
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);

    }


    @Test
    public void onActivityCreatedWhenZeroMedicationIsFetched() throws Exception {
        mPTHMedicationFragment.mExistingMedication=null;
        SupportFragmentTestUtil.startFragment(mPTHMedicationFragment);



//        SupportFragmentTestUtil.startFragment(mPTHMedicationFragment);
        Assert.assertNull(mPTHMedicationFragment.getFragmentActivity());

    }

}
