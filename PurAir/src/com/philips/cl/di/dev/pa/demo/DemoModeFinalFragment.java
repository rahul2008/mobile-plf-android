package com.philips.cl.di.dev.pa.demo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.ews.SetupDialogFactory;
import com.philips.cl.di.dev.pa.util.Fonts;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class DemoModeFinalFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.setup_congratulation, null);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

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
				.getGillsansLight(getActivity()));
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
