package com.philips.cl.di.digitalcare.productdetails;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.philips.cl.di.digitalcare.DigitalCareBaseFragment;
import com.philips.cl.di.digitalcare.R;
import com.philips.cl.di.digitalcare.analytics.AnalyticsConstants;
import com.philips.cl.di.digitalcare.analytics.AnalyticsTracker;
import com.philips.cl.di.digitalcare.customview.DigitalCareFontButton;
import com.philips.cl.di.digitalcare.util.DLog;

/**
 * ProductDetailsFragment will help to show product details.
 * 
 * @author : Ritesh.jha@philips.com
 * 
 * @since : 16 Jan 2015
 */
public class ProductDetailsFragment extends DigitalCareBaseFragment {

	private static String TAG = ProductDetailsFragment.class.getSimpleName();
	private DigitalCareFontButton mBtnManual = null;
	private DigitalCareFontButton mBtnDownload = null;
	private DigitalCareFontButton mBtnProductInfo = null;

	/**
	 * PORTRAIT PHONE
	 */
	private RelativeLayout mFirstContainer;
	private RelativeLayout mSecondContainer;
	private LinearLayout.LayoutParams mFirstContainerParams = null;
	private LinearLayout.LayoutParams mSecondContainerParams = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		DLog.d(TAG, "onCreateView");
		View mView = inflater.inflate(R.layout.fragment_view_product,
				container, false);

		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		DLog.d(TAG, "onActivityCreated");
		super.onActivityCreated(savedInstanceState);
		mFirstContainer = (RelativeLayout) getActivity().findViewById(
				R.id.toplayout);
		mSecondContainer = (RelativeLayout) getActivity().findViewById(
				R.id.prodbuttons);
		
		mBtnManual = (DigitalCareFontButton) getActivity().findViewById(
				R.id.manual_open);
		mBtnDownload = (DigitalCareFontButton) getActivity().findViewById(
				R.id.manual_download);
		mBtnProductInfo = (DigitalCareFontButton) getActivity().findViewById(
				R.id.prodinfo_button);
		mBtnManual.setTransformationMethod(null);
		mBtnDownload.setTransformationMethod(null);
		mBtnProductInfo.setTransformationMethod(null);

		mFirstContainerParams = (LinearLayout.LayoutParams) mFirstContainer
				.getLayoutParams();
		mSecondContainerParams = (LinearLayout.LayoutParams) mSecondContainer
				.getLayoutParams();
		// init();
		Configuration config = getResources().getConfiguration();
		setViewParams(config);

		AnalyticsTracker.trackPage(AnalyticsConstants.PAGE_PRODCUT_DETAILS);
	}

	@Override
	public void onConfigurationChanged(Configuration config) {
		super.onConfigurationChanged(config);

		setViewParams(config);
	}

	@Override
	public void setViewParams(Configuration config) {
		// if (isTablet()) {
		if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
			mFirstContainerParams.leftMargin = mFirstContainerParams.rightMargin = mLeftRightMarginPort;
			mSecondContainerParams.leftMargin = mSecondContainerParams.rightMargin = mLeftRightMarginPort;
		} else {
			mFirstContainerParams.leftMargin = mFirstContainerParams.rightMargin = mLeftRightMarginLand;
			mSecondContainerParams.leftMargin = mSecondContainerParams.rightMargin = mLeftRightMarginLand;
		}
		// }
		mFirstContainer.setLayoutParams(mFirstContainerParams);
		mSecondContainer.setLayoutParams(mSecondContainerParams);
	}

	// private boolean isTablet() {
	// return (this.getActivity().getResources().getConfiguration().screenLayout
	// & Configuration.SCREENLAYOUT_SIZE_MASK) >=
	// Configuration.SCREENLAYOUT_SIZE_LARGE;
	// }

	@Override
	public void onClick(View v) {

	}

	@Override
	public String getActionbarTitle() {
		return getResources().getString(R.string.product_info);
	}
}
