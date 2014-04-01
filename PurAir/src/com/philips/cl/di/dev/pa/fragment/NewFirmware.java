package com.philips.cl.di.dev.pa.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.activity.TermsAndConditionsActivity;
import com.philips.cl.di.dev.pa.util.NetworkUtils;

public class NewFirmware extends BaseFragment implements OnClickListener{
		
			
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.new_firmware, container, false);
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
