package com.philips.cl.di.dev.pa.ews;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.util.Fonts;

public class EWSCancelDialogFragment extends DialogFragment {
	
	public static EWSCancelDialogFragment newInstance() {
		EWSCancelDialogFragment fragment =  new EWSCancelDialogFragment();
		
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
		TextView tvMessage = (TextView) getView().findViewById(R.id.tv_cancel_wifi_setup_message);
		tvMessage.setTypeface(Fonts.getGillsansLight(getActivity()));
		Button cancelWifiYes = (Button) getView().findViewById(R.id.btn_cancel_wifi_yes);
		cancelWifiYes.setTypeface(Fonts.getGillsansLight(getActivity()));
		
		Button cancelWifiNo = (Button) getView().findViewById(R.id.btn_cancel_wifi_no);
		cancelWifiNo.setTypeface(Fonts.getGillsansLight(getActivity()));
		cancelWifiYes.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getActivity().finish();
			}
		});
		cancelWifiNo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		setCancelable(false);
		
	}
	
}

