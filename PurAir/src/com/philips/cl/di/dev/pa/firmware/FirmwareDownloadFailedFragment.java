package com.philips.cl.di.dev.pa.firmware;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.firmware.FirmwareUpdateActivity;

public class FirmwareDownloadFailedFragment extends BaseFragment implements OnClickListener{
		
	private TextView tvFirmwareUpdMsg; 
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.firmware_download_failed, container, false);
		initViews(view);
		return view;
	}

	private void initViews(View view) {
		tvFirmwareUpdMsg = (TextView) view.findViewById(R.id.firmware_update_msg);
		tvFirmwareUpdMsg.setText(getString(R.string.firmware_failed_msg, FirmwareUpdateActivity.getPurifierName())) ;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		default:
			break;
		
	 }
	}
}
