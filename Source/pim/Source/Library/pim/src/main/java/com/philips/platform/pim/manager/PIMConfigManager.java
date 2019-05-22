package com.philips.platform.pim.manager;

import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveryService;
import com.philips.platform.pif.DataInterface.USR.enums.UserLoggedInState;

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
        if (mPimUserManager.getUserLoggedInState() == UserLoggedInState.USER_NOT_LOGGED_IN)
            downloadSDServiceURLs(serviceDiscoveryInterface, listOfServiceId);
        else {
            mLoggingInterface.log(DEBUG, TAG, "downloadSDServiceURLs skipped as user is logged in. ");
        }
    }

    private void downloadSDServiceURLs(ServiceDiscoveryInterface serviceDiscoveryInterface, ArrayList<String> listOfServiceId) {
        mLoggingInterface.log(DEBUG, TAG, "downloadSDServiceURLs called");
        new Thread(() -> {
            serviceDiscoveryInterface.getServicesWithCountryPreference(listOfServiceId, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
                @Override
                public void onSuccess(Map<String, ServiceDiscoveryService> urlMap) {
                    mLoggingInterface.log(DEBUG, TAG, "DownloadSDServiceURLs success callbback recieved");

                    ServiceDiscoveryService serviceDiscoveryService = urlMap.get(PIM_BASEURL);
                    if (serviceDiscoveryService == null) {
                        mLoggingInterface.log(DEBUG, TAG, "DownloadSDServiceURLs success  : serviceDiscovery response is null");
                    } else {
                        PIMSettingManager.getInstance().setLocale(serviceDiscoveryService.getLocale());
                        String configUrls = serviceDiscoveryService.getConfigUrls();
                        if (configUrls != null) {
                            PIMOidcDiscoveryManager pimOidcDiscoveryManager = new PIMOidcDiscoveryManager();
                            mLoggingInterface.log(DEBUG, TAG, "DownloadSDServiceURLs success : getConfigUrls : " + configUrls);
                            pimOidcDiscoveryManager.downloadOidcUrls(configUrls);
                        } else {
                            mLoggingInterface.log(DEBUG, TAG, "DownloadSDServiceURLs success : No service url found for Issuer service id");
                        }
                    }
                }

                @Override
                public void onError(ERRORVALUES error, String message) {
                    mLoggingInterface.log(DEBUG, TAG, "DownloadSDServiceURLs error. ERRORVALUES: " + error + " Message: " + message);
                }
            }, null);
        }).start();
    }
}
