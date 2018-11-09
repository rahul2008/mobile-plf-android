/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.platform.ths.settings;

import android.view.View;

import com.americanwell.sdk.AWSDK;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.ths.BuildConfig;
import com.philips.platform.ths.R;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.listener.ActionBarListener;

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
public class THSCustomerSupportFragmentTest {
    THSCustomerSupportFragment mThsCustomerSupportFragment;

    @Mock
    AppInfraInterface appInfraInterface;

    @Mock
    AppTaggingInterface appTaggingInterface;

    @Mock
    LoggingInterface loggingInterface;

    @Mock
    ServiceDiscoveryInterface serviceDiscoveryMock;

    @Mock
    AWSDK awsdkMock;

    @Mock
    ActionBarListener actionBarListenerMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        THSManager.getInstance().setAwsdk(awsdkMock);

        when(appInfraInterface.getTagging()).thenReturn(appTaggingInterface);
        when(appInfraInterface.getTagging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(appTaggingInterface);
        when(appInfraInterface.getLogging()).thenReturn(loggingInterface);
        when(appInfraInterface.getLogging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(loggingInterface);
        when(appInfraInterface.getServiceDiscovery()).thenReturn(serviceDiscoveryMock);
        THSManager.getInstance().setAppInfra(appInfraInterface);

        mThsCustomerSupportFragment = new THSCustomerSupportFragment();
        mThsCustomerSupportFragment.setActionBarListener(actionBarListenerMock);
        SupportFragmentTestUtil.startFragment(mThsCustomerSupportFragment);
    }

    @Test
    public void onClick() throws Exception {
        final View viewById = mThsCustomerSupportFragment.getView().findViewById(R.id.ths_customer_support_phone_number_id);
        viewById.performClick();
    }

}