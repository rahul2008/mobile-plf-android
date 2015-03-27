package com.philips.cl.di.digitalcare.productregistration;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.philips.cl.di.digitalcare.DigitalCareBaseFragment;
import com.philips.cl.di.digitalcare.R;
import com.philips.cl.di.digitalcare.analytics.AnalyticsConstants;
import com.philips.cl.di.digitalcare.analytics.AnalyticsTracker;

/**
 *	ProductRegistrationFragment will help to register the particular 
 *	product based of category.
 * 
 * @author : Ritesh.jha@philips.com
 * 
 * @since: 10 Dec 2014
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
		
		AnalyticsTracker.trackPage(AnalyticsConstants.PAGE_PRODUCT_REGISTRATION);
	}

	@Override
	public void onConfigurationChanged(Configuration config) {
		super.onConfigurationChanged(config);
		setViewParams(config);
	}

	@Override
	public void setViewParams(Configuration config) {
		if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
			mParams.leftMargin = mParams.rightMargin = mLeftRightMarginPort;
		} else {
			mParams.leftMargin = mParams.rightMargin = mLeftRightMarginLand;
		}
		mProdRegParentFirst.setLayoutParams(mParams);
		mProdRegParentSecond.setLayoutParams(mParams);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}

	@Override
	public String getActionbarTitle() {
		return getResources()
				.getString(R.string.opt_register_my_product);
	}
}
