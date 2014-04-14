package com.philips.cl.di.dev.pa.firmware;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;


public class DownloadingFirmwareFragment extends BaseFragment implements OnClickListener{
	
	private TextView tvDownloadingFirmware; 
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.downloading_firmware, container, false);
		initViews(view);
		
		return view;
	}

	private void initViews(View view) {
		tvDownloadingFirmware = (TextView) view.findViewById(R.id.downloading_firmware_for_purifier_msg);
		FirmwareUpdateActivity activity=(FirmwareUpdateActivity) getActivity();
		tvDownloadingFirmware.setText(getString(R.string.downloading_firmware_for_purifier_msg, activity.getPurifierName())) ;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		default:
			break;
		
	 }
	}
}
