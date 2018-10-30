/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.appointment;

import android.os.Bundle;
import android.view.View;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.provider.AvailableProvider;
import com.americanwell.sdk.entity.provider.Provider;
import com.americanwell.sdk.entity.provider.ProviderType;
import com.americanwell.sdk.manager.ConsumerManager;
import com.americanwell.sdk.manager.PracticeProvidersManager;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.ths.BuildConfig;
import com.philips.platform.ths.CustomRobolectricRunnerAmwel;
import com.philips.platform.ths.R;
import com.philips.platform.ths.providerdetails.THSProviderEntity;
import com.philips.platform.ths.registration.dependantregistration.THSConsumer;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.view.widget.RatingBar;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.philips.platform.ths.utility.THSConstants.THS_APPLICATION_ID;
import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(CustomRobolectricRunnerAmwel.class)
public class THSProviderNotAvailableFragmentTest {

    THSProviderNotAvailableFragment mThsProviderNotAvailableFragment;

    @Mock
    AWSDK awsdkMock;

    @Mock
    ConsumerManager consumerManagerMock;

    @Mock
    THSConsumer thsConsumerMock;

    @Mock
    AppTaggingInterface appTaggingInterface;

    @Mock
    AppInfraInterface appInfraInterfaceMock;

    @Mock
    AppConfigurationInterface appConfigurationInterfaceMock;

    @Mock
    ServiceDiscoveryInterface serviceDiscoveryMock;

    @Mock
    LoggingInterface loggingInterface;

    @Mock
    THSSDKError thssdkErrorMock;

    @Mock
    SDKError sdkErrorMock;

    @Mock
    PracticeProvidersManager practiceProvidersManagerMock;

    @Mock
    Provider providerMock;

    @Mock
    THSAvailableProvider thsAvailableProvider;

    @Mock
    ActionBarListener actionBarListenerMock;

    @Mock
    THSAvailableProviderList thsAvailableProviderListMock;

    @Mock
    AvailableProvider availableProviderMock;

    @Mock
    ProviderType providerTypeMock;

    @Mock
    THSProviderNotAvailablePresenter thsProviderNotAvailablePresenterMock;

    @Mock
    Date dateMock;

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

        Bundle bundle = new Bundle();
        bundle.putParcelable(THSConstants.THS_PROVIDER,providerMock);
        bundle.putParcelable(THSConstants.THS_PROVIDER_ENTITY,thsAvailableProvider);

        when(awsdkMock.getPracticeProvidersManager()).thenReturn(practiceProvidersManagerMock);

        THSManager.getInstance().setAppInfra(appInfraInterfaceMock);

        mThsProviderNotAvailableFragment = new THSProviderNotAvailableFragment();
        mThsProviderNotAvailableFragment.setArguments(bundle);
        mThsProviderNotAvailableFragment.setActionBarListener(actionBarListenerMock);

        List<AvailableProvider> list = new ArrayList<>();
        list.add(availableProviderMock);

        when(thsAvailableProviderListMock.getAvailableProvidersList()).thenReturn(list);

        mThsProviderNotAvailableFragment.setDate(dateMock);

        when(providerMock.getSpecialty()).thenReturn(providerTypeMock);
        when(providerTypeMock.getName()).thenReturn("sdd");
        when(providerMock.getRating()).thenReturn(2);

        SupportFragmentTestUtil.startFragment(mThsProviderNotAvailableFragment);
    }

    @Test
    public void getProvider() throws Exception {
        final Provider provider = mThsProviderNotAvailableFragment.getProvider();
        assertNotNull(provider);
    }

    @Test
    public void updateProviderDetails() throws Exception {
        mThsProviderNotAvailableFragment.updateProviderDetails(thsAvailableProviderListMock);
    }

    @Test
    public void getThsProviderEntity() throws Exception {
        final THSProviderEntity thsProviderEntity = mThsProviderNotAvailableFragment.getThsProviderEntity();
        assertNotNull(thsProviderEntity);
    }

    @Test
    public void onClick() throws Exception {
        mThsProviderNotAvailableFragment.mThsProviderNotAvailablePresenter = thsProviderNotAvailablePresenterMock;
        final View viewById = mThsProviderNotAvailableFragment.getView().findViewById(R.id.calendar_view);
        viewById.performClick();
        verify(thsProviderNotAvailablePresenterMock).onEvent(R.id.calendar_view);
    }

    @Test
    public void setProvider(){
        mThsProviderNotAvailableFragment.setProvider(providerMock);
        final Provider provider = mThsProviderNotAvailableFragment.getProvider();
        assertNotNull(provider);
    }

}