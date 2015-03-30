package com.philips.cl.di.digitalcare.rateandreview;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.philips.cl.di.digitalcare.DigitalCareBaseFragment;
import com.philips.cl.di.digitalcare.DigitalCareConfigManager;
import com.philips.cl.di.digitalcare.R;
import com.philips.cl.di.digitalcare.analytics.AnalyticsConstants;
import com.philips.cl.di.digitalcare.analytics.AnalyticsTracker;
import com.philips.cl.di.digitalcare.customview.DigitalCareFontButton;
import com.philips.cl.di.digitalcare.util.DLog;

/**
 * RateThisAppFragment class is used to rate the app and review the product.
 * 
 * @author: naveen@philips.com
 * @since: Jan 11, 2015
 */
public class RateThisAppFragment extends DigitalCareBaseFragment {

	private static String TAG = RateThisAppFragment.class.getSimpleName();
	private Button mRatePlayStoreBtn = null;;
	private Button mRatePhilipsBtn = null;
	private LinearLayout mLayoutParent = null;
	private final String APPRATER_PLAYSTORE_BROWSER_BASEURL = "http://play.google.com/store/apps/details?id=";
	private final String APPRATER_PLAYSTORE_APP_BASEURL = "market://details?id=";
	private FrameLayout.LayoutParams mLayoutParams = null;
	private Uri mStoreUri = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		DLog.d(TAG, "onCreateView");
		View mView = inflater.inflate(R.layout.fragment_tellus, container,
				false);
		mStoreUri = Uri.parse(APPRATER_PLAYSTORE_BROWSER_BASEURL
				+ DigitalCareConfigManager.getAppPackageName());
		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		DLog.d(TAG, "onActivityCreated");
		super.onActivityCreated(savedInstanceState);

		AnalyticsTracker.trackAction(AnalyticsConstants.ACTION_KEY_EXIT_LINK,
				AnalyticsConstants.MAP_KEY_EXIT_LINK, mStoreUri.toString());

		mRatePlayStoreBtn = (DigitalCareFontButton) getActivity().findViewById(
				R.id.tellus_PlayStoreReviewButton);
		mRatePhilipsBtn = (DigitalCareFontButton) getActivity().findViewById(
				R.id.tellus_PhilipsReviewButton);
		mLayoutParent = (LinearLayout) getActivity().findViewById(
				R.id.parentLayout);
		mRatePlayStoreBtn.setOnClickListener(this);
		mRatePhilipsBtn.setOnClickListener(this);

		mLayoutParams = (FrameLayout.LayoutParams) mLayoutParent
				.getLayoutParams();
		Configuration config = getResources().getConfiguration();
		setViewParams(config);
	}

	@Override
	public void onConfigurationChanged(Configuration config) {
		super.onConfigurationChanged(config);

		setViewParams(config);
	}

	private void rateThisApp() {
		Uri uri = Uri.parse(APPRATER_PLAYSTORE_APP_BASEURL
				+ DigitalCareConfigManager.getAppPackageName());
		Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
		try {
			startActivity(goToMarket);
		} catch (ActivityNotFoundException e) {
			startActivity(new Intent(Intent.ACTION_VIEW, mStoreUri));
		}
	}

	private void rateProductReview() {
		// TODO: We need to integrate BazaarVocie SDK. Below implementation is
		// temprory.
		String url = "http://www.philips.co.uk/c-p/BT9280_33/beardtrimmer-series-9000-waterproof-beard-trimmer-with-worlds-first-laser-guide/reviewandawards";
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		startActivity(i);
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if (id == R.id.tellus_PhilipsReviewButton) {
			rateProductReview();
		} else if (id == R.id.tellus_PlayStoreReviewButton) {
			rateThisApp();
		}
	};

	@Override
	public void setViewParams(Configuration config) {
		if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
			mLayoutParams.leftMargin = mLayoutParams.rightMargin = mLeftRightMarginPort;
		} else {
			mLayoutParams.leftMargin = mLayoutParams.rightMargin = mLeftRightMarginLand;
		}
		mLayoutParent.setLayoutParams(mLayoutParams);
	}

	@Override
	public String getActionbarTitle() {
		return getResources().getString(R.string.opt_what_you_think);
	}
}
