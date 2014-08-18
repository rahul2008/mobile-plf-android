package com.philips.cl.di.dev.pa.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.util.Fonts;

public class GPSLocationDialogFragment extends DialogFragment {
	
	public static GPSLocationDialogFragment newInstance() {
		GPSLocationDialogFragment fragment = new GPSLocationDialogFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		View view = inflater.inflate(R.layout.cancel_wifi_setup, container, false);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle arg0) {
		super.onActivityCreated(arg0);
		if (getActivity() == null) {
			return;
		}
		
		TextView tvHeader = (TextView) getView().findViewById(R.id.tv_cancel_wifi_setup_header);
		tvHeader.setTypeface(Fonts.getGillsansLight(getActivity()));
		tvHeader.setText(getString(R.string.location_services_title));
		
		TextView tvMessage = (TextView) getView().findViewById(R.id.tv_cancel_wifi_setup_message);
		tvMessage.setTypeface(Fonts.getGillsansLight(getActivity()));
		tvMessage.setText(getString(R.string.location_services_turned_off_text));
		
		Button goToSetting = (Button) getView().findViewById(R.id.btn_cancel_wifi_no);
		goToSetting.setTypeface(Fonts.getGillsansLight(getActivity()));
		goToSetting.setText(getString(R.string.wifi_enable_button_txt));
		goToSetting.setBackgroundResource(R.drawable.blue_button_selector);
		
		Button cancel = (Button) getView().findViewById(R.id.btn_cancel_wifi_yes);
		cancel.setTypeface(Fonts.getGillsansLight(getActivity()));
		cancel.setText(getString(R.string.cancel));
		
		goToSetting.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            	startActivity(intent);
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		setCancelable(false);
	}
}
