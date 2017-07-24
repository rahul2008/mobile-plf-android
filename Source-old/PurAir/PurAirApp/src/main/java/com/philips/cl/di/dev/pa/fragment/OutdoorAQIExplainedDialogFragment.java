package com.philips.cl.di.dev.pa.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.util.TrackPageConstants;

public class OutdoorAQIExplainedDialogFragment extends DialogFragment {
	
	public static OutdoorAQIExplainedDialogFragment newInstance() {
		return new OutdoorAQIExplainedDialogFragment();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		View view = inflater.inflate(R.layout.od_air_quality_explain, container, false);
		
		ImageView close = (ImageView) view.findViewById(R.id.aqiAnalysisClose);
		if (close != null) {
			close.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					OutdoorAQIExplainedDialogFragment.this.dismiss();
				}
			});
		}
		
		return view; 
	}
	
	@Override
	public void onActivityCreated(Bundle arg0) {
		super.onActivityCreated(arg0);
		MetricsTracker.trackPage(TrackPageConstants.OUTDOOR_AIR_ANALYSIS_EXPLAINED);
	}

}
