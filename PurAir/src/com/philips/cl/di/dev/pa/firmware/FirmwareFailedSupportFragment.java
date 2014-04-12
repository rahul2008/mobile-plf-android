package com.philips.cl.di.dev.pa.firmware;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.firmware.FirmwareUpdateActivity;
import com.philips.cl.di.dev.pa.util.Fonts;

public class FirmwareFailedSupportFragment extends BaseFragment implements OnClickListener{
		
	private TextView tvFirmwareFailedUpd; 
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.firmware_failed_support, container, false);
		initViews(view);
		return view;
	}

	private void initViews(View view) {
		tvFirmwareFailedUpd = (TextView) view.findViewById(R.id.firmware_failed_update);
		FirmwareUpdateActivity activity=(FirmwareUpdateActivity) getActivity();
		tvFirmwareFailedUpd.setText(getString(R.string.firmware_failed_update, activity.getPurifierName())) ;
		Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
		btnCancel.setTypeface(Fonts.getGillsans(getActivity()));
		btnCancel.setOnClickListener(this);
		Button btnSupport = (Button) view.findViewById(R.id.btn_support);
		btnSupport.setTypeface(Fonts.getGillsans(getActivity()));
		btnSupport.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_cancel:
			((FirmwareUpdateActivity) getActivity()).setDownloadFailedCount(0);
			getActivity().finish();
			break;
		case R.id.btn_support:
			((FirmwareUpdateActivity) getActivity()).setDownloadFailedCount(0);
			getFragmentManager()
			.beginTransaction()
			.replace(R.id.firmware_container, new FirmwareContactSupportFragment(), "FirmwareContactSupportFragment")
			.commit();
			break;
		
		default:
			break;
		
	 }
	}
}
