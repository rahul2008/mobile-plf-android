package com.philips.cl.di.dev.pa.ews;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.util.Fonts;
import com.philips.cl.di.dev.pa.util.MetricsTracker;

public class EWSStartFragment extends EWSBaseFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.setup_intro_screen, null);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		MetricsTracker.trackActionEWSStart();
		((EWSActivity) getActivity()).setActionBarHeading(EWSConstant.EWS_STEP_START);

		Button startEwsBtn = (Button) getView().findViewById(
				R.id.setup_get_start_btn);
		startEwsBtn.setTypeface(Fonts.getCentraleSansLight(getActivity()));
		startEwsBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (getActivity() == null) return;
				((EWSActivity) getActivity()).checkWifiConnectivity();
			}
		});
		
		ViewGroup scrollView = (ScrollView) getView().findViewById(R.id.scrollView);
		setBackground(scrollView, R.drawable.ews_nav_bar_2x, Color.BLACK, .1F);
	}

}
