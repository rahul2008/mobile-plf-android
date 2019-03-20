package com.philips.platform.pim.manager;

import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveryService;

import java.util.ArrayList;
import java.util.Map;

public class PIMConfigManager {
    private final ArrayList<String> listOfServiceId;
    private ServiceDiscoveryInterface serviceDiscoveryInterface;
    private PIMOidcDiscoveryManager pimOidcDiscoveryManager;
    private PIMAuthManager pimAuthManager;

    public PIMConfigManager() {
        serviceDiscoveryInterface = PIMSettingManager.getInstance().getmServiceDiscoveryInterface();
        listOfServiceId = new ArrayList<>();
        listOfServiceId.add("pim.baseurl");
        downloadSDServiceURLSWithCompletion();
    }

    private void downloadSDServiceURLSWithCompletion() {
        serviceDiscoveryInterface.getServicesWithCountryPreference(listOfServiceId, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
            @Override
            public void onSuccess(Map<String, ServiceDiscoveryService> urlMap) {
                pimAuthManager = new PIMAuthManager();
                pimOidcDiscoveryManager = new PIMOidcDiscoveryManager(pimAuthManager);
                pimOidcDiscoveryManager.downloadOidcUrls("baseurl");
            }

            @Override
            public void onError(ERRORVALUES error, String message) {

            }
        }, null);
    }


}
