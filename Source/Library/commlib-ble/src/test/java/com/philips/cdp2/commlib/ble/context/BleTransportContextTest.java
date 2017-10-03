/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.ble.context;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.cdp2.commlib.core.configuration.RuntimeConfiguration;
import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.exceptions.SHNBluetoothHardwareUnavailableException;
import com.philips.pins.shinelib.utility.SHNLogger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static com.philips.pins.shinelib.utility.SHNLogger.registerLogger;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@PrepareForTest({SHNLogger.class})
@RunWith(PowerMockRunner.class)
public class BleTransportContextTest {

    @Mock
    private Context mockedContext;

    @Mock
    private SHNCentral mockedShnCentral;

    @Mock
    private RuntimeConfiguration runtimeConfigurationMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        PowerMockito.mockStatic(SHNLogger.class);
    }

    @Test
    public void givenBleTransportContextIsConstructed_whenLogIsEnabledInRuntimeConfiguration_thenALoggerIsRegisteredOnBluelib() {
        when(runtimeConfigurationMock.isLogEnabled()).thenReturn(true);

        constructBleTransportContext();

        PowerMockito.verifyStatic(atLeastOnce());
        registerLogger(any(SHNLogger.LoggerImplementation.class));
    }

    @Test
    public void givenBleTransportContextIsConstructed_whenLogIsDisabledInRuntimeConfiguration_thenNoLoggerIsRegisteredOnBluelib() {
        when(runtimeConfigurationMock.isLogEnabled()).thenReturn(false);

        constructBleTransportContext();

        PowerMockito.verifyStatic(never());
        registerLogger(any(SHNLogger.LoggerImplementation.class));
    }

    @NonNull
    private BleTransportContext constructBleTransportContext() {
        return new BleTransportContext(runtimeConfigurationMock) {
            @Override
            SHNCentral createBlueLib(final Context context, final boolean showPopupIfBLEIsTurnedOff) throws SHNBluetoothHardwareUnavailableException {
                return mockedShnCentral;
            }
        };
    }
}
