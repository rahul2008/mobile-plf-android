package com.philips.cl.di.dev.pa.registration;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class CreateAccountFragment extends BaseFragment implements OnClickListener {

	private EditText mEditTextName;
	private EditText mEditTextEmail;
	private EditText mEditTextPassword;
	
	private CheckBox mCheckBoxReceivInfo;
	
	private Button mButtonCreateAccount;
	
	private LinearLayout mLayoutMyPhilips;
	private LinearLayout mLayoutFacebook;
	private LinearLayout mLayoutTwitter;
	private LinearLayout mLayoutGooglePlus;
	
	private String mName;
	private String mEmail;
	private String mPassword;
	private boolean mReceiveInfo;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.air_registration_create_account_fragment, container,
				false);
		initViews(view);
		return view;
	}
	
	private void initViews(View view) {
		mEditTextName = (EditText) view.findViewById(R.id.etName);
		mEditTextEmail = (EditText) view.findViewById(R.id.etEmailAddress);
		mEditTextPassword = (EditText) view.findViewById(R.id.etPassword);
		
		mCheckBoxReceivInfo = (CheckBox) view.findViewById(R.id.rbReceiveInformation);
		mCheckBoxReceivInfo.setOnClickListener(this);
		
		mButtonCreateAccount = (Button) view.findViewById(R.id.btnCreateAccount);
		mButtonCreateAccount.setOnClickListener(this);
		
		mLayoutMyPhilips = (LinearLayout) view.findViewById(R.id.llMyPhilips);
		mLayoutFacebook = (LinearLayout) view.findViewById(R.id.llFacebook);
		mLayoutTwitter = (LinearLayout) view.findViewById(R.id.llTwitter);
		mLayoutGooglePlus = (LinearLayout) view.findViewById(R.id.llGooglPlus);
		
		((ImageView) mLayoutMyPhilips.findViewById(R.id.logo)).setImageResource(R.drawable.indoor_pollutants);
		((FontTextView) mLayoutMyPhilips.findViewById(R.id.title)).setText(R.string.my_philips);
		mLayoutMyPhilips.setOnClickListener(this);
		
		((ImageView) mLayoutFacebook.findViewById(R.id.logo)).setImageResource(R.drawable.indoor_pollutants);
		((FontTextView) mLayoutFacebook.findViewById(R.id.title)).setText(R.string.facebook);
		mLayoutFacebook.setOnClickListener(this);
		
		((ImageView) mLayoutTwitter.findViewById(R.id.logo)).setImageResource(R.drawable.indoor_pollutants);
		((FontTextView) mLayoutTwitter.findViewById(R.id.title)).setText(R.string.twitter);
		mLayoutTwitter.setOnClickListener(this);
		
		((ImageView) mLayoutGooglePlus.findViewById(R.id.logo)).setImageResource(R.drawable.indoor_pollutants);
		((FontTextView) mLayoutGooglePlus.findViewById(R.id.title)).setText(R.string.google_plus);
		mLayoutGooglePlus.setOnClickListener(this);
	}
	
	private void createAccount() {
		mName = mEditTextName.getText().toString();
		mEmail = mEditTextEmail.getText().toString();
		mPassword = mEditTextPassword.getText().toString();
		mReceiveInfo = mCheckBoxReceivInfo.isChecked();
		
		// TODO make API call 
		Log.e("TEMP", "name: " + mName + " email: " + mEmail + " password: " + mPassword + " receiveInfo: " + mReceiveInfo);
	}
	
	private void showSignInDialog(final int stringId) {
		final Dialog dialog = new Dialog(getActivity());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.air_registration_sign_in_dialog);

		FontTextView title = (FontTextView) dialog.findViewById(R.id.tvSingInTitle);
		title.setText(stringId);
		Button btnClose = (Button) dialog.findViewById(R.id.btnClose);
		final Button btnSignIn = (Button) dialog.findViewById(R.id.btnSignIn);
		final EditText etEmail = (EditText) dialog.findViewById(R.id.etEmailAddress);
		final EditText etPassword = (EditText) dialog.findViewById(R.id.etPassword);
		
		OnClickListener clickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if (v == btnSignIn) {
					String email = etEmail.getText().toString();
					String password = etPassword.getText().toString();
					
					switch (stringId) {
					case R.string.sign_in_to_my_philips:
						Log.e("TEMP", "My Philips: E-mail: " + email + " Password: " + password);
						
						// TODO remove, this is only for test ---------------
						showErrorDialog(R.string.already_registerd);
						// --------------------------------------------------
						break;
					case R.string.sign_in_to_facebook:
						Log.e("TEMP", "Facebook: E-mail: " + email + " Password: " + password);
						
						// TODO remove, this is only for test ---------------
						showErrorDialog(R.string.password_not_correct);
						// --------------------------------------------------
						break;
					case R.string.sign_in_to_twitter:
						Log.e("TEMP", "Twitter: E-mail: " + email + " Password: " + password);
						break;
					case R.string.sign_in_to_google_plus:
						Log.e("TEMP", "Google+: E-mail: " + email + " Password: " + password);
						break;
					default:
						break;
					}
				}
				dialog.dismiss();
			}
		};
		
		btnClose.setOnClickListener(clickListener);
		btnSignIn.setOnClickListener(clickListener);

		dialog.show();
	}
	
	private void showErrorDialog(int stringId) {
		final Dialog dialog = new Dialog(getActivity());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.air_registration_error_dialog);

		Button btnClose = (Button) dialog.findViewById(R.id.btnClose);
		FontTextView tvErrorMessage = (FontTextView) dialog.findViewById(R.id.tvErrorMessage);
		tvErrorMessage.setText(stringId);
		
		btnClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		
		dialog.show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rbReceiveInformation:
			break;
		case R.id.btnCreateAccount:
			createAccount();
			break;
		case R.id.llMyPhilips:
			showSignInDialog(R.string.sign_in_to_my_philips);
			break;
		case R.id.llFacebook:
			showSignInDialog(R.string.sign_in_to_facebook);
			break;
		case R.id.llTwitter:
			showSignInDialog(R.string.sign_in_to_twitter);
			break;
		case R.id.llGooglPlus:
			showSignInDialog(R.string.sign_in_to_google_plus);
			break;
		default:
			break;
		}
	}
}
