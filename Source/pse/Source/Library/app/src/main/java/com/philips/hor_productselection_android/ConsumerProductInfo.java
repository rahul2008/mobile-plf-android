package com.philips.hor_productselection_android;


public class ConsumerProductInfo {
    private static String mGroup = null;
    private static String mSector = null;
    private static String mCatalog = null;
    private static String mCategory = null;
    private static String mSubCategory = null;
    private static String mCtn = null;


    public String getGroup() {
        return mGroup;
    }

    public static void setGroup(String mGroup) {
        ConsumerProductInfo.mGroup = mGroup;
    }

    public String getSector() {
        return mSector;
    }

    public static void setSector(String mSector) {
        ConsumerProductInfo.mSector = mSector;
    }

    public String getCatalog() {
        return mCatalog;
    }

    public static void setCatalog(String mCatalog) {
        ConsumerProductInfo.mCatalog = mCatalog;
    }

    public String getCategory() {
        return mCategory;
    }

    public static void setCategory(String mCategory) {
        ConsumerProductInfo.mCategory = mCategory;
    }

    public String getSubCategory() {
        return mSubCategory;
    }

    public static void setSubCategory(String mSubCategory) {
        ConsumerProductInfo.mSubCategory = mSubCategory;
    }

    public String getCtn() {
        return mCtn;
    }

    public static void setCtn(String mCtn) {
        ConsumerProductInfo.mCtn = mCtn;
    }

    public String getProductTitle() {
        return null;
    }

}
