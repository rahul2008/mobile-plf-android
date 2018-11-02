/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.platform.ths.base;

import android.view.ViewGroup;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.ths.BuildConfig;
import com.philips.platform.ths.uappclasses.THSCompletionProtocol;
import com.philips.platform.ths.utility.THSManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static com.philips.platform.ths.utility.THSConstants.THS_APPLICATION_ID;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
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
        assert THSManager.getInstance().getVisitContext() == null;
    }

    @Test
    public void onNetworkConnectionChangedtrue() throws Exception {
        mThsBaseFragment.onNetworkConnectionChanged(true);
    }

    @Test(expected = NullPointerException.class)
    public void onNetworkConnectionChangedFalse() throws Exception {
        mThsBaseFragment.onNetworkConnectionChanged(false);
    }

    @Test
    public void customProgress(){
        mThsBaseFragment.createCustomProgressBar(viewGroupMock,0);
    }
}