package com.philips.cl.di.dev.pa.firmware;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.firmware.FirmwareConstants.FragmentID;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class FirmwareInstalledFragment extends BaseFragment {

	private FontTextView firmwareVersion;
	//	private FontTextView firmwareReleaseDate; //Need to get release date
	private Button btnInstalled;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.new_firmware_update, container, false);
		initViews(view);
		((FirmwareUpdateActivity) getActivity()).setActionBar(FragmentID.FIRMWARE_INSTALLED);
		return view;
	}

	private void initViews(View view) {
		firmwareVersion = (FontTextView) view.findViewById(R.id.firmware_version);
		ALog.i(ALog.FIRMWARE, "NFUFrag$init((FirmwareUpdateActivity) getActivity()).getCurrentVersion() " + ((FirmwareUpdateActivity) getActivity()).getCurrentVersion());
		firmwareVersion.setText(((FirmwareUpdateActivity) getActivity()).getCurrentVersion());
		//		firmwareReleaseDate = (FontTextView) view.findViewById(R.id.firmware_releasedate);
		btnInstalled = (Button) view.findViewById(R.id.btn_installed);
		btnInstalled.setText(getString(R.string.installed));
		btnInstalled.setBackgroundResource(R.drawable.button_bg_gray);
		btnInstalled.setClickable(false);
	}
}
