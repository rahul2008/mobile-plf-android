package com.philips.cl.di.dev.pa.ews;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.util.Fonts;

public class AlertDialogAutoNetworkSwitchOn extends DialogFragment {
	
	public static AlertDialogAutoNetworkSwitchOn newInstance() {
		
		AlertDialogAutoNetworkSwitchOn fragment = new AlertDialogAutoNetworkSwitchOn();
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		return inflater.inflate(R.layout.setup_alert_auto_network_switch_on, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle saveInstanceState) {
		super.onActivityCreated(saveInstanceState);
		
		Button cancelBtn = (Button) getView().findViewById(R.id.setup_alert_ans_cancel_btn);
		cancelBtn.setTypeface(Fonts.getCentraleSansLight(getActivity()));
		cancelBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		
		setCancelable(true);
	}
}
