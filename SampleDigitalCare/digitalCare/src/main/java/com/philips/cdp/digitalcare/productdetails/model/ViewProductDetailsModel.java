package com.philips.cdp.digitalcare.productdetails.model;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;


import com.philips.cdp.prxclient.RequestManager;
import com.philips.cdp.prxclient.prxdatabuilder.ProductSummaryBuilder;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.prxclient.response.ResponseListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;


/**
 * Description : Class gets the data from the PRX Component & created the object of this class required for the "View Product Details UI Screen".
 * <p/>
 * Created by naveen@philips.com on 02-Nov-15.
 */
public class ViewProductDetailsModel {

    private static final String TAG = ViewProductDetailsModel.class.getSimpleName();

    private String mProductName = null;
    private String mCtnName = null;
    private String mProductImage = null;
    private String mManualLink = null;
    private String mProductInfoLink = null;
    private List<String> mVideoLinks = null;

    public String getProductName() {
        return mProductName;
    }

    public void setProductName(String mProductName) {
        this.mProductName = mProductName;
    }

    public String getCtnName() {
        return mCtnName;
    }

    public void setCtnName(String mCtnName) {
        this.mCtnName = mCtnName;
    }

    public String getProductImage() {
        return mProductImage;
    }

    public void setProductImage(String mProductImage) {
        this.mProductImage = mProductImage;
    }

    public String getManualLink() {
        return mManualLink;
    }

    public void setManualLink(String mManualLink) {
        this.mManualLink = mManualLink;
    }

    public String getProductInfoLink() {
        return mProductInfoLink;
    }

    public void setProductInfoLink(String mProductInfoLink) {
        this.mProductInfoLink = mProductInfoLink;
    }

    public List<String> getVideoLinks() {
        return mVideoLinks;
    }

    public void setVideoLinks(List<String> mVideoLinks) {
        this.mVideoLinks = mVideoLinks;
    }
}
