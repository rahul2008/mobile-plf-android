package com.philips.platform.pim.manager;

import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveryService;

import java.util.ArrayList;
import java.util.Map;

import static com.philips.platform.pim.utilities.PIMConstants.PIM_BASEURL;

public class PIMConfigManager {
    private final ArrayList<String> listOfServiceId;
    private PIMOidcDiscoveryManager pimOidcDiscoveryManager;
    private PIMAuthManager pimAuthManager;

    // TODO: Create init method, inject servicediscovery for testing purpose
    public PIMConfigManager() {
        listOfServiceId = new ArrayList<>();
        listOfServiceId.add(PIM_BASEURL);
    }

    private void downloadSDServiceURLs(ServiceDiscoveryInterface serviceDiscoveryInterface) {
        serviceDiscoveryInterface.getServicesWithCountryPreference(listOfServiceId, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
            @Override
            public void onSuccess(Map<String, ServiceDiscoveryService> urlMap) {
                pimAuthManager = new PIMAuthManager();
                pimOidcDiscoveryManager = new PIMOidcDiscoveryManager(pimAuthManager);
                // TODO: Populate config if already downloaded from usermanager's authstate
                pimOidcDiscoveryManager.downloadOidcUrls("baseurl");
            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                // TODO: Handle error

            }
        }, null);
    }


    public void init(ServiceDiscoveryInterface serviceDiscoveryInterface) {
        downloadSDServiceURLs(serviceDiscoveryInterface);
    }
}
