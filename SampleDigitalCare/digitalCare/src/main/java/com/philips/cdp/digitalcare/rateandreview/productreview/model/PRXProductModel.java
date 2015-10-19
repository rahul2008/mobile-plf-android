package com.philips.cdp.digitalcare.rateandreview.productreview.model;

/**
 * @author naveen@philips.com
 */
public class PRXProductModel {

    private String mReviewPageUrl = null;
    private String mProductImageUrl = null;
    private String mProductName = null;
    private String mProductCtn = null;

    public String getmReviewPageUrl() {
        return mReviewPageUrl;
    }

    public void setmReviewPageUrl(String mReviewPageUrl) {
        this.mReviewPageUrl = mReviewPageUrl;
    }

    public String getmProductImageUrl() {
        return mProductImageUrl;
    }

    public void setmProductImageUrl(String mProductImageUrl) {
        this.mProductImageUrl = mProductImageUrl;
    }

    public String getmProductName() {
        return mProductName;
    }

    public void setmProductName(String mProductName) {
        this.mProductName = mProductName;
    }

    public String getmProductCtn() {
        return mProductCtn;
    }

    public void setmProductCtn(String mProductCtn) {
        this.mProductCtn = mProductCtn;
    }
}
