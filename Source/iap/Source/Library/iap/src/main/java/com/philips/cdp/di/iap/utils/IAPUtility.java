package com.philips.cdp.di.iap.utils;

import android.view.View;

import com.philips.cdp.di.iap.integration.IAPOrderFlowCompletion;
import com.philips.platform.pif.DataInterface.USR.UserDataInterface;

/**
 * Created by philips on 5/2/19.
 */

public class IAPUtility {

    private int maxCartCount;
    private IAPOrderFlowCompletion iapOrderFlowCompletion;
    private boolean isHybrisSupported = true;
    private View bannerView = null;
    private boolean isVoucherEnable = false;
    private UserDataInterface mUserDataInterface;
    private String appName;
    private String localeTag;

    private IAPUtility() {
    }

    public int getMaxCartCount() {
        return maxCartCount;
    }

    public void setMaxCartCount(int maxCartCount) {
        this.maxCartCount = maxCartCount;
    }

    public IAPOrderFlowCompletion getIapOrderFlowCompletion() {
        return iapOrderFlowCompletion;
    }

    public void setIapOrderFlowCompletion(IAPOrderFlowCompletion iapOrderFlowCompletion) {
        this.iapOrderFlowCompletion = iapOrderFlowCompletion;
    }

    public boolean isHybrisSupported() {
        return isHybrisSupported;
    }

    public void setHybrisSupported(boolean hybrisSupported) {
        isHybrisSupported = hybrisSupported;
    }

    public View getBannerView() {
        return bannerView;
    }

    public void setBannerView(View bannerView) {
        this.bannerView = bannerView;
    }

    public boolean isVoucherEnable() {
        return isVoucherEnable;
    }

    public void setVoucherEnable(boolean voucherEnable) {
        isVoucherEnable = voucherEnable;
    }

    public UserDataInterface getUserDataInterface() {
        return mUserDataInterface;
    }

    public void setUserDataInterface(UserDataInterface mUserDataInterface) {
       this.mUserDataInterface =  mUserDataInterface;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getLocaleTag() {
        return localeTag;
    }

    public void setLocaleTag(String localeTag) {
        this.localeTag = localeTag;
    }

    private static class IAPUtilitySingleton
    {
        private static final IAPUtility INSTANCE = new IAPUtility();
    }

    public static IAPUtility getInstance()
    {
        return IAPUtilitySingleton.INSTANCE;
    }

}
