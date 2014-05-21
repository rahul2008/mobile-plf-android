package com.philips.cl.di.dev.pa.registration;

import java.util.ArrayList;
import android.app.ProgressDialog;
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

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.view.FontTextView;
import com.philips.cl.di.reg.User;
import com.philips.cl.di.reg.dao.DIUserProfile;
import com.philips.cl.di.reg.errormapping.Error;
import com.philips.cl.di.reg.errormapping.ErrorMessage;
import com.philips.cl.di.reg.handlers.TraditionalRegistrationHandler;

public class CreateAccountFragment extends BaseFragment implements OnClickListener, TraditionalRegistrationHandler {
	
	public enum ErrorType { NAME, PASSWORD, EMAIL, NONE}

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
	
	private User user;
	private ProgressDialog progressDialog;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.air_registration_create_account_fragment, container, false);
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
		// TODO make API call 
		Log.e("TEMP", "name: " + mName + " email: " + mEmail + " password: " + mPassword + " receiveInfo: " + mReceiveInfo);
		showProgressDialog() ;
		user = new User(PurAirApplication.getAppContext());
		DIUserProfile profile = new DIUserProfile();
		profile.setEmail(mEmail);
		profile.setGivenName(mName);
		profile.setPassword(mPassword);
		profile.setOlderThanAgeLimit(true);
		profile.setReceiveMarketingEmail(mReceiveInfo);
		
		ArrayList<DIUserProfile> profileList = new ArrayList<DIUserProfile>();
		profileList.add(profile);
		
		user.registerNewUserUsingTraditional(profileList, this, PurAirApplication.getAppContext());
	}
	
	private void showSignInDialog(SignInDialogFragment.DialogType type) {
		SignInDialogFragment dialog = SignInDialogFragment.newInstance(type);
		FragmentManager fragMan = getFragmentManager();
		dialog.show(fragMan, null);
	}
	
	private void showErrorDialog(Error type) {
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
			getInput();
			switch (isInputValidated()) {
			case NONE:		
				createAccount();
				break;
			case PASSWORD:
				showErrorDialog(Error.INVALID_PASSWORD) ;
				break;
			case EMAIL:
				showErrorDialog(Error.INVALID_EMAILID) ;
				break;			
			case NAME:
				showErrorDialog(Error.INVALID_USERNAME_OR_PASSWORD) ;
				break;
			default:
				break;
			}
			
			break;
		case R.id.llMyPhilips:
			showSignInDialog(SignInDialogFragment.DialogType.MY_PHILIPS);
			break;
		case R.id.llFacebook:
			showSignInDialog(SignInDialogFragment.DialogType.FACEBOOK);
			break;
		case R.id.llTwitter:
			showSignInDialog(SignInDialogFragment.DialogType.TWITTER);
			break;
		case R.id.llGooglPlus:
			showSignInDialog(SignInDialogFragment.DialogType.GOOGLE_PLUS);
			break;
		}
	}

	private void getInput() {
		mName = mEditTextName.getText().toString();
		mEmail = mEditTextEmail.getText().toString();
		mPassword = mEditTextPassword.getText().toString();
		mReceiveInfo = mCheckBoxReceivInfo.isChecked();
	}

	private ErrorType isInputValidated() {
		ALog.i(ALog.USER_REGISTRATION, "isInputValidated name " + mName + " pass " + mPassword + " email " + mEmail);
		if(mName == null || mName.length() < 1) return ErrorType.NAME;
		if(! EmailValidator.getInstance().validate(mEmail)) return ErrorType.EMAIL;
		if(mPassword == null || mPassword.length() < 6) return ErrorType.PASSWORD;
		return ErrorType.NONE ;
	}

	private void showProgressDialog() {
		progressDialog = new ProgressDialog(getActivity());
		progressDialog.setMessage(getString(R.string.please_wait));
		progressDialog.setCancelable(false);
		progressDialog.show();
	}
	
	private void cancelProgressDialog() {
		if(progressDialog != null && progressDialog.isShowing()) {
			progressDialog.cancel() ;
		}
	}
	
	@Override
	public void onRegisterSuccess() {
		ALog.i(ALog.USER_REGISTRATION, "onRegisterSuccess");
		cancelProgressDialog() ;
		showSuccessFragment();
	}

	@Override
	public void onRegisterFailedWithFailure(int error) {
		ALog.i(ALog.USER_REGISTRATION, "onRegisterFailedWithFailure error " + new ErrorMessage().getError(error));
		cancelProgressDialog() ;
		showErrorDialog(UserRegistrationController.getInstance().getErrorEnum(error));
	}
	
	
	private void showSuccessFragment() {
		if(getActivity() != null && getActivity() instanceof MainActivity) {
			((MainActivity) getActivity()).showFragment(new SignedInFragment());
		}
	}
	
}
