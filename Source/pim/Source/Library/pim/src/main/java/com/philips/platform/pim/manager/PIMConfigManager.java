package com.philips.platform.pim.manager;

import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveryService;
import com.philips.platform.pim.utilities.PIMConstants;

import java.util.ArrayList;
import java.util.Map;

import static com.philips.platform.appinfra.logging.LoggingInterface.LogLevel.DEBUG;
import static com.philips.platform.pim.utilities.PIMConstants.PIM_BASEURL;

public class PIMConfigManager {
    private static final String TAG = PIMConfigManager.class.getSimpleName();
    private final ArrayList<String> listOfServiceId;
    private LoggingInterface mLoggingInterface;


    // TODO: Create init method, inject servicediscovery for testing purpose
    public PIMConfigManager() {
        mLoggingInterface = PIMSettingManager.getInstance().getLoggingInterface();
        listOfServiceId = new ArrayList<>();
        listOfServiceId.add(PIMConstants.PIM_BASEURL);
        mLoggingInterface.log(DEBUG,TAG,"Added Service id : "+listOfServiceId.get(listOfServiceId.size()-1));
    }

    private void downloadSDServiceURLs(ServiceDiscoveryInterface serviceDiscoveryInterface) {
        mLoggingInterface.log(DEBUG,TAG,"downloadSDServiceURLs called");
        serviceDiscoveryInterface.getServicesWithCountryPreference(listOfServiceId, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
            @Override
            public void onSuccess(Map<String, ServiceDiscoveryService> urlMap) {
                mLoggingInterface.log(DEBUG,TAG,"getServicesWithCountryPreference : onSuccess");
                PIMAuthManager pimAuthManager = new PIMAuthManager();
                PIMOidcDiscoveryManager pimOidcDiscoveryManager = new PIMOidcDiscoveryManager(pimAuthManager);
                // TODO: Populate config if already downloaded from usermanager's authstate
                ServiceDiscoveryService serviceDiscoveryService = urlMap.get(PIM_BASEURL);
                if(serviceDiscoveryService != null && serviceDiscoveryService.getConfigUrls() != null) {
                    mLoggingInterface.log(DEBUG,TAG,"getServicesWithCountryPreference : onSuccess : getConfigUrls : "+serviceDiscoveryService.getConfigUrls());
                    pimOidcDiscoveryManager.downloadOidcUrls(serviceDiscoveryService.getConfigUrls());
                }else if(serviceDiscoveryService == null){
                    mLoggingInterface.log(DEBUG,TAG,"getServicesWithCountryPreference : onSuccess : serviceDiscoveryService is null");
                }else{
                    mLoggingInterface.log(DEBUG,TAG,"getServicesWithCountryPreference : onSuccess : getConfigUrls is null");
                }
            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                mLoggingInterface.log(DEBUG,TAG,"getServicesWithCountryPreference : onError. ERRORVALUES: "+error+" Message: "+message);
            }
        }, null);
    }


    public void init(ServiceDiscoveryInterface serviceDiscoveryInterface) {
        mLoggingInterface.log(DEBUG,TAG,"init called");
        downloadSDServiceURLs(serviceDiscoveryInterface);
    }
}
