package com.philips.platform.appinfra.aikm;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInstrumentation;
import com.philips.platform.appinfra.aikm.exception.AIKMJsonFileNotFoundException;
import com.philips.platform.appinfra.aikm.model.AIKMService;
import com.philips.platform.appinfra.aikm.model.OnGetServicesListener;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.model.AISDResponse;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveryService;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AIKManagerTest extends AppInfraInstrumentation {


    private AIKManager aikManager;
    private ServiceDiscoveryInterface serviceDiscoveryInterfaceMock;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        AppInfra appInfraMock = mock(AppInfra.class);
        serviceDiscoveryInterfaceMock = mock(ServiceDiscoveryInterface.class);
        when(appInfraMock.getServiceDiscovery()).thenReturn(serviceDiscoveryInterfaceMock);
        aikManager = new AIKManager(appInfraMock) {

            @Override
            ServiceDiscoveryInterface getServiceDiscovery() {
                return serviceDiscoveryInterfaceMock;
            }
        };
    }

    public void testInvokingServices() throws AIKMJsonFileNotFoundException, JSONException {
        Context context = getInstrumentation().getContext();
        AppInfra appInfraMock = mock(AppInfra.class);
        when(appInfraMock.getAppInfraContext()).thenReturn(context);
        final ServiceDiscoveryInterface serviceDiscoveryInterfaceMock = mock(ServiceDiscoveryInterface.class);
        LoggingInterface loggingInterfaceMock = mock(LoggingInterface.class);
        when(appInfraMock.getServiceDiscovery()).thenReturn(serviceDiscoveryInterfaceMock);
        when(appInfraMock.getLogging()).thenReturn(loggingInterfaceMock);
        OnGetServicesListener onGetServicesListenerMock = mock(OnGetServicesListener.class);
        ArrayList<String> serviceIds = new ArrayList<>();
        serviceIds.add("service_id");
        aikManager = new AIKManager(appInfraMock);
        aikManager.getValueForServiceIds(serviceIds, AISDResponse.AISDPreference.AISDCountryPreference, null, onGetServicesListenerMock);
    }


    public void testGettingServiceDiscoveryUrl() {
        final ArrayList<AIKMService> aiKmServices = new ArrayList<>();
        ArrayList<String> serviceIds = new ArrayList<>();
        OnGetServicesListener onGetServicesListenerMock = mock(OnGetServicesListener.class);

        ServiceDiscoveryInterface.OnGetServiceUrlMapListener serviceUrlMapListener = aikManager.getSDUrlsListener(serviceIds, aiKmServices, AISDResponse.AISDPreference.AISDLanguagePreference, onGetServicesListenerMock);
        serviceUrlMapListener.onError(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.SECURITY_ERROR, "error in security");
        verify(onGetServicesListenerMock).onError(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.SECURITY_ERROR, "error in security");
    }

    public void testFetchGettingKeyBagUrlsListener() {
        OnGetServicesListener onGetServicesListenerMock = mock(OnGetServicesListener.class);
        final ArrayList<AIKMService> aiKmServices = new ArrayList<>();
        TreeMap<String, ServiceDiscoveryService> urlMap = getStringServiceDiscoveryServiceTreeMap();
        ServiceDiscoveryInterface.OnGetServiceUrlMapListener onGetServiceUrlMapListener = aikManager.getKMappedGroomListener(onGetServicesListenerMock, aiKmServices, urlMap);
        onGetServiceUrlMapListener.onError(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.SECURITY_ERROR, "security error");
        verify(onGetServicesListenerMock).onError(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.SECURITY_ERROR, "security error");
        onGetServiceUrlMapListener.onSuccess(urlMap);
        verify(onGetServicesListenerMock).onSuccess(aiKmServices);
        assertEquals(aiKmServices.size(), 3);
        assertEquals(aiKmServices.get(0).getConfigUrls(), "url");
        assertEquals(aiKmServices.get(1).getmError(), "error1");
    }

    @NonNull
    private TreeMap<String, ServiceDiscoveryService> getStringServiceDiscoveryServiceTreeMap() {
        TreeMap<String, ServiceDiscoveryService> urlMap = new TreeMap<>();
        ServiceDiscoveryService value = new ServiceDiscoveryService();
        value.setConfigUrl("url");
        value.setmError("error");
        ServiceDiscoveryService value1 = new ServiceDiscoveryService();
        value1.setConfigUrl("url1");
        value1.setmError("error1");
        ServiceDiscoveryService value2 = new ServiceDiscoveryService();
        value2.setConfigUrl("url2");
        value2.setmError("error2");
        urlMap.put("service_id", value);
        urlMap.put("service_id1", value1);
        urlMap.put("service_id2", value2);
        return urlMap;
    }


    public void testGetServiceDiscoveryUrlMap() {
        ServiceDiscoveryInterface.OnGetServiceUrlMapListener onGetServiceUrlMapListener = mock(ServiceDiscoveryInterface.OnGetServiceUrlMapListener.class);
        aikManager.getServiceDiscoveryUrlMap(null, AISDResponse.AISDPreference.AISDCountryPreference, null, onGetServiceUrlMapListener);
        verify(serviceDiscoveryInterfaceMock).getServicesWithCountryPreference(null, onGetServiceUrlMapListener);
        aikManager.getServiceDiscoveryUrlMap(null, AISDResponse.AISDPreference.AISDLanguagePreference, null, onGetServiceUrlMapListener);
        verify(serviceDiscoveryInterfaceMock).getServicesWithLanguagePreference(null, onGetServiceUrlMapListener);

        Map<String, String> replacement = new HashMap<>();
        aikManager.getServiceDiscoveryUrlMap(null, AISDResponse.AISDPreference.AISDCountryPreference, replacement, onGetServiceUrlMapListener);
        verify(serviceDiscoveryInterfaceMock).getServicesWithCountryPreference(null, onGetServiceUrlMapListener, replacement);
        aikManager.getServiceDiscoveryUrlMap(null, AISDResponse.AISDPreference.AISDLanguagePreference, replacement, onGetServiceUrlMapListener);
        verify(serviceDiscoveryInterfaceMock).getServicesWithLanguagePreference(null, onGetServiceUrlMapListener, replacement);
    }

}