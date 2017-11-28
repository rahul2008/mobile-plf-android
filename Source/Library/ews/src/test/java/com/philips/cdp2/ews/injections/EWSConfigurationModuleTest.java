/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.injections;

import android.content.Context;

import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.configuration.ContentConfiguration;
import com.philips.cdp2.ews.configuration.HappyFlowContentConfiguration;
import com.philips.cdp2.ews.configuration.TroubleShootContentConfiguration;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class EWSConfigurationModuleTest {

    private EWSConfigurationModule subject;

    @Mock private Context mockContext;
    @Mock private ContentConfiguration mockContentConfiguration;
    @Mock private BaseContentConfiguration mockBaseContentConfiguration;
    @Mock private HappyFlowContentConfiguration mockHappyContentConfiguration;
    @Mock private TroubleShootContentConfiguration mockTroubleShootContentConfiguration;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        subject = new EWSConfigurationModule(mockContext, mockContentConfiguration);
        when(mockContentConfiguration.getBaseContentConfiguration()).thenReturn(mockBaseContentConfiguration);
        when(mockContentConfiguration.getHappyFlowContentConfiguration()).thenReturn(mockHappyContentConfiguration);
        when(mockContentConfiguration.getTroubleShootContentConfiguration()).thenReturn(mockTroubleShootContentConfiguration);
    }

    @Test
    public void provideEWSConfigurationContent() throws Exception {
        assertEquals(subject.provideEWSConfigurationContent(), mockBaseContentConfiguration);
    }

    @Test
    public void provideHappyFlowContentConfiguration() throws Exception {
        assertEquals(subject.provideHappyFlowContentConfiguration(), mockHappyContentConfiguration);
    }

    @Test
    public void provideTroubleShootContentConfiguration() throws Exception {
        assertEquals(subject.provideTroubleShootContentConfiguration(), mockTroubleShootContentConfiguration);
    }

    @Test
    public void provideStringProvider() throws Exception {
        assertNotNull(subject.provideStringProvider());
    }

}