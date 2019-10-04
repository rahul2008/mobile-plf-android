package com.philips.cdp.di.mec.integration;

import com.philips.cdp.di.mec.container.CartModelContainer;
import com.philips.cdp.di.mec.session.HybrisDelegate;
import com.philips.cdp.di.mec.utils.MECConstant;
import com.philips.cdp.di.mec.utils.MECLog;
import com.philips.cdp.di.mec.utils.MECUtility;
import com.philips.cdp.di.mec.utils.Utility;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveryService;
import com.philips.platform.uappframework.launcher.UiLauncher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class MECServiceDiscoveryWrapper {
    public static final String COMPLETE_PRODUCT_LIST = "completeProductList";
    private MECSettings mIAPSettings;
    private ArrayList<String> listOfServiceId;
    ServiceDiscoveryInterface.OnGetServiceUrlMapListener serviceUrlMapListener;
    private ServiceDiscoveryInterface serviceDiscoveryInterface;
    private boolean isCartVisible;
    private static final String IAP_PRIVACY_URL = "iap.privacyPolicy";
    private static final String IAP_FAQ_URL = "iap.faq";
    private static final String IAP_TERMS_URL = "iap.termOfUse";
    private static final String IAP_BASE_URL = "iap.baseurl";

    MECServiceDiscoveryWrapper(MECSettings pIAPSettings) {
        mIAPSettings = pIAPSettings;
        listOfServiceId = new ArrayList<>();
        listOfServiceId.add(IAP_BASE_URL);
        listOfServiceId.add(IAP_PRIVACY_URL);
        listOfServiceId.add(IAP_FAQ_URL);
        listOfServiceId.add(IAP_TERMS_URL);

        AppInfraInterface appInfra = CartModelContainer.getInstance().getAppInfraInstance();
        serviceDiscoveryInterface = appInfra.getServiceDiscovery();

    }

    void getLocaleFromServiceDiscovery(final UiLauncher pUiLauncher, final MECHandler pIAPHandler, final MECLaunchInput pIapLaunchInput, final MECListener iapListener, final String entry) {


        serviceUrlMapListener = new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
            @Override
            public void onSuccess(Map<String, ServiceDiscoveryService> map) {
                MECLog.i(MECLog.LOG, " getServicesWithCountryPreference Map" + map.toString());
                Collection<ServiceDiscoveryService> collection = map.values();


                List<ServiceDiscoveryService> list = new ArrayList<>();
                list.addAll(collection);

                ServiceDiscoveryService discoveryService = map.get(IAP_PRIVACY_URL);
                assert discoveryService != null;
                String privacyUrl = discoveryService.getConfigUrls();
                if(privacyUrl != null) {
                    MECUtility.getInstance().setPrivacyUrl(privacyUrl);
                }

                ServiceDiscoveryService services = map.get(IAP_FAQ_URL);
                assert services != null;
                String faqUrl = services.getConfigUrls();
                if(faqUrl != null) {
                    MECUtility.getInstance().setFaqUrl(faqUrl);
                }

                ServiceDiscoveryService service = map.get(IAP_TERMS_URL);
                assert service != null;
                String termsUrl = service.getConfigUrls();
                if(termsUrl != null) {
                    MECUtility.getInstance().setTermsUrl(termsUrl);
                }


                ServiceDiscoveryService serviceDiscoveryService = map.get(IAP_BASE_URL);

                pIAPHandler.initIAPRequisite();
                String locale = serviceDiscoveryService.getLocale();
                String configUrls = serviceDiscoveryService.getConfigUrls();


                if (locale != null) {
                    setLangAndCountry(locale);
                }
                //Condition for launching IAP screens
                if (iapListener == null && entry == null) {
                    if (configUrls == null || configUrls.isEmpty() || !MECUtility.getInstance().isHybrisSupported()) {
                        mIAPSettings.setUseLocalData(true);
                    } else {
                        // TODO Retailer view hence making the userLocalData to true
                        mIAPSettings.setUseLocalData(false);
                        //    String urlPort = "https://acc.us.pil.shop.philips.com/en_US";//;"https://www.occ.shop.philips.com/en_US";
                        mIAPSettings.setHostPort(configUrls + "/");
                    }
                    pIAPHandler.initControllerFactory();
                    launchingIAP(pIAPHandler, pUiLauncher, pIapLaunchInput);
                } else {
                    //Condition for returning gatCartCount API and getCompleteProductlist API
                    if (configUrls == null || !MECUtility.getInstance().isHybrisSupported()) {
                        mIAPSettings.setUseLocalData(true);
                    } else {
                        // TODO Retailer view hence making the userLocalData to true
                        mIAPSettings.setUseLocalData(false);
                        //String urlPort = "https://acc.us.pil.shop.philips.com/en_US";//;"https://www.occ.shop.philips.com/en_US";
                        mIAPSettings.setHostPort(configUrls + "/");
                        mIAPSettings.setProposition(loadConfigParams());
                        pIAPHandler.initControllerFactory();
                        if (entry.equalsIgnoreCase(COMPLETE_PRODUCT_LIST))

                            try {
                                pIAPHandler.getExposedAPIImplementor().getCompleteProductList(iapListener);
                            } catch (Exception e) {
                                MECLog.e(MECLog.LOG, e.getMessage());
                            }
                        else {
                            pIAPHandler.getExposedAPIImplementor().getProductCartCount(iapListener);
                        }
                    }
                }
            }

            @Override
            public void onError(ERRORVALUES errorvalues, String s) {
                if (errorvalues.name().equals(ERRORVALUES.NO_NETWORK)) {
                    iapListener.onFailure(MECConstant.MEC_ERROR_SERVER_ERROR);
                    MECLog.i(MECLog.LOG, "ServiceDiscoveryInterface ==errorvalues " + errorvalues.name() + "String= " + s);
                }
            }
        };
        serviceDiscoveryInterface.getServicesWithCountryPreference(listOfServiceId, serviceUrlMapListener,null);
    }

    private void launchingIAP(MECHandler pIAPHandler, UiLauncher pUiLauncher, MECLaunchInput pUappLaunchInput) {
        mIAPSettings.setProposition(loadConfigParams());
        Utility.setVoucherCode(pUappLaunchInput.getVoucher());
        MECUtility.getInstance().setMaxCartCount(pUappLaunchInput.getMaxCartCount());
        MECUtility.getInstance().setHybrisSupported(pUappLaunchInput.isHybrisSupported());
        MECUtility.getInstance().setMecOrderFlowCompletion(pUappLaunchInput.getMecOrderFlowCompletion());
        if(pUappLaunchInput.getMecBannerEnabler()!=null) {
            MECUtility.getInstance().setBannerView(pUappLaunchInput.getMecBannerEnabler().getBannerView());
        }
        if (!mIAPSettings.isUseLocalData() && (!pIAPHandler.isStoreInitialized(mIAPSettings.getContext()))) {
            pIAPHandler.initMEC(pUiLauncher, pUappLaunchInput);
        } else {
            pIAPHandler.launchMEC(pUiLauncher, pUappLaunchInput);
        }
    }

    private String loadConfigParams() {
        AppConfigurationInterface mConfigInterface = CartModelContainer.getInstance().getAppInfraInstance().getConfigInterface();
        AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface.AppConfigurationError();

        String propositionId = (String) mConfigInterface.getPropertyForKey("propositionid", "IAP", configError);

        if (configError.getErrorCode() != null) {
            MECLog.e(MECLog.LOG, "VerticalAppConfig ==loadConfigurationFromAsset " + configError.getErrorCode().toString());
        }
        
        return propositionId;
    }

    private void setLangAndCountry(String locale) {
        String[] localeArray;
        localeArray = locale.split("_");
        CartModelContainer.getInstance().setLanguage(localeArray[0]);
        CartModelContainer.getInstance().setCountry(localeArray[1]);
        HybrisDelegate.getInstance().getStore().setLangAndCountry(localeArray[0], localeArray[1]);
        MECLog.i(MECLog.LOG, "setLangAndCountry Locale = " + HybrisDelegate.getInstance().getStore().getLocale());
    }

    public Boolean getCartVisiblityByConfigUrl(final MECListener listener, final MECHandler iapHandler) {


        serviceUrlMapListener = new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
            @Override
            public void onSuccess(Map<String, ServiceDiscoveryService> map) {
                MECLog.i(MECLog.LOG, " getServicesWithCountryPreference Map" + map.toString());
                Collection<ServiceDiscoveryService> collection = map.values();

                List<ServiceDiscoveryService> list = new ArrayList<>();
                list.addAll(collection);
                ServiceDiscoveryService serviceDiscoveryService = list.get(0);
                String configUrls = serviceDiscoveryService.getConfigUrls();
                if (configUrls == null || MECUtility.getInstance().isHybrisSupported() == false) {
                    mIAPSettings.setUseLocalData(true);
                    isCartVisible = false;
                } else {
                    mIAPSettings.setUseLocalData(false);
                    isCartVisible = true;
                }
                iapHandler.initIAPRequisite();
                listener.onSuccess(isCartVisible);
            }

            @Override
            public void onError(ERRORVALUES errorvalues, String s) {
                MECLog.i(MECLog.LOG, "ServiceDiscoveryInterface ==errorvalues " + errorvalues.name() + "String= " + s);
                listener.onFailure(MECConstant.MEC_ERROR_SERVER_ERROR);
            }
        };
        serviceDiscoveryInterface.getServicesWithCountryPreference(listOfServiceId, serviceUrlMapListener,null);
        return isCartVisible;
    }

}
