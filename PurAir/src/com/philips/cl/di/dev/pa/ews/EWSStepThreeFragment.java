package com.philips.cl.di.dev.pa.ews;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.util.Fonts;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.util.TrackPageConstants;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class EWSStepThreeFragment extends Fragment {
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.ews_steps_2_3, null);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		MetricsTracker.trackPage(TrackPageConstants.EWS_CONFIRM_WIFI_ON);
		((EWSActivity) getActivity()).setActionBarHeading(EWSConstant.EWS_STEP_TWO);
		
		String orangeStr = "<font color=#EF6921>"+getString(R.string.orange)+"</font>";
		String msg1 = getString(R.string.step2_msg1) + orangeStr+".";
		((FontTextView) getView().findViewById(R.id.setup_step2_message1)).setText(Html.fromHtml(msg1));

		String msg2 = getString(R.string.step2_msg2) + "<font color=#EF6921>"+getString(R.string.orange)+"</font>?";
		((FontTextView) getView().findViewById(R.id.setup_step2_message2)).setText(Html.fromHtml(msg2));
		
		Button yesBtn = (Button) getView().findViewById(R.id.setup_step2_yes_btn);
		yesBtn.setTypeface(Fonts.getCentraleSansLight(getActivity()));
		Button noBtn = (Button) getView().findViewById(R.id.setup_step2_no_btn);
		noBtn.setTypeface(Fonts.getCentraleSansLight(getActivity()));
		
		yesBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((EWSActivity) getActivity()).connectToAirPurifier();
			}
		});
		
		noBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((EWSActivity) getActivity()).showEWSSetUpInstructionsDialog();
			}
		});
	}
}
