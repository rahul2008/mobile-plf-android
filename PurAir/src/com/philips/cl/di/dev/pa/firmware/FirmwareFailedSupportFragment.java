package com.philips.cl.di.dev.pa.firmware;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;

public class FirmwareFailedSupportFragment extends BaseFragment implements OnClickListener{
		
			
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.firmware_failed_support, container, false);
		initViews(view);
		return view;
	}

	private void initViews(View view) {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		default:
			break;
		
	 }
	}
}
