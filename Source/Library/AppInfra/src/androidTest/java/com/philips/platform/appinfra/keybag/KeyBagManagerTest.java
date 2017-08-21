package com.philips.platform.appinfra.keybag;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInstrumentation;
import com.philips.platform.appinfra.keybag.model.AIKMService;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.model.AISDResponse;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveryService;

import java.util.ArrayList;
import java.util.TreeMap;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class KeyBagManagerTest extends AppInfraInstrumentation {


    private KeyBagManager keyBagManager;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        AppInfra appInfraMock = mock(AppInfra.class);
        ServiceDiscoveryInterface serviceDiscoveryInterfaceMock = mock(ServiceDiscoveryInterface.class);
        when(appInfraMock.getServiceDiscovery()).thenReturn(serviceDiscoveryInterfaceMock);
        keyBagManager = new KeyBagManager(appInfraMock);
    }

  /*  public void testInvokingServices() throws KeyBagJsonFileNotFoundException {
        Context context = getInstrumentation().getContext();
        AppInfra appInfraMock = mock(AppInfra.class);
        when(appInfraMock.getAppInfraContext()).thenReturn(context);
        ServiceDiscoveryInterface serviceDiscoveryInterfaceMock = mock(ServiceDiscoveryInterface.class);
        LoggingInterface loggingInterfaceMock = mock(LoggingInterface.class);
        when(appInfraMock.getServiceDiscovery()).thenReturn(serviceDiscoveryInterfaceMock);
        when(appInfraMock.getLogging()).thenReturn(loggingInterfaceMock);
        ServiceDiscoveryInterface.OnGetKeyBagMapListener onGetKeyBagMapListenerMock = mock(ServiceDiscoveryInterface.OnGetKeyBagMapListener.class);
        final ServiceDiscoveryInterface.OnGetServiceUrlMapListener serviceUrlMapListenerMock = mock(ServiceDiscoveryInterface.OnGetServiceUrlMapListener.class);
//        final KeyBagHelper keyBagHelperMock = mock(KeyBagHelper.class);
//        when(keyBagHelperMock.init(context,"somefile")).thenReturn(false);

        final KeyBagHelper test = spy(new KeyBagHelper(appInfraMock));
        when(test.init(context,"AIKeyBag.json")).thenReturn(false);

        ArrayList<String> serviceIds = new ArrayList<>();
        keyBagManager = new KeyBagManager(appInfraMock) {
            @NonNull
            @Override
            KeyBagHelper getKeyBagHelper() {
                return test;
            }

            @NonNull
            @Override
            ServiceDiscoveryInterface.OnGetServiceUrlMapListener fetchGettingServiceDiscoveryUrlsListener(List<String> serviceIds, List<AIKMService> aiKmServices, AISDResponse.AISDPreference aiSdPreference, ServiceDiscoveryInterface.OnGetKeyBagMapListener onGetKeyBagMapListener) {
                return serviceUrlMapListenerMock;
            }
        };

        keyBagManager.getServicesForServiceIds(serviceIds, AISDResponse.AISDPreference.AISDCountryPreference, null, onGetKeyBagMapListenerMock);
        verify(test).getServiceDiscoveryUrlMap(serviceIds, AISDResponse.AISDPreference.AISDCountryPreference, null, serviceUrlMapListenerMock);
    }*/


    public void testGettingServiceDiscoveryUrl() {
        final ArrayList<AIKMService> aiKmServices = new ArrayList<>();
        ArrayList<String> serviceIds = new ArrayList<>();
        ServiceDiscoveryInterface.OnGetKeyBagMapListener onGetKeyBagMapListenerMock = mock(ServiceDiscoveryInterface.OnGetKeyBagMapListener.class);

        ServiceDiscoveryInterface.OnGetServiceUrlMapListener serviceUrlMapListener = keyBagManager.fetchGettingServiceDiscoveryUrlsListener(serviceIds, aiKmServices, AISDResponse.AISDPreference.AISDLanguagePreference, onGetKeyBagMapListenerMock);
        serviceUrlMapListener.onError(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.SECURITY_ERROR,"error in security");
        verify(onGetKeyBagMapListenerMock).onError(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.SECURITY_ERROR,"error in security");

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
        serviceUrlMapListener.onSuccess(urlMap);
        assertEquals(aiKmServices.size(),3);
        assertEquals(aiKmServices.get(0).getConfigUrls(),"url");
        assertEquals(aiKmServices.get(1).getmError(),"error1");
    }

    public void testFetchGettingKeyBagUrlsListener() {
        ServiceDiscoveryInterface.OnGetKeyBagMapListener onGetKeyBagMapListenerMock = mock(ServiceDiscoveryInterface.OnGetKeyBagMapListener.class);
        final ArrayList<AIKMService> aiKmServices = new ArrayList<>();
        aiKmServices.add(new AIKMService());
        aiKmServices.add(new AIKMService());
        aiKmServices.add(new AIKMService());
        ServiceDiscoveryInterface.OnGetServiceUrlMapListener onGetServiceUrlMapListener = keyBagManager.fetchGettingKeyBagUrlsListener(onGetKeyBagMapListenerMock, aiKmServices);
        onGetServiceUrlMapListener.onError(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.SECURITY_ERROR,"security error");
        verify(onGetKeyBagMapListenerMock).onError(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.SECURITY_ERROR,"security error");
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
        onGetServiceUrlMapListener.onSuccess(urlMap);
        verify(onGetKeyBagMapListenerMock).onSuccess(aiKmServices);
    }
}