/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.Fragments;

import android.os.Bundle;
import android.view.View;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.uikit.customviews.CircularLineProgressBar;


public class WebBuyFromRetailers extends WebFragment {
    private CircularLineProgressBar mProgress;
    private boolean mShowProgressBar = true;
    public static final String TAG = WebBuyFromRetailers.class.getName();

    @Override
    protected String getWebUrl() {
        Bundle bundle = getArguments();
        return bundle.getString(IAPConstant.IAP_BUY_URL);
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle(getArguments().getString(IAPConstant.IAP_STORE_NAME));
    }

    public static WebBuyFromRetailers createInstance(Bundle args, AnimationType animType) {
        WebBuyFromRetailers fragment = new WebBuyFromRetailers();
        args.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal());
        fragment.setArguments(args);
        return fragment;
    }
}