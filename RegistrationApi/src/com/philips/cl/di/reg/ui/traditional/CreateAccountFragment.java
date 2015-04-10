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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cl.di.reg.R;
import com.philips.cl.di.reg.User;
import com.philips.cl.di.reg.dao.DIUserProfile;
import com.philips.cl.di.reg.handlers.TraditionalRegistrationHandler;
import com.philips.cl.di.reg.ui.utils.RLog;
import com.philips.cl.di.reg.ui.utils.ValidatorUtility;

public class CreateAccountFragment extends RegistrationBaseFragment
		implements OnClickListener, TraditionalRegistrationHandler {

	private View mView;
	private LinearLayout mFirstLayout = null;
	private TextView mSecondLayout = null;
	private LinearLayout mThirdLayout = null;
	private LinearLayout mFourthLayout = null;
	private LinearLayout.LayoutParams mParams = null;
	private Button mBtnCreateAccount;
	private EditText mEtUserName, mEtEmail, mEtPassword;
	private CheckBox mCbTerms;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		RLog.d(RLog.FRAGMENT_LIFECYCLE,
				"UserCreateAccountFragment : onCreateView");
		mView = inflater.inflate(R.layout.create_account_fragment, null);
		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		RLog.d(RLog.FRAGMENT_LIFECYCLE,
				"UserCreateAccountFragment : onActivityCreated");
		mFirstLayout = (LinearLayout) getActivity().findViewById(
				R.id.first_part);

		mSecondLayout = (TextView) getActivity().findViewById(
				R.id.password_details);

		mThirdLayout = (LinearLayout) getActivity().findViewById(
				R.id.third_part);

		mFourthLayout = (LinearLayout) getActivity().findViewById(
				R.id.fourth_part);

		mParams = (LayoutParams) mFirstLayout.getLayoutParams();
		Configuration config = getResources().getConfiguration();
		setViewParams(config);
		initialize();

	}

	@Override
	public void onConfigurationChanged(Configuration config) {
		RLog.d(RLog.FRAGMENT_LIFECYCLE,
				"UserCreateAccountFragment : onConfigurationChanged");
		super.onConfigurationChanged(config);
		setViewParams(config);
	}

	@Override
	public void setViewParams(Configuration config) {
		RLog.d(RLog.FRAGMENT_LIFECYCLE,
				"UserCreateAccountFragment : setViewParams");

		if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
			mParams.leftMargin = mParams.rightMargin = mLeftRightMarginPort;
		} else {
			mParams.leftMargin = mParams.rightMargin = mLeftRightMarginLand;
		}
		mFirstLayout.setLayoutParams(mParams);
		mSecondLayout.setLayoutParams(mParams);
		mThirdLayout.setLayoutParams(mParams);
		mFourthLayout.setLayoutParams(mParams);

	}

	@Override
	public String getActionbarTitle() {
		return getResources().getString(R.string.create_account);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.btn_register) {
			register();
		} else {
			
		}
	}

	private void initialize() {
		mBtnCreateAccount = (Button) mView.findViewById(R.id.btn_register);
		mEtUserName = (EditText) mView.findViewById(R.id.et_reg_fname);
		mEtEmail = (EditText) mView.findViewById(R.id.et_reg_email);
		mEtPassword = (EditText) mView.findViewById(R.id.et_reg_password);
		mCbTerms = (CheckBox) mView.findViewById(R.id.cb_register_terms);
		mBtnCreateAccount.setOnClickListener(this);
	}

	private void register() {
		boolean isValidPassword = validatePassword();
		boolean isEmailPassword = validateEmail();
		boolean isNamePassword = validateName();
		if (isValidPassword && isEmailPassword && isNamePassword) {
			ArrayList<DIUserProfile> userProfileArray = new ArrayList<DIUserProfile>();
			User user = new User(getActivity());
			DIUserProfile profileab = new DIUserProfile();
			profileab.setGivenName(mEtUserName.getText().toString());
			profileab.setEmail(mEtEmail.getText().toString());
			profileab.setPassword(mEtPassword.getText().toString());
			profileab.setOlderThanAgeLimit(true);
			profileab.setReceiveMarketingEmail(mCbTerms.isChecked());
			userProfileArray.add(profileab);
			user.registerNewUserUsingTraditional(userProfileArray, this);
		}
	}

	private boolean validateName() {
		if (!isValidName(mEtUserName.getText().toString())) {
			Toast.makeText(getActivity(), "Name Wrong", Toast.LENGTH_LONG)
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

	private boolean validatePassword() {
		if (!isValidPassword(mEtPassword.getText().toString())) {
			Toast.makeText(getActivity(), "Password Wrong", Toast.LENGTH_LONG)
					.show();
			return false;
		}
		return true;
	}

	private boolean isValidName(String pName) {
		if (pName == null)
			return false;
		if (pName.length() > 0)
			return true;

		return false;
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
		if (pPassword.length() < 8)
			return false;
		return ValidatorUtility.isValidPassword(pPassword);
	}

	@Override
	public void onRegisterSuccess() {
		Toast.makeText(getActivity(), "Registration Success", Toast.LENGTH_LONG)
				.show();
	}

	@Override
	public void onRegisterFailedWithFailure(int error) {
		Toast.makeText(getActivity(), "Registration Failure", Toast.LENGTH_LONG)
				.show();

	}

}
