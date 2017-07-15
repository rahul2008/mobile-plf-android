package com.philips.platform.ths.providerdetails;


import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.practice.Practice;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.americanwell.sdk.manager.PracticeProvidersManager;
import com.philips.platform.ths.CustomRobolectricRunnerAmwel;
import com.philips.platform.ths.activity.THSLaunchActivity;
import com.philips.platform.ths.base.THSBaseView;
import com.philips.platform.ths.providerslist.THSProviderListPresenter;
import com.philips.platform.ths.providerslist.THSProviderListViewInterface;
import com.philips.platform.ths.utility.THSManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.junit.Assert.assertNotNull;

@RunWith(CustomRobolectricRunnerAmwel.class)
public class THSProviderDetailsFragmentTest {

    private THSLaunchActivity mActivity;
    private THSProviderDetailsFragment providerDetailsFragment;

    @Mock
    AWSDK awsdkMock;

    @Mock
    THSProviderListViewInterface THSProviderListViewInterface;

    @InjectMocks
    THSProviderListPresenter providerListPresenter;

    @Mock
    Consumer consumer;
    @Mock
    ProviderInfo providerInfo;

    @Mock
    Practice practice;

    @Mock
    PracticeProvidersManager practiseprovidermanagerMock;

    @Mock
    THSBaseView THSBaseView;

    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);
        ShadowLog.stream = System.out;
        THSManager.getInstance().setAwsdk(awsdkMock);
        Mockito.when(THSBaseView.getFragmentActivity()).thenReturn(mActivity);
        Mockito.when(THSManager.getInstance().getAwsdk(mActivity).getPracticeProvidersManager()).thenReturn(practiseprovidermanagerMock);
        providerDetailsFragment = new THSProviderDetailsFragment();
        providerDetailsFragment.setConsumerAndPractice(consumer,practice);
    }

    @Test
    public  void testFragment(){
        assertNotNull(providerDetailsFragment);
    }

    @Test
    public void testFragmentActionBarName(){

    }
}
