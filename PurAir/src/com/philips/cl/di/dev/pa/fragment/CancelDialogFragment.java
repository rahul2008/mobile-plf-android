package com.philips.cl.di.dev.pa.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.cpp.PairingHandler;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager;
import com.philips.cl.di.dev.pa.util.Fonts;

public class CancelDialogFragment extends DialogFragment {
	
	public final int REMOTE = 1;
	private static ToggleButton remote;
	
	public static CancelDialogFragment newInstance(String msg, int state, ToggleButton remoteControlBtn) {
		CancelDialogFragment fragment =  new CancelDialogFragment();
		
		Bundle bundle = new Bundle();
		bundle.putString("msg", msg);
		bundle.putInt("state", state);
		fragment.setArguments(bundle);
		remote=remoteControlBtn;
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
		
		if (getArguments() == null) return;
		
		TextView tvHeader = (TextView) getView().findViewById(R.id.tv_cancel_wifi_setup_header);
		tvHeader.setTypeface(Fonts.getGillsansLight(getActivity()));
		
		((TextView) getView().findViewById(R.id.tv_cancel_wifi_setup_message)).setVisibility(View.GONE);
		
		Button cancelWifiYes = (Button) getView().findViewById(R.id.btn_cancel_wifi_yes);
		cancelWifiYes.setTypeface(Fonts.getGillsansLight(getActivity()));
		
		Button cancelWifiNo = (Button) getView().findViewById(R.id.btn_cancel_wifi_no);
		cancelWifiNo.setTypeface(Fonts.getGillsansLight(getActivity()));
		
		tvHeader.setText(getArguments().getString("msg"));
		
		final int index = getArguments().getInt("state", -1);
		
		cancelWifiYes.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switch (index) {
				case REMOTE:
					PairingHandler pm = new PairingHandler((MainActivity)getActivity(), PurifierManager.getInstance().getCurrentPurifier());
					pm.initializeRelationshipRemoval();
					((MainActivity) getActivity()).setVisibilityAirPortTaskProgress(View.VISIBLE);
					break;

				default:
					break;
				}
				dismiss();
			}
		});
		cancelWifiNo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
				remote.setChecked(true);
			}
		});
		setCancelable(false);
	}
}

