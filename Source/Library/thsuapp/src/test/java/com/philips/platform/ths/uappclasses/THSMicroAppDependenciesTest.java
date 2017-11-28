/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.uappclasses;

import android.content.Context;

import com.americanwell.sdk.entity.consumer.Consumer;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.ths.registration.dependantregistration.THSConsumer;
import com.philips.platform.ths.utility.THSManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

public class THSMicroAppDependenciesTest {

    THSMicroAppDependencies mThsMicroAppDependencies;

    @Mock
    AppInfraInterface appInfraInterfaceMock;

    @Mock
    Context context;

    @Mock
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

}