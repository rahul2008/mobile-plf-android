package com.philips.amwelluapp.providerslist;


import android.support.v4.widget.SwipeRefreshLayout;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.practice.Practice;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.americanwell.sdk.manager.PracticeProvidersManager;
import com.philips.amwelluapp.CustomRobolectricRunnerAmwel;
import com.philips.amwelluapp.activity.PTHLaunchActivity;
import com.philips.amwelluapp.base.PTHBaseView;
import com.philips.amwelluapp.utility.PTHManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.List;

import static org.junit.Assert.assertNotNull;

@RunWith(CustomRobolectricRunnerAmwel.class)
public class PTHProvidersListFragmentTest {

    private PTHLaunchActivity mActivity;
    private PTHProvidersListFragment pthProvidersListFragment;

    @Mock
    AWSDK awsdkMock;

    @Mock
    PTHProviderListViewInterface pthProviderListViewInterface;

    @InjectMocks
    PTHProviderListPresenter providerListPresenter;

    @Mock
    Consumer consumer;
    @Mock
    Practice practice;

    @Mock
    PracticeProvidersManager practiseprovidermanagerMock;

    @Mock
    PTHBaseView pthBaseView;

    @Mock
    List<ProviderInfo> providerInfos;

    @Mock
    PTHProvidersListFragment pthProvidersListFragmentMock;

    @Mock
    SwipeRefreshLayout swipeRL;

    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);
        ShadowLog.stream = System.out;
        PTHManager.getInstance().setAwsdk(awsdkMock);
        Mockito.when(pthBaseView.getFragmentActivity()).thenReturn(mActivity);
        Mockito.when(PTHManager.getInstance().getAwsdk(mActivity).getPracticeProvidersManager()).thenReturn(practiseprovidermanagerMock);
        pthProvidersListFragment = new PTHProvidersListFragment();
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
