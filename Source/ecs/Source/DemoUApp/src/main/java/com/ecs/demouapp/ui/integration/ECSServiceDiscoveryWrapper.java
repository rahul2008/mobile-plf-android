package com.ecs.demouapp.ui.integration;


import com.ecs.demouapp.ui.container.CartModelContainer;
import com.ecs.demouapp.ui.session.HybrisDelegate;
import com.ecs.demouapp.ui.utils.ECSConstant;
import com.ecs.demouapp.ui.utils.ECSLog;
import com.ecs.demouapp.ui.utils.ECSUtility;
import com.ecs.demouapp.ui.utils.Utility;
import com.philips.cdp.di.ecs.util.ECSConfig;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveryService;
import com.philips.platform.uappframework.launcher.UiLauncher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ECSServiceDiscoveryWrapper {
    public static final String COMPLETE_PRODUCT_LIST = "completeProductList";
    private ECSSettings mIAPSettings;
    private ArrayList<String> listOfServiceId;
    ServiceDiscoveryInterface.OnGetServiceUrlMapListener serviceUrlMapListener;
    private ServiceDiscoveryInterface serviceDiscoveryInterface;
    private boolean isCartVisible;

    ECSServiceDiscoveryWrapper(ECSSettings pIAPSettings) {
        mIAPSettings = pIAPSettings;
        listOfServiceId = new ArrayList<>();
        listOfServiceId.add("iap.baseurl");
        AppInfraInterface appInfra = CartModelContainer.getInstance().getAppInfraInstance();
        serviceDiscoveryInterface = appInfra.getServiceDiscovery();

    }

    void getLocaleFromServiceDiscovery(final UiLauncher pUiLauncher, final ECSHandler pIAPHandler, final ECSLaunchInput pIapLaunchInput, final ECSListener iapListener, final String entry) {


        serviceUrlMapListener = new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
            @Override
            public void onSuccess(Map<String, ServiceDiscoveryService> map) {
                ECSLog.i(ECSLog.LOG, " getServicesWithCountryPreference Map" + map.toString());
                Collection<ServiceDiscoveryService> collection = map.values();



                List<ServiceDiscoveryService> list = new ArrayList<>();
                list.addAll(collection);
                ServiceDiscoveryService serviceDiscoveryService = list.get(0);

                pIAPHandler.initIAPRequisite();
                String locale = serviceDiscoveryService.getLocale();

                String configUrls = serviceDiscoveryService.getConfigUrls();
                if (locale != null) {
                    setLangAndCountry(locale);
                    ECSConfig.INSTANCE.setLocale(locale);
                }
                //Condition for launching IAP screens
                if (iapListener == null && entry == null) {
                    if (configUrls == null || configUrls.isEmpty() || !ECSUtility.getInstance().isHybrisSupported()) {
                        mIAPSettings.setUseLocalData(true);
                    } else {
                        // TODO Retailer view hence making the userLocalData to true
                        mIAPSettings.setUseLocalData(false);
                        //    String urlPort = "https://acc.us.pil.shop.philips.com/en_US";//;"https://www.occ.shop.philips.com/en_US";
                        mIAPSettings.setHostPort(configUrls + "/");
                        ECSConfig.INSTANCE.setBaseURL(configUrls + "/");
                    }
                    pIAPHandler.initControllerFactory();
                    launchingIAP(pIAPHandler, pUiLauncher, pIapLaunchInput);
                } else {
                    //Condition for returning gatCartCount API and getCompleteProductlist API
                    if (configUrls == null || !ECSUtility.getInstance().isHybrisSupported()) {
                        mIAPSettings.setUseLocalData(true);
                    } else {
                        // TODO Retailer view hence making the userLocalData to true
                        mIAPSettings.setUseLocalData(false);
                        //String urlPort = "https://acc.us.pil.shop.philips.com/en_US";//;"https://www.occ.shop.philips.com/en_US";
                        mIAPSettings.setHostPort(configUrls + "/");
                        ECSConfig.INSTANCE.setBaseURL(configUrls + "/");

                        mIAPSettings.setProposition(loadConfigParams());
                        pIAPHandler.initControllerFactory();
                        if (entry.equalsIgnoreCase(COMPLETE_PRODUCT_LIST))

                            try {
                                pIAPHandler.getExposedAPIImplementor().getCompleteProductList(iapListener);
                            } catch (Exception e) {
                                ECSLog.e(ECSLog.LOG, e.getMessage());
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
                    iapListener.onFailure(ECSConstant.IAP_ERROR_SERVER_ERROR);
                    ECSLog.i(ECSLog.LOG, "ServiceDiscoveryInterface ==errorvalues " + errorvalues.name() + "String= " + s);
                }
            }
        };
        serviceDiscoveryInterface.getServicesWithCountryPreference(listOfServiceId, serviceUrlMapListener,null);
    }

    private void launchingIAP(ECSHandler pIAPHandler, UiLauncher pUiLauncher, ECSLaunchInput pUappLaunchInput) {
        mIAPSettings.setProposition(loadConfigParams());
        Utility.setVoucherCode(pUappLaunchInput.getVoucher());
        ECSUtility.getInstance().setMaxCartCount(pUappLaunchInput.getMaxCartCount());
        ECSUtility.getInstance().setHybrisSupported(pUappLaunchInput.isHybrisSupported());
        ECSUtility.getInstance().setIapOrderFlowCompletion(pUappLaunchInput.getIapOrderFlowCompletion());
        if(pUappLaunchInput.getECSBannerEnabler()!=null) {
            ECSUtility.getInstance().setBannerView(pUappLaunchInput.getECSBannerEnabler().getBannerView());
        }
        if (!mIAPSettings.isUseLocalData() && (!pIAPHandler.isStoreInitialized(mIAPSettings.getContext()))) {
            pIAPHandler.initIAP(pUiLauncher, pUappLaunchInput);
        } else {
            pIAPHandler.launchIAP(pUiLauncher, pUappLaunchInput);
        }
    }

    private String loadConfigParams() {
        AppConfigurationInterface mConfigInterface = CartModelContainer.getInstance().getAppInfraInstance().getConfigInterface();
        AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface.AppConfigurationError();

        String propositionId = (String) mConfigInterface.getPropertyForKey("propositionid", "IAP", configError);

        if (configError.getErrorCode() != null) {
            ECSLog.e(ECSLog.LOG, "VerticalAppConfig ==loadConfigurationFromAsset " + configError.getErrorCode().toString());
        }
        
        return propositionId;
    }

    private void setLangAndCountry(String locale) {
        String[] localeArray;
        localeArray = locale.split("_");
        CartModelContainer.getInstance().setLanguage(localeArray[0]);
        CartModelContainer.getInstance().setCountry(localeArray[1]);
        HybrisDelegate.getInstance().getStore().setLangAndCountry(localeArray[0], localeArray[1]);
        ECSLog.i(ECSLog.LOG, "setLangAndCountry Locale = " + HybrisDelegate.getInstance().getStore().getLocale());
    }

    public Boolean getCartVisiblityByConfigUrl(final ECSListener listener, final ECSHandler iapHandler) {


        serviceUrlMapListener = new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
            @Override
            public void onSuccess(Map<String, ServiceDiscoveryService> map) {
                ECSLog.i(ECSLog.LOG, " getServicesWithCountryPreference Map" + map.toString());
                Collection<ServiceDiscoveryService> collection = map.values();

                List<ServiceDiscoveryService> list = new ArrayList<>();
                list.addAll(collection);
                ServiceDiscoveryService serviceDiscoveryService = list.get(0);
                String configUrls = serviceDiscoveryService.getConfigUrls();
                if (configUrls == null || ECSUtility.getInstance().isHybrisSupported() == false) {
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
                ECSLog.i(ECSLog.LOG, "ServiceDiscoveryInterface ==errorvalues " + errorvalues.name() + "String= " + s);
                listener.onFailure(ECSConstant.IAP_ERROR_SERVER_ERROR);
            }
        };
        serviceDiscoveryInterface.getServicesWithCountryPreference(listOfServiceId, serviceUrlMapListener,null);
        return isCartVisible;
    }

}
