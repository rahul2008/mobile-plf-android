package com.philips.cl.di.dev.pa.ews;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.util.TrackPageConstants;

public class EWSNetworkChangeFragment extends EWSBaseFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.ews_step1_connect_2_your_network, null);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		MetricsTracker.trackPage(TrackPageConstants.EWS_NETWORK_CHANGE);
		((EWSActivity) getActivity())
				.setActionBarHeading(EWSConstant.EWS_STEP_CHANGE_NETWORK);
		
		ViewGroup scrollView = (ScrollView) getView().findViewById(R.id.scrollView);
		setBackground(scrollView, R.drawable.ews_nav_bar_2x, Color.BLACK, .1F);
	}

}
