package com.philips.cdp.di.mec.integration;


import com.philips.cdp.di.mec.utils.MECUtility;
import com.philips.cdp.di.mec.utils.Utility;
import com.philips.platform.uappframework.launcher.UiLauncher;

public class MECConfigure {
    public static final String COMPLETE_PRODUCT_LIST = "completeProductList";
    private MECSettings mMECSettings;
    private boolean isCartVisible;

    MECConfigure(MECSettings pIAPSettings) {
        mMECSettings = pIAPSettings;
    }

    void configureMEC(final UiLauncher pUiLauncher, final MECHandler pMECHandler, final MECLaunchInput pIapLaunchInput, final MECListener mecListener, final String entry) {


    }

    public void launchingIAP(MECHandler pMECHandler, UiLauncher pUiLauncher, MECLaunchInput pUappLaunchInput) {
        Utility.setVoucherCode(pUappLaunchInput.getVoucher());
        MECUtility.getInstance().setMaxCartCount(pUappLaunchInput.getMaxCartCount());
        MECUtility.getInstance().setHybrisSupported(pUappLaunchInput.isHybrisSupported());
        MECUtility.getInstance().setMecOrderFlowCompletion(pUappLaunchInput.getMecOrderFlowCompletion());
        if (pUappLaunchInput.getMecBannerEnabler() != null) {
            MECUtility.getInstance().setBannerView(pUappLaunchInput.getMecBannerEnabler().getBannerView());
        }
        if (!mMECSettings.isUseLocalData()) {
            pMECHandler.initMEC(pUiLauncher, pUappLaunchInput);
        } else {
            pMECHandler.launchMEC(pUiLauncher, pUappLaunchInput);
        }
    }


    public void getCartVisiblityByConfigUrl(final MECListener listener, final MECHandler iapHandler) {


    }

}
