package com.philips.cl.di.dev.pa.ews;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class EWSErrorSSIDFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.ews_error_ssid, null);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		((EWSActivity) getActivity())
				.setActionBarHeading(EWSConstant.EWS_STEP_ERROR_SSID);

		String ssid = ((EWSActivity) getActivity()).getNetworkSSID();
		((FontTextView) getView().findViewById(R.id.ews_error_ssid_network))
				.setText(ssid);
	}
}
