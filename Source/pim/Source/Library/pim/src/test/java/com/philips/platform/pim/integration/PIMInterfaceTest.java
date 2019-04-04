package com.philips.platform.pim.integration;

import android.content.Context;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.pim.manager.PIMSettingManager;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappSettings;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import integration.PIMInterface;

@PrepareForTest({PIMSettingManager.class})
@RunWith(PowerMockRunner.class)
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
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        pimInterface = new PIMInterface();
        Mockito.when(mockUappSettings.getContext()).thenReturn(mockContext);
        Mockito.when(mockAppInfraInterface.getLogging()).thenReturn(mockLoggingInterface);
        Mockito.when(mockAppInfraInterface.getTagging()).thenReturn(mockAppTaggingInterface);
        Mockito.when(mockAppInfraInterface.getServiceDiscovery()).thenReturn(mockServiceDiscoveryInterface);
        Mockito.when(mockAppInfraInterface.getSecureStorage()).thenReturn(mockSecureStorageInterface);
        Mockito.when(mockUappDependencies.getAppInfra()).thenReturn(mockAppInfraInterface);

        PowerMockito.mockStatic(PIMSettingManager.class);
        PIMSettingManager mockPimSettingManager = PowerMockito.mock(PIMSettingManager.class);
        PowerMockito.when(PIMSettingManager.class,"getInstance").thenReturn(mockPimSettingManager);
    }

    @Test
    public void testInit_NotNull() {
        pimInterface.init(mockUappDependencies, mockUappSettings);

        assertNotNull(mockContext);
        assertNotNull(mockUappDependencies);
        assertNotNull(mockUappSettings);
    }

//    @Test
//    public void testSetPIMUserManager_NotNull() {
//        PIMUserManager mockPIMUserManager = Mockito.mock(PIMUserManager.class);
//        PIMSettingManager.getInstance().setPimUserManager(mockPIMUserManager);
//        assertNotNull(mockPIMUserManager);
//    }

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