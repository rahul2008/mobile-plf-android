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
import com.philips.cl.di.dev.pa.ews.SetupDialogFactory;
import com.philips.cl.di.dev.pa.util.Fonts;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.util.TrackPageConstants;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class DemoModeFinalFragment extends EWSBaseFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.setup_congratulation, null);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		MetricsTracker.trackPage(TrackPageConstants.DEMO_MODE_ADD_PURIFIER_SUCCESS);
		
		ViewGroup scrollView = (ScrollView) getView().findViewById(R.id.scrollView);
		setBackground(scrollView, R.drawable.ews_nav_bar_2x, Color.BLACK, .1F);
		
		((DemoModeActivity) getActivity())
				.setActionbarTitle(DemoModeConstant.DEMO_MODE_STEP_FINAL);

		FontTextView msg = (FontTextView) getView().findViewById(
				R.id.congratulation_msg);
		msg.setText(getString(R.string.demo_mode_final_step_msg));

		Button startControlPurifierBtn = (Button) getView().findViewById(
				R.id.finish_congratulation_btn);
		startControlPurifierBtn
				.setText(getString(R.string.demo_mode_final_step_btn_txt));
		startControlPurifierBtn.setTypeface(Fonts
				.getCentraleSansLight(getActivity()));
		startControlPurifierBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((DemoModeActivity) getActivity()).showHomeScreen();
			}
		});
		SetupDialogFactory.getInstance((DemoModeActivity) getActivity())
				.dismissSignalStrength();
	}
}
