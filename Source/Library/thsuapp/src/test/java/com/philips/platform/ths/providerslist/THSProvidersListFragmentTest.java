package com.philips.platform.ths.providerslist;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.Language;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.State;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.practice.OnDemandSpecialty;
import com.americanwell.sdk.entity.practice.Practice;
import com.americanwell.sdk.entity.practice.PracticeInfo;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.PracticeProvidersManager;
import com.americanwell.sdk.manager.SDKCallback;
import com.philips.platform.ths.CustomRobolectricRunnerAmwel;
import com.philips.platform.ths.activity.THSLaunchActivity;
import com.philips.platform.ths.base.THSBaseView;
import com.philips.platform.ths.registration.THSConsumer;
import com.philips.platform.ths.utility.THSManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(CustomRobolectricRunnerAmwel.class)
public class THSProvidersListFragmentTest {

    private THSLaunchActivity mActivity;
    private THSProvidersListFragment thsProvidersListFragment;
    private THSProviderListPresenter thsProviderListPresenter;

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
    THSProvidersListFragment thsProvidersListFragmentMock;

    @Mock
    SwipeRefreshLayout swipeRL;

    @Mock
    THSConsumer thsConsumerMock;

    @Mock
    THSProviderListPresenter thsProviderListPresenterMock;

    @Captor
    ArgumentCaptor<THSProvidersListCallback> argCaptor;

    @Mock
    THSProvidersListCallback thsProvidersListCallback;

    @Mock
    THSManager thsManager;

    @Mock
    FragmentActivity fragmentActivityMock;
    @Mock
    List<THSProviderInfo> providerInfoListMock;

    @Mock
    SDKError sdkErrorMock;

    @Mock
    THSProviderListViewInterface thsProviderListViewInterfaceMock;


    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);
        ShadowLog.stream = System.out;
        THSManager.getInstance().setAwsdk(awsdkMock);
        THSManager.getInstance().setPTHConsumer(thsConsumerMock);
        when(THSBaseView.getFragmentActivity()).thenReturn(mActivity);
        when(THSManager.getInstance().getAwsdk(mActivity).getPracticeProvidersManager()).thenReturn(practiseprovidermanagerMock);
        thsProvidersListFragment = new THSProvidersListFragment();
        thsProviderListPresenter = new THSProviderListPresenter(thsProvidersListFragmentMock,thsProvidersListFragmentMock);
        Bundle bundle = new Bundle();
        bundle.putParcelable("Provider List Fragment",practice);
        thsProvidersListFragment.setArguments(bundle);

    }

    @Test
    public void testProviderListFragmentLaunch(){
        SupportFragmentTestUtil.startFragment(thsProvidersListFragment);
        assertNotNull(thsProvidersListFragment);
    }

    @Test
    public void testProviderListPresenter() throws AWSDKInstantiationException {
        SupportFragmentTestUtil.startFragment(thsProvidersListFragment);
        when(thsProviderListPresenterMock.getPthManager()).thenReturn(thsManager);
        when(thsProvidersListFragmentMock.getFragmentActivity()).thenReturn(fragmentActivityMock);
        thsProviderListPresenterMock.fetchProviderList(consumer,practice);
        verify(practiseprovidermanagerMock).findProviders(any(Consumer.class),any(PracticeInfo.class), any(OnDemandSpecialty.class),any(String.class),any(Set.class),any(Set.class),
               any(State.class),any(Language.class),any(Integer.class),
               any(SDKCallback.class));

    }

    @Test
    public void testProviderListPresenterCallback(){
        SupportFragmentTestUtil.startFragment(thsProvidersListFragment);
        when(thsProvidersListFragmentMock.isFragmentAttached()).thenReturn(true);
        when(thsProviderListPresenterMock.isProviderAvailable(providerInfoListMock)).thenReturn(true);
        when(providerInfoListMock.size()).thenReturn(1);
        thsProviderListPresenter.onProvidersListReceived(providerInfoListMock,null);
        verify(thsProvidersListFragmentMock).updateMainView(any(Boolean.class));

    }
}
