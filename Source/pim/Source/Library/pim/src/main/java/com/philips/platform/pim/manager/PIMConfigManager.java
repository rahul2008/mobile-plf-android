package com.philips.platform.pim.manager;

import android.util.Log;

import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveryService;
import com.philips.platform.pim.utilities.PIMConstants;

import java.util.ArrayList;
import java.util.Map;

import static com.philips.platform.pim.utilities.PIMConstants.PIM_BASEURL;

public class PIMConfigManager {
    private static final String TAG = PIMConfigManager.class.getSimpleName();
    private final ArrayList<String> listOfServiceId;


    // TODO: Create init method, inject servicediscovery for testing purpose
    public PIMConfigManager() {

        listOfServiceId = new ArrayList<>();
        listOfServiceId.add(PIMConstants.PIM_BASEURL);
    }

    private void downloadSDServiceURLs(ServiceDiscoveryInterface serviceDiscoveryInterface) {
        serviceDiscoveryInterface.getServicesWithCountryPreference(listOfServiceId, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
            @Override
            public void onSuccess(Map<String, ServiceDiscoveryService> urlMap) {
                PIMAuthManager pimAuthManager = new PIMAuthManager();
                PIMOidcDiscoveryManager pimOidcDiscoveryManager = new PIMOidcDiscoveryManager(pimAuthManager);
                // TODO: Populate config if already downloaded from usermanager's authstate
                ServiceDiscoveryService serviceDiscoveryService = urlMap.get(PIM_BASEURL);
                pimOidcDiscoveryManager.downloadOidcUrls(serviceDiscoveryService.getConfigUrls());
            }

            @Override
            public void onError(ERRORVALUES error, String message) {

                Log.d(TAG,"onError : "+error+" message : "+message);

            }
        }, null);
    }


    public void init(ServiceDiscoveryInterface serviceDiscoveryInterface) {
        downloadSDServiceURLs(serviceDiscoveryInterface);
    }
}
