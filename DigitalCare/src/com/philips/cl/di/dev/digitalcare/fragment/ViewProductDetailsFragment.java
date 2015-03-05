package com.philips.cl.di.dev.digitalcare.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.philips.cl.di.dev.digitalcare.R;

public class ViewProductDetailsFragment extends DigitalCareBaseFragment {

	private static String TAG = ViewProductDetailsFragment.class
			.getSimpleName();

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
		Log.d(TAG, "onCreateView");
		View mView = inflater.inflate(R.layout.fragment_view_product,
				container, false);

		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Log.d(TAG, "onActivityCreated");
		super.onActivityCreated(savedInstanceState);
		mFirstContainer = (RelativeLayout) getActivity().findViewById(
				R.id.toplayout);
		mSecondContainer = (RelativeLayout) getActivity().findViewById(
				R.id.prodbuttons);

		mFirstContainerParams = (LinearLayout.LayoutParams) mFirstContainer
				.getLayoutParams();
		mSecondContainerParams = (LinearLayout.LayoutParams) mSecondContainer
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
				mFirstContainerParams.leftMargin = mFirstContainerParams.rightMargin = mLeftRightMarginPort;
				mSecondContainerParams.leftMargin = mSecondContainerParams.rightMargin = mLeftRightMarginPort;
			} else {
				mFirstContainerParams.leftMargin = mFirstContainerParams.rightMargin = mLeftRightMarginLand;
				mSecondContainerParams.leftMargin = mSecondContainerParams.rightMargin = mLeftRightMarginLand;
			}
		}
		mFirstContainer.setLayoutParams(mFirstContainerParams);
		mSecondContainer.setLayoutParams(mSecondContainerParams);
	}

	private boolean isTablet() {
		return (this.getActivity().getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}
}
