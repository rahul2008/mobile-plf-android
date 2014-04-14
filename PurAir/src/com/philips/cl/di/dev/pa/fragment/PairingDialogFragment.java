package com.philips.cl.di.dev.pa.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.util.Fonts;

public class PairingDialogFragment extends DialogFragment {
	
	private static final String EXTRA_EUI64 = "com.philips.cl.di.dev.pa.extraeui64";
	
	public static PairingDialogFragment newInstance(String purifierEui64) {
		PairingDialogFragment fragment = new PairingDialogFragment();
		
		Bundle args = new Bundle();
		args.putString(EXTRA_EUI64, purifierEui64);
		fragment.setArguments(args);
		
		return fragment;
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.tutorial_custom_dialog, container, false);
		
		final Activity activity = this.getActivity();
		
		TextView message = (TextView) view.findViewById(R.id.take_tour_alert);
		Button btnClose = (Button) view.findViewById(R.id.btn_close);
		Button btn_pair = (Button) view.findViewById(R.id.btn_yes);

		message.setText(R.string.pair_title);
		btn_pair.setText(R.string.pair);

		btnClose.setTypeface(Fonts.getGillsans(activity));
		btn_pair.setTypeface(Fonts.getGillsans(activity));

		btnClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		
		btn_pair.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				if (activity instanceof MainActivity) {
					((MainActivity) activity).startPairing(getArguments().getString(EXTRA_EUI64));
				}

			}
		});
		
		return view;
	}
	

}
