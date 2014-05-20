package com.philips.cl.di.dev.pa.demo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.util.Fonts;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class DemoModeStartFragement extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.setup_intro_screen, null);
		
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		FontTextView msgText = (FontTextView) getView().findViewById(R.id.setup_intro_message1_txt);
		msgText.setText(R.string.demo_mode_intro_msg);
		
		((ImageView) getView().findViewById(R.id.setup_intro_attention_img)).setVisibility(View.INVISIBLE);
		FontTextView txt = (FontTextView) getView().findViewById(R.id.setup_intro_message2_txt);
		txt.setText("");
		txt.setVisibility(View.INVISIBLE);
		
		((DemoModeActivity) getActivity()).setActionbarTitle(DemoModeConstant.DEMO_MODE_STEP_INTRO);
		
		Button startDemoBtn = (Button) getView().findViewById(R.id.setup_get_start_btn);
		startDemoBtn.setTypeface(Fonts.getGillsansLight(getActivity()));
		startDemoBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((DemoModeActivity) getActivity()).showStepOneScreen();
			}
		});
	}
	
}
