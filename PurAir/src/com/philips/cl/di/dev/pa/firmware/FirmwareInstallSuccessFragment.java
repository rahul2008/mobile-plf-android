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
import com.philips.cl.di.dev.pa.util.Fonts;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class FirmwareInstallSuccessFragment extends BaseFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.firmware_update_successful,
				container, false);
		Button startAppBtn = (Button) view.findViewById(R.id.btn_start_app);
		startAppBtn.setTypeface(Fonts.getGillsans(getActivity()));
		((FirmwareUpdateActivity) getActivity())
				.setActionBar(FragmentID.FIRMWARE_INSTALL_SUCCESS);
		FontTextView firmwareVersion = (FontTextView) view
				.findViewById(R.id.firmware_current_version);
		firmwareVersion.setText(getString(R.string.firmware_current_version)
				+ " "
				+ ((FirmwareUpdateActivity) getActivity()).getUpgradeVersion());

		startAppBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getActivity().finish();
			}
		});
		return view;
	}
}
