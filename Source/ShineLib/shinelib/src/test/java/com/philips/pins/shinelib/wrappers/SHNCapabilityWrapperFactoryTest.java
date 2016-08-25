/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.wrappers;

import com.philips.pins.shinelib.SHNCapability;
import com.philips.pins.shinelib.SHNCapabilityType;
import com.philips.pins.shinelib.capabilities.CapabilityBluetoothDirect;
import com.philips.pins.shinelib.capabilities.SHNCapabilityConfigEnergyIntake;
import com.philips.pins.shinelib.capabilities.SHNCapabilityConfigHeartRateZones;
import com.philips.pins.shinelib.capabilities.SHNCapabilityConfigSedentary;
import com.philips.pins.shinelib.capabilities.SHNCapabilityDeviceDiagnostics;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

public class SHNCapabilityWrapperFactoryTest {

    @Mock
    private SHNCapabilityDeviceDiagnostics deviceDiagnosticsMock;

    @Mock
    private SHNCapabilityConfigSedentary sedentaryMock;

    @Mock
    private SHNCapabilityConfigHeartRateZones heartRateZonesMock;

    @Mock
    private SHNCapabilityConfigEnergyIntake energyIntakeMock;

    @Mock
    private CapabilityBluetoothDirect bluetoothDirectMock;

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
        SHNCapability capabilityWrapper = SHNCapabilityWrapperFactory.createCapabilityWrapper(sedentaryMock, SHNCapabilityType.CONFIG_SEDENTARY, null, null);
        assertThat(capabilityWrapper).isInstanceOf(SHNCapabilityConfigSedentaryWrapper.class);
    }

    @Test
    public void ShouldCreateSHNCapabilityConfigHeartRateZonesWrapper_WhenHeartRateZonesIsProvided() {
        SHNCapability capabilityWrapper = SHNCapabilityWrapperFactory.createCapabilityWrapper(heartRateZonesMock, SHNCapabilityType.CONFIG_HEARTRATE_ZONES, null, null);
        assertThat(capabilityWrapper).isInstanceOf(SHNCapabilityConfigHeartRateZonesWrapper.class);
    }

    @Test
    public void ShouldCreateASHNCapabilityConfigEnergyIntakeWrapper_WhenConfigEnergyIntakeIsProvided() {
        SHNCapability capabilityWrapper = SHNCapabilityWrapperFactory.createCapabilityWrapper(energyIntakeMock, SHNCapabilityType.CONFIG_ENERGY_INTAKE, null, null);
        assertThat(capabilityWrapper).isInstanceOf(SHNCapabilityConfigEnergyIntakeWrapper.class);
    }

    @Test
    public void ShouldCreateCapabilityBluetoothDirectWrapper_WhenBluetoothDirectIsProvided() {
        SHNCapability capabilityWrapper = SHNCapabilityWrapperFactory.createCapabilityWrapper(bluetoothDirectMock, SHNCapabilityType.BLUETOOTH_DIRECT, null, null);
        assertThat(capabilityWrapper).isInstanceOf(CapabilityBluetoothDirect.class);
    }
}