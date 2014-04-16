package com.philips.cl.di.dev.pa.firmware;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.firmware.FirmwareConstants.FragmentID;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.util.Fonts;

public class FirmwareDownloadFailedFragment extends BaseFragment implements OnClickListener{

	private TextView tvFirmwareUpdMsg; 

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.firmware_download_failed, container, false);
		initViews(view);
		((FirmwareUpdateActivity) getActivity()).setActionBar(FragmentID.FIRMWARE_DOWNLOAD_FAILED);
		return view;
	}

	private void initViews(View view) {
		tvFirmwareUpdMsg = (TextView) view.findViewById(R.id.firmware_update_msg);		
		FirmwareUpdateActivity activity=(FirmwareUpdateActivity) getActivity();
		tvFirmwareUpdMsg.setText(getString(R.string.firmware_failed_msg, activity.getPurifierName())) ;
		Button btnCancelFirmwareFailed = (Button) view.findViewById(R.id.btn_cancel_firmware_failed);
		btnCancelFirmwareFailed.setTypeface(Fonts.getGillsans(getActivity()));
		btnCancelFirmwareFailed.setOnClickListener(this);
		Button btnTryAgainFirmwareFailed = (Button) view.findViewById(R.id.btn_try_again_firmware_failed);
		btnTryAgainFirmwareFailed.setTypeface(Fonts.getGillsans(getActivity()));
		btnTryAgainFirmwareFailed.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_cancel_firmware_failed:
			getActivity().finish();
			break;
		case R.id.btn_try_again_firmware_failed:
			FirmwareDownloadFragment.setCounter(0);
			FirmwareUpdateActivity.setCancelled(false);
			((FirmwareUpdateActivity) getActivity()).setDeviceDetailsLocally(FirmwareConstants.VERSION, ((FirmwareUpdateActivity) getActivity()).getUpgradeVersion());

			getFragmentManager()
			.beginTransaction()
			.replace(R.id.firmware_container, new FirmwareDownloadFragment(), FirmwareConstants.FIRMWARE_DOWNLOAD_FRAGMENT)
			.commit();
			break;

		default:
			break;
		}
	}
}
