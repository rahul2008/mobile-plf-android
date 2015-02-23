package com.philips.cl.di.dev.pa.ews;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.util.Fonts;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.util.TrackPageConstants;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class EWSStepOneFragment extends Fragment {
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.ews_step1, null);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		MetricsTracker.trackPage(TrackPageConstants.EWS_CONFIRM_SSID);
		((EWSActivity) getActivity()).setActionBarHeading(EWSConstant.EWS_STEP_ONE);
		
		Button noBtn = (Button) getView().findViewById(R.id.ews_step1_no_btn);
		noBtn.setTypeface(Fonts.getCentraleSansLight(getActivity()));
		noBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((EWSActivity) getActivity()).changeWifiNetwork();
			}
		});
		
		Button yesBtn = (Button) getView().findViewById(R.id.ews_step1_yes_btn);
		yesBtn.setTypeface(Fonts.getCentraleSansLight(getActivity()));
		yesBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((EWSActivity) getActivity()).showStepTwoPowerOnPurifier();
			}
		});
		
		String ssid = ((EWSActivity) getActivity()).getNetworkSSID();
		
		((FontTextView) getView().findViewById(R.id.ews_step1_wifi_network)).setText(ssid);
	}
	
}
