
package com.philips.cl.di.reg.ui.traditional;

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
import com.philips.cl.di.reg.handlers.TraditionalRegistrationHandler;
import com.philips.cl.di.reg.ui.customviews.XEmail;
import com.philips.cl.di.reg.ui.customviews.XUserName;
import com.philips.cl.di.reg.ui.customviews.XPassword;
import com.philips.cl.di.reg.ui.customviews.onUpdateListener;
import com.philips.cl.di.reg.ui.utils.RLog;

public class CreateAccountFragment extends RegistrationBaseFragment implements OnClickListener,
        TraditionalRegistrationHandler, onUpdateListener {

	private LinearLayout mFirstLayout;

	private TextView mTvpasswordDetails;

	private LinearLayout mThirdLayout;

	private RelativeLayout mFourthLayout;

	private Button mBtnCreateAccount;

	private CheckBox mCbTerms;

	private User mUser;

	private XUserName mRegNameField;

	private XEmail mEtEmail;

	private XPassword mPasswordField;

	private final int EMAIL_ALEADY_EXIST = 14;

	private ProgressBar mPbSpinner;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "UserCreateAccountFragment : onCreateView");
		View view = inflater.inflate(R.layout.fragment_create_account, container, false);
		initUI(view);
		return view;
	}

	@Override
	public void onConfigurationChanged(Configuration config) {
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "UserCreateAccountFragment : onConfigurationChanged");
		super.onConfigurationChanged(config);
		setViewParams(config);
	}

	@Override
	public void setViewParams(Configuration config) {
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "UserCreateAccountFragment : setViewParams");
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
		mThirdLayout = (LinearLayout) view.findViewById(R.id.ll_create_account_container);
		mFourthLayout = (RelativeLayout) view.findViewById(R.id.ll_singin_options);

		mBtnCreateAccount = (Button) view.findViewById(R.id.btn_register);
		mCbTerms = (CheckBox) view.findViewById(R.id.cb_register_terms);
		mBtnCreateAccount.setOnClickListener(this);

		mRegNameField = (XUserName) view.findViewById(R.id.rl_name_field);
		mEtEmail = (XEmail) view.findViewById(R.id.rl_email_field);
		mEtEmail.setOnUpdateListener(this);
		mPasswordField = (XPassword) view.findViewById(R.id.rl_password_field);

		mPbSpinner = (ProgressBar) view.findViewById(R.id.pb_spinner);
		setViewParams(getResources().getConfiguration());
		mUser = new User(getActivity().getApplicationContext());
	}

	private void register() {

		if (mRegNameField.ismValidName() && mEtEmail.isValidEmail()
		        && mPasswordField.isValidPassword()) {
			showSpinner();

			mUser.registerUserInfoForTraditional(mRegNameField.getName().toString(), mEtEmail
			        .getEmailId().toString(), mPasswordField.getPassword().toString(), true,
			        mCbTerms.isChecked(), this);

		} else {
			Toast.makeText(getActivity(), "Please enter the valid entries", Toast.LENGTH_LONG)
			        .show();
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
	public void onRegisterFailedWithFailure(int errorType) {

		if (errorType == EMAIL_ALEADY_EXIST) {
			mEtEmail.setErrDescription(getResources().getString(R.string.email_already_used));
			mEtEmail.showJanarainError();
		} else {
			Toast.makeText(getActivity(), "Check internet connection", Toast.LENGTH_LONG).show();
		}
		hideSpinner();
		Toast.makeText(getActivity(), "Registration Failure", Toast.LENGTH_LONG).show();
	}

	@Override
	public String getActionbarTitle() {
		return getResources().getString(R.string.create_account);
	}

	@Override
	public void onUpadte() {
		// TODO check for all filed are valid and update ui accordingly

		if (mRegNameField.ismValidName() && mEtEmail.isValidEmail()
		        && mPasswordField.isValidPassword()) {
			// Enable

		} else {
			// Disable button
		}

	}
}
