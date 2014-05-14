package com.philips.cl.di.dev.pa.registration;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class RegistrationErrorDialogFragment extends DialogFragment {

	public static enum dialog_type {PASSWORD_INCORRECT, ALREADY_REGISTERED};
	private static final String DIALOG_SELECTED = "com.philips.cl.dev.pa.registration.error_dialog";
	private FontTextView message;
	private Button btnClose;

	public static RegistrationErrorDialogFragment newInstance(dialog_type showDialog) {
		RegistrationErrorDialogFragment fragment = new RegistrationErrorDialogFragment();

		Bundle args = new Bundle();
		args.putSerializable(DIALOG_SELECTED, showDialog);
		fragment.setArguments(args);		
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.air_registration_error_dialog, container, false);

		initializeView(view);

		return view;
	}

	private void initializeView(View view) {	
		message = (FontTextView) view.findViewById(R.id.tvErrorMessage);
		btnClose = (Button) view.findViewById(R.id.btnClose);
		
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		
		dialog_type dialog = (dialog_type) getArguments().getSerializable(DIALOG_SELECTED);
		
		if (dialog == dialog_type.PASSWORD_INCORRECT) {	
			message.setText(R.string.password_not_correct);
		}
		else if (dialog == dialog_type.ALREADY_REGISTERED) {
			message.setText(R.string.already_registerd);
		}
		
		btnClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}
}
