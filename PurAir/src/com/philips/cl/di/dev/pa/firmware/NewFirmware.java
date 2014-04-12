package com.philips.cl.di.dev.pa.firmware;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;

public class NewFirmware extends BaseFragment implements OnClickListener{
		
			
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.new_firmware, container, false);
		initViews(view);
		return view;
	}

	private void initViews(View view) {
		Button updateButton = (Button) view.findViewById(R.id.btn_firmware_update);
		updateButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.btn_firmware_update:
			FirmwareUpdateActivity.setCancelled(false);
			((FirmwareUpdateActivity) getActivity()).setDeviceDetailsLocally("version", ((FirmwareUpdateActivity) getActivity()).getUpgradeVersion());
			
			getFragmentManager()
			.beginTransaction()
			.replace(R.id.firmware_container, new FirmwareDownloadFragment(), "NewDownloadFirmware")
			.commit();
			break;
		
		default:
			break;
		
	 }
	}

}
