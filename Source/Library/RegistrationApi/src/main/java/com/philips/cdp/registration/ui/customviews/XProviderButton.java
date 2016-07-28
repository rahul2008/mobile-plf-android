/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.customviews;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.ui.utils.FontLoader;
import com.philips.cdp.registration.ui.utils.RegConstants;

public class XProviderButton extends RelativeLayout {

    private static final String XMLNS = "http://reg.lib/schema";


    private Context mContext;

    private int mProviderNameStringID = -1;

    private int mProviderBackgroundID = -1;

    private int mProviderTextColorID = -1;

    private ProgressBar mPbSpinner;

    private FrameLayout mFlProvider;

    private TextView mIvProviderLogo;

    private TextView mTvProvider;

    public XProviderButton(Context context) {
        super(context);
        mContext = context;
        initUi(R.layout.reg_provider_btn);
    }

    public XProviderButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;

        mProviderNameStringID = attrs.getAttributeResourceValue(XProviderButton.XMLNS,
                "providerName", -1);

        mProviderBackgroundID = attrs.getAttributeResourceValue(XProviderButton.XMLNS,
                "providerBackground", 0);
        mProviderTextColorID = attrs.getAttributeResourceValue(XProviderButton.XMLNS,
                "providerTextColor", 0);
        initUi(R.layout.reg_provider_btn);
    }

    private void initUi(int resourceId) {

        /** inflate amount layout */
        LayoutInflater li = LayoutInflater.from(mContext);
        li.inflate(resourceId, this, true);

        mFlProvider = (FrameLayout) findViewById(R.id.fl_reg_provider_bg);
        mIvProviderLogo = (TextView) findViewById(R.id.iv_reg_provider_logo);
        FontLoader.getInstance().setTypeface(mIvProviderLogo, RegConstants.PUIICON_TTF);
        mTvProvider = (TextView) findViewById(R.id.tv_reg_provider_name);
        mPbSpinner = (ProgressBar) findViewById(R.id.pb_reg_spinner);
        if (mProviderNameStringID != -1) {
            mTvProvider.setText(mContext.getResources().getString(mProviderNameStringID));
        }

        if (mProviderBackgroundID != -1) {
            mFlProvider.setBackgroundResource(mProviderBackgroundID);
        }

        if (mProviderTextColorID != -1) mTvProvider.setTextColor(
                ContextCompat.getColor(mContext, mProviderTextColorID));
    }

    public void showProgressBar() {
        mPbSpinner.setVisibility(VISIBLE);
    }

    public void hideProgressBar() {
        mPbSpinner.setVisibility(INVISIBLE);
    }

    public void setProviderBackgroundID(int providerBackgroundID) {
        mFlProvider.setBackgroundResource(providerBackgroundID);
    }

    public void setProviderName(int stringId) {
        mTvProvider.setText(mContext.getResources().getString(stringId));
    }

    public void setProviderLogoID(int providerLogoID) {
        mIvProviderLogo.setText(providerLogoID);
    }

    public void setProviderTextColor(int providerTextColorID) {
        mTvProvider.setTextColor(ContextCompat.getColor(mContext,providerTextColorID));
    }
}
