package com.philips.cl.di.dev.pa.firmware;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;

public class FirmwareInstallFragment extends BaseFragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.installing_firmware, null);
		ProgressBar bar = (ProgressBar) view.findViewById(R.id.progress_firmware_install);
		bar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getFragmentManager()
				.beginTransaction()
				.replace(R.id.firmware_container, new FirmwareInstallSuccessFragment(), "NewDownloadFirmware")
				.commit();
			}
		});
		return view;
	}
}
