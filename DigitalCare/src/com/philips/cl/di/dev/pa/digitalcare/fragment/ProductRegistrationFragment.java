package com.philips.cl.di.dev.pa.digitalcare.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.philips.cl.di.dev.pa.digitalcare.R;

/*
 *	ProductRegistrationFragment will help to register the particular 
 *	product based of category.
 * 
 * Author : Ritesh.jha@philips.com
 * 
 * Creation Date : 10 Dec 2014
 */
public class ProductRegistrationFragment extends BaseFragment {
	private LinearLayout mProdRegParentFirst = null;
	private LinearLayout mProdRegParentSecond = null;

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
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		LinearLayout.LayoutParams params = (LayoutParams) mProdRegParentFirst
				.getLayoutParams();
		params.leftMargin = params.rightMargin = (int) getActivity()
				.getResources().getDimension(R.dimen.activity_margin);
		mProdRegParentFirst.setLayoutParams(params);
		mProdRegParentSecond.setLayoutParams(params);
	}
}
