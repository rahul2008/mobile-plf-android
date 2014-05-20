package com.philips.cl.di.dev.pa.ews;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.util.Fonts;

public class EWSFinalStepFragment  extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.setup_congratulation, null); 
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		((EWSActivity) getActivity()).setActionBarHeading(EWSConstant.EWS_STEP_FINAL);
		
		Button startControlPurifierBtn = (Button) getView().findViewById(R.id.finish_congratulation_btn);
		startControlPurifierBtn.setTypeface(Fonts.getGillsansLight(getActivity()));
		startControlPurifierBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((EWSActivity) getActivity()).showHomeScreen();
			}
		});
	}
}
