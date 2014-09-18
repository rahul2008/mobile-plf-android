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

public class EWSStartFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.setup_intro_screen, null);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		((EWSActivity) getActivity()).setActionBarHeading(EWSConstant.EWS_STEP_START);

		Button startEwsBtn = (Button) getView().findViewById(
				R.id.setup_get_start_btn);
		startEwsBtn.setTypeface(Fonts.getGillsansLight(getActivity()));
		startEwsBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (getActivity() == null) return;
				((EWSActivity) getActivity()).checkWifiConnectivity();
			}
		});
	}

}
