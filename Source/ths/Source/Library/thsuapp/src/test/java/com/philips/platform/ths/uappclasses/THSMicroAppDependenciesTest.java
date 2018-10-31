/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.uappclasses;

import android.content.Context;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.ths.registration.dependantregistration.THSConsumer;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class THSMicroAppDependenciesTest {

    private THSMicroAppDependencies mThsMicroAppDependencies;

    @Mock
    private
    AppInfraInterface appInfraInterfaceMock;

    @Mock
    private
    Context context;

    @Mock
    private
    THSConsumer thsConsumerMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mThsMicroAppDependencies = new THSMicroAppDependencies(appInfraInterfaceMock);
    }

    @Test
    public void setThsConsumer() throws Exception {
        mThsMicroAppDependencies.setThsConsumer(thsConsumerMock);
        assertNotNull(THSManager.getInstance().getThsConsumer(context));
    }

    @Test
    public void assertThatOnBoardingFirebaseFlowIsSet() throws Exception {
        mThsMicroAppDependencies.setOnBoradingABFlow(THSConstants.THS_ONBOARDING_ABFLOW2);
        assertTrue(THSManager.getInstance().getOnBoradingABFlow().equalsIgnoreCase(THSConstants.THS_ONBOARDING_ABFLOW2));
    }

    @Test
    public void assertThatProviderFirebaseFlowIsSet() throws Exception {
        mThsMicroAppDependencies.setProviderListABFlow(THSConstants.THS_PROVIDERLIST_ABFLOW2);
        assertTrue(THSManager.getInstance().getProviderListABFlow().equalsIgnoreCase(THSConstants.THS_PROVIDERLIST_ABFLOW2));
    }

}