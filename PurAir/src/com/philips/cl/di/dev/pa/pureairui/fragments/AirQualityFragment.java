package com.philips.cl.di.dev.pa.pureairui.fragments;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.pureairui.MainActivity;
import com.philips.cl.di.dev.pa.utils.Fonts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class AirQualityFragment extends Fragment implements OnClickListener {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.air_quality_fragment, container, false);
		initializeView(view);
		return view;
	}

	private void initializeView(View view) {
		TextView lblOutdoorColorExplained= (TextView) view.findViewById(R.id.lbl_outdoor_colors_explained);
		TextView lblIndoorColorExplained=(TextView) view.findViewById(R.id.lbl_indoor_colors_explained);
		TextView lblIndoorPollutant=(TextView) view.findViewById(R.id.lbl_indoor_pollutant);
		TextView lblVistashield=(TextView) view.findViewById(R.id.lbl_vistashield);
		TextView lblGuardEnv=(TextView) view.findViewById(R.id.lbl_guard_environment);
		lblOutdoorColorExplained.setOnClickListener(this);
		lblIndoorColorExplained.setOnClickListener(this);
		lblIndoorPollutant.setOnClickListener(this);
		lblVistashield.setOnClickListener(this);
		lblGuardEnv.setOnClickListener(this);
		
		lblOutdoorColorExplained.setTypeface(Fonts.getGillsans(getActivity()));
		lblIndoorColorExplained.setTypeface(Fonts.getGillsans(getActivity()));
		lblIndoorPollutant.setTypeface(Fonts.getGillsans(getActivity()));
		lblVistashield.setTypeface(Fonts.getGillsans(getActivity()));
		lblGuardEnv.setTypeface(Fonts.getGillsans(getActivity()));
		
	}

	@Override
	public void onClick(View view) {
		Intent in;
		Bundle bundle = new Bundle();
		
		MainActivity activity=(MainActivity) getActivity();
		switch(view.getId()){
		case R.id.lbl_outdoor_colors_explained:
			activity.showFragment(new OutdoorAirColorIndicationFragment());
			activity.setTitle(getString(R.string.outdoor_colors_explained));
			break;
		case R.id.lbl_indoor_colors_explained:
			activity.showFragment(new IndoorAirColorIndicationFragment());
			activity.setTitle(getString(R.string.indoor_colors_explained));
			break;
		case R.id.lbl_indoor_pollutant:
			activity.setAirExplainedActivity(11);
			activity.showFragment(new AirColorExplainedStaticFragment());
			activity.setTitle(getString(R.string.indoor_pollutants));
			break;
		case R.id.lbl_vistashield:
			activity.setAirExplainedActivity(12);
			activity.showFragment(new AirColorExplainedStaticFragment());
			activity.setTitle(getString(R.string.vistashield_ips_system));
			break;
		case R.id.lbl_guard_environment:
			activity.setAirExplainedActivity(13);
			activity.showFragment(new AirColorExplainedStaticFragment());
			activity.setTitle(getString(R.string.guards_environment));
			break;
		}
	}
	
	
}
