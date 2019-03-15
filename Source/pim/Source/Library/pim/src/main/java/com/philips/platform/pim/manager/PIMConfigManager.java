package com.philips.platform.pim.manager;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.pim.rest.PIMListener;
import com.philips.platform.pim.utilities.PIMStorageUtility;

import java.util.ArrayList;

public class PIMConfigManager {
    private final ArrayList<String> listOfServiceId;
    private ServiceDiscoveryInterface serviceDiscoveryInterface;
    private PIMOidcDiscoveryManager pimOidcDiscoveryManager;

    public PIMConfigManager(AppInfraInterface appInfraInterface) {
        serviceDiscoveryInterface = appInfraInterface.getServiceDiscovery();
        listOfServiceId = new ArrayList<>();
        listOfServiceId.add("pim.baseurl");
        PIMAuthManager pimAuthManager = new PIMAuthManager();
        PIMStorageUtility pimStorageUtility = new PIMStorageUtility();
        pimOidcDiscoveryManager = new PIMOidcDiscoveryManager(pimAuthManager, pimStorageUtility);


    }

    public void downloadSDServiceURLSWithCompletion(ServiceDiscoveryInterface serviceDiscoveryInterface) {
        downloadOidcConfigration("https://v2.api.us.janrain.com/c2a48310-9715-3beb-895e-000000000000/login");
//        serviceDiscoveryInterface.getServicesWithCountryPreference(listOfServiceId, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
//            @Override
//            public void onSuccess(Map<String, ServiceDiscoveryService> urlMap) {
//                downloadOidcConfigration("");
//            }
//
//            @Override
//            public void onError(ERRORVALUES error, String message) {
//
//            }
//        }, null);
    }

    private void downloadOidcConfigration(String baseUrl) {
        pimOidcDiscoveryManager.downloadOidcUrls(baseUrl, new PIMListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {

            }
        });
    }
}
