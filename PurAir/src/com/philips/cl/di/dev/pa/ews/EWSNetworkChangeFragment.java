package com.philips.cl.di.dev.pa.ews;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.util.TrackPageConstants;

public class EWSNetworkChangeFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.ews_connect_2_your_network, null);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		MetricsTracker.trackPage(TrackPageConstants.EWS_NETWORK_CHANGE);
		((EWSActivity) getActivity())
				.setActionBarHeading(EWSConstant.EWS_STEP_CHANGE_NETWORK);
	}

}
