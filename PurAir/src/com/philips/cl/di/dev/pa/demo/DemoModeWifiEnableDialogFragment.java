package com.philips.cl.di.dev.pa.demo;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.Fonts;
import com.philips.cl.di.dev.pa.util.networkutils.NetworkReceiver;
import com.philips.cl.di.dev.pa.util.networkutils.NetworkStateListener;

public class DemoModeWifiEnableDialogFragment  extends DialogFragment implements NetworkStateListener{
	
	public static DemoModeWifiEnableDialogFragment newInstance() {
		
		DemoModeWifiEnableDialogFragment fragment = new DemoModeWifiEnableDialogFragment();
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		return inflater.inflate(R.layout.wifi_enable_dialog, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle saveInstanceState) {
		super.onActivityCreated(saveInstanceState);
		
		ImageView support = (ImageView) getView().findViewById(R.id.wifi_enable_dialog_support);
		support.setOnClickListener(wifiClickListener);
		
		ImageView close = (ImageView) getView().findViewById(R.id.wifi_enable_dialog_close);
		close.setOnClickListener(wifiClickListener);
		
		Button gotoSetting = (Button) getView().findViewById(R.id.wifi_enable_go_2_setting);
		gotoSetting.setTypeface(Fonts.getGillsansLight(getActivity()));
		gotoSetting.setOnClickListener(wifiClickListener); 
		
		setCancelable(false);
	}
	
	private  OnClickListener wifiClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.wifi_enable_go_2_setting:
				Intent intent = new Intent(Settings.ACTION_SETTINGS);
				((DemoModeActivity) getActivity()).startActivityForResult(intent, DemoModeConstant.REQUEST_CODE);
				break;
			case R.id.wifi_enable_dialog_support:
				((DemoModeActivity) getActivity()).showSupportScreen();
				break;
			default:
				break;
			}
			dismiss();
		}
	};
	
	public void onResume() {
		super.onResume();
		NetworkReceiver.getInstance().addNetworkStateListener(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		NetworkReceiver.getInstance().removeNetworkStateListener(this);
	}

	@Override
	public void onConnected() {
		//Wifi connected dismiss dialog
		if (getActivity() == null) return;
		ConnectivityManager connManager = 
				(ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		if (networkInfo != null && networkInfo.isConnected()) {
			try {
				dismiss();
			} catch (Exception e) {
				ALog.e(ALog.DEMO_MODE, "Failed to dissmiss wifi enable dialog " + e.getMessage());
			}
		}
		
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
	}

}
