package com.philips.platform.pim.configration;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;

import junit.framework.TestCase;

import net.openid.appauth.AuthorizationServiceConfiguration;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@PrepareForTest(PIMOIDCConfigration.class)
@RunWith(PowerMockRunner.class)
public class PIMOIDCConfigrationTest extends TestCase {

    @Mock
    private AuthorizationServiceConfiguration mockAuthorizationServiceConfiguration;
    @Mock
    private AppInfraInterface mockAppInfraInterface;
    @Mock
    private AppConfigurationInterface mockAppConfigurationInterface;
    private static final String CLIENT_ID = "clientId";
    @Mock
    private AppConfigurationInterface.AppConfigurationError mockAppConfigurationError;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        when(mockAppInfraInterface.getConfigInterface()).thenReturn(mockAppConfigurationInterface);
        whenNew(AppConfigurationInterface.AppConfigurationError.class).withNoArguments().thenReturn(mockAppConfigurationError);
    }

    @After
    public void tearDown() throws Exception {
        mockAuthorizationServiceConfiguration = null;
        mockAppInfraInterface = null;
        mockAppConfigurationInterface = null;
    }
}