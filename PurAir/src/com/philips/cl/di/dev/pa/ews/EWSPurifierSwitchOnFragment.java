package com.philips.cl.di.dev.pa.ews;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.util.Fonts;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.util.TrackPageConstants;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class EWSPurifierSwitchOnFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.ews_steps_2_3, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		MetricsTracker.trackPage(TrackPageConstants.EWS_SWITCH_ON_PURIFIER);
		((EWSActivity) getActivity())
				.setActionBarHeading(EWSConstant.EWS_STEP_TWO);

		((FontTextView) getView().findViewById(R.id.setup_step2_title))
				.setText(getString(R.string.step20_off_3));

		((FontTextView) getView().findViewById(R.id.setup_step2_instruction))
				.setText(getString(R.string.verify_power_on_title));

		((ImageView) getView().findViewById(R.id.setup_step2_img))
				.setImageResource(R.drawable.purifier_power_on);

		((FontTextView) getView().findViewById(R.id.setup_step2_message1))
				.setText(getString(R.string.verify_power_on_msg1));

		((FontTextView) getView().findViewById(R.id.setup_step2_message2))
				.setText(getString(R.string.verify_power_on_msg2));

		Button yesBtn = (Button) getView().findViewById(
				R.id.setup_step2_yes_btn);
		yesBtn.setTypeface(Fonts.getCentraleSansLight(getActivity()));
		yesBtn.setText(getString(R.string.yes));

		Button noBtn = (Button) getView().findViewById(R.id.setup_step2_no_btn);
		noBtn.setTypeface(Fonts.getCentraleSansLight(getActivity()));
		noBtn.setText(getString(R.string.no));

		yesBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((EWSActivity) getActivity()).showStepThree();
			}
		});

		noBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((EWSActivity) getActivity()).showPowerOnInstructionsDialog();
			}
		});

	}
}