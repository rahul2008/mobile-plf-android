package com.philips.cl.di.reg.ui.traditional;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.philips.cl.di.reg.R;
import com.philips.cl.di.reg.User;
import com.philips.cl.di.reg.handlers.TraditionalLoginHandler;
import com.philips.cl.di.reg.ui.utils.RLog;
import com.philips.cl.di.reg.ui.utils.ValidatorUtility;

public class SignInAccountFragment extends RegistrationBaseFragment
		implements OnClickListener, TraditionalLoginHandler {

	private LinearLayout mFirstLayout = null;
	private LinearLayout mSecondLayout = null;
	private View mView;
	private LinearLayout.LayoutParams mParams = null;
	private Button mSigninBtn;
	private EditText mEtEmail, mEtPassword;
	private User mUser;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		RLog.d(RLog.FRAGMENT_LIFECYCLE,
				"UserPhilipsAccountSignInFragment : onCreateView");
		mView = inflater.inflate(R.layout.sign_in_account_fragment, null);
		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		RLog.d(RLog.FRAGMENT_LIFECYCLE,
				"UserPhilipsAccountSignInFragment : onActivityCreated");
		initialize();
		Configuration config = getResources().getConfiguration();
		setViewParams(config);
	}

	@Override
	public void onConfigurationChanged(Configuration config) {
		super.onConfigurationChanged(config);
		RLog.d(RLog.FRAGMENT_LIFECYCLE,
				"UserPhilipsAccountSignInFragment : onConfigurationChanged");
		setViewParams(config);
	}

	@Override
	public void setViewParams(Configuration config) {
		RLog.d(RLog.FRAGMENT_LIFECYCLE,
				"UserPhilipsAccountSignInFragment : setViewParams");
		if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
			mParams.leftMargin = mParams.rightMargin = mLeftRightMarginPort;
		} else {
			mParams.leftMargin = mParams.rightMargin = mLeftRightMarginLand;
		}
		mFirstLayout.setLayoutParams(mParams);
		mSecondLayout.setLayoutParams(mParams);

	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.sign_in_btn) {
			signIn();
		} else {
		}
	}

	private void initialize() {
		mSigninBtn = (Button) mView.findViewById(R.id.sign_in_btn);
		mSigninBtn.setOnClickListener(this);
		mFirstLayout = (LinearLayout) getActivity().findViewById(
				R.id.first_part);
		mSecondLayout = (LinearLayout) getActivity().findViewById(
				R.id.second_part);
		mParams = (LayoutParams) mFirstLayout.getLayoutParams();
		mEtEmail = (EditText) mView.findViewById(R.id.et_signin_email);
		mEtPassword = (EditText) mView.findViewById(R.id.et_signin_password);
		mUser = new User(getActivity().getApplicationContext());
	}

	@Override
	public String getActionbarTitle() {
		return getResources().getString(R.string.sign_in);
	}

	private void signIn() {
		boolean isValidPassword = validatePassword();
		boolean isEmailPassword = validateEmail();

		if (isValidPassword && isEmailPassword) {
			if (mUser != null)
				mUser.loginUsingTraditional(mEtEmail.getText().toString(),
						mEtPassword.getText().toString(), this);
		}

	}

	private boolean validatePassword() {
		if (!isValidPassword(mEtPassword.getText().toString())) {
			Toast.makeText(getActivity(), "Password Wrong", Toast.LENGTH_LONG)
					.show();
			return false;
		}

		return true;
	}

	private boolean validateEmail() {
		if (!isValidEmail(mEtEmail.getText().toString())) {
			Toast.makeText(getActivity(), "Email Wrong", Toast.LENGTH_LONG)
					.show();
			return false;
		}
		return true;
	}

	private boolean isValidEmail(String pMail) {
		if (pMail == null)
			return false;
		if (pMail.length() == 0)
			return false;

		return ValidatorUtility.isValidEmail(pMail);
	}

	private boolean isValidPassword(String pPassword) {
		if (pPassword == null)
			return false;
		if (pPassword.length() > 0)
			return true;
		return false;
	}

	@Override
	public void onLoginSuccess() {
		Toast.makeText(getActivity(), "SignIN Success", Toast.LENGTH_LONG)
				.show();
		((RegistrationActivity) getActivity())
				.addFragment(new WelcomeFragment());
	}

	@Override
	public void onLoginFailedWithError(int error) {
		Toast.makeText(getActivity(), "SignIN Failure", Toast.LENGTH_LONG)
				.show();
	}

}
