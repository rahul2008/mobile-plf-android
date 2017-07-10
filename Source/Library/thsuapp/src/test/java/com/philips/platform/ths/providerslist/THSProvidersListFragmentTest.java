package com.philips.platform.ths.providerslist;


import android.support.v4.widget.SwipeRefreshLayout;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.practice.Practice;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.americanwell.sdk.manager.PracticeProvidersManager;
import com.philips.platform.ths.CustomRobolectricRunnerAmwel;
import com.philips.platform.ths.activity.THSLaunchActivity;
import com.philips.platform.ths.base.THSBaseView;
import com.philips.platform.ths.utility.THSManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.shadows.ShadowLog;

import java.util.List;

import static org.junit.Assert.assertNotNull;

@RunWith(CustomRobolectricRunnerAmwel.class)
public class THSProvidersListFragmentTest {

    private THSLaunchActivity mActivity;
    private THSProvidersListFragment pthProvidersListFragment;

    @Mock
    AWSDK awsdkMock;

    @Mock
    com.philips.platform.ths.providerslist.THSProviderListViewInterface THSProviderListViewInterface;

    @InjectMocks
    THSProviderListPresenter providerListPresenter;

    @Mock
    Consumer consumer;
    @Mock
    Practice practice;

    @Mock
    PracticeProvidersManager practiseprovidermanagerMock;

    @Mock
    THSBaseView THSBaseView;

    @Mock
    List<ProviderInfo> providerInfos;

    @Mock
    THSProvidersListFragment pthProvidersListFragmentMock;

    @Mock
    SwipeRefreshLayout swipeRL;

    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);
        ShadowLog.stream = System.out;
        THSManager.getInstance().setAwsdk(awsdkMock);
        Mockito.when(THSBaseView.getFragmentActivity()).thenReturn(mActivity);
        Mockito.when(THSManager.getInstance().getAwsdk(mActivity).getPracticeProvidersManager()).thenReturn(practiseprovidermanagerMock);
        pthProvidersListFragment = new THSProvidersListFragment();
        pthProvidersListFragment.setPracticeAndConsumer(practice,consumer);
    }

    @Test
    public  void testFragment(){
        assertNotNull(pthProvidersListFragment);
    }

    @Test
    public void testFragmentActionBarName(){
//        SupportFragmentTestUtil.startFragment(pthProvidersListFragment);
    }
}
