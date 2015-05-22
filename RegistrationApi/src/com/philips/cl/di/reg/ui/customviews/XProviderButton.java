/*----------------------------------------------------------------------------
Copyright(c) Philips Electronics India Ltd
All rights reserved. Reproduction in whole or in part is prohibited without
the written consent of the copyright holder.
Project           : Registration
Description       : Custom view for text loading different type face
----------------------------------------------------------------------------*/

package com.philips.cl.di.reg.ui.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.cl.di.reg.R;

public class XProviderButton extends RelativeLayout {

	private static final String XMLNS = "http://reg.lib/schema";

	private Context mContext;

	private int mProviderNameStringID;

	private int mProviderLogoID;

	private int mProviderBackgroundID;

	private int mProviderTextColorID;

	private ProgressBar mPbSpinner;

	public XProviderButton(Context context) {
		super(context);
		mContext = context;
		initUi(R.layout.x_provider_btn);
	}

	public XProviderButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;

		mProviderNameStringID = attrs.getAttributeResourceValue(XProviderButton.XMLNS,
		        "providerName", -1);

		mProviderLogoID = attrs.getAttributeResourceValue(XProviderButton.XMLNS, "providerLogo", 0);
		mProviderBackgroundID = attrs.getAttributeResourceValue(XProviderButton.XMLNS,
		        "providerBackground", 0);
		mProviderTextColorID = attrs.getAttributeResourceValue(XProviderButton.XMLNS,
		        "providerTextColor", 0);
		initUi(R.layout.x_provider_btn);
	}

	private void initUi(int resourceId) {

		/** inflate amount layout */
		LayoutInflater li = LayoutInflater.from(mContext);
		li.inflate(resourceId, this, true);

		FrameLayout flProvider = (FrameLayout) findViewById(R.id.fl_reg_provider_bg);
		ImageView ivProviderLogo = (ImageView) findViewById(R.id.iv_reg_provider_logo);
		TextView tvProvider = (TextView) findViewById(R.id.tv_reg_provider_name);
		mPbSpinner = (ProgressBar) findViewById(R.id.pb_reg_spinner);
		if (mProviderNameStringID != -1) {
			tvProvider.setText(mContext.getResources().getString(mProviderNameStringID));
		}

		if (mProviderLogoID != -1) {
			ivProviderLogo.setImageResource(mProviderLogoID);
		}

		if (mProviderBackgroundID != -1) {
			flProvider.setBackgroundResource(mProviderBackgroundID);
		}

		if (mProviderTextColorID != -1) {
			tvProvider.setTextColor(mContext.getResources().getColor(mProviderTextColorID));
		}
	}

	public void showProgressBar() {
		mPbSpinner.setVisibility(VISIBLE);
	}

	public void hideProgressBar() {
		mPbSpinner.setVisibility(INVISIBLE);
	}

}
