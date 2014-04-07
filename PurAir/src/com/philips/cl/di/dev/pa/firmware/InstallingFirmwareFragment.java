package com.philips.cl.di.dev.pa.firmware;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;

public class InstallingFirmwareFragment extends BaseFragment implements OnClickListener{
		
	private TextView tvInstallingFirmware; 
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.installing_firmware, container, false);
		initViews(view);
		return view;
	}

	private void initViews(View view) {
		tvInstallingFirmware = (TextView) view.findViewById(R.id.installing_firmware_for_purifier_msg);
		tvInstallingFirmware.setText(getString(R.string.installing_firmware_for_purifier_msg, FirmwareUpdateActivity.getPurifierName())) ;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		default:
			break;
		
	 }
	}
}
