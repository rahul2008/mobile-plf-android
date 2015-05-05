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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cl.di.reg.R;
import com.philips.cl.di.reg.User;
import com.philips.cl.di.reg.events.EventHelper;
import com.philips.cl.di.reg.events.EventListener;
import com.philips.cl.di.reg.handlers.TraditionalRegistrationHandler;
import com.philips.cl.di.reg.settings.RegistrationHelper;
import com.philips.cl.di.reg.ui.customviews.XEmail;
import com.philips.cl.di.reg.ui.customviews.XPassword;
import com.philips.cl.di.reg.ui.customviews.XRegError;
import com.philips.cl.di.reg.ui.customviews.XUserName;
import com.philips.cl.di.reg.ui.customviews.onUpdateListener;
import com.philips.cl.di.reg.ui.utils.FontLoader;
import com.philips.cl.di.reg.ui.utils.NetworkUtility;
import com.philips.cl.di.reg.ui.utils.RLog;
import com.philips.cl.di.reg.ui.utils.RegConstants;

public class CreateAccountFragment extends RegistrationBaseFragment implements
		OnClickListener, TraditionalRegistrationHandler, onUpdateListener,
		EventListener {

	private LinearLayout mLlCreateAccountFields;
	private TextView mTvpasswordDetails;
	private LinearLayout mLlCreateAccountContainer;
	private RelativeLayout mRlCreateActtBtnContainer;
	private Button mBtnCreateAccount;
	private CheckBox mCbTerms;
	private User mUser;
	private XUserName mEtName;
	private XEmail mEtEmail;
	private XPassword mEtPassword;
	private final int EMAIL_ALEADY_EXIST = 14;
	private XRegError mRegError;
	private ProgressBar mPbSpinner;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		RLog.d(RLog.FRAGMENT_LIFECYCLE,
				"UserCreateAccountFragment : onCreateView");
		EventHelper.getInstance().registerEventNotification(
				RegConstants.IS_ONLINE, this);
		EventHelper.getInstance().registerEventNotification(
				RegConstants.JANRAIN_INIT_SUCCESS, this);
		EventHelper.getInstance().registerEventNotification(
				RegConstants.JANRAIN_INIT_FAILURE, this);
		View view = inflater.inflate(R.layout.fragment_create_account,
				container, false);
		initUI(view);
		return view;
	}

	@Override
	public void onConfigurationChanged(Configuration config) {
		RLog.d(RLog.FRAGMENT_LIFECYCLE,
				"UserCreateAccountFragment : onConfigurationChanged");
		super.onConfigurationChanged(config);
		setViewParams(config);
	}

	@Override
	public void onDestroy() {
		EventHelper.getInstance().unregisterEventNotification(
				RegConstants.IS_ONLINE, this);
		EventHelper.getInstance().unregisterEventNotification(
				RegConstants.JANRAIN_INIT_SUCCESS, this);
		EventHelper.getInstance().unregisterEventNotification(
				RegConstants.JANRAIN_INIT_FAILURE, this);
		super.onDestroy();
	}

	@Override
	public void setViewParams(Configuration config) {
		applyParams(config, mLlCreateAccountFields);
		applyParams(config, mTvpasswordDetails);
		applyParams(config, mLlCreateAccountContainer);
		applyParams(config, mRlCreateActtBtnContainer);
		applyParams(config, mRegError);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_reg_register) {
			register();
		}
	}

	private void initUI(View view) {
		consumeTouch(view);
		mLlCreateAccountFields = (LinearLayout) view
				.findViewById(R.id.ll_reg_create_account_fields);
		mTvpasswordDetails = (TextView) view
				.findViewById(R.id.tv_reg_password_details);
		mLlCreateAccountContainer = (LinearLayout) view
				.findViewById(R.id.ll_reg_create_account_container);
		mRlCreateActtBtnContainer = (RelativeLayout) view
				.findViewById(R.id.rl_reg_singin_options);

		mBtnCreateAccount = (Button) view.findViewById(R.id.btn_reg_register);
		mCbTerms = (CheckBox) view.findViewById(R.id.cb_reg_register_terms);
		FontLoader.getInstance().setTypeface(mCbTerms,
				"fonts/CentraleSans-Light.otf");

		mBtnCreateAccount.setOnClickListener(this);

		mEtName = (XUserName) view.findViewById(R.id.rl_reg_name_field);
		mEtName.setOnUpdateListener(this);
		mEtEmail = (XEmail) view.findViewById(R.id.rl_reg_email_field);
		mEtEmail.setOnUpdateListener(this);
		mEtPassword = (XPassword) view.findViewById(R.id.rl_reg_password_field);
		mEtPassword.setOnUpdateListener(this);
		mPbSpinner = (ProgressBar) view
				.findViewById(R.id.pb_reg_activate_spinner);
		mPbSpinner.setClickable(false);
		mPbSpinner.setEnabled(true);
		mRegError = (XRegError) view.findViewById(R.id.reg_error_msg);

		setViewParams(getResources().getConfiguration());
		handleUiErrorState();
		mUser = new User(getActivity().getApplicationContext());
	}

	private void register() {
		showSpinner();
		mEtName.clearFocus();
		mEtEmail.clearFocus();
		mEtPassword.clearFocus();
		mUser.registerUserInfoForTraditional(mEtName.getName().toString(),
				mEtEmail.getEmailId().toString(), mEtPassword.getPassword()
						.toString(), true, mCbTerms.isChecked(), this);
	}

	private void showSpinner() {
		mPbSpinner.setVisibility(View.VISIBLE);
		mBtnCreateAccount.setEnabled(false);
	}

	private void hideSpinner() {
		mPbSpinner.setVisibility(View.INVISIBLE);
		mBtnCreateAccount.setEnabled(true);
	}

	private void handleUiErrorState() {
		if (NetworkUtility.getInstance().isOnline()) {
			if (RegistrationHelper.isJanrainIntialized()) {
				mRegError.hideError();
			} else {
				mRegError.setError(getString(R.string.No_Internet_Connection));
			}
		} else {
			mRegError.setError(getString(R.string.No_Internet_Connection));
		}
	}

	@Override
	public void onRegisterSuccess() {
		hideSpinner();
		Toast.makeText(getActivity(), "Registration Success", Toast.LENGTH_LONG)
				.show();
		getRegistrationMainActivity().addFragment(
				new AccountActivationFragment());
	}

	@Override
	public void onRegisterFailedWithFailure(int errorType) {

		if (errorType == EMAIL_ALEADY_EXIST) {
			mEtEmail.setErrDescription(getResources().getString(
					R.string.email_already_used));
			mEtEmail.showInvalidEmailAlert();
			mRegError.setError(getResources().getString(
					R.string.email_already_used));
		} else {
			mRegError.setError(getString(R.string.No_Internet_Connection));
		}
		hideSpinner();
	}

	@Override
	public String getActionbarTitle() {
		return getResources().getString(R.string.create_account);
	}

	@Override
	public void onUpadte() {
		updateUiStatus();
	}

	private void updateUiStatus() {
		if (mEtName.ismValidName() && mEtEmail.isValidEmail()
				&& mEtPassword.isValidPassword()
				&& NetworkUtility.getInstance().isOnline()
				&& RegistrationHelper.isJanrainIntialized()) {
			mBtnCreateAccount.setEnabled(true);
			mRegError.hideError();
		} else {
			mBtnCreateAccount.setEnabled(false);
		}
	}

	@Override
	public void onEventReceived(String event) {
		if (RegConstants.IS_ONLINE.equals(event)) {
			handleUiErrorState();
			updateUiStatus();
		} else if (RegConstants.JANRAIN_INIT_SUCCESS.equals(event)) {
			updateUiStatus();
		}
	}
}
