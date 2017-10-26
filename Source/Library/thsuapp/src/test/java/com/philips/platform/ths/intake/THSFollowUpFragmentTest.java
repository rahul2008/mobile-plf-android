package com.philips.platform.ths.intake;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.manager.ConsumerManager;
import com.philips.platform.ths.CustomRobolectricRunnerAmwel;
import com.philips.platform.ths.activity.THSLaunchActivity;
import com.philips.platform.ths.registration.THSConsumerWrapper;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.listener.ActionBarListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(CustomRobolectricRunnerAmwel.class)
public class THSFollowUpFragmentTest {

    THSFollowUpFragmentTestMock tHSFollowUpFragment;

    private THSLaunchActivity mActivity;
    private THSFollowUpFragment mTHSFollowUpFragment;


    @Mock
    AWSDK awsdkMock;

    @Mock
    THSConsumerWrapper pthConsumer;

    @Mock
    Consumer consumerMock;

    @Mock
    ActionBarListener actionBarListenerMock;

    @Mock
    ConsumerManager consumerManagerMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ShadowLog.stream = System.out;
        THSManager.getInstance().setAwsdk(awsdkMock);
        tHSFollowUpFragment = new THSFollowUpFragmentTestMock();
        tHSFollowUpFragment.setActionBarListener(actionBarListenerMock);

        when(pthConsumer.getConsumer()).thenReturn(consumerMock);
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);


    }


    public void onActivityCreatedTest(){
        SupportFragmentTestUtil.startFragment(tHSFollowUpFragment);
        assertNotNull(pthConsumer.getConsumer().getPhone());
    }


    @Test
    public void test(){

    }
}