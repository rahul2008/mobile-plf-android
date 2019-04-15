package com.philips.platform.pim.manager;

import android.net.Uri;

import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscovery;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveryService;

import junit.framework.TestCase;

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
public class PIMConfigManagerTest extends TestCase {

    private PIMConfigManager pimConfigManager;
    @Mock
    private ServiceDiscoveryInterface mockServiceDiscoveryInterface;
    @Mock
    private ServiceDiscoveryInterface.OnGetServiceUrlMapListener mockOnGetServiceUrlMapListener;
    @Captor
    private ArgumentCaptor<ServiceDiscoveryInterface.OnGetServiceUrlMapListener> captor;
    @Captor
    private ArgumentCaptor<ArrayList<String>> captorArrayList;
    @Mock
    private LoggingInterface mockLoggingInterface;
    @Mock
    private PIMSettingManager mockPimSettingManager;
    @Mock
    private ServiceDiscoveryService mockServiceDiscoveryService;


    @Before
    public void setup() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);

        mockStatic(PIMSettingManager.class);
        when(PIMSettingManager.getInstance()).thenReturn(mockPimSettingManager);
        when(mockPimSettingManager.getLoggingInterface()).thenReturn(mockLoggingInterface);

        mockStatic(Uri.class);
        Uri uri = mock(Uri.class);
        when(Uri.class, "parse", anyString()).thenReturn(uri);

        pimConfigManager = new PIMConfigManager(pimUserManager);
    }

    /**
     * Can't verify downloadOidcUrls() from here,as instance of PIMOidcDiscoveryManager
     * and PIMAuthManager are created locally onSuccess
     */
    @Test
    public void verifyGetServicesWithCountryPreference_OnSuccess() {
        pimConfigManager.init(mockServiceDiscoveryInterface);
        verify(mockServiceDiscoveryInterface).getServicesWithCountryPreference(captorArrayList.capture(), captor.capture(), eq(null));
        mockOnGetServiceUrlMapListener = captor.getValue();

        Map<String,ServiceDiscoveryService> mockMap = mock(Map.class);
        when(mockMap.get(any())).thenReturn(mockServiceDiscoveryService);
        when(mockServiceDiscoveryService.getConfigUrls()).thenReturn(new String());

        mockOnGetServiceUrlMapListener.onSuccess(mockMap);
        verify(mockLoggingInterface).log(DEBUG, PIMConfigManager.class.getSimpleName(), "getServicesWithCountryPreference : onSuccess : getConfigUrls : " + mockServiceDiscoveryService.getConfigUrls());
    }

    @Test
    public void verifyGetServicesWithCountryPreference_OnSuccess_ServiceDiscoveryServiceIsNull() {
        pimConfigManager.init(mockServiceDiscoveryInterface);
        verify(mockServiceDiscoveryInterface).getServicesWithCountryPreference(captorArrayList.capture(), captor.capture(), eq(null));
        mockOnGetServiceUrlMapListener = captor.getValue();

        Map<String,ServiceDiscoveryService> mockMap = mock(Map.class);
        when(mockMap.get(any())).thenReturn(null);
        mockOnGetServiceUrlMapListener.onSuccess(mockMap);
        verify(mockLoggingInterface).log(DEBUG,PIMConfigManager.class.getSimpleName(),"getServicesWithCountryPreference : onSuccess : serviceDiscovery response is null");
    }

    /**
     * Can't verify downloadOidcUrls() from here,as instance of PIMOidcDiscoveryManager
     * and PIMAuthManager are created locally onSuccess
     */
    @Test
    public void verifyGetServicesWithCountryPreference_OnSuccess_ConfigURLIsNull() {
        pimConfigManager.init(mockServiceDiscoveryInterface);
        verify(mockServiceDiscoveryInterface).getServicesWithCountryPreference(captorArrayList.capture(), captor.capture(), eq(null));
        mockOnGetServiceUrlMapListener = captor.getValue();

        Map<String,ServiceDiscoveryService> mockMap = mock(Map.class);
        when(mockMap.get(any())).thenReturn(mockServiceDiscoveryService);
        when(mockServiceDiscoveryService.getConfigUrls()).thenReturn(null);

        mockOnGetServiceUrlMapListener.onSuccess(mockMap);
        verify(mockLoggingInterface).log(DEBUG, PIMConfigManager.class.getSimpleName(), "getServicesWithCountryPreference : onSuccess : No service url found for Issuer service id");
    }

    @Test
    public void verifyGetServicesWithCountryPreference_OnError_NoNetwork_NotNull() {
        pimConfigManager.init(mockServiceDiscoveryInterface);
        verify(mockServiceDiscoveryInterface).getServicesWithCountryPreference(captorArrayList.capture(), captor.capture(), eq(null));
        mockOnGetServiceUrlMapListener = captor.getValue();

        ServiceDiscovery.Error sdError = new ServiceDiscovery.Error(ServiceDiscoveryInterface
                .OnErrorListener.ERRORVALUES.NO_NETWORK, "No Network");
        mockOnGetServiceUrlMapListener.onError(sdError.getErrorvalue(), sdError.getMessage());
        assertNotNull(sdError.getErrorvalue());
    }

    @Test
    public void verifyGetServicesWithCountryPreference_OnError_SecurityError_NotNull() {
        pimConfigManager.init(mockServiceDiscoveryInterface);
        verify(mockServiceDiscoveryInterface).getServicesWithCountryPreference(captorArrayList.capture(), captor.capture(), eq(null));
        mockOnGetServiceUrlMapListener = captor.getValue();

        ServiceDiscovery.Error sdError = new ServiceDiscovery.Error(ServiceDiscoveryInterface
                .OnErrorListener.ERRORVALUES.SECURITY_ERROR, "Security Error");
        mockOnGetServiceUrlMapListener.onError(sdError.getErrorvalue(), sdError.getMessage());
        assertNotNull(sdError.getErrorvalue());
    }

    @Test
    public void verifyGetServicesWithCountryPreference_OnError_ServerError_NotNull() {
        pimConfigManager.init(mockServiceDiscoveryInterface);
        verify(mockServiceDiscoveryInterface).getServicesWithCountryPreference(captorArrayList.capture(), captor.capture(), eq(null));
        mockOnGetServiceUrlMapListener = captor.getValue();

        ServiceDiscovery.Error sdError = new ServiceDiscovery.Error(ServiceDiscoveryInterface
                .OnErrorListener.ERRORVALUES.SERVER_ERROR, "ErrorMessage");
        mockOnGetServiceUrlMapListener.onError(sdError.getErrorvalue(), sdError.getMessage());
        assertNotNull(sdError.getErrorvalue());
    }

    @Test
    public void verifyGetServicesWithCountryPreference_OnError_CONNECTIONTIMEOUT_NotNull() {
        pimConfigManager.init(mockServiceDiscoveryInterface);
        verify(mockServiceDiscoveryInterface).getServicesWithCountryPreference(captorArrayList.capture(), captor.capture(), eq(null));
        mockOnGetServiceUrlMapListener = captor.getValue();

        ServiceDiscovery.Error sdError = new ServiceDiscovery.Error(ServiceDiscoveryInterface
                .OnErrorListener.ERRORVALUES.CONNECTION_TIMEOUT, "ErrorMessage");
        mockOnGetServiceUrlMapListener.onError(sdError.getErrorvalue(), sdError.getMessage());
        assertNotNull(sdError.getErrorvalue());
    }

    @Test
    public void verifyGetServicesWithCountryPreference_OnError_INVALIDRESPONSE_NotNull() {
        pimConfigManager.init(mockServiceDiscoveryInterface);
        verify(mockServiceDiscoveryInterface).getServicesWithCountryPreference(captorArrayList.capture(), captor.capture(), eq(null));
        mockOnGetServiceUrlMapListener = captor.getValue();

        ServiceDiscovery.Error sdError = new ServiceDiscovery.Error(ServiceDiscoveryInterface
                .OnErrorListener.ERRORVALUES.INVALID_RESPONSE, "ErrorMessage");
        mockOnGetServiceUrlMapListener.onError(sdError.getErrorvalue(), sdError.getMessage());
        assertNotNull(sdError.getErrorvalue());
    }

    @Test
    public void verifyGetServicesWithCountryPreference_OnError_UNKNOWNERROR_NotNull() {
        pimConfigManager.init(mockServiceDiscoveryInterface);
        verify(mockServiceDiscoveryInterface).getServicesWithCountryPreference(captorArrayList.capture(), captor.capture(), eq(null));
        mockOnGetServiceUrlMapListener = captor.getValue();

        ServiceDiscovery.Error sdError = new ServiceDiscovery.Error(ServiceDiscoveryInterface
                .OnErrorListener.ERRORVALUES.UNKNOWN_ERROR, "ErrorMessage");
        mockOnGetServiceUrlMapListener.onError(sdError.getErrorvalue(), sdError.getMessage());
        assertNotNull(sdError.getErrorvalue());
    }

    @Test
    public void verifyGetServicesWithCountryPreference_OnError_NoServiceLocalEreror_NotNull() {
        pimConfigManager.init(mockServiceDiscoveryInterface);
        verify(mockServiceDiscoveryInterface).getServicesWithCountryPreference(captorArrayList.capture(), captor.capture(), eq(null));
        mockOnGetServiceUrlMapListener = captor.getValue();

        ServiceDiscovery.Error sdError = new ServiceDiscovery.Error(ServiceDiscoveryInterface
                .OnErrorListener.ERRORVALUES.NO_SERVICE_LOCALE_ERROR, "ErrorMessage");
        mockOnGetServiceUrlMapListener.onError(sdError.getErrorvalue(), sdError.getMessage());
        assertNotNull(sdError.getErrorvalue());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        pimConfigManager = null;
        mockServiceDiscoveryInterface = null;
        mockOnGetServiceUrlMapListener = null;
        mockPimSettingManager = null;
        captor = null;
        captorArrayList = null;
    }
}