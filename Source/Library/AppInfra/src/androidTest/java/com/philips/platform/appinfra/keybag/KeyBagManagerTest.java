package com.philips.platform.appinfra.keybag;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInstrumentation;
import com.philips.platform.appinfra.keybag.exception.KeyBagJsonFileNotFoundException;
import com.philips.platform.appinfra.keybag.model.AIKMService;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.model.AISDResponse;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveryService;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
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

    public void testInvokingServices() throws KeyBagJsonFileNotFoundException {
        Context context = getInstrumentation().getContext();
        AppInfra appInfraMock = mock(AppInfra.class);
        when(appInfraMock.getAppInfraContext()).thenReturn(context);
        ServiceDiscoveryInterface serviceDiscoveryInterfaceMock = mock(ServiceDiscoveryInterface.class);
        LoggingInterface loggingInterfaceMock = mock(LoggingInterface.class);
        when(appInfraMock.getServiceDiscovery()).thenReturn(serviceDiscoveryInterfaceMock);
        when(appInfraMock.getLogging()).thenReturn(loggingInterfaceMock);
        ServiceDiscoveryInterface.OnGetServicesListener onGetServicesListenerMock = mock(ServiceDiscoveryInterface.OnGetServicesListener.class);
        final ServiceDiscoveryInterface.OnGetServiceUrlMapListener serviceUrlMapListenerMock = mock(ServiceDiscoveryInterface.OnGetServiceUrlMapListener.class);

        InputStream inputStream = null;
        try {
            inputStream = context.getResources().getAssets().open("AIKeyBag.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        final GroomHelper groomHelperMock = mock(GroomHelper.class);
        ArrayList<String> serviceIds = new ArrayList<>();
        final InputStream finalInputStream = inputStream;
        keyBagManager = new KeyBagManager(appInfraMock) {
            @NonNull
            @Override
            GroomHelper getGroomHelper() {
                return groomHelperMock;
            }

            @Override
            InputStream getInputStream(Context mContext, String fileName) throws KeyBagJsonFileNotFoundException {
                return finalInputStream;
            }

            @NonNull
            @Override
            ServiceDiscoveryInterface.OnGetServiceUrlMapListener fetchGettingServiceDiscoveryUrlsListener(List<String> serviceIds, List<AIKMService> aiKmServices, AISDResponse.AISDPreference aiSdPreference, ServiceDiscoveryInterface.OnGetServicesListener onGetServicesListener) {
                return serviceUrlMapListenerMock;
            }
        };

        keyBagManager.getServicesForServiceIds(serviceIds, AISDResponse.AISDPreference.AISDCountryPreference, null, onGetServicesListenerMock);
        verify(groomHelperMock).getServiceDiscoveryUrlMap(serviceIds, AISDResponse.AISDPreference.AISDCountryPreference, null, serviceUrlMapListenerMock);
    }


    public void testGettingServiceDiscoveryUrl() {
        final ArrayList<AIKMService> aiKmServices = new ArrayList<>();
        ArrayList<String> serviceIds = new ArrayList<>();
        ServiceDiscoveryInterface.OnGetServicesListener onGetServicesListenerMock = mock(ServiceDiscoveryInterface.OnGetServicesListener.class);

        ServiceDiscoveryInterface.OnGetServiceUrlMapListener serviceUrlMapListener = keyBagManager.fetchGettingServiceDiscoveryUrlsListener(serviceIds, aiKmServices, AISDResponse.AISDPreference.AISDLanguagePreference, onGetServicesListenerMock);
        serviceUrlMapListener.onError(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.SECURITY_ERROR, "error in security");
        verify(onGetServicesListenerMock).onError(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.SECURITY_ERROR, "error in security");
    }

    public void testFetchGettingKeyBagUrlsListener() {
        ServiceDiscoveryInterface.OnGetServicesListener onGetServicesListenerMock = mock(ServiceDiscoveryInterface.OnGetServicesListener.class);
        final ArrayList<AIKMService> aiKmServices = new ArrayList<>();
        TreeMap<String, ServiceDiscoveryService> urlMap = getStringServiceDiscoveryServiceTreeMap();
        ServiceDiscoveryInterface.OnGetServiceUrlMapListener onGetServiceUrlMapListener = keyBagManager.fetchGettingKeyBagUrlsListener(onGetServicesListenerMock, aiKmServices, urlMap);
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
}