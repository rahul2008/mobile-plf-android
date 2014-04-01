package com.philips.cl.di.dev.pa.pureairui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cl.di.dev.pa.R;

public class HomeOutDoorFragment extends BaseFragment {
	
	private View vMain;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		vMain = inflater.inflate(R.layout.ll_home_outdoor_container, container, false);
		return vMain;
	}
}
