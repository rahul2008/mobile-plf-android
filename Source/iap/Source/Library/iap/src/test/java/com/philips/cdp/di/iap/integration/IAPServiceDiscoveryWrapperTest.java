package com.philips.cdp.di.iap.integration;

import android.content.Context;

import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveryService;
import com.philips.platform.uappframework.launcher.UiLauncher;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class IAPServiceDiscoveryWrapperTest {

    private IAPSettings mIAPSettings;
    private IAPHandler iapHandler;
    IAPServiceDiscoveryWrapper iapServiceDiscoveryWrapper;
    AppInfraInterface appInfraInstance;
    @Mock
    ServiceDiscoveryInterface.OnGetServiceUrlMapListener serviceUrlMapListenerMock;
    @Mock
    Context pContext;
    Map<String, ServiceDiscoveryService> map;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        TestUtils.getStubbedHybrisDelegate();
        mIAPSettings = new IAPSettings(pContext);
        iapHandler = new IAPHandler(mock(IAPDependencies.class), mIAPSettings);
        CartModelContainer.getInstance().setAppInfraInstance(mock(AppInfra.class));


        map = new HashMap<>();
        ServiceDiscoveryService discoveryService = new ServiceDiscoveryService();
        discoveryService.init("en_US", "iap.baseurl");
        discoveryService.setConfigUrl("https://acc.occ.shop.philips.com/en_US");
        map.put("iap.baseurl", discoveryService);

        appInfraInstance = CartModelContainer.getInstance().getAppInfraInstance();

        when(appInfraInstance.getServiceDiscovery()).thenReturn(mock(ServiceDiscoveryInterface.class));
        when(appInfraInstance.getConfigInterface()).thenReturn(mock(AppConfigurationInterface.class));
    }

    @Test
    public void testGetLocaleFromServiceDiscoveryOnSuccessWithConfigURL() throws Exception {
        ServiceDiscoveryService discoveryService = new ServiceDiscoveryService();
        discoveryService.init("en_US", "iap.baseurl");
        discoveryService.setConfigUrl("https://acc.occ.shop.philips.com/en_US");
        map.put("iap.baseurl", discoveryService);
        iapServiceDiscoveryWrapper = new IAPServiceDiscoveryWrapper(mIAPSettings);
        iapServiceDiscoveryWrapper.serviceUrlMapListener = serviceUrlMapListenerMock;
        iapServiceDiscoveryWrapper.getLocaleFromServiceDiscovery(mock(UiLauncher.class), mock(IAPHandler.class), mock(IAPLaunchInput.class), null, null);
        iapServiceDiscoveryWrapper.serviceUrlMapListener.onSuccess(map);
    }

    @Test
    public void testGetLocaleFromServiceDiscoveryOnSuccessWithoutConfigurl() throws Exception {
        ServiceDiscoveryService discoveryService = new ServiceDiscoveryService();
        discoveryService.init("en_US", "iap.baseurl");
        discoveryService.setConfigUrl(null);
        map.put("iap.baseurl", discoveryService);
        iapServiceDiscoveryWrapper = new IAPServiceDiscoveryWrapper(mIAPSettings);
        iapServiceDiscoveryWrapper.serviceUrlMapListener = serviceUrlMapListenerMock;
        iapServiceDiscoveryWrapper.getLocaleFromServiceDiscovery(mock(UiLauncher.class), mock(IAPHandler.class), mock(IAPLaunchInput.class), null, null);
        iapServiceDiscoveryWrapper.serviceUrlMapListener.onSuccess(map);
    }


    @Test(expected = NullPointerException.class)
    public void testGetLocaleFromServiceDiscoveryOnSuccessWithConfigurlWithListnerAndEntry() throws Exception {
        ServiceDiscoveryService discoveryService = new ServiceDiscoveryService();
        discoveryService.init("en_US", "iap.baseurl");
        discoveryService.setConfigUrl("https://acc.occ.shop.philips.com/en_US");
        map.put("iap.baseurl", discoveryService);
        iapServiceDiscoveryWrapper = new IAPServiceDiscoveryWrapper(mIAPSettings);
        iapServiceDiscoveryWrapper.serviceUrlMapListener = serviceUrlMapListenerMock;
        iapServiceDiscoveryWrapper.getLocaleFromServiceDiscovery(mock(UiLauncher.class), mock(IAPHandler.class), mock(IAPLaunchInput.class), mock(IAPListener.class), "productCartCount");
        iapServiceDiscoveryWrapper.serviceUrlMapListener.onSuccess(map);
    }


    @Test
    public void testGetLocaleFromServiceDiscoveryOnSuccessWithoutConfigurlWithListnerAndEntryProductCartCount() throws Exception {
        ServiceDiscoveryService discoveryService = new ServiceDiscoveryService();
        discoveryService.init("en_US", "iap.baseurl");
        discoveryService.setConfigUrl(null);
        map.put("iap.baseurl", discoveryService);
        iapServiceDiscoveryWrapper = new IAPServiceDiscoveryWrapper(mIAPSettings);
        iapServiceDiscoveryWrapper.serviceUrlMapListener = serviceUrlMapListenerMock;
        iapServiceDiscoveryWrapper.getLocaleFromServiceDiscovery(mock(UiLauncher.class), mock(IAPHandler.class), mock(IAPLaunchInput.class), mock(IAPListener.class), "productCartCount");
//        iapServiceDiscoveryWrapper.serviceUrlMapListener.onSuccess(map);
    }

    @Test
    public void testGetLocaleFromServiceDiscoveryOnSuccessWithoutConfigurlWithListnerAndEntryCompleteProductList() throws Exception {
        ServiceDiscoveryService discoveryService = new ServiceDiscoveryService();
        discoveryService.init("en_US", "iap.baseurl");
        discoveryService.setConfigUrl(null);
        map.put("iap.baseurl", discoveryService);
        iapServiceDiscoveryWrapper = new IAPServiceDiscoveryWrapper(mIAPSettings);
        iapServiceDiscoveryWrapper.serviceUrlMapListener = serviceUrlMapListenerMock;
        iapServiceDiscoveryWrapper.getLocaleFromServiceDiscovery(mock(UiLauncher.class), mock(IAPHandler.class), mock(IAPLaunchInput.class), mock(IAPListener.class), "completeProductList");
        iapServiceDiscoveryWrapper.serviceUrlMapListener.onSuccess(map);
    }

    @Test
    public void testGetLocaleFromServiceDiscoveryOnSuccessWithConfigurlWithListnerAndEntryCompleteProductList() throws Exception {
        ServiceDiscoveryService discoveryService = new ServiceDiscoveryService();
        discoveryService.init("en_US", "iap.baseurl");
        discoveryService.setConfigUrl("https://acc.occ.shop.philips.com/en_US");
        map.put("iap.baseurl", discoveryService);
        iapServiceDiscoveryWrapper = new IAPServiceDiscoveryWrapper(mIAPSettings);
        iapServiceDiscoveryWrapper.serviceUrlMapListener = serviceUrlMapListenerMock;
        iapServiceDiscoveryWrapper.getLocaleFromServiceDiscovery(mock(UiLauncher.class), mock(IAPHandler.class), mock(IAPLaunchInput.class), mock(IAPListener.class), "completeProductList");
        iapServiceDiscoveryWrapper.serviceUrlMapListener.onSuccess(map);
    }

    @Test
    public void testGetLocaleFromServiceDiscoveryOnError() throws Exception {
        ServiceDiscoveryService discoveryService = new ServiceDiscoveryService();
        discoveryService.init("en_US", "iap.baseurl");
        discoveryService.setConfigUrl(null);
        map.put("iap.baseurl", discoveryService);
        iapServiceDiscoveryWrapper = new IAPServiceDiscoveryWrapper(mIAPSettings);
        iapServiceDiscoveryWrapper.serviceUrlMapListener = serviceUrlMapListenerMock;
        iapServiceDiscoveryWrapper.getLocaleFromServiceDiscovery(mock(UiLauncher.class), mock(IAPHandler.class), mock(IAPLaunchInput.class), null, null);
        iapServiceDiscoveryWrapper.serviceUrlMapListener.onError(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.NO_NETWORK, "No Connection");
    }

    @Test
    public void testCartVisiblityFromServiceDiscovery() throws Exception {
        ServiceDiscoveryService discoveryService = new ServiceDiscoveryService();
        discoveryService.init("en_US", "iap.baseurl");
        discoveryService.setConfigUrl(null);
        map.put("iap.baseurl", discoveryService);
        iapServiceDiscoveryWrapper = new IAPServiceDiscoveryWrapper(mIAPSettings);
        iapServiceDiscoveryWrapper.serviceUrlMapListener = serviceUrlMapListenerMock;
        iapServiceDiscoveryWrapper.getCartVisiblityByConfigUrl(mock(IAPListener.class), mock(IAPHandler.class));
        iapServiceDiscoveryWrapper.serviceUrlMapListener.onSuccess(map);
    }

    @Test
    public void testCartVisiblityFromServiceDiscoveryWithConfigNotNull() throws Exception {
        ServiceDiscoveryService discoveryService = new ServiceDiscoveryService();
        discoveryService.init("en_US", "iap.baseurl");
        discoveryService.setConfigUrl("https://acc.occ.shop.philips.com/en_US");
        map.put("iap.baseurl", discoveryService);
        iapServiceDiscoveryWrapper = new IAPServiceDiscoveryWrapper(mIAPSettings);
        iapServiceDiscoveryWrapper.serviceUrlMapListener = serviceUrlMapListenerMock;
        iapServiceDiscoveryWrapper.getCartVisiblityByConfigUrl(mock(IAPListener.class), mock(IAPHandler.class));
        iapServiceDiscoveryWrapper.serviceUrlMapListener.onSuccess(map);
    }

    @Test
    public void testCartVisiblityFromServiceDiscoveryOnErro() throws Exception {
        ServiceDiscoveryService discoveryService = new ServiceDiscoveryService();
        discoveryService.init("en_US", "iap.baseurl");
        discoveryService.setConfigUrl(null);
        map.put("iap.baseurl", discoveryService);
        iapServiceDiscoveryWrapper = new IAPServiceDiscoveryWrapper(mIAPSettings);
        iapServiceDiscoveryWrapper.serviceUrlMapListener = serviceUrlMapListenerMock;
        iapServiceDiscoveryWrapper.getCartVisiblityByConfigUrl(mock(IAPListener.class), mock(IAPHandler.class));
        iapServiceDiscoveryWrapper.serviceUrlMapListener.onError(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.CONNECTION_TIMEOUT, "No Connection");
    }
}