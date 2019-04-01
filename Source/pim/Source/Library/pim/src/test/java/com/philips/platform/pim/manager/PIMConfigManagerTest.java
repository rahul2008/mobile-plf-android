package com.philips.platform.pim.manager;

import android.net.Uri;

import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveryService;
import com.philips.platform.pim.utilities.PIMConstants;

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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    @Mock
    private PIMOidcDiscoveryManager mockPimOidcDiscoveryManager;


    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(Uri.class);
        Uri uri = mock(Uri.class);
        PowerMockito.when(Uri.class, "parse", anyString()).thenReturn(uri);
        pimConfigManager = new PIMConfigManager();
    }

    @Test
    public void testPIMConfigManagerInit_VerifyCountryPreference(){
        pimConfigManager.init(mockServiceDiscoveryInterface);
        verify(mockServiceDiscoveryInterface).getServicesWithCountryPreference(captorArrayList.capture(), captor.capture(), eq(null));
        mockOnGetServiceUrlMapListener = captor.getValue();
    }


    @Test
    public void testOnSuccess_VerifyDownloadOidcUrl() {
        Map<String, ServiceDiscoveryService> urlMap = new HashMap<>();
        ServiceDiscoveryService serviceDiscoveryService = new ServiceDiscoveryService();
        serviceDiscoveryService.setConfigUrl("https://stg.api.accounts.philips.com/c2a48310-9715-3beb-895e-000000000000/login");
        urlMap.put("userreg.janrainoidc.issuer", serviceDiscoveryService);
        mockOnGetServiceUrlMapListener.onSuccess(urlMap);
        mockPimOidcDiscoveryManager.downloadOidcUrls(urlMap.get(PIMConstants.PIM_BASEURL).getConfigUrls());
        verify(mockPimOidcDiscoveryManager).downloadOidcUrls(any(String.class));
    }



    @Test
    public void testOnError() {
         mockOnGetServiceUrlMapListener.onError(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.NO_NETWORK, "");
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        pimConfigManager = null;
        mockPimOidcDiscoveryManager = null;
        mockServiceDiscoveryInterface = null;
        mockOnGetServiceUrlMapListener = null;
    }
}