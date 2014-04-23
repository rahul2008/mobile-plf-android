package com.philips.cl.di.dev.pa.firmware;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.firmware.FirmwareConstants.FragmentID;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class FirmwareUpdateFragment extends BaseFragment implements OnClickListener{

	private FontTextView firmwareVersion;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.update_firmware, container, false);
		initViews(view);
		((FirmwareUpdateActivity) getActivity()).setActionBar(FragmentID.FIRMWARE_UPDATE);
		return view;
	}

	private void initViews(View view) {
		Button updateButton = (Button) view.findViewById(R.id.btn_firmware_update);
		updateButton.setOnClickListener(this);
		firmwareVersion = (FontTextView) view.findViewById(R.id.firmware_version);
		ALog.i(ALog.FIRMWARE, "NFUFrag$init((FirmwareUpdateActivity) getActivity()).getCurrentVersion() " + ((FirmwareUpdateActivity) getActivity()).getCurrentVersion());
		firmwareVersion.setText(((FirmwareUpdateActivity) getActivity()).getCurrentVersion());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_firmware_update:
			FirmwareUpdateActivity.setCancelled(false);
			((FirmwareUpdateActivity) getActivity()).setDeviceDetailsLocally(FirmwareConstants.VERSION, ((FirmwareUpdateActivity) getActivity()).getUpgradeVersion());

			getFragmentManager()
			.beginTransaction()
			.replace(R.id.firmware_container, new FirmwareDownloadFragment(), FirmwareDownloadFragment.class.getSimpleName())
			.commit();
			break;

		default:
			break;
		}
	}
}
