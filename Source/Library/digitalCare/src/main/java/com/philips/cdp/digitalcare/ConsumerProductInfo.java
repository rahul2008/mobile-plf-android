/**
 * This class responsible for holding the Product(CTN) specific data, As per the user input or the user selection
 * with respect to the locale.
 *
 * @author naveen@philips.com
 *
 *  Copyright (c) 2016 Philips. All rights reserved.
 */

package com.philips.cdp.digitalcare;

public class ConsumerProductInfo {
    private String mGroup = null;
    private String mSector = null;
    private String mCatalog = null;
    private String mCategory = null;
    private String mSubCategoryKeyFromSummary = null;
    private String mCtn = null;
    private String mProductTitle = null;


    public String getGroup() {
        return mGroup;
    }

    public void setGroup(String group) {
        mGroup = group;
    }

    public String getSector() {
        return mSector;
    }

    public void setSector(String sector) {
        mSector = sector;
    }

    public String getCatalog() {
        return mCatalog;
    }

    public void setCatalog(String catalog) {
        mCatalog = catalog;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        mCategory = category;
    }

    public String getSubCategoryKey() {
        return mSubCategoryKeyFromSummary;
    }

    public void setSubCategoryKey(String subCategory) {
        mSubCategoryKeyFromSummary = subCategory;
    }

    public String getCtn() {
        return mCtn;
    }

    public void setCtn(String ctn) {
        mCtn = ctn;
    }

    public String getProductTitle() {
        return mProductTitle;
    }

    public void setProductReviewUrl(String productTitle) {
        mProductTitle = productTitle;
    }
}
