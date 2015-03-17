package com.philips.cl.di.digitalcare.rateandreview;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.philips.cl.di.digitalcare.DigitalCareBaseFragment;
import com.philips.cl.di.digitalcare.R;
import com.philips.cl.di.digitalcare.customview.DigitalCareFontButton;

public class RateThisAppFragment extends DigitalCareBaseFragment {

	private static String TAG = RateThisAppFragment.class.getSimpleName();
	private Button RatePlayStore, RatePhilips;
	private String mWeblink = "http://www.philips.co.uk/c-p/BT9280_33/beardtrimmer-series-9000-waterproof-beard-trimmer-with-worlds-first-laser-guide/reviewandawards";
	private FrameLayout.LayoutParams mLayoutParams = null;
	private LinearLayout mLayoutParent = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG, "onCreateView");
		View mView = inflater.inflate(R.layout.fragment_tellus, container,
				false);

		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Log.d(TAG, "onActivityCreated");
		super.onActivityCreated(savedInstanceState);
		RatePlayStore = (DigitalCareFontButton) getActivity().findViewById(
				R.id.tellus_PlayStoreReviewButton);
		RatePhilips = (DigitalCareFontButton) getActivity().findViewById(
				R.id.tellus_PhilipsReviewButton);
		mLayoutParent = (LinearLayout) getActivity().findViewById(
				R.id.parentLayout);
		RatePlayStore.setOnClickListener(this);
		RatePhilips.setOnClickListener(this);

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

	private void rateTheAirFryerApplication() {
		Uri uri = Uri.parse("market://details?id="
				+ "com.philips.cl.di.kitchenappliances.airfryer");
		Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
		try {
			startActivity(goToMarket);
		} catch (ActivityNotFoundException e) {
			startActivity(new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://play.google.com/store/apps/details?id="
							+ "com.philips.cl.di.kitchenappliances.airfryer")));
		}
	}

	private void tempRatePhilipsTrimmer() {
		String url = mWeblink;
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		startActivity(i);
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if (id == R.id.tellus_PhilipsReviewButton) {
			tempRatePhilipsTrimmer();
		} else if (id == R.id.tellus_PlayStoreReviewButton) {
			rateTheAirFryerApplication();
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
		return getResources()
				.getString(R.string.opt_what_you_think);
	}
}
