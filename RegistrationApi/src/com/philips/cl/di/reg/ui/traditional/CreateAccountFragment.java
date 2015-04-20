package com.philips.cl.di.reg.ui.traditional;

import java.util.ArrayList;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cl.di.reg.R;
import com.philips.cl.di.reg.User;
import com.philips.cl.di.reg.dao.DIUserProfile;
import com.philips.cl.di.reg.handlers.TraditionalRegistrationHandler;
import com.philips.cl.di.reg.ui.customviews.XEmailField;
import com.philips.cl.di.reg.ui.customviews.XNameField;
import com.philips.cl.di.reg.ui.customviews.XPasswordField;
import com.philips.cl.di.reg.ui.utils.RLog;

public class CreateAccountFragment extends RegistrationBaseFragment implements
		OnClickListener, TraditionalRegistrationHandler{
	private LinearLayout mFirstLayout;
	private TextView mTvpasswordDetails;
	private LinearLayout mThirdLayout;
	private RelativeLayout mFourthLayout;
	private Button mBtnCreateAccount;
	private Button mBtnTest;
	private CheckBox mCbTerms;
	private User mUser;
	private XNameField mRegErrorName;
	private XEmailField mEMEmailField;
	private XPasswordField mPasswordField;
	
	private ProgressBar mPbSpinner;
	
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		RLog.d(RLog.FRAGMENT_LIFECYCLE,"UserCreateAccountFragment : onCreateView");
		View view = inflater.inflate(R.layout.fragment_create_account, container,false);
		initUI(view);
		return view;
	}

	@Override
	public void onConfigurationChanged(Configuration config) {
		RLog.d(RLog.FRAGMENT_LIFECYCLE,"UserCreateAccountFragment : onConfigurationChanged");
		super.onConfigurationChanged(config);
		setViewParams(config);
	}

	@Override
	public void setViewParams(Configuration config) {
		RLog.d(RLog.FRAGMENT_LIFECYCLE,"UserCreateAccountFragment : setViewParams");
		LinearLayout.LayoutParams params = (LayoutParams) mFirstLayout.getLayoutParams();
		if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
			params.leftMargin = params.rightMargin = mLeftRightMarginPort;
		} else {
			params.leftMargin = params.rightMargin = mLeftRightMarginLand;
		}
		mFirstLayout.setLayoutParams(params);
		mTvpasswordDetails.setLayoutParams(params);
		mThirdLayout.setLayoutParams(params);
		mFourthLayout.setLayoutParams(params);
	}
	

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_register) {
			register();
		}
	}

	private void initUI(View view) {
		
		mFirstLayout = (LinearLayout) view.findViewById(R.id.first_part);
		
		mTvpasswordDetails = (TextView) view.findViewById(R.id.password_details);
		mThirdLayout = (LinearLayout) view.findViewById(R.id.third_part);
		mFourthLayout = (RelativeLayout) view.findViewById(R.id.fourth_part);

		mBtnCreateAccount = (Button) view.findViewById(R.id.btn_register);
		mCbTerms = (CheckBox) view.findViewById(R.id.cb_register_terms);
		mBtnCreateAccount.setOnClickListener(this);
		
		mRegErrorName = (XNameField) view.findViewById(R.id.rl_name_field);
		mEMEmailField = (XEmailField) view.findViewById(R.id.rl_email_field);
		mPasswordField = (XPasswordField) view.findViewById(R.id.rl_password_field);
		
		mPbSpinner = (ProgressBar) view.findViewById(R.id.pb_spinner);
		mBtnTest = (Button) view.findViewById(R.id.btn_test);
		mBtnTest.setOnClickListener(this);
		setViewParams(getResources().getConfiguration());
		mUser = new User(getActivity().getApplicationContext());
	}

	private void register() {
			
		if (mRegErrorName.ismValidName() && mEMEmailField.isValidEmail() && mPasswordField.isValidPassword()) {
			showSpinner();
			
			  ArrayList<DIUserProfile> userProfileArray = new
			  ArrayList<DIUserProfile>(); User user = new User(getActivity());
			  DIUserProfile profileab = new DIUserProfile();
			  profileab.setGivenName(mRegErrorName.getName().toString());
			  profileab.setEmail(mEMEmailField.getEmailId().toString());
			  profileab.setPassword(mPasswordField.getPassword().toString());
			  profileab.setOlderThanAgeLimit(true);
			  profileab.setReceiveMarketingEmail(mCbTerms.isChecked());
			  userProfileArray.add(profileab);
			  user.registerNewUserUsingTraditional(userProfileArray, this);
			 
			/*
			mUser.registerUserInfoForTraditional(mEtUserName.getText()
					.toString().trim(), mEtEmail.getText().toString().trim(), mEtPassword
					.getText().toString().trim(), true, mCbTerms.isChecked(), this);*/
			
		} else {
			Toast.makeText(getActivity(), "Please enter the valid entries",
					Toast.LENGTH_LONG).show();
		}
	}
	
	private void showSpinner() {
		mPbSpinner.setVisibility(View.VISIBLE);
	}

	private void hideSpinner() {
		mPbSpinner.setVisibility(View.INVISIBLE);
	}
	
	@Override
	public void onRegisterSuccess() {
		hideSpinner();
		Toast.makeText(getActivity(), "Registration Success", Toast.LENGTH_LONG).show();
		((RegistrationActivity) getActivity()).addFragment(new ActivateAccountFragment(mUser));
	}

	@Override
	public void onRegisterFailedWithFailure(int error) {
		
		if (error == 14) {
			mEMEmailField.setErrDescription(getResources().getString(R.string.email_already_used));
			mEMEmailField.showJanarainError();
		} else {
			Toast.makeText(getActivity(), "Check internet connection", Toast.LENGTH_LONG).show();
		}
		hideSpinner();
		Toast.makeText(getActivity(), "Registration Failure", Toast.LENGTH_LONG)
				.show();
	}

	@Override
	public String getActionbarTitle() {
		return getResources().getString(R.string.create_account);
	}
}
