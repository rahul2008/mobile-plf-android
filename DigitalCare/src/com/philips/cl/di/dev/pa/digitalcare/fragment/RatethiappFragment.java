package com.philips.cl.di.dev.pa.digitalcare.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.philips.cl.di.dev.pa.digitalcare.R;
import com.philips.cl.di.dev.pa.digitalcare.customview.FontButton;

public class RatethiappFragment extends BaseFragment {

	private static String TAG = RatethiappFragment.class.getSimpleName();
	private Button RatePlayStore, RatePhilips;
	private String mWeblink = "http://www.philips.co.uk/c-p/BT9280_33/beardtrimmer-series-9000-waterproof-beard-trimmer-with-worlds-first-laser-guide/reviewandawards";

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
		RatePlayStore = (FontButton) getActivity().findViewById(
				R.id.tellus_PlayStoreReviewButton);
		RatePhilips = (FontButton) getActivity().findViewById(
				R.id.tellus_PlayStoreReviewButton);
		RatePlayStore.setOnClickListener(ClickListener);
		RatePhilips.setOnClickListener(ClickListener);
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
	
	private void tempRatePhilipsTrimmer()
	{
		Toast.makeText(getActivity(), "Philips Products review button!!", Toast.LENGTH_SHORT).show();
	}

	private OnClickListener ClickListener = new OnClickListener() {

		public void onClick(View view) {
			int id = view.getId();
			switch (id) {
			case R.id.tellus_PhilipsReviewButton:
				tempRatePhilipsTrimmer();
				break;
			case R.id.tellus_PlayStoreReviewButton:
				rateTheAirFryerApplication();
				break;
			}
		};
	};
}
