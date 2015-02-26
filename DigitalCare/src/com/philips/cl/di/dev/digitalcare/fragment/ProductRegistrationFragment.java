package com.philips.cl.di.dev.digitalcare.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.philips.cl.di.dev.digitalcare.R;

/*
 *	ProductRegistrationFragment will help to register the particular 
 *	product based of category.
 * 
 * Author : Ritesh.jha@philips.com
 * 
 * Creation Date : 10 Dec 2015
 */
public class ProductRegistrationFragment extends DigitalCareBaseFragment {
	private LinearLayout mProdRegParentFirst = null;
	private LinearLayout mProdRegParentSecond = null;
	private LinearLayout.LayoutParams mParams = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_product_registration,
				container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mProdRegParentFirst = (LinearLayout) getActivity().findViewById(
				R.id.prodRegParentFirst);
		mProdRegParentSecond = (LinearLayout) getActivity().findViewById(
				R.id.prodRegParentSecond);

		mParams = (LayoutParams) mProdRegParentFirst.getLayoutParams();
		Configuration config = getResources().getConfiguration();
		setViewParams(config);
	}

	@Override
	public void onConfigurationChanged(Configuration config) {
		super.onConfigurationChanged(config);
		setViewParams(config);
	}

	private void setViewParams(Configuration config) {
		if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
			mParams.leftMargin = mParams.rightMargin = mLeftRightMarginPort;
		} else {
			mParams.leftMargin = mParams.rightMargin = mLeftRightMarginLand;
		}
		mProdRegParentFirst.setLayoutParams(mParams);
		mProdRegParentSecond.setLayoutParams(mParams);
	}
}
