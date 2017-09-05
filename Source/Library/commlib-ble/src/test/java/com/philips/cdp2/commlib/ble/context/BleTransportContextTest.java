package com.philips.cdp2.commlib.ble.context;

import android.content.Context;

import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.exceptions.SHNBluetoothHardwareUnavailableException;
import com.philips.pins.shinelib.utility.SHNLogger;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appidentity.AppIdentityInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static com.philips.pins.shinelib.utility.SHNLogger.registerLogger;
import static com.philips.platform.appinfra.appidentity.AppIdentityInterface.AppState.PRODUCTION;
import static com.philips.platform.appinfra.appidentity.AppIdentityInterface.AppState.STAGING;
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
    private AppInfraInterface mockedAppInfra;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        PowerMockito.mockStatic(SHNLogger.class);
    }

    @Test
    public void givenAppStateIsProductionWhenTransportContextIsCreatedThenLoggerIsNotRegistered() throws Exception {
        setupAppInfraToReturnAppState(PRODUCTION);

        constructBleTransportContext();

        PowerMockito.verifyStatic(never());
        registerLogger(any(SHNLogger.LoggerImplementation.class));
    }

    @Test
    public void givenAppStateIsNotProductionWhenTransportContextIsCreatedThenLoggerIsRegistered() throws Exception {
        setupAppInfraToReturnAppState(STAGING);

        constructBleTransportContext();

        PowerMockito.verifyStatic(atLeastOnce());
        registerLogger(any(SHNLogger.LoggerImplementation.class));
    }

    private void constructBleTransportContext() {
        new BleTransportContext(mockedContext) {
            @Override
            SHNCentral createBlueLib(final Context context, final boolean showPopupIfBLEIsTurnedOff) throws SHNBluetoothHardwareUnavailableException {
                return mockedShnCentral;
            }

            @Override
            AppInfraInterface createAppInfra(final Context context) {
                return mockedAppInfra;
            }
        };
    }

    private void setupAppInfraToReturnAppState(final AppIdentityInterface.AppState state) {
        when(mockedAppInfra.getAppIdentity()).thenReturn(new AppIdentityInterface() {
            @Override
            public String getAppName() {
                return null;
            }

            @Override
            public String getAppVersion() {
                return null;
            }

            @Override
            public AppState getAppState() {
                return state;
            }

            @Override
            public String getLocalizedAppName() {
                return null;
            }

            @Override
            public String getMicrositeId() {
                return null;
            }

            @Override
            public String getSector() {
                return null;
            }

            @Override
            public String getServiceDiscoveryEnvironment() {
                return null;
            }
        });
    }
}