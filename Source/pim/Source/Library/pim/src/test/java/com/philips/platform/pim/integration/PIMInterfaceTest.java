package com.philips.platform.pim.integration;

import android.content.Context;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.securestorage.SecureStorage;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscovery;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.pim.manager.PIMConfigManager;
import com.philips.platform.pim.manager.PIMSettingManager;
import com.philips.platform.pim.manager.PIMUserManager;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappSettings;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import integration.PIMInterface;

@RunWith(MockitoJUnitRunner.class)
public class PIMInterfaceTest extends TestCase {

    private PIMInterface pimInterface;
    @Mock
    private AppInfraInterface mockAppInfraInterface;
    @Mock
    private UappDependencies mockUappDependencies;
    @Mock
    private UappSettings mockUappSettings;
    @Mock
    Context mockContext;
    @Mock
    LoggingInterface mockLoggingInterface;
    @Mock
    AppTaggingInterface mockAppTaggingInterface;
    @Mock
    ServiceDiscoveryInterface mockServiceDiscoveryInterface;
    @Mock
    SecureStorageInterface mockSecureStorageInterface;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        pimInterface = new PIMInterface();
        Mockito.when(mockUappSettings.getContext()).thenReturn(mockContext);
        Mockito.when(mockAppInfraInterface.getLogging()).thenReturn(mockLoggingInterface);
        Mockito.when(mockAppInfraInterface.getTagging()).thenReturn(mockAppTaggingInterface);
        Mockito.when(mockAppInfraInterface.getServiceDiscovery()).thenReturn(mockServiceDiscoveryInterface);
        Mockito.when(mockAppInfraInterface.getSecureStorage()).thenReturn(mockSecureStorageInterface);
        Mockito.when(mockUappDependencies.getAppInfra()).thenReturn(mockAppInfraInterface);
    }

    @Test
    public void testInit_NotNull() {
        pimInterface.init(mockUappDependencies, mockUappSettings);

        assertNotNull(mockContext);
        assertNotNull(mockUappDependencies);
        assertNotNull(mockUappSettings);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();

        pimInterface = null;
        mockAppInfraInterface = null;
        mockAppTaggingInterface = null;
        mockContext = null;
        mockLoggingInterface = null;
        mockServiceDiscoveryInterface = null;
        mockSecureStorageInterface = null;
    }
}