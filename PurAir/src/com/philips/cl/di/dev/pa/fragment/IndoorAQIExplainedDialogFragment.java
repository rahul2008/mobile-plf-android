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
import com.philips.cl.di.dev.pa.customviews.CustomTextView;

public class IndoorAQIExplainedDialogFragment extends DialogFragment {
	
	private static final String EXTRA_INDOORTITLE = "extra_indoortitle";
	private static final String EXTRA_OUTDOORTITLE = "extra_outdoortitle";
	
	public static IndoorAQIExplainedDialogFragment newInstance(String indoorTitle, String outdoorTitle) {
		IndoorAQIExplainedDialogFragment fragment =  new IndoorAQIExplainedDialogFragment();
		
		Bundle args= new Bundle();
		args.putString(EXTRA_INDOORTITLE, indoorTitle);
		args.putString(EXTRA_OUTDOORTITLE, outdoorTitle);
		fragment.setArguments(args);
		
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		View view = inflater.inflate(R.layout.indoor_air_quality_explain, container, false);
		
		ImageView close = (ImageView) view.findViewById(R.id.aqiAnalysisClose);
		if (close != null) {
			close.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					IndoorAQIExplainedDialogFragment.this.dismiss();
				}
			});
		}
		
		String outdoorTitle = getArguments().getString(EXTRA_OUTDOORTITLE);
		String indoorTitle = getArguments().getString(EXTRA_INDOORTITLE);
		
		((CustomTextView) view.findViewById(R.id.aqiAnalysisMsg11)).setText
			(getString(R.string.outdoor_analysis_detail2_head100)+" '"+indoorTitle+"'  " +
			getString(R.string.outdoor_analysis_detail2_head102)+" '"+outdoorTitle+"' " +
					getString(R.string.outdoor_analysis_detail2_head102));
		
		return view; 
	}

}
