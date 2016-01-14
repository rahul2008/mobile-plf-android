/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.wrappers;

import com.philips.pins.shinelib.SHNCapability;
import com.philips.pins.shinelib.SHNCapabilityType;
import com.philips.pins.shinelib.capabilities.SHNCapabilityDeviceDiagnostics;
import com.philips.pins.shinelib.capabilities.SHNCapabilitySedentary;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

public class SHNCapabilityWrapperFactoryTest {

    @Mock
    private SHNCapabilityDeviceDiagnostics deviceDiagnosticsMock;

    @Mock
    private SHNCapabilitySedentary sedentaryMock;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void ShouldCreateSHNCapabilityDeviceDiagnosticsWrapper_WhenDEVICE_DIAGNOSTICIsProvided() {
        SHNCapability capabilityWrapper = SHNCapabilityWrapperFactory.createCapabilityWrapper(deviceDiagnosticsMock, SHNCapabilityType.DEVICE_DIAGNOSTIC, null, null);
        assertThat(capabilityWrapper).isInstanceOf(SHNCapabilityDeviceDiagnosticsWrapper.class);
    }

    @Test
    public void ShouldCreateSHNCapabilitySedentaryWrapper_WhenSedentaryIsProvided() {
        SHNCapability capabilityWrapper = SHNCapabilityWrapperFactory.createCapabilityWrapper(sedentaryMock, SHNCapabilityType.Sedentary, null, null);
        assertThat(capabilityWrapper).isInstanceOf(SHNCapabilityConfigSedentaryWrapper.class);
    }
}