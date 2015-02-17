package com.philips.cl.di.dev.pa.digitalcare.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.philips.cl.di.dev.pa.digitalcare.R;

public class ViewProductDetailsFragment extends DigitalCareBaseFragment {

	private static String TAG = ViewProductDetailsFragment.class
			.getSimpleName();

	// private float WIDTH, HEIGHT;

	/**
	 * PORTRAIT PHONE
	 */
	private RelativeLayout mSubContainer;
	private LinearLayout.LayoutParams mSubContainerParams = null;

	// mProductButtonsContainer,
	// mProductVideoAdsContainer;
	// private ImageView mProductImage;
	// private TextView mProductName, mProductVariant;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG, "onCreateView");
		View mView = inflater.inflate(R.layout.fragment_view_product,
				container, false);

		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Log.d(TAG, "onActivityCreated");
		super.onActivityCreated(savedInstanceState);
		mSubContainer = (android.widget.RelativeLayout) getActivity()
				.findViewById(R.id.container);

		mSubContainerParams = (android.widget.LinearLayout.LayoutParams) mSubContainer
				.getLayoutParams();
		// init();
		Configuration config = getResources().getConfiguration();
		setViewParams(config);
	}

	@Override
	public void onConfigurationChanged(Configuration config) {
		super.onConfigurationChanged(config);

		setViewParams(config);
	}

	private void setViewParams(Configuration config) {

		if (isTablet()) {

			if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
				mSubContainerParams.leftMargin = mSubContainerParams.rightMargin = mLeftRightMarginLand;
			} else {
				mSubContainerParams.leftMargin = mSubContainerParams.rightMargin = mLeftRightMarginLand;
			}
		}
		mSubContainer.setLayoutParams(mSubContainerParams);
	}

	private boolean isTablet() {
		return (this.getActivity().getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	// private void init() {
	// DisplayMetrics displayMetrics = context.getResources()
	// .getDisplayMetrics();
	// int width = displayMetrics.widthPixels;
	// int height = displayMetrics.heightPixels;
	// WIDTH = (float) width / 1000;
	// HEIGHT = (float) height / 1000;
	// Log.d(TAG, "Width & Height are : " + width + " & Height : " + height);
	// Log.d(TAG, "WIDTH & HEIGHT are : " + WIDTH + " & Height : " + HEIGHT);
	//
	// }

}
