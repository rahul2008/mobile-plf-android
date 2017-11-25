/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.base;

import android.os.Bundle;
import android.view.ViewGroup;

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
import com.philips.platform.ths.appointment.THSAvailableProvider;
import com.philips.platform.ths.appointment.THSAvailableProviderList;
import com.philips.platform.ths.appointment.THSProviderNotAvailableFragment;
import com.philips.platform.ths.appointment.THSProviderNotAvailablePresenter;
import com.philips.platform.ths.registration.dependantregistration.THSConsumer;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.uappclasses.THSCompletionProtocol;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.listener.ActionBarListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.matchers.Null;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.philips.platform.ths.utility.THSConstants.THS_APPLICATION_ID;
import static org.mockito.Mockito.when;

@RunWith(CustomRobolectricRunnerAmwel.class)
public class THSBaseFragmentTest {

    THSBaseFragment mThsBaseFragment;

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
    ViewGroup viewGroupMock;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(appInfraInterfaceMock.getTagging()).thenReturn(appTaggingInterface);
        when(appInfraInterfaceMock.getTagging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(appTaggingInterface);
        when(appInfraInterfaceMock.getConfigInterface()).thenReturn(appConfigurationInterfaceMock);
        when(appInfraInterfaceMock.getServiceDiscovery()).thenReturn(serviceDiscoveryMock);
        when(appInfraInterfaceMock.getLogging()).thenReturn(loggingInterface);
        when(appInfraInterfaceMock.getLogging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(loggingInterface);

        THSManager.getInstance().setAppInfra(appInfraInterfaceMock);


        mThsBaseFragment = new THSBaseFragment();
        SupportFragmentTestUtil.startFragment(mThsBaseFragment);
    }

    @Test
    public void finishActivityAffinity() throws Exception {
        mThsBaseFragment.finishActivityAffinity();
    }

    @Test
    public void exitFromAmWell() throws Exception {
        mThsBaseFragment.exitFromAmWell(THSCompletionProtocol.THSExitType.Other);
        assert THSManager.getInstance().getPthVisitContext() == null;
    }

    @Test
    public void onNetworkConnectionChangedtrue() throws Exception {
        mThsBaseFragment.onNetworkConnectionChanged(true);
    }

    @Test(expected = NullPointerException.class)
    public void onNetworkConnectionChangedFalse() throws Exception {
        mThsBaseFragment.onNetworkConnectionChanged(false);
    }

    @Test(expected = NullPointerException.class)
    public void customProgress(){
        mThsBaseFragment.createCustomProgressBar(viewGroupMock,0);
    }
}