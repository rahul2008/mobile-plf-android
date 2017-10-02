/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.ble.context;

import android.content.Context;

import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.exceptions.SHNBluetoothHardwareUnavailableException;
import com.philips.pins.shinelib.utility.SHNLogger;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.appidentity.AppIdentityInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;
import java.util.Map;

import static com.philips.cdp2.commlib.core.configuration.RuntimeConfiguration.CONFIG_KEY_CONSOLE_LOG_ENABLED;
import static com.philips.cdp2.commlib.core.configuration.RuntimeConfiguration.CONFIG_KEY_LOG_CONFIG_DEBUG;
import static com.philips.cdp2.commlib.core.configuration.RuntimeConfiguration.CONFIG_PROPERTY_APPINFRA;
import static com.philips.cdp2.commlib.core.configuration.RuntimeConfiguration.CONFIG_KEY_LOG_CONFIG_RELEASE;
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
    public void givenAppStateIsProductionAndConsoleLogEnabledWhenTransportContextIsCreatedThenLoggerIsNotRegistered() throws Exception {
        setupAppInfraToReturnAppState(PRODUCTION, true);

        constructBleTransportContext();

        PowerMockito.verifyStatic(never());
        registerLogger(any(SHNLogger.LoggerImplementation.class));
    }

    @Test
    public void givenAppStateIsNotProductionAndConsoleLogEnabledWhenTransportContextIsCreatedThenLoggerIsRegistered() throws Exception {
        setupAppInfraToReturnAppState(STAGING, true);

        constructBleTransportContext();

        PowerMockito.verifyStatic(atLeastOnce());
        registerLogger(any(SHNLogger.LoggerImplementation.class));
    }

    @Test
    public void givenAppStateIsProductionAndConsoleLogDisabledWhenTransportContextIsCreatedThenLoggerIsNotRegistered() throws Exception {
        setupAppInfraToReturnAppState(PRODUCTION, false);

        constructBleTransportContext();

        PowerMockito.verifyStatic(never());
        registerLogger(any(SHNLogger.LoggerImplementation.class));
    }

    @Test
    public void givenAppStateIsNotProductionAndConsoleLogDisabledWhenTransportContextIsCreatedThenLoggerIsRegistered() throws Exception {
        setupAppInfraToReturnAppState(STAGING, false);

        constructBleTransportContext();

        PowerMockito.verifyStatic(never());
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

    private void setupAppInfraToReturnAppState(final AppIdentityInterface.AppState state, final boolean consoleLogEnabled) {
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

        final Map<String, Object> loggingConfig = new HashMap<>();
        loggingConfig.put(CONFIG_KEY_CONSOLE_LOG_ENABLED, consoleLogEnabled);

        when(mockedAppInfra.getConfigInterface()).thenReturn(new AppConfigurationInterface() {

            @Override
            public Object getPropertyForKey(String key, String group, AppConfigurationError appConfigurationError) throws IllegalArgumentException {
                if (group.equals(CONFIG_PROPERTY_APPINFRA) && (key.equals(CONFIG_KEY_LOG_CONFIG_DEBUG) || key.equals(CONFIG_KEY_LOG_CONFIG_RELEASE))) {
                    return loggingConfig;
                }

                return null;
            }

            @Override
            public boolean setPropertyForKey(String s, String s1, Object o, AppConfigurationError appConfigurationError) throws IllegalArgumentException {
                return false;
            }

            @Override
            public Object getDefaultPropertyForKey(String s, String s1, AppConfigurationError appConfigurationError) throws IllegalArgumentException {
                return null;
            }

            @Override
            public void refreshCloudConfig(OnRefreshListener onRefreshListener) {

            }

            @Override
            public void resetConfig() {

            }
        });
    }
}
