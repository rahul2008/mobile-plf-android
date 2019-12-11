/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.mec.integration;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.philips.cdp.di.ecs.ECSServices;
import com.philips.cdp.di.mec.common.MECFragmentLauncher;
import com.philips.cdp.di.mec.common.MECLauncherActivity;
import com.philips.cdp.di.mec.utils.MECConstant;
import com.philips.cdp.di.mec.utils.MECDataHolder;
import com.philips.cdp.di.mec.utils.MECutility;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveryService;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

class MECHandler {
    private MECDependencies mMECDependencies;
    private MECSettings mMECSetting;
    private UiLauncher mUiLauncher;
    private MECLaunchInput mLaunchInput;
    private AppInfra appInfra;
    private ArrayList<String> listOfServiceId;
    ServiceDiscoveryInterface.OnGetServiceUrlMapListener serviceUrlMapListener;
    private static final String IAP_PRIVACY_URL = "iap.privacyPolicy";
    private static final String IAP_FAQ_URL = "iap.faq";
    private static final String IAP_TERMS_URL = "iap.termOfUse";
    private static final String IAP_BASE_URL = "iap.baseurl";

    MECHandler(MECDependencies pMECDependencies, MECSettings pMecSettings, UiLauncher pUiLauncher, MECLaunchInput pLaunchInput) {
        this.mMECDependencies = pMECDependencies;
        this.mMECSetting = pMecSettings;
        this.mUiLauncher = pUiLauncher;
        this.mLaunchInput = pLaunchInput;

    }


    void launchMEC() {
        appInfra= (AppInfra) mMECDependencies.getAppInfra() ;
        AppConfigurationInterface configInterface = appInfra.getConfigInterface();
        AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface.AppConfigurationError();
        String propertyForKey = (String) configInterface.getPropertyForKey("propositionid", "MEC", configError);
        ECSServices ecsServices = new ECSServices(propertyForKey,appInfra);
        MecHolder.INSTANCE.eCSServices=ecsServices; // singleton
        MECDataHolder.INSTANCE.appinfra = appInfra;
        MECDataHolder.INSTANCE.propositionId = propertyForKey;
        MECDataHolder.INSTANCE.mecBannerEnabler = mLaunchInput.getMecBannerEnabler();
        MECDataHolder.INSTANCE.setHybrisEnabled(mLaunchInput.getHybrisEnabled());
        MECDataHolder.INSTANCE.mecBazaarVoiceInput = mLaunchInput.getMecBazaarVoiceInput();
        MECDataHolder.INSTANCE.blackListedRetailers = Objects.requireNonNull(mLaunchInput.getIgnoreRetailers());


        getUrl();
        if (mUiLauncher instanceof ActivityLauncher) {
            launchMECasActivity();
        } else {
            launchMECasFragment();
        }
    }

    void getUrl()
    {

        listOfServiceId = new ArrayList<>();
        listOfServiceId.add(IAP_BASE_URL);
        listOfServiceId.add(IAP_PRIVACY_URL);
        listOfServiceId.add(IAP_FAQ_URL);
        listOfServiceId.add(IAP_TERMS_URL);
        serviceUrlMapListener = new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
            @Override
            public void onSuccess(Map<String, ServiceDiscoveryService> map) {
                Collection<ServiceDiscoveryService> collection = map.values();


                List<ServiceDiscoveryService> list = new ArrayList<>();
                list.addAll(collection);

                ServiceDiscoveryService discoveryService = map.get(IAP_PRIVACY_URL);
                assert discoveryService != null;
                String privacyUrl = discoveryService.getConfigUrls();
                if(privacyUrl != null) {
                    MECDataHolder.INSTANCE.setPrivacyUrl(privacyUrl);
                }

            }

            @Override
            public void onError(ERRORVALUES errorvalues, String s) {
                if (errorvalues.name().equals(ERRORVALUES.NO_NETWORK)) {
                }
            }
        };
        appInfra.getServiceDiscovery().getServicesWithCountryPreference(listOfServiceId, serviceUrlMapListener,null);
    }



    protected void launchMECasActivity() {
        Intent intent = new Intent(mMECSetting.getContext(), MECLauncherActivity.class);
        intent.putExtra(MECConstant.INSTANCE.getMEC_LANDING_SCREEN(), mLaunchInput.getMLandingView());
        ActivityLauncher activityLauncher = (ActivityLauncher) mUiLauncher;
        Bundle bundle =  getBundle();
        bundle.putInt(MECConstant.INSTANCE.getMEC_KEY_ACTIVITY_THEME(), activityLauncher.getUiKitTheme());
        intent.putExtras(bundle);
        mMECSetting.getContext().startActivity(intent);

    }

    protected void launchMECasFragment() {
        FragmentLauncher fragmentLauncher = (FragmentLauncher) mUiLauncher;
        Bundle bundle =  getBundle();
        bundle.putInt(MECConstant.INSTANCE.getMEC_LANDING_SCREEN(), mLaunchInput.getMLandingView());
        bundle.putInt("fragment_container", fragmentLauncher.getParentContainerResourceID()); // frame_layout for fragment
        loadDecisionFragment(bundle);


    }

     void loadDecisionFragment(Bundle bundle ){
        MECFragmentLauncher mecFragmentLauncher = new MECFragmentLauncher();
         mecFragmentLauncher.setArguments(bundle);

         FragmentLauncher fragmentLauncher = (FragmentLauncher)mUiLauncher;
         MECDataHolder.INSTANCE.setActionBarListener(fragmentLauncher.getActionbarListener(), mLaunchInput.getMecListener());
         String tag = mecFragmentLauncher.getClass().getName();
         FragmentTransaction transaction = fragmentLauncher.getFragmentActivity().getSupportFragmentManager().beginTransaction();
         transaction.replace(fragmentLauncher.getParentContainerResourceID(), mecFragmentLauncher, tag);
         transaction.commitAllowingStateLoss();

    }

    Bundle getBundle (){

        Bundle mBundle = new Bundle();
        if (mLaunchInput.getMMECFlowInput() != null) {
            if (mLaunchInput.getMMECFlowInput().getProductCTN() != null) {
                mBundle.putString(MECConstant.INSTANCE.getMEC_PRODUCT_CTN_NUMBER_FROM_VERTICAL(),
                        mLaunchInput.getMMECFlowInput().getProductCTN());
            }
            if (mLaunchInput.getMMECFlowInput().getProductCTNs() != null) {
                mBundle.putStringArrayList(MECConstant.INSTANCE.getCATEGORISED_PRODUCT_CTNS(),
                        mLaunchInput.getMMECFlowInput().getProductCTNs());
            }
            mBundle.putStringArrayList(MECConstant.INSTANCE.getMEC_IGNORE_RETAILER_LIST(), mLaunchInput.getIgnoreRetailers());
        }
        return mBundle;
    }


}
