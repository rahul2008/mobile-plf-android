package com.philips.cl.di.dev.pa.pureairui.fragments;

import com.philips.cl.di.dev.pa.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BuyOnlineFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.buy_online_fragment, container, false);
		return view;
	}
}
