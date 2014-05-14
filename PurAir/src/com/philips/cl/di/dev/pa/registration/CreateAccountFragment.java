package com.philips.cl.di.dev.pa.registration;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
		
		showErrorDialog(RegistrationErrorDialogFragment.dialog_type.PASSWORD_INCORRECT);
	}
	
	private void showSignInDialog(SignInDialogFragment.dialog_type type) {
		SignInDialogFragment dialog = SignInDialogFragment.newInstance(type);
		FragmentManager fragMan = getFragmentManager();
		dialog.show(fragMan, null);
	}
	
	private void showErrorDialog(RegistrationErrorDialogFragment.dialog_type type) {
		RegistrationErrorDialogFragment dialog = RegistrationErrorDialogFragment.newInstance(type);
		FragmentManager fragMan = getFragmentManager();
		dialog.show(fragMan, null);
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
			showSignInDialog(SignInDialogFragment.dialog_type.MY_PHILIPS);
			break;
		case R.id.llFacebook:
			showSignInDialog(SignInDialogFragment.dialog_type.FACEBOOK);
			break;
		case R.id.llTwitter:
			showSignInDialog(SignInDialogFragment.dialog_type.TWITTER);
			break;
		case R.id.llGooglPlus:
			showSignInDialog(SignInDialogFragment.dialog_type.GOOGLE_PLUS);
			break;
		default:
			break;
		}
	}
}
