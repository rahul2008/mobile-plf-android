package com.philips.platform.pim.manager;


import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.pim.configration.PIMConfigration;
import com.philips.platform.pim.configration.PIMHSDPConfigration;
import com.philips.platform.pim.configration.PIMOIDCConfigration;
import com.philips.platform.pim.integration.PIMDependencies;
import com.philips.platform.pim.utilities.PIMConstants;
import com.philips.platform.pim.utilities.PIMUtilities;


public class PIMSettingManager {
    private static final PIMSettingManager instance = new PIMSettingManager();
    private LoggingInterface mLoggingInterface;
    private AppTaggingInterface mTaggingInterface;
    private SecureStorageInterface mSecureStorageInterface;
    private RestInterface mRestInterface;
    private AppConfigurationInterface mAppConfigurationInterface;
    private ServiceDiscoveryInterface mServiceDiscoveryInterface;
    private PIMConfigration pimConfigration;
    private PIMOIDCConfigration mPimoidcConfigration;
    private PIMHSDPConfigration mPimhsdpConfigration;

    //TODO : We should make be null once AuthorizationServiceDiscovery stored in SecureStorage
    public void setPimOidcConfigration(PIMOIDCConfigration pimOidcConfigration) {
        mPimoidcConfigration = pimOidcConfigration;
    }


    public PIMOIDCConfigration getPiOidcConfigration() {
        return mPimoidcConfigration;
    }

    public void setPimhsdpConfigration() {
        this.mPimhsdpConfigration = new PIMHSDPConfigration();
    }

    public void setDependencies(PIMDependencies pimDependencies) {
        AppInfraInterface appInfraInterface = pimDependencies.getAppInfra();
        mLoggingInterface = appInfraInterface.getLogging().createInstanceForComponent(PIMConstants.COMPONENT_TAGS_ID, PIMUtilities.getComponentVersion());
        mTaggingInterface = appInfraInterface.getTagging().createInstanceForComponent(PIMConstants.COMPONENT_TAGS_ID, PIMUtilities.getComponentVersion());
        mAppConfigurationInterface = appInfraInterface.getConfigInterface();
        mRestInterface = appInfraInterface.getRestClient();
        mSecureStorageInterface = appInfraInterface.getSecureStorage();
        mServiceDiscoveryInterface = appInfraInterface.getServiceDiscovery();
    }

    public static PIMSettingManager getInstance() {
        return instance;
    }


    public LoggingInterface getLoggingInterface() {
        return mLoggingInterface;
    }

    public AppTaggingInterface getTaggingInterface() {
        return mTaggingInterface;
    }


    public AppConfigurationInterface getAppConfigurationInterface() {
        return mAppConfigurationInterface;
    }

    public RestInterface getRestInterface() {
        return mRestInterface;
    }

    public SecureStorageInterface getSecureStorageInterface() {
        return mSecureStorageInterface;
    }

    public ServiceDiscoveryInterface getmServiceDiscoveryInterface() {
        return mServiceDiscoveryInterface;
    }

}
