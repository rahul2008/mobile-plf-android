package com.philips.cl.di.dev.pa.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.AirQualityExplainedStaticActivity;
import com.philips.cl.di.dev.pa.activity.IndoorAirColorIndicationActivity;
import com.philips.cl.di.dev.pa.activity.OutdoorAirColorIndicationActivity;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.util.TrackPageConstants;

public class AirQualityFragment extends BaseFragment implements OnClickListener {
	public static final int INDOOR_POLLUTANT_SCREEN = 11;
	public static final int VITASHIELD_SCREEN = 12;
	public static final int GUARD_EVNVIRONMENT_SCREEN = 13;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.air_quality_fragment, container,
				false);
		initializeView(view);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		MetricsTracker.trackPage(TrackPageConstants.AIR_QUALITY_EXPLAINED);
	}
	
	private void initializeView(View view) {
		TextView lblOutdoorColorExplained = (TextView) view.findViewById(R.id.lbl_outdoor_colors_explained);
		TextView lblIndoorColorExplained = (TextView) view.findViewById(R.id.lbl_indoor_colors_explained);
		TextView lblIndoorPollutant = (TextView) view.findViewById(R.id.lbl_indoor_pollutant);
		TextView lblVistashield = (TextView) view.findViewById(R.id.lbl_vistashield);
		TextView lblGuardEnv = (TextView) view.findViewById(R.id.lbl_guard_environment);
		lblOutdoorColorExplained.setOnClickListener(this);
		lblIndoorColorExplained.setOnClickListener(this);
		lblIndoorPollutant.setOnClickListener(this);
		lblVistashield.setOnClickListener(this);
		lblGuardEnv.setOnClickListener(this);

		/*
		 * lblOutdoorColorExplained.setTypeface(Fonts.getGillsans(getActivity()))
		 * ;
		 * lblIndoorColorExplained.setTypeface(Fonts.getGillsans(getActivity())
		 * ); lblIndoorPollutant.setTypeface(Fonts.getGillsans(getActivity()));
		 * lblVistashield.setTypeface(Fonts.getGillsans(getActivity()));
		 * lblGuardEnv.setTypeface(Fonts.getGillsans(getActivity()));
		 */

	}

	@Override
	public void onClick(View view) {
		Intent in;
		switch (view.getId()) {
		case R.id.lbl_outdoor_colors_explained:
			in = new Intent(getActivity(),
					OutdoorAirColorIndicationActivity.class);
			startActivity(in);
			break;
		case R.id.lbl_indoor_colors_explained:
			in = new Intent(getActivity(),
					IndoorAirColorIndicationActivity.class);
			startActivity(in);
			break;
		case R.id.lbl_indoor_pollutant:
			in = new Intent(getActivity(),
					AirQualityExplainedStaticActivity.class);
			in.putExtra("AIR_QUALITY_ACTIVITY", INDOOR_POLLUTANT_SCREEN);
			startActivity(in);
			break;
		case R.id.lbl_vistashield:
			in = new Intent(getActivity(),
					AirQualityExplainedStaticActivity.class);
			in.putExtra("AIR_QUALITY_ACTIVITY", VITASHIELD_SCREEN);
			startActivity(in);
			break;
		case R.id.lbl_guard_environment:
			in = new Intent(getActivity(),
					AirQualityExplainedStaticActivity.class);
			in.putExtra("AIR_QUALITY_ACTIVITY", GUARD_EVNVIRONMENT_SCREEN);
			startActivity(in);
			break;
		default:
			break;
		}
	}

}
