/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.productCatalog;

import com.philips.cdp.prxclient.datamodels.summary.ReviewStatistics;

public class ProductCatalogData {
    private int mTotalItems;
    private int mStockLevel;

    private String mCtnNumber;
    private String mProductTitle;
    private String mImageURL;
    private String mDiscountedPrice;
    private String mFormattedTotalPrice;
    private String mFormattedTotalPriceWithTax;
    private String mFormattedPrice;
    private String mPriceValue;
    private String mMarketingTextHeader;
    private ReviewStatistics mReviewStatistics;

    public ProductCatalogData() {
    }

    public String getDiscountedPrice() {
        return mDiscountedPrice;
    }

    public void setDiscountedPrice(final String mDiscountedPrice) {
        this.mDiscountedPrice = mDiscountedPrice;
    }

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

    public int getStockLevel() {
        return mStockLevel;
    }

    public String getFormattedTotalPriceWithTax() {
        return mFormattedTotalPriceWithTax;
    }

    public void setFormattedTotalPriceWithTax(String mFormattedTotalPriceWithTax) {
        this.mFormattedTotalPriceWithTax = mFormattedTotalPriceWithTax;
    }

    public String getFormattedPrice() {
        return mFormattedPrice;
    }

    public void setFormattedPrice(String mFormattedPrice) {
        this.mFormattedPrice = mFormattedPrice;
    }

    public String getFormattedTotalPrice() {
        return mFormattedTotalPrice;
    }

    public void setFormattedTotalPrice(String mFormattedTotalPrice) {
        this.mFormattedTotalPrice = mFormattedTotalPrice;
    }

    public ReviewStatistics getReviewStatistics() {
        return mReviewStatistics;
    }

    public void setReviewStatistics(ReviewStatistics mReviewStatistics) {
        this.mReviewStatistics = mReviewStatistics;
    }
}