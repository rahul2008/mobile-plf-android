package com.philips.hor_productselection_android;



public class ProductSelectionProductInfo extends ConsumerProductInfo {
    private static String mGroup = null;
    private static String mSector = null;
    private static String mCatalog = null;
    private static String mCategory = null;
    private static String mSubCategory = null;
    private static String mCtn = null;

    @Override
    public String getGroup() {
        return mGroup;
    }

    @Override
    public String getSector() {
        return mSector;
    }

    @Override
    public String getCatalog() {
        return mCatalog;
    }

    @Override
    public String getCategory() {
        return mCategory;
    }

    @Override
    public String getSubCategory() {
        return mSubCategory;
    }

    @Override
    public String getCtn() {
        return mCtn;
    }

    @Override
    public String getProductTitle() {
        return null;
    }

    public static void setGroup(String mGroup) {
        ProductSelectionProductInfo.mGroup = mGroup;
    }

    public static void setSector(String mSector) {
        ProductSelectionProductInfo.mSector = mSector;
    }

    public static void setCatalog(String mCatalog) {
        ProductSelectionProductInfo.mCatalog = mCatalog;
    }

    public static void setCategory(String mCategory) {
        ProductSelectionProductInfo.mCategory = mCategory;
    }

    public static void setSubCategory(String mSubCategory) {
        ProductSelectionProductInfo.mSubCategory = mSubCategory;
    }

    public static void setCtn(String mCtn) {
        ProductSelectionProductInfo.mCtn = mCtn;
    }

}
