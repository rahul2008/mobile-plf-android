/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.core.configuration;

import android.content.Context;

import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface.AppConfigurationError;
import com.philips.platform.appinfra.appidentity.AppIdentityInterface;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.Map;

import static com.philips.cdp2.commlib.core.configuration.RuntimeConfiguration.CONFIG_KEY_CONSOLE_LOG_ENABLED;
import static com.philips.platform.appinfra.appidentity.AppIdentityInterface.AppState.PRODUCTION;
import static com.philips.platform.appinfra.appidentity.AppIdentityInterface.AppState.STAGING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class RuntimeConfigurationTest {

    @Mock
    private Context contextMock;

    @Mock
    private AppInfraInterface appInfraInterfaceMock;

    @Mock
    private AppIdentityInterface appIdentityMock;

    @Mock
    private AppConfigurationInterface appConfigurationInterfaceMock;

    private final Map<String, Object> logConfig = new HashMap<>();

    private RuntimeConfiguration runtimeConfiguration;

    @Before
    public void setUp() {
        initMocks(this);

        DICommLog.disableLogging();

        when(appInfraInterfaceMock.getAppIdentity()).thenReturn(appIdentityMock);
        when(appInfraInterfaceMock.getConfigInterface()).thenReturn(appConfigurationInterfaceMock);
        when(appConfigurationInterfaceMock.getPropertyForKey(anyString(), anyString(), any(AppConfigurationError.class))).thenReturn(logConfig);

        runtimeConfiguration = new RuntimeConfiguration(contextMock, appInfraInterfaceMock);
    }

    @Test
    public void givenAppStateIsProduction_andConsoleLoggingIsEnabled_thenLogIsDisabled() {
        when(appIdentityMock.getAppState()).thenReturn(PRODUCTION);

        logConfig.put(CONFIG_KEY_CONSOLE_LOG_ENABLED, true);

        assertThat(runtimeConfiguration.isLogEnabled()).isFalse();
    }

    @Test
    public void givenAppStateIsProduction_andConsoleLoggingIsDisabled_thenLogIsDisabled() {
        when(appIdentityMock.getAppState()).thenReturn(PRODUCTION);

        logConfig.put(CONFIG_KEY_CONSOLE_LOG_ENABLED, false);

        assertThat(runtimeConfiguration.isLogEnabled()).isFalse();
    }

    @Test
    public void givenAppStateIsNotProduction_andConsoleLoggingIsEnabled_thenLogIsEnabled() {
        when(appIdentityMock.getAppState()).thenReturn(STAGING);

        logConfig.put(CONFIG_KEY_CONSOLE_LOG_ENABLED, true);

        assertThat(runtimeConfiguration.isLogEnabled()).isTrue();
    }

    @Test
    public void givenAppStateIsNotProduction_andConsoleLoggingIsDisabled_thenLogIsDisabled() {
        when(appIdentityMock.getAppState()).thenReturn(STAGING);

        logConfig.put(CONFIG_KEY_CONSOLE_LOG_ENABLED, false);

        assertThat(runtimeConfiguration.isLogEnabled()).isFalse();
    }

    @Test
    public void givenAnInstance_whenGetContext_thenReturnTheContext() {
        assertThat(runtimeConfiguration.getContext()).isEqualTo(contextMock);
    }

}
