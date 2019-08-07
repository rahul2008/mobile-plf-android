/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.screens;

import android.os.Bundle;
import android.text.TextUtils;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.utils.IAPConstant;


public class WebPrivacy extends WebFragment {
    public static final String TAG = WebPrivacy.class.getName();
    private String mUrl, termsUrl, faqUrl;

    @Override
    protected boolean isJavaScriptEnable() {
        return true;
    }

    @Override
    protected String getWebUrl() {
        termsUrl = getArguments().getString(IAPConstant.IAP_TERMS);
        faqUrl = getArguments().getString(IAPConstant.IAP_FAQ);
        if(!TextUtils.isEmpty(termsUrl)){
            mUrl = getArguments().getString(IAPConstant.IAP_TERMS_URL);
        } else if(!TextUtils.isEmpty(faqUrl)){
            mUrl = getArguments().getString(IAPConstant.IAP_FAQ_URL);
        } else {
            mUrl = getArguments().getString(IAPConstant.IAP_PRIVACY_URL);
        }
        return mUrl;
    }

    public static WebPrivacy createInstance(Bundle args, AnimationType animType) {
        WebPrivacy fragment = new WebPrivacy();
        args.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!TextUtils.isEmpty(termsUrl)){
            setTitleAndBackButtonVisibility(R.string.iap_terms_conditions,true);
        } else if(!TextUtils.isEmpty(faqUrl)){
            setTitleAndBackButtonVisibility(R.string.iap_faq,true);
        } else {
            setTitleAndBackButtonVisibility(R.string.iap_privacy,true);
        }
    }
}
