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


    private KeyBagManager keyBagInterface;

    private String rawData = "{\n" +
            "  \"appinfra.localtesting\" : [\n" +
            "    {\"clientId\":\"4c73b365\"},\n" +
            "    {\"clientId\":\"2d35b548bb\"},\n" +
            "    {\"clientId\":\"ecbb6a284f\"},\n" +
            "    {\"new\":\"49967f42cf\",\"clientId\":\"d68cc08550\"},\n" +
            "    {\"new\":\"e88cf71dab\",\"clientId\":\"89c821b20a\"}\n" +
            "  ],\n" +
            "  \"userreg.janrain.cdn\" : [\n" +
            "    {\"clientId\":\"c9e153c5\"},\n" +
            "    {\"clientId\":\"0ce51950ec\"},\n" +
            "    {\"clientId\":\"d0f1cfb3aa\"},\n" +
            "    {\"new\":\"b1f9f948ec\",\"clientId\":\"02cc3dd7da\"},\n" +
            "    {\"new\":\"e6e173c5da\",\"clientId\":\"b83d533301\"}\n" +
            "  ]\n" +
            "}";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        AppInfra appInfraMock = mock(AppInfra.class);
        ServiceDiscoveryInterface serviceDiscoveryInterfaceMock = mock(ServiceDiscoveryInterface.class);
        when(appInfraMock.getServiceDiscovery()).thenReturn(serviceDiscoveryInterfaceMock);
        keyBagInterface = new KeyBagManager(appInfraMock);
    }


    public void testGettingServiceDiscoveryUrl() {
        final ArrayList<AIKMService> aiKmServices = new ArrayList<>();
        ArrayList<String> serviceIds = new ArrayList<>();
        ServiceDiscoveryInterface.OnGetKeyBagMapListener onGetKeyBagMapListenerMock = mock(ServiceDiscoveryInterface.OnGetKeyBagMapListener.class);

        ServiceDiscoveryInterface.OnGetServiceUrlMapListener serviceUrlMapListener = keyBagInterface.fetchGettingServiceDiscoveryUrlsListener(serviceIds, aiKmServices, AISDResponse.AISDPreference.AISDLanguagePreference, onGetKeyBagMapListenerMock);
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
}