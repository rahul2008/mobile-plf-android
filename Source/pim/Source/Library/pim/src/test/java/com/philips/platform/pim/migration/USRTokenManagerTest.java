package com.philips.platform.pim.migration;

import android.net.Uri;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscovery;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveryService;
import com.philips.platform.pim.listeners.RefreshUSRTokenListener;
import com.philips.platform.pim.manager.PIMAuthManager;
import com.philips.platform.pim.manager.PIMConfigManager;
import com.philips.platform.pim.manager.PIMSettingManager;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Map;

import static com.philips.platform.appinfra.logging.LoggingInterface.LogLevel.DEBUG;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@PrepareForTest({Uri.class, PIMSettingManager.class})
@RunWith(PowerMockRunner.class)
public class USRTokenManagerTest extends TestCase {
    private static final String JR_CAPTURE_REFRESH_SECRET = "jr_capture_refresh_secret";
    private static final String JR_CAPTURE_SIGNED_IN_USER = "jr_capture_signed_in_user";
    private static final String JR_CAPTURE_FLOW = "jr_capture_flow";
    private USRTokenManager usrTokenManager;
    @Mock
    private ServiceDiscoveryInterface mockServiceDiscoveryInterface;
    @Mock
    private ServiceDiscoveryInterface.OnGetServiceUrlMapListener mockOnGetServiceUrlMapListener;
    @Captor
    private ArgumentCaptor<ServiceDiscoveryInterface.OnGetServiceUrlMapListener> captor;
    @Mock
    private AppInfraInterface mockAppInfraInterface;
    @Mock
    private LoggingInterface mockLoggingInterface;
    @Mock
    private PIMSettingManager mockPimSettingManager;
    @Mock
    private ServiceDiscoveryService mockServiceDiscoveryService;
    @Mock
    private RefreshUSRTokenListener mockRefreshUSRTokenListener;
    @Captor
    private ArgumentCaptor<ArrayList<String>> captorArrayList;
    @Mock
    private SecureStorageInterface mockSecureStorageInterface;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        mockStatic(PIMSettingManager.class);
        when(PIMSettingManager.getInstance()).thenReturn(mockPimSettingManager);
        when(mockPimSettingManager.getLoggingInterface()).thenReturn(mockLoggingInterface);
        when(mockAppInfraInterface.getSecureStorage()).thenReturn(mockSecureStorageInterface);
        when(mockAppInfraInterface.getServiceDiscovery()).thenReturn(mockServiceDiscoveryInterface);
//        when(mockSecureStorageInterface.fetchValueForKey(JR_CAPTURE_SIGNED_IN_USER,new SecureStorageInterface.SecureStorageError())).thenReturn(anyString());
        usrTokenManager = new USRTokenManager(mockAppInfraInterface);
    }

    @Test
    public void test_GetServicesWithCountryPreference_OnSuccess() {
        usrTokenManager.fetchRefreshedAccessToken(mockRefreshUSRTokenListener);
        verify(mockServiceDiscoveryInterface).getServicesWithCountryPreference(captorArrayList.capture(), captor.capture(), eq(null));
        mockOnGetServiceUrlMapListener = captor.getValue();
        Map<String, ServiceDiscoveryService> mockMap = mock(Map.class);
        when(mockMap.get(any())).thenReturn(mockServiceDiscoveryService);
        when(mockServiceDiscoveryService.getConfigUrls()).thenReturn(new String());
        mockOnGetServiceUrlMapListener.onSuccess(mockMap);
        verify(mockLoggingInterface).log(DEBUG, PIMMigrationManager.class.getSimpleName(), "downloadUserUrlFromSD onSuccess. Refresh Url : " + "" + " Locale : " + null);
        verify(mockLoggingInterface).log(DEBUG, PIMMigrationManager.class.getSimpleName(), "downloadUserUrlFromSD onSuccess");
        verify(mockLoggingInterface).log(DEBUG, PIMMigrationManager.class.getSimpleName(), "Migration Failed!! " + "Signed_in_user not found");
    }


    @Test
    public void test_GetServicesWithCountryPreference_OnError() {
        usrTokenManager.fetchRefreshedAccessToken(mockRefreshUSRTokenListener);
        verify(mockServiceDiscoveryInterface).getServicesWithCountryPreference(captorArrayList.capture(), captor.capture(), eq(null));
        mockOnGetServiceUrlMapListener = captor.getValue();
        ServiceDiscovery.Error sdError = new ServiceDiscovery.Error(ServiceDiscoveryInterface
                .OnErrorListener.ERRORVALUES.CONNECTION_TIMEOUT, "Connection Timeout");
        mockOnGetServiceUrlMapListener.onError(sdError.getErrorvalue(), sdError.getMessage());
        assertNotNull(sdError.getErrorvalue());
        verify(mockLoggingInterface).log(DEBUG, PIMMigrationManager.class.getSimpleName(), "Migration Failed!! " + " Error in downloadUserUrlFromSD : " + "Connection Timeout");
    }

    @Test
    public void test_GetServicesWithCountryPreference_OnSuccess_When_URLMap_IsNotNull() {
        Map<String, ServiceDiscoveryService> mockMap = null;
        usrTokenManager.fetchRefreshedAccessToken(mockRefreshUSRTokenListener);
        verify(mockServiceDiscoveryInterface).getServicesWithCountryPreference(captorArrayList.capture(), captor.capture(), eq(null));
        mockOnGetServiceUrlMapListener = captor.getValue();
        mockOnGetServiceUrlMapListener.onSuccess(mockMap);
        verify(mockLoggingInterface).log(DEBUG, PIMMigrationManager.class.getSimpleName(), "Migration Failed!! " + " Error in downloadUserUrlFromSD : " + "Not able to fetch config url");
    }

    @Test
    public void test_GetServicesWithCountryPreference_OnSuccess_When_UserIsSignedIn() {
        Map<String, ServiceDiscoveryService> mockMap = mock(Map.class);
        when(mockMap.get(any())).thenReturn(mockServiceDiscoveryService);
        when(mockServiceDiscoveryService.getConfigUrls()).thenReturn(new String());
        usrTokenManager.fetchRefreshedAccessToken(mockRefreshUSRTokenListener);
        verify(mockServiceDiscoveryInterface).getServicesWithCountryPreference(captorArrayList.capture(), captor.capture(), eq(null));
        mockOnGetServiceUrlMapListener = captor.getValue();
        mockOnGetServiceUrlMapListener.onSuccess(mockMap);
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

}