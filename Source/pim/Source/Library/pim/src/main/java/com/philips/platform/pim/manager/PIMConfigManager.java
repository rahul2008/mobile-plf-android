package com.philips.platform.pim.manager;

import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveryService;

import java.util.ArrayList;
import java.util.Map;

import static com.philips.platform.appinfra.logging.LoggingInterface.LogLevel.DEBUG;

public class PIMConfigManager {
    private static final String TAG = PIMConfigManager.class.getSimpleName();
    private LoggingInterface mLoggingInterface;
    private final String PIM_BASEURL = "userreg.janrainoidc.issuer";
    private PIMUserManager mPimUserManager;


    public PIMConfigManager(PIMUserManager pimUserManager) {
        mPimUserManager = pimUserManager;
        mLoggingInterface = PIMSettingManager.getInstance().getLoggingInterface();
    }

    public void init(ServiceDiscoveryInterface serviceDiscoveryInterface) {
        mLoggingInterface.log(DEBUG, TAG, "init called");
        ArrayList<String> listOfServiceId = new ArrayList<>();
        listOfServiceId.add(PIM_BASEURL);
        if (mPimUserManager.getAuthState() == null && mPimUserManager.getOIDCUserProfile() == null)
            downloadSDServiceURLs(serviceDiscoveryInterface, listOfServiceId);
    }

    private void downloadSDServiceURLs(ServiceDiscoveryInterface serviceDiscoveryInterface, ArrayList<String> listOfServiceId) {
        mLoggingInterface.log(DEBUG, TAG, "downloadSDServiceURLs called");
        new Thread(() -> {
            serviceDiscoveryInterface.getServicesWithCountryPreference(listOfServiceId, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
                @Override
                public void onSuccess(Map<String, ServiceDiscoveryService> urlMap) {
                    mLoggingInterface.log(DEBUG, TAG, "getServicesWithCountryPreference : onLoginSuccess");

                    ServiceDiscoveryService serviceDiscoveryService = urlMap.get(PIM_BASEURL);

                    if (serviceDiscoveryService == null) {
                        mLoggingInterface.log(DEBUG, TAG, "getServicesWithCountryPreference : onLoginSuccess : serviceDiscovery response is null");
                    } else {
                        String configUrls = serviceDiscoveryService.getConfigUrls();
                        if (configUrls != null) {
                            PIMOidcDiscoveryManager pimOidcDiscoveryManager = new PIMOidcDiscoveryManager();
                            mLoggingInterface.log(DEBUG, TAG, "getServicesWithCountryPreference : onLoginSuccess : getConfigUrls : " + configUrls);
                            // TODO: Addressed:Deepthi 15 Apr Populate config if already downloaded from usermanager's authstate
                            pimOidcDiscoveryManager.downloadOidcUrls(configUrls);
                        } else {
                            mLoggingInterface.log(DEBUG, TAG, "getServicesWithCountryPreference : onLoginSuccess : No service url found for Issuer service id");
                        }
                    }
                }

                @Override
                public void onError(ERRORVALUES error, String message) {
                    mLoggingInterface.log(DEBUG, TAG, "getServicesWithCountryPreference : onLoginFailed. ERRORVALUES: " + error + " Message: " + message);
                }
            }, null);
        }).start();
    }
}
