package com.philips.platform.ths.providerslist;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

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
import com.philips.platform.ths.R;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(CustomRobolectricRunnerAmwel.class)
public class THSProvidersListFragmentTest {

    private THSLaunchActivity mActivity;
    private THSProvidersListFragment thsProvidersListFragment;
    private THSProviderListFragmentMock thsProvidersListFragmentTest;
    private THSProviderListPresenter thsProviderListPresenter;
    private THSProvidersListAdapter thsProvidersListAdapter;

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

    @Mock
    THSProvidersListAdapter thsProvidersListAdapterMock;

    @Mock
    RecyclerView recyclerViewMock;

    @Mock
    ViewGroup viewGroupMock;

    @Mock
    ProviderInfo providerInfo;

    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);
        ShadowLog.stream = System.out;
        THSManager.getInstance().setAwsdk(awsdkMock);
        THSManager.getInstance().setPTHConsumer(thsConsumerMock);
        when(THSBaseView.getFragmentActivity()).thenReturn(mActivity);
        when(THSManager.getInstance().getAwsdk(mActivity).getPracticeProvidersManager()).thenReturn(practiseprovidermanagerMock);
        thsProvidersListFragment = new THSProvidersListFragment();
        thsProvidersListFragmentTest = new THSProviderListFragmentMock();
        thsProviderListPresenter = new THSProviderListPresenter(thsProvidersListFragmentMock,thsProvidersListFragmentMock){
            @Override
            boolean isProviderAvailable(List<THSProviderInfo> providerInfoList) {
                return true;
            }
        };
        Bundle bundle = new Bundle();
        bundle.putParcelable("Provider List Fragment",practice);
        thsProvidersListFragment.setArguments(bundle);
        List list = new ArrayList();
        list.add(providerInfo);
        thsProvidersListAdapter = new THSProvidersListAdapter(list);

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
        verify(thsProvidersListFragmentMock).updateProviderAdapterList(any(List.class));

    }

    @Test
    public void testOnEventGetStartedInPresenter(){
        SupportFragmentTestUtil.startFragment(thsProvidersListFragment);
        thsProviderListPresenter.onEvent(R.id.getStartedButton);
        verify(practiseprovidermanagerMock).getOnDemandSpecialties(any(Consumer.class),any(PracticeInfo.class),any(String.class),any(SDKCallback.class));
    }

    @Test(expected = NullPointerException.class)
    public void testOnEventLaunchAppointmentInPresenter(){
        SupportFragmentTestUtil.startFragment(thsProvidersListFragment);
        thsProviderListPresenter.onEvent(R.id.getScheduleAppointmentButton);
        verify(thsProviderListPresenterMock).launchAvailableProviderListFragment(any(Practice.class));
    }

    @Test
    public void testOnClickInFragment(){
        SupportFragmentTestUtil.startFragment(thsProvidersListFragmentTest);
        thsProvidersListFragmentTest.THSProviderListPresenter = thsProviderListPresenterMock;
        final View getStartedBtn = thsProvidersListFragmentTest.getView().findViewById(R.id.getStartedButton);
        final View getScheduleAppointmentButton = thsProvidersListFragmentTest.getView().findViewById(R.id.getScheduleAppointmentButton);
        thsProvidersListFragmentTest.onClick(getStartedBtn);
        verify(thsProviderListPresenterMock).onEvent(any(Integer.class));
        thsProvidersListFragmentTest.onClick(getScheduleAppointmentButton);
        verify(thsProviderListPresenterMock,atLeastOnce()).onEvent(any(Integer.class));
    }

    @Test
    public void testOnProviderListUpdate(){
        SupportFragmentTestUtil.startFragment(thsProvidersListFragmentTest);
        thsProvidersListFragmentTest.recyclerView = recyclerViewMock;
        thsProvidersListFragmentTest.updateProviderAdapterList(providerInfoListMock);
        thsProvidersListFragmentTest.updateMainView(true);
        thsProvidersListFragmentTest.updateMainView(false);
        verify(recyclerViewMock).setAdapter(any(RecyclerView.Adapter.class));
    }

    /*@Test
    public void testAdapterOnBindViewHolder(){
        THSProvidersListAdapter.MyViewHolder myViewHolder = new THSProvidersListAdapter.MyViewHolder(viewGroupMock);
        thsProvidersListAdapter.onBindViewHolder(myViewHolder,0);
        verify(myViewHolder.name,times(2)).setText(anyString());
    }*/



}
