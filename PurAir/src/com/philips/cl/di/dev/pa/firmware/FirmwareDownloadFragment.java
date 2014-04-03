package com.philips.cl.di.dev.pa.firmware;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;

public class FirmwareDownloadFragment extends BaseFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.downloading_firmware, null);
		ProgressBar bar = (ProgressBar) view.findViewById(R.id.downloading_progressbar);
		bar.incrementProgressBy(50);
		bar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getFragmentManager()
				.beginTransaction()
				.replace(R.id.firmware_container, new FirmwareInstallFragment(), "NewDownloadFirmware")
				.commit();
			}
		});
		return view;
	}
}
