/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.productCatalog;

public class ProductCatalogData {

    private String mCtnNumber;
    private String mProductTitle;
    private String mImageURL;
    private int mTotalItems;
    private int mStockLevel;

    public String getDiscountedPrice() {
        return mDiscountedPrice;
    }

    public void setDiscountedPrice(final String mDiscountedPrice) {
        this.mDiscountedPrice = mDiscountedPrice;
    }

    private String mDiscountedPrice;
    private String mTotalPriceFormatedPrice;
    private String mTotalPriceWithTaxFormatedPrice;
    private String mFormatedPrice;
    private String mPriceValue;

    public String getPriceValue() {
        return mPriceValue;
    }

    public void setPriceValue(String mPriceValue) {
        this.mPriceValue = mPriceValue;
    }

    public String getMarketingTextHeader() {
        return mMarketingTextHeader;
    }

    public void setMarketingTextHeader(final String pMarketingTextHeader) {
        this.mMarketingTextHeader = pMarketingTextHeader;
    }

    private String mMarketingTextHeader;


    public ProductCatalogData() {
    }

    public String getCtnNumber() {
        return mCtnNumber;
    }

    public void setCtnNumber(String ctnNumber) {
        mCtnNumber = ctnNumber;
    }

    public void setProductTitle(String productTitle) {
        mProductTitle = productTitle;
    }

    public String getProductTitle() {
        return mProductTitle;
    }

    public void setImageUrl(String url) {
        mImageURL = url;
    }

    public String getImageURL() {
        return mImageURL;
    }

    public void setTotalItems(int totalItems) {
        mTotalItems = totalItems;
    }

    public int getTotalItems() {
        return mTotalItems;
    }

    public void setStockLevel(final int stockLevel) {
        mStockLevel = stockLevel;
    }

    public String getTotalPriceWithTaxFormatedPrice() {
        return mTotalPriceWithTaxFormatedPrice;
    }

    public void setTotalPriceWithTaxFormatedPrice(String mTotalPriceWithTaxFormatedPrice) {
        this.mTotalPriceWithTaxFormatedPrice = mTotalPriceWithTaxFormatedPrice;
    }

    public String getFormatedPrice() {
        return mFormatedPrice;
    }

    public void setFormatedPrice(String mFormatedPrice) {
        this.mFormatedPrice = mFormatedPrice;
    }

    public String getTotalPriceFormatedPrice() {
        return mTotalPriceFormatedPrice;
    }

    public void setTotalPriceFormatedPrice(String mTotalPriceFormatedPrice) {
        this.mTotalPriceFormatedPrice = mTotalPriceFormatedPrice;
    }
}