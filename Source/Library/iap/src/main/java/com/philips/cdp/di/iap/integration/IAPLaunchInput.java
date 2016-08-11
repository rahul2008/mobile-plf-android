package com.philips.cdp.di.iap.integration;


import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;

import java.util.ArrayList;

/**
 * Created by Apple on 10/08/16.
 */
public class IAPLaunchInput extends UappLaunchInput {
    private boolean mUseLocalData;
    int mLandingViews;
    ArrayList<String> mProductCTNs;

    public boolean isUseLocalData() {
        return mUseLocalData;
    }

    public void setUseLocalData(boolean mUseLocalData) {
        this.mUseLocalData = mUseLocalData;
    }

    public void setIAPFlow(int pLandingViews, ArrayList<String> pProductCTNs) {
        mLandingViews = pLandingViews;
        mProductCTNs = pProductCTNs;
    }

    public interface IAPFlows {
        int IAP_PRODUCT_CATALOG_VIEW = 0;
        int IAP_SHOPPING_CART_VIEW = 1;
        int IAP_PURCHASE_HISTORY_VIEW = 2;
        int IAP_PRODUCT_DETAIL_VIEW = 3;
        int IAP_BUY_DIRECT_VIEW = 4;
    }
}
