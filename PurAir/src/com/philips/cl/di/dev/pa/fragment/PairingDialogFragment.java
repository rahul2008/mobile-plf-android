package com.philips.cl.di.dev.pa.fragment;

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
	private static final String DIALOG_SELECTED="com.philips.cl.dev.pa.pairingDialog";
	public static enum dialog_type {SHOW_DIALOG, PAIRING_FAILED, PAIRING_SUCCESS};
	private TextView message;
	private Button btnClose;
	private Button btn_pair;

	public static PairingDialogFragment newInstance(String purifierEui64, dialog_type showDialog) {
		PairingDialogFragment fragment = new PairingDialogFragment();

		Bundle args = new Bundle();
		args.putSerializable(DIALOG_SELECTED, showDialog);
		if(purifierEui64!=null) {
			args.putString(EXTRA_EUI64, purifierEui64);
		}

		fragment.setArguments(args);		
		return fragment;		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.tutorial_custom_dialog, container, false);
		setCancelable(false);

		initializeView(view);

		return view;
	}

	private void initializeView(View view) {	
		message = (TextView) view.findViewById(R.id.take_tour_alert);
		btnClose = (Button) view.findViewById(R.id.btn_close);
		btn_pair = (Button) view.findViewById(R.id.btn_yes);
		message.setTypeface(Fonts.getGillsans(getActivity()));
		dialog_type dialog = (dialog_type)getArguments().getSerializable(DIALOG_SELECTED);
		if(dialog==dialog_type.SHOW_DIALOG){	
			initializePairingDialogView();
		}
		else if(dialog==dialog_type.PAIRING_SUCCESS){
			initializePairingDialogSuccessView();
		}
		else if(dialog==dialog_type.PAIRING_FAILED){
			initializePairingDialogFailedView();
		}
	}

	private void initializePairingDialogSuccessView() {

		getDialog().setTitle(R.string.congratulations);
		message.setText(R.string.pairing_success);
		btn_pair.setVisibility(View.GONE);

		btnClose.setTypeface(Fonts.getGillsans(getActivity()));			
		btnClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

	}

	private void initializePairingDialogFailedView() {

		getDialog().setTitle(R.string.error_title);
		message.setText(R.string.pairing_failed);
		btn_pair.setText(R.string.try_again);

		btnClose.setTypeface(Fonts.getGillsans(getActivity()));
		btn_pair.setTypeface(Fonts.getGillsans(getActivity()));

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
				if (getActivity() instanceof MainActivity) {
					((MainActivity) getActivity()).startPairing(getArguments().getString(EXTRA_EUI64));
				}

			}
		});

	}

	private void initializePairingDialogView() {
		getDialog().setTitle(R.string.pair_title);
		message.setText(R.string.pair_text);
		btn_pair.setText(R.string.pair);

		btnClose.setTypeface(Fonts.getGillsans(getActivity()));
		btn_pair.setTypeface(Fonts.getGillsans(getActivity()));

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
				if (getActivity() instanceof MainActivity) {
					((MainActivity) getActivity()).startPairing(getArguments().getString(EXTRA_EUI64));
				}

			}
		});
	}


}
