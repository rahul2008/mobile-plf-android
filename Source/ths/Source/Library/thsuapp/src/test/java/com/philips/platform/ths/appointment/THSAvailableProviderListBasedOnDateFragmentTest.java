/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.appointment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.Authentication;
import com.americanwell.sdk.entity.Language;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.SDKLocalDate;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.practice.Practice;
import com.americanwell.sdk.entity.provider.AvailableProviders;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.ConsumerManager;
import com.americanwell.sdk.manager.PracticeProvidersManager;
import com.americanwell.sdk.manager.SDKCallback;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.ths.BuildConfig;
import com.philips.platform.ths.CustomRobolectricRunnerAmwel;
import com.philips.platform.ths.R;
import com.philips.platform.ths.init.THSInitFragment;
import com.philips.platform.ths.init.THSInitPresenter;
import com.philips.platform.ths.login.THSAuthentication;
import com.philips.platform.ths.registration.dependantregistration.THSConsumer;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.view.widget.ProgressBar;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.Date;

import static com.philips.platform.ths.utility.THSConstants.THS_APPLICATION_ID;
import static com.philips.platform.ths.utility.THSConstants.THS_AVAILABLE_PROVIDER_LIST;
import static com.philips.platform.ths.utility.THSConstants.THS_PRACTICE_INFO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(CustomRobolectricRunnerAmwel.class)
public class THSAvailableProviderListBasedOnDateFragmentTest {

    THSAvailableProviderListBasedOnDateFragment mTHSAvailableProviderListBasedOnDateFragment;

    @Mock
    Date dateMock;

    @Mock
    Practice practiceMock;

    @Mock
    THSAvailableProviderList thsAvailableProviderList;

    @Mock
    AWSDK awsdkMock;


    @Mock
    AppInfraInterface appInfraInterfaceMock;

    @Mock
    THSSDKError thssdkErrorMock;


    @Mock
    SDKError sdkErrorMock;

    @Mock
    AppTaggingInterface appTaggingInterface;

    @Mock
    LoggingInterface loggingInterface;

    @Mock
    AppConfigurationInterface appConfigurationInterfaceMock;

    @Mock
    THSConsumer thsConsumerMock;

    @Mock
    THSAvailableProviderListBasedOnDatePresenter thsAvailableProviderListBasedOnDatePresenterMock;

    @Mock
    ServiceDiscoveryInterface serviceDiscoveryMock;

    @Mock
    ConsumerManager consumerManagerMock;

    @Mock
    ActionBarListener actionBarListenerMock;

    @Mock
    PracticeProvidersManager practiceProvidersManagerMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);


        THSManager.getInstance().setAwsdk(awsdkMock);
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);
        THSManager.getInstance().setThsConsumer(thsConsumerMock);
        when(appInfraInterfaceMock.getTagging()).thenReturn(appTaggingInterface);
        when(appInfraInterfaceMock.getTagging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(appTaggingInterface);
        when(appInfraInterfaceMock.getConfigInterface()).thenReturn(appConfigurationInterfaceMock);
        when(appInfraInterfaceMock.getServiceDiscovery()).thenReturn(serviceDiscoveryMock);
        when(appInfraInterfaceMock.getLogging()).thenReturn(loggingInterface);
        when(appInfraInterfaceMock.getLogging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(loggingInterface);
        when(thssdkErrorMock.getSdkError()).thenReturn(sdkErrorMock);

        when(awsdkMock.getPracticeProvidersManager()).thenReturn(practiceProvidersManagerMock);

        THSManager.getInstance().setAppInfra(appInfraInterfaceMock);


        Bundle bundle = new Bundle();
        bundle.putSerializable(THSConstants.THS_DATE,dateMock);
        bundle.putParcelable(THS_PRACTICE_INFO,practiceMock);
        bundle.putParcelable(THS_AVAILABLE_PROVIDER_LIST,thsAvailableProviderList);
        mTHSAvailableProviderListBasedOnDateFragment = new THSAvailableProviderListBasedOnDateFragment();
        mTHSAvailableProviderListBasedOnDateFragment.setArguments(bundle);
        mTHSAvailableProviderListBasedOnDateFragment.setActionBarListener(actionBarListenerMock);
        SupportFragmentTestUtil.startFragment(mTHSAvailableProviderListBasedOnDateFragment);
    }
    
    @Test
    public void refreshListWithAwsdkException() throws Exception {

        doThrow(AWSDKInstantiationException.class).when(practiceProvidersManagerMock).findFutureAvailableProviders((Consumer)isNull(), any(Practice.class),
                anyString(), any(Language.class), any(Date.class), anyInt(),anyInt(), any(SDKCallback.class));
        mTHSAvailableProviderListBasedOnDateFragment.refreshView();
        verify(practiceProvidersManagerMock).findFutureAvailableProviders((Consumer)isNull(), any(Practice.class),
                (String) isNull(), (Language)isNull(), any(Date.class), (Integer) isNull(),(Integer)isNull(), any(SDKCallback.class));
    }

    @Test
    public void refreshList() throws Exception {
        mTHSAvailableProviderListBasedOnDateFragment.mTHSAvailableProviderListBasedOnDatePresenter = thsAvailableProviderListBasedOnDatePresenterMock;
        mTHSAvailableProviderListBasedOnDateFragment.refreshView();
        verify(thsAvailableProviderListBasedOnDatePresenterMock).getAvailableProvidersBasedOnDate();
    }

    @Test
    public void refreshListWhenmThsAvailableProviderListisNull() throws AWSDKInstantiationException {
        mTHSAvailableProviderListBasedOnDateFragment.mTHSAvailableProviderListBasedOnDatePresenter = thsAvailableProviderListBasedOnDatePresenterMock;
        mTHSAvailableProviderListBasedOnDateFragment.mThsAvailableProviderList = null;
        mTHSAvailableProviderListBasedOnDateFragment.refreshList();
        verify(thsAvailableProviderListBasedOnDatePresenterMock).getAvailableProvidersBasedOnDate();
    }

    @Test
    public void onClick() throws Exception {
        mTHSAvailableProviderListBasedOnDateFragment.mTHSAvailableProviderListBasedOnDatePresenter = thsAvailableProviderListBasedOnDatePresenterMock;
        final View viewById = mTHSAvailableProviderListBasedOnDateFragment.getView().findViewById(R.id.calendar_view);
        viewById.performClick();
        verify(thsAvailableProviderListBasedOnDatePresenterMock).onEvent(R.id.calendar_view);
    }

    @Test
    public void setDate() throws Exception {
        mTHSAvailableProviderListBasedOnDateFragment.setDate(dateMock);
        assertNotNull(mTHSAvailableProviderListBasedOnDateFragment.mDate);
        assertThat(mTHSAvailableProviderListBasedOnDateFragment.mDate).isInstanceOf(Date.class);
    }

}