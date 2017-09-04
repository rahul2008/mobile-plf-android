package com.philips.cdp2.commlib.ble.context;

import android.content.Context;

import com.philips.cdp2.commlib_ble.BuildConfig;
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
import org.powermock.reflect.Whitebox;

import static com.philips.pins.shinelib.utility.SHNLogger.registerLogger;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.MockitoAnnotations.initMocks;

@PrepareForTest({SHNLogger.class, BuildConfig.class})
@RunWith(PowerMockRunner.class)
public class BleTransportContextTest {

    @Mock
    private Context mockedContext;

    @Mock
    private SHNCentral shnCentralMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        PowerMockito.mockStatic(SHNLogger.class);
        PowerMockito.mockStatic(BuildConfig.class);
    }

    @Test
    public void givenReleaseBuildTypeWhenTransportContextIsCreatedThenLoggerIsNotRegistered() throws Exception {
        Whitebox.setInternalState(BuildConfig.class, "DEBUG", false);

        new BleTransportContext(mockedContext) {
            @Override
            SHNCentral createBlueLib(final Context context, final boolean showPopupIfBLEIsTurnedOff) throws SHNBluetoothHardwareUnavailableException {
                return shnCentralMock;
            }
        };

        PowerMockito.verifyStatic(never());
        registerLogger(any(SHNLogger.LoggerImplementation.class));
    }

    @Test
    public void givenDebugBuildTypeWhenTransportContextIsCreatedThenLoggerIsRegistered() throws Exception {
        Whitebox.setInternalState(BuildConfig.class, "DEBUG", true);

        new BleTransportContext(mockedContext) {
            @Override
            SHNCentral createBlueLib(final Context context, final boolean showPopupIfBLEIsTurnedOff) throws SHNBluetoothHardwareUnavailableException {
                return shnCentralMock;
            }
        };

        PowerMockito.verifyStatic(atLeastOnce());
        registerLogger(any(SHNLogger.LoggerImplementation.class));
    }
}