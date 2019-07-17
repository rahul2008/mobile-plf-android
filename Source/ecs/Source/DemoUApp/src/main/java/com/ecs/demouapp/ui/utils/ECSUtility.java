package com.ecs.demouapp.ui.utils;

import android.view.View;


import com.ecs.demouapp.ui.integration.ECSOrderFlowCompletion;
import com.philips.cdp.di.ecs.ECSServices;
import com.philips.cdp.di.ecs.model.products.PaginationEntity;
import com.philips.platform.pif.DataInterface.USR.UserDataInterface;

/**
 * Created by philips on 5/2/19.
 */

public class ECSUtility {

    private int maxCartCount;
    private ECSOrderFlowCompletion iapOrderFlowCompletion;
    private boolean isHybrisSupported = true;
    private View bannerView = null;
    private boolean isVoucherEnable = false;
    private UserDataInterface mUserDataInterface;
    private String appName;
    private String localeTag;
    private PaginationEntity paginationEntity;

    private ECSUtility() {
    }

    public int getMaxCartCount() {
        return maxCartCount;
    }

    public void setMaxCartCount(int maxCartCount) {
        this.maxCartCount = maxCartCount;
    }

    public ECSOrderFlowCompletion getIapOrderFlowCompletion() {
        return iapOrderFlowCompletion;
    }

    public void setIapOrderFlowCompletion(ECSOrderFlowCompletion iapOrderFlowCompletion) {
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

    public int getmTotalPages() {
        return mTotalPages;
    }

    public void setmTotalPages(int mTotalPages) {
        this.mTotalPages = mTotalPages;
    }

    public PaginationEntity getPaginationEntity() {
        return paginationEntity;
    }

    public void setPaginationEntity(PaginationEntity paginationEntity) {
        this.paginationEntity = paginationEntity;
    }

    public ECSServices getEcsServices() {
        return ecsServices;
    }

    ECSServices ecsServices;

    public void setEcsService(ECSServices ecsServices) {
        this.ecsServices = ecsServices;
    }

    private static class IAPUtilitySingleton
    {
        private static final ECSUtility INSTANCE = new ECSUtility();
    }

    public static ECSUtility getInstance()
    {
        return IAPUtilitySingleton.INSTANCE;
    }

    private int mTotalResults = 0;
    private int mCurrentPage = -1;
    private int mRemainingProducts = 0;
    private int mTotalPages = -1;

    public int getmTotalResults() {
        return mTotalResults;
    }

    public void setmTotalResults(int mTotalResults) {
        this.mTotalResults = mTotalResults;
    }

    public int getmCurrentPage() {
        return mCurrentPage;
    }

    public void setmCurrentPage(int mCurrentPage) {
        this.mCurrentPage = mCurrentPage;
    }

    public int getmRemainingProducts() {
        return mRemainingProducts;
    }

    public void setmRemainingProducts(int mRemainingProducts) {
        this.mRemainingProducts = mRemainingProducts;
    }

    public void resetPegination(){

        mTotalResults = 0;
        mCurrentPage = -1;
        mRemainingProducts = 0;
        mTotalPages = -1;
    }



}
