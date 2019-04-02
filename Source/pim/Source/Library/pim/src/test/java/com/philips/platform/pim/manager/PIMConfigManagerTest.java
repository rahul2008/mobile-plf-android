package com.philips.platform.pim.manager;

import android.net.Uri;

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
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@PrepareForTest({Uri.class})
@RunWith(PowerMockRunner.class)
public class PIMConfigManagerTest extends TestCase {

    PIMConfigManager pimConfigManager;

    @Mock
    ServiceDiscoveryInterface mockServiceDiscoveryInterface;
    @Mock
    ServiceDiscoveryInterface.OnGetServiceUrlMapListener mockOnGetServiceUrlMapListener;
    @Captor
    private ArgumentCaptor<ServiceDiscoveryInterface.OnGetServiceUrlMapListener> captor;
    @Captor
    private ArgumentCaptor<ArrayList<String>> captorArrayList;


    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(Uri.class);
        Uri uri = mock(Uri.class);
        PowerMockito.when(Uri.class, "parse", anyString()).thenReturn(uri);
        pimConfigManager = new PIMConfigManager();
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
        
        Map<String, ServiceDiscoveryService> urlMap = new HashMap<>();
        ServiceDiscoveryService serviceDiscoveryService = new ServiceDiscoveryService();
        serviceDiscoveryService.setConfigUrl("https://stg.api.accounts.philips.com/c2a48310-9715-3beb-895e-000000000000/login");
        urlMap.put("userreg.janrainoidc.issuer", serviceDiscoveryService);
        mockOnGetServiceUrlMapListener.onSuccess(urlMap);
    }

    @Test
    public void verifyGetServicesWithCountryPreference_OnError_NoNetwork_NotNull() {
        pimConfigManager.init(mockServiceDiscoveryInterface);
        verify(mockServiceDiscoveryInterface).getServicesWithCountryPreference(captorArrayList.capture(), captor.capture(), eq(null));
        mockOnGetServiceUrlMapListener = captor.getValue();

        ServiceDiscovery.Error sdError = new ServiceDiscovery.Error(ServiceDiscoveryInterface
                .OnErrorListener.ERRORVALUES.NO_NETWORK, "No Network");
        mockOnGetServiceUrlMapListener.onError(sdError.getErrorvalue(),sdError.getMessage());
        assertNotNull(sdError.getErrorvalue());
    }

    @Test
    public void verifyGetServicesWithCountryPreference_OnError_SecurityError_NotNull() {
        pimConfigManager.init(mockServiceDiscoveryInterface);
        verify(mockServiceDiscoveryInterface).getServicesWithCountryPreference(captorArrayList.capture(), captor.capture(), eq(null));
        mockOnGetServiceUrlMapListener = captor.getValue();

        ServiceDiscovery.Error sdError = new ServiceDiscovery.Error(ServiceDiscoveryInterface
                .OnErrorListener.ERRORVALUES.SECURITY_ERROR, "Security Error");
        mockOnGetServiceUrlMapListener.onError(sdError.getErrorvalue(),sdError.getMessage());
        assertNotNull(sdError.getErrorvalue());
    }

    @Test
    public void verifyGetServicesWithCountryPreference_OnError_ServerError_NotNull() {
        pimConfigManager.init(mockServiceDiscoveryInterface);
        verify(mockServiceDiscoveryInterface).getServicesWithCountryPreference(captorArrayList.capture(), captor.capture(), eq(null));
        mockOnGetServiceUrlMapListener = captor.getValue();

        ServiceDiscovery.Error sdError = new ServiceDiscovery.Error(ServiceDiscoveryInterface
                .OnErrorListener.ERRORVALUES.SERVER_ERROR, "ErrorMessage");
        mockOnGetServiceUrlMapListener.onError(sdError.getErrorvalue(),sdError.getMessage());
        assertNotNull(sdError.getErrorvalue());
    }

    @Test
    public void verifyGetServicesWithCountryPreference_OnError_CONNECTIONTIMEOUT_NotNull() {
        pimConfigManager.init(mockServiceDiscoveryInterface);
        verify(mockServiceDiscoveryInterface).getServicesWithCountryPreference(captorArrayList.capture(), captor.capture(), eq(null));
        mockOnGetServiceUrlMapListener = captor.getValue();

        ServiceDiscovery.Error sdError = new ServiceDiscovery.Error(ServiceDiscoveryInterface
                .OnErrorListener.ERRORVALUES.CONNECTION_TIMEOUT, "ErrorMessage");
        mockOnGetServiceUrlMapListener.onError(sdError.getErrorvalue(),sdError.getMessage());
        assertNotNull(sdError.getErrorvalue());
    }

    @Test
    public void verifyGetServicesWithCountryPreference_OnError_INVALIDRESPONSE_NotNull() {
        pimConfigManager.init(mockServiceDiscoveryInterface);
        verify(mockServiceDiscoveryInterface).getServicesWithCountryPreference(captorArrayList.capture(), captor.capture(), eq(null));
        mockOnGetServiceUrlMapListener = captor.getValue();

        ServiceDiscovery.Error sdError = new ServiceDiscovery.Error(ServiceDiscoveryInterface
                .OnErrorListener.ERRORVALUES.INVALID_RESPONSE, "ErrorMessage");
        mockOnGetServiceUrlMapListener.onError(sdError.getErrorvalue(),sdError.getMessage());
        assertNotNull(sdError.getErrorvalue());
    }

    @Test
    public void verifyGetServicesWithCountryPreference_OnError_UNKNOWNERROR_NotNull() {
        pimConfigManager.init(mockServiceDiscoveryInterface);
        verify(mockServiceDiscoveryInterface).getServicesWithCountryPreference(captorArrayList.capture(), captor.capture(), eq(null));
        mockOnGetServiceUrlMapListener = captor.getValue();

        ServiceDiscovery.Error sdError = new ServiceDiscovery.Error(ServiceDiscoveryInterface
                .OnErrorListener.ERRORVALUES.UNKNOWN_ERROR, "ErrorMessage");
        mockOnGetServiceUrlMapListener.onError(sdError.getErrorvalue(),sdError.getMessage());
        assertNotNull(sdError.getErrorvalue());
    }

    @Test
    public void verifyGetServicesWithCountryPreference_OnError_NoServiceLocalEreror_NotNull() {
        pimConfigManager.init(mockServiceDiscoveryInterface);
        verify(mockServiceDiscoveryInterface).getServicesWithCountryPreference(captorArrayList.capture(), captor.capture(), eq(null));
        mockOnGetServiceUrlMapListener = captor.getValue();

        ServiceDiscovery.Error sdError = new ServiceDiscovery.Error(ServiceDiscoveryInterface
                .OnErrorListener.ERRORVALUES.NO_SERVICE_LOCALE_ERROR, "ErrorMessage");
        mockOnGetServiceUrlMapListener.onError(sdError.getErrorvalue(),sdError.getMessage());
        assertNotNull(sdError.getErrorvalue());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        pimConfigManager = null;
        mockServiceDiscoveryInterface = null;
        mockOnGetServiceUrlMapListener = null;
    }
}