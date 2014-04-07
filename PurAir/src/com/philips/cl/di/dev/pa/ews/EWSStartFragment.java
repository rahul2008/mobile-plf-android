package com.philips.cl.di.dev.pa.ews;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.util.Fonts;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class EWSStartFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.ews_intro_screen, null);
		
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		Button startEwsBtn = (Button) getView().findViewById(R.id.ews_get_start_btn);
		startEwsBtn.setTypeface(Fonts.getGillsansLight(getActivity()));
		startEwsBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((EWSActivity) getActivity()).checkWifiConnectivity();
			}
		});
	}
	
}
