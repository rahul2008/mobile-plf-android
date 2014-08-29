package com.philips.cl.di.dev.pa.registration;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.view.FontTextView;
import com.philips.cl.di.reg.errormapping.Error;

public class RegistrationErrorDialogFragment extends DialogFragment {

//	public static enum DialogType {INVALID_NAME, INVALID_PASSWORD, INVALID_EMAILID, INCORRECT_PASSWORD,ALREADY_REGISTERED, };
	private static final String DIALOG_SELECTED = "com.philips.cl.dev.pa.registration.error_dialog";
	private FontTextView message;
	private ImageButton btnClose;

	public static RegistrationErrorDialogFragment newInstance(Error showDialog) {
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
		btnClose = (ImageButton) view.findViewById(R.id.btnClose);
		
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		
		Error dialog = (Error) getArguments().getSerializable(DIALOG_SELECTED);
		
		switch (dialog) {
		case INCORRECT_PASSWORD:
			message.setText(R.string.password_not_correct);
			break;
		case EMAIL_ALREADY_EXIST:
			message.setText(R.string.already_registerd);
			break;
		case INVALID_EMAILID:
			message.setText(R.string.invalid_email);
			break;
		case INVALID_USERNAME_OR_PASSWORD:
			message.setText(R.string.invalid_username_or_password);
			break;
		case INVALID_PASSWORD:
			message.setText(R.string.invalid_password) ;
			break;
		case ACCOUNT_DOESNOT_EXIST:
			message.setText(R.string.no_account) ;
			break;
		case AUTHENTICATION_CANCELED_BY_USER:
			message.setText(R.string.authentication_cancelled_by_user) ;
			break;
		case AUTHENTICATION_FAILED:
			message.setText(R.string.authentication_failed) ;
			break;
		case CONFIGURATION_FAILED:
			message.setText(R.string.configuration_failed) ;
			break;
		case EMAIL_ADDRESS_IN_USE:
			message.setText(R.string.already_registerd) ;
			break;
		case INVALID_PARAM:
			message.setText(R.string.invalid_params) ;
			break;
		case NO_NETWORK_CONNECTION:
			message.setText(R.string.no_network_connection) ;
			break;
		case TWO_STEP_ERROR:
		case MERGE_FLOW_ERROR:
		default:
			message.setText(R.string.generic_error) ;
			break;
		}		
		btnClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}
}
