/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.ths.providerslist;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.Language;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.State;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.practice.OnDemandSpecialty;
import com.americanwell.sdk.entity.practice.Practice;
import com.americanwell.sdk.entity.practice.PracticeInfo;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.americanwell.sdk.entity.provider.ProviderVisibility;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.internal.entity.provider.ProviderTypeImpl;
import com.americanwell.sdk.manager.PracticeProvidersManager;
import com.americanwell.sdk.manager.SDKCallback;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.ths.BuildConfig;
import com.philips.platform.ths.CustomRobolectricRunnerAmwel;
import com.philips.platform.ths.R;
import com.philips.platform.ths.activity.THSLaunchActivity;
import com.philips.platform.ths.appointment.THSAvailableProvider;
import com.philips.platform.ths.base.THSBaseView;
import com.philips.platform.ths.registration.THSConsumerWrapper;
import com.philips.platform.ths.registration.dependantregistration.THSConsumer;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uid.view.widget.NotificationBadge;
import com.philips.platform.uid.view.widget.RatingBar;

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
import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.philips.platform.ths.utility.THSConstants.THS_APPLICATION_ID;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
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
    THSConsumerWrapper thsConsumerMock;

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
    THSProviderInfo thsProviderInfoMock;

    @Mock
    ProviderInfo providerInfoMock;

    @Mock
    ImageView imageView;

    @Mock
    THSProvidersListAdapter.MyViewHolder myViewHolderMock;

    @Mock
    Resources resources;

    @Mock
    TextView textViewIsAvailable, tvName, tvPractice;

    @Mock
    RatingBar ratingBarMock;

    List list;

    ProviderVisibility pv;

    ProviderTypeImpl pt;

    @Mock
    List<THSProviderInfo> thsProviderInfoListMock;

    @Mock
    View.OnClickListener onClickListenerMock;

    @Mock
    RelativeLayout relativeLayoutMock;

    @Mock
    NotificationBadge nbMock;

    @Mock
    AppInfraInterface appInfraInterface;

    @Mock
    AppTaggingInterface appTaggingInterface;

    @Mock
    LoggingInterface loggingInterface;

    @Mock
    THSConsumer thsConsumer;

    @Mock
    Consumer consumerMock;

    @Mock
    ServiceDiscoveryInterface serviceDiscoveryMock;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ShadowLog.stream = System.out;
        THSManager.getInstance().setAwsdk(awsdkMock);
        THSManager.getInstance().setPTHConsumer(thsConsumerMock);

        THSManager.getInstance().setThsConsumer(thsConsumer);
        THSManager.getInstance().setThsParentConsumer(thsConsumer);
        when(thsConsumer.getConsumer()).thenReturn(consumerMock);

        when(appInfraInterface.getTagging()).thenReturn(appTaggingInterface);
        when(appInfraInterface.getTagging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(appTaggingInterface);
        when(appInfraInterface.getLogging()).thenReturn(loggingInterface);
        when(appInfraInterface.getLogging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(loggingInterface);
        when(appInfraInterface.getServiceDiscovery()).thenReturn(serviceDiscoveryMock);
        THSManager.getInstance().setAppInfra(appInfraInterface);

        when(THSBaseView.getFragmentActivity()).thenReturn(mActivity);
        when(THSManager.getInstance().getAwsdk(mActivity).getPracticeProvidersManager()).thenReturn(practiseprovidermanagerMock);
        thsProvidersListFragment = new THSProvidersListFragment();
        thsProvidersListFragmentTest = new THSProviderListFragmentMock();
        thsProviderListPresenter = new THSProviderListPresenter(thsProvidersListFragmentMock, thsProvidersListFragmentMock) {
            @Override
            boolean isProviderAvailable(List<THSProviderInfo> providerInfoList) {
                return true;
            }
        };
        Bundle bundle = new Bundle();
        bundle.putParcelable("Provider List Fragment", practice);
        thsProvidersListFragment.setArguments(bundle);
        setUpAdapter();

    }

    private void setUpAdapter() {
        pv = ProviderVisibility.WEB_AVAILABLE;
        thsProviderInfoMock.setTHSProviderInfo(providerInfoMock);
        list = new ArrayList();
        myViewHolderMock.isAvailableStatus = imageView;
        myViewHolderMock.isAvailble = textViewIsAvailable;
        myViewHolderMock.name = tvName;
        myViewHolderMock.providerRating = ratingBarMock;
        myViewHolderMock.practice = tvPractice;
        myViewHolderMock.notificationBadge = nbMock;
        myViewHolderMock.relativeLayout = relativeLayoutMock;
        myViewHolderMock.relativeLayout.setOnClickListener(onClickListenerMock);
    }

    @Test
    public void testProviderListFragmentLaunch() {
        SupportFragmentTestUtil.startFragment(thsProvidersListFragment);
        assertNotNull(thsProvidersListFragment);
    }

    @Test
    public void testProviderListPresenter() throws AWSDKInstantiationException {
        SupportFragmentTestUtil.startFragment(thsProvidersListFragment);
        when(thsProviderListPresenterMock.getPthManager()).thenReturn(thsManager);
        when(thsProvidersListFragmentMock.getFragmentActivity()).thenReturn(fragmentActivityMock);
        thsProviderListPresenterMock.fetchProviderList(consumer, practice);
        verify(practiseprovidermanagerMock).findProviders((Consumer)isNull(), (PracticeInfo)isNull(), (OnDemandSpecialty)isNull(), (String)isNull(), (Set)isNull(), (Set)isNull(),
                (State)isNull(), (Language)isNull(),(Integer)isNull(),
                any(SDKCallback.class));

    }

    @Test
    public void testProviderListPresenterCallback() {
        SupportFragmentTestUtil.startFragment(thsProvidersListFragment);
        when(thsProvidersListFragmentMock.isFragmentAttached()).thenReturn(true);
        when(thsProviderListPresenterMock.isProviderAvailable(providerInfoListMock)).thenReturn(true);
        when(providerInfoListMock.size()).thenReturn(1);
        thsProviderListPresenter.onProvidersListReceived(providerInfoListMock, null);
        verify(thsProvidersListFragmentMock).updateMainView(any(Boolean.class));
        verify(thsProvidersListFragmentMock).updateProviderAdapterList(any(List.class));

    }

    @Test
    public void testOnEventGetStartedInPresenter() {
        SupportFragmentTestUtil.startFragment(thsProvidersListFragment);
        thsProviderListPresenter.onEvent(R.id.getStartedButton);
        verify(practiseprovidermanagerMock).getOnDemandSpecialties((Consumer)isNull(),(PracticeInfo)isNull(), (String)isNull(), any(SDKCallback.class));
    }

    @Test(expected = NullPointerException.class)
    public void testOnEventLaunchAppointmentInPresenter() {
        SupportFragmentTestUtil.startFragment(thsProvidersListFragment);
        thsProviderListPresenter.onEvent(R.id.getScheduleAppointmentButton);
        verify(thsProviderListPresenterMock).launchAvailableProviderListFragment(any(Practice.class));
    }

    @Test
    public void testOnClickInFragment() {
        SupportFragmentTestUtil.startFragment(thsProvidersListFragmentTest);
        thsProvidersListFragmentTest.THSProviderListPresenter = thsProviderListPresenterMock;
        final View getStartedBtn = thsProvidersListFragmentTest.getView().findViewById(R.id.getStartedButton);
        final View getScheduleAppointmentButton = thsProvidersListFragmentTest.getView().findViewById(R.id.getScheduleAppointmentButton);
        thsProvidersListFragmentTest.onClick(getStartedBtn);
        verify(thsProviderListPresenterMock).onEvent(any(Integer.class));
        thsProvidersListFragmentTest.onClick(getScheduleAppointmentButton);
        verify(thsProviderListPresenterMock, atLeastOnce()).onEvent(any(Integer.class));
    }

    @Test
    public void testOnProviderListUpdate() {
        SupportFragmentTestUtil.startFragment(thsProvidersListFragmentTest);
        thsProvidersListFragmentTest.recyclerView = recyclerViewMock;
        thsProvidersListFragmentTest.updateProviderAdapterList(providerInfoListMock);
        thsProvidersListFragmentTest.updateMainView(true);
        thsProvidersListFragmentTest.updateMainView(false);
        verify(recyclerViewMock).setAdapter(any(RecyclerView.Adapter.class));
    }

    @Test
    public void testAdapterOnBindViewHolderIf() {

        list.add(thsProviderInfoMock);
        when(thsProviderInfoMock.getVisibility()).thenReturn(pv);
        when(thsProviderInfoMock.getProviderInfo()).thenReturn(providerInfoMock);
        when(providerInfoMock.getFullName()).thenReturn("Something");
        when(providerInfoMock.getSpecialty()).thenReturn(pt);
        when(providerInfoMock.hasImage()).thenReturn(true);
        when(thsProvidersListAdapterMock.getName(thsProviderInfoMock)).thenReturn("test");
        when(imageView.getContext()).thenReturn(fragmentActivityMock);
        when(fragmentActivityMock.getResources()).thenReturn(resources);
        when(resources.getString(R.string.ths_provider_available)).thenReturn("Available now");
        thsProvidersListAdapter = new THSProvidersListAdapter(list);
        thsProvidersListAdapter.onBindViewHolder(myViewHolderMock, 0);
        verify(myViewHolderMock.isAvailble,atLeastOnce()).setText("Available now");
        pv = ProviderVisibility.OFFLINE;
        when(thsProviderInfoMock.getVisibility()).thenReturn(pv);
        when(resources.getString(R.string.ths_provider_offline)).thenReturn("Schedule appointment");
        thsProvidersListAdapter = new THSProvidersListAdapter(list);
        thsProvidersListAdapter.onBindViewHolder(myViewHolderMock, 0);
        verify(myViewHolderMock.isAvailble,atLeastOnce()).setText("Schedule appointment");
        pv = ProviderVisibility.WEB_BUSY;
        when(thsProviderInfoMock.getVisibility()).thenReturn(pv);
        when(resources.getString(R.string.ths_provider_busy)).thenReturn("Patient waiting");
        thsProvidersListAdapter = new THSProvidersListAdapter(list);
        thsProvidersListAdapter.onBindViewHolder(myViewHolderMock, 0);
        verify(myViewHolderMock.isAvailble,atLeastOnce()).setText("Patient waiting");
        verify(myViewHolderMock.name, atLeastOnce()).setText(anyString());
    }


    @Mock
    THSAvailableProvider thsAvailableProviderMock;

    @Mock
    List<Date> listDatesMock;

    @Test
    public void testAdapterOnBindViewHolderElse() {

        list.add(thsAvailableProviderMock);
        when(thsProviderInfoMock.getRating()).thenReturn(0);
        when(thsAvailableProviderMock.getAvailableAppointmentTimeSlots()).thenReturn(listDatesMock);
        when(thsAvailableProviderMock.getAvailableAppointmentTimeSlots().size()).thenReturn(10);
        when(thsProvidersListAdapterMock.getName(thsProviderInfoMock)).thenReturn("test");
        when(textViewIsAvailable.getContext()).thenReturn(fragmentActivityMock);
        when(fragmentActivityMock.getResources()).thenReturn(resources);
        when(resources.getString(R.string.ths_provider_available_timeslots)).thenReturn("Available time-slots");
        thsProvidersListAdapter = new THSProvidersListAdapter(list);
        thsProvidersListAdapter.onBindViewHolder(myViewHolderMock, 0);
        verify(myViewHolderMock.relativeLayout, atLeastOnce()).setOnClickListener(any(View.OnClickListener.class));

    }
}
