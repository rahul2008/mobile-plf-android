package com.philips.cl.di.dev.pa.digitalcare.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.philips.cl.di.dev.pa.digitalcare.R;

/*
 *	ContactUsFragment will help to provide options to contact Philips.
 * 
 * Author : Ritesh.jha@philips.com
 * 
 * Creation Date : 19 Jan 2014
 */
public class ContactUsFragment extends BaseFragment {
	 private LinearLayout mConactUsParent = null;
	// private LinearLayout mProdRegParentSecond = null;
	 private FrameLayout.LayoutParams mParams = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_contact_us, container,
				false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mConactUsParent = (LinearLayout) getActivity().findViewById(
		 R.id.contactUsParent);
		// mProdRegParentSecond = (LinearLayout) getActivity().findViewById(
		// R.id.prodRegParentSecond);
		//
		 mParams = (FrameLayout.LayoutParams) mConactUsParent.getLayoutParams();
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
		mConactUsParent.setLayoutParams(mParams);
//		mProdRegParentSecond.setLayoutParams(mParams);
	}
}
