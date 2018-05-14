/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.ble.context;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.bluelib.plugindefinition.ReferenceNodeDeviceDefinitionInfo;
import com.philips.cdp2.commlib.ble.BleDeviceCache;
import com.philips.cdp2.commlib.ble.discovery.BleDiscoveryStrategy;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.core.configuration.RuntimeConfiguration;
import com.philips.cdp2.commlib.core.exception.TransportUnavailableException;
import com.philips.cdp2.commlib.core.util.Availability.AvailabilityListener;
import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNCentral.SHNCentralListener;
import com.philips.pins.shinelib.exceptions.SHNBluetoothHardwareUnavailableException;
import com.philips.pins.shinelib.utility.SHNLogger;
import com.philips.pins.shinelib.utility.SHNLogger.LoggerImplementation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@PrepareForTest({SHNLogger.class})
@RunWith(PowerMockRunner.class)
public class BleTransportContextTest {

    @Mock
    private Context contextMock;

    @Mock
    private SHNCentral shnCentralMock;

    @Mock
    private RuntimeConfiguration runtimeConfigurationMock;

    @Mock
    private BleDeviceCache bleDeviceCacheMock;

    @Mock
    private CommunicationStrategy communicationStrategyMock;

    @Mock
    private AvailabilityListener<BleTransportContext> availabilityListenerMock;

    @Captor
    private ArgumentCaptor<SHNCentralListener> shnCentralListenerArgumentCaptor;

    private BleTransportContext bleTransportContext;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mockStatic(SHNLogger.class);

        when(runtimeConfigurationMock.getContext()).thenReturn(contextMock);
    }

    @Test
    public void givenBleTransportContextIsConstructed_whenLogIsEnabledInRuntimeConfiguration_thenALoggerIsRegisteredOnBluelib() {
        when(runtimeConfigurationMock.isLogEnabled()).thenReturn(true);

        constructBleTransportContext();

        verifyStatic(SHNLogger.class, times(1));
        SHNLogger.registerLogger(any(LoggerImplementation.class));
    }

    @Test
    public void givenBleTransportContextIsConstructed_whenLogIsDisabledInRuntimeConfiguration_thenNoLoggerIsRegisteredOnBluelib() {
        when(runtimeConfigurationMock.isLogEnabled()).thenReturn(false);

        constructBleTransportContext();

        verifyStatic(SHNLogger.class, never());
        SHNLogger.registerLogger(any(LoggerImplementation.class));
    }

    @Test(expected = TransportUnavailableException.class)
    public void givenBluetoothHardwareIsUnavailable_whenBleTransportContextIsConstructed_thenThrowAnException() {
        new BleTransportContext(runtimeConfigurationMock) {
            @NonNull
            @Override
            SHNCentral createCentral(final Context context, final boolean showPopupIfBLEIsTurnedOff) throws SHNBluetoothHardwareUnavailableException {
                throw new SHNBluetoothHardwareUnavailableException();
            }
        };
    }

    @Test
    public void givenBleTransportContextIsConstructed_whenBluetoothHardwareBecomesUnavailable_thenClearDeviceCache() {
        constructBleTransportContext();

        when(shnCentralMock.isBluetoothAdapterEnabled()).thenReturn(false);
        shnCentralListenerArgumentCaptor.getValue().onStateUpdated(shnCentralMock);

        verify(bleDeviceCacheMock, times(1)).clear();
    }

    @Test
    public void givenBleTransportContextIsConstructed_whenObtainingDiscoveryStrategy_thenReturnBleDiscoveryStrategy() {
        constructBleTransportContext();

        assertThat(bleTransportContext.getDiscoveryStrategy() instanceof BleDiscoveryStrategy).isTrue();
    }

    @Test
    public void givenBleTransportContextIsConstructed_whenAddingAvailabilityListener_thenNotifyListenerOfCurrentAvailability() {
        constructBleTransportContext();

        bleTransportContext.addAvailabilityListener(availabilityListenerMock);

        verify(availabilityListenerMock, times(1)).onAvailabilityChanged(bleTransportContext);
    }

    private void constructBleTransportContext() {
        bleTransportContext = new BleTransportContext(runtimeConfigurationMock) {
            @NonNull
            @Override
            SHNCentral createCentral(final Context context, final boolean showPopupIfBLEIsTurnedOff) throws SHNBluetoothHardwareUnavailableException {
                return shnCentralMock;
            }

            @NonNull
            @Override
            BleDeviceCache createBleDeviceCache() {
                return bleDeviceCacheMock;
            }

            @NonNull
            @Override
            public CommunicationStrategy createCommunicationStrategyFor(@NonNull NetworkNode networkNode) {
                return communicationStrategyMock;
            }
        };

        verify(shnCentralMock).registerDeviceDefinition(any(ReferenceNodeDeviceDefinitionInfo.class));
        verify(shnCentralMock).registerShnCentralListener(shnCentralListenerArgumentCaptor.capture());
    }
}
