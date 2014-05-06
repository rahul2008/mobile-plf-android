package com.philips.cl.di.dev.pa.scheduler;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.firmware.FirmwareConstants.FragmentID;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;

public class SchedulerFragment extends BaseFragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.scheduler_container, container, false);
		initViews(view);
		((SchedulerActivity) getActivity()).setActionBar(FragmentID.FIRMWARE_INSTALLED);
		return view;
	}

	private void initViews(View view) {
	}
}

