package com.philips.cl.di.dev.pa.demo;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.ews.EWSBaseFragment;
import com.philips.cl.di.dev.pa.util.Fonts;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class DemoModeStartFragement extends EWSBaseFragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.setup_intro_screen, null);
		
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		ViewGroup scrollView = (ScrollView) getView().findViewById(R.id.scrollView);
		setBackground(scrollView, R.drawable.ews_nav_bar_2x, Color.BLACK, .1F);
		
		FontTextView msgText = (FontTextView) getView().findViewById(R.id.setup_intro_message1_txt);
		msgText.setText(R.string.demo_mode_intro_msg);
		
		FontTextView txt = (FontTextView) getView().findViewById(R.id.setup_intro_message2_txt);
		txt.setText("");
		txt.setVisibility(View.INVISIBLE);
		
		((DemoModeActivity) getActivity()).setActionbarTitle(DemoModeConstant.DEMO_MODE_STEP_INTRO);
		
		Button startDemoBtn = (Button) getView().findViewById(R.id.setup_get_start_btn);
		startDemoBtn.setTypeface(Fonts.getCentraleSansLight(getActivity()));
		startDemoBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((DemoModeActivity) getActivity()).gotoStepOneScreen();
			}
		});
	}
	
}
