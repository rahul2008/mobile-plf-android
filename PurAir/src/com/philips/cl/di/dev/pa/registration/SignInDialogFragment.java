package com.philips.cl.di.dev.pa.registration;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class SignInDialogFragment extends DialogFragment {

	public static enum dialog_type {MY_PHILIPS, FACEBOOK, TWITTER, GOOGLE_PLUS};
	private static final String DIALOG_SELECTED = "com.philips.cl.dev.pa.registration.sign_in_dialog";
	private FontTextView title;
	private Button btnClose;
	private Button btnSignIn;

	public static SignInDialogFragment newInstance(dialog_type showDialog) {
		SignInDialogFragment fragment = new SignInDialogFragment();

		Bundle args = new Bundle();
		args.putSerializable(DIALOG_SELECTED, showDialog);
		fragment.setArguments(args);		
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.air_registration_sign_in_dialog, container, false);
		setCancelable(false);
		initializeView(view);

		return view;
	}

	private void initializeView(View view) {	
		title = (FontTextView) view.findViewById(R.id.tvSingInTitle);
		btnClose = (Button) view.findViewById(R.id.btnClose);
		btnSignIn = (Button) view.findViewById(R.id.btnSignIn);
		
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		
		final dialog_type dialog = (dialog_type) getArguments().getSerializable(DIALOG_SELECTED);
		
		if (dialog == dialog_type.MY_PHILIPS) {	
			title.setText(R.string.sign_in_to_my_philips);
		} else if (dialog == dialog_type.FACEBOOK) {
			title.setText(R.string.sign_in_to_facebook);
		} else if (dialog == dialog_type.TWITTER) {
			title.setText(R.string.sign_in_to_twitter);
		} else if (dialog == dialog_type.GOOGLE_PLUS) {
			title.setText(R.string.sign_in_to_google_plus);
		}

		final EditText etEmail = (EditText) view.findViewById(R.id.etEmailAddress);
		final EditText etPassword = (EditText) view.findViewById(R.id.etPassword);
		
		OnClickListener clickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if (v == btnSignIn) {
					String email = etEmail.getText().toString();
					String password = etPassword.getText().toString();
					
					switch (dialog) {
					case MY_PHILIPS:
						Log.e("TEMP", "My Philips: E-mail: " + email + " Password: " + password);
						break;
					case FACEBOOK:
						Log.e("TEMP", "Facebook: E-mail: " + email + " Password: " + password);
						break;
					case TWITTER:
						Log.e("TEMP", "Twitter: E-mail: " + email + " Password: " + password);
						break;
					case GOOGLE_PLUS:
						Log.e("TEMP", "Google+: E-mail: " + email + " Password: " + password);
						break;
					default:
						break;
					}
				}
				dismiss();
			}
		};
		
		btnClose.setOnClickListener(clickListener);
		btnSignIn.setOnClickListener(clickListener);
	}
}
