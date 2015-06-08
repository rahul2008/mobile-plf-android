
package com.philips.cl.di.reg.ui.traditional;

import android.content.Context;
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

import com.philips.cl.di.reg.R;
import com.philips.cl.di.reg.User;
import com.philips.cl.di.reg.dao.UserRegistrationFailureInfo;
import com.philips.cl.di.reg.events.EventHelper;
import com.philips.cl.di.reg.events.EventListener;
import com.philips.cl.di.reg.events.NetworStateListener;
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

public class CreateAccountFragment extends RegistrationBaseFragment implements OnClickListener,
        TraditionalRegistrationHandler, onUpdateListener, NetworStateListener, EventListener {

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

	private XRegError mRegError;

	private ProgressBar mPbSpinner;

	private Context mContext;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "UserCreateAccountFragment : onCreateView");
		mContext = getRegistrationMainActivity().getApplicationContext();

		RegistrationHelper.getInstance().registerNetworkStateListener(this);
		EventHelper.getInstance()
		        .registerEventNotification(RegConstants.JANRAIN_INIT_SUCCESS, this);
		EventHelper.getInstance()
		        .registerEventNotification(RegConstants.JANRAIN_INIT_FAILURE, this);
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
	public void onDestroy() {
		RegistrationHelper.getInstance().unRegisterNetworkListener(this);
		EventHelper.getInstance().unregisterEventNotification(RegConstants.JANRAIN_INIT_SUCCESS,
		        this);
		EventHelper.getInstance().unregisterEventNotification(RegConstants.JANRAIN_INIT_FAILURE,
		        this);
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
		mTvpasswordDetails = (TextView) view.findViewById(R.id.tv_reg_password_details);
		mLlCreateAccountContainer = (LinearLayout) view
		        .findViewById(R.id.ll_reg_create_account_container);
		mRlCreateActtBtnContainer = (RelativeLayout) view.findViewById(R.id.rl_reg_singin_options);

		mBtnCreateAccount = (Button) view.findViewById(R.id.btn_reg_register);
		mCbTerms = (CheckBox) view.findViewById(R.id.cb_reg_register_terms);
		FontLoader.getInstance().setTypeface(mCbTerms, "CentraleSans-Light.otf");

		mBtnCreateAccount.setOnClickListener(this);

		mEtName = (XUserName) view.findViewById(R.id.rl_reg_name_field);
		mEtName.setOnUpdateListener(this);
		mEtEmail = (XEmail) view.findViewById(R.id.rl_reg_email_field);
		mEtEmail.setOnUpdateListener(this);
		mEtPassword = (XPassword) view.findViewById(R.id.rl_reg_password_field);
		mEtPassword.setOnUpdateListener(this);
		mPbSpinner = (ProgressBar) view.findViewById(R.id.pb_reg_activate_spinner);
		mPbSpinner.setClickable(false);
		mPbSpinner.setEnabled(true);
		mRegError = (XRegError) view.findViewById(R.id.reg_error_msg);
		setViewParams(getResources().getConfiguration());
		handleUiState();
		mUser = new User(mContext);
	}

	private void register() {
		showSpinner();
		mEtName.clearFocus();
		mEtEmail.clearFocus();
		mEtPassword.clearFocus();
		mUser.registerUserInfoForTraditional(mEtName.getName().toString(), mEtEmail.getEmailId()
		        .toString(), mEtPassword.getPassword().toString(), true, mCbTerms.isChecked(), this);
	}

	private void showSpinner() {
		mPbSpinner.setVisibility(View.VISIBLE);
		mBtnCreateAccount.setEnabled(false);
	}

	private void hideSpinner() {
		mPbSpinner.setVisibility(View.INVISIBLE);
		mBtnCreateAccount.setEnabled(true);
	}

	private void handleUiState() {
		if (NetworkUtility.isNetworkAvailable(mContext)) {
			if (RegistrationHelper.getInstance().isJanrainIntialized()) {
				mRegError.hideError();
			} else {
				mRegError.setError(mContext.getResources().getString(R.string.NoNetworkConnection));
			}
		} else {
			mRegError.setError(mContext.getResources().getString(R.string.NoNetworkConnection));
		}
	}

	@Override
	public void onRegisterSuccess() {
		hideSpinner();
		getRegistrationMainActivity().addFragment(new AccountActivationFragment());
	}

	@Override
	public void onRegisterFailedWithFailure(UserRegistrationFailureInfo userRegistrationFailureInfo) {

		if (null != userRegistrationFailureInfo.getEmailErrorMessage()) {
			mEtEmail.setErrDescription(userRegistrationFailureInfo.getEmailErrorMessage());
			mEtEmail.showInvalidAlert();
		}
		if (null != userRegistrationFailureInfo.getPasswordErrorMessage()) {
			mEtPassword.setErrDescription(userRegistrationFailureInfo.getPasswordErrorMessage());
			mEtPassword.showInvalidAlert();
		}

		if (null != userRegistrationFailureInfo.getFirstNameErrorMessage()) {
			mEtName.setErrDescription(userRegistrationFailureInfo.getFirstNameErrorMessage());
			mEtName.showInvalidAlert();
		}

		mRegError.setError(userRegistrationFailureInfo.getErrorDescription());

		hideSpinner();
	}

	@Override
	public String getActionbarTitle() {
		return getResources().getString(R.string.RegCreateAccount_NavTitle);
	}

	@Override
	public void onUpadte() {
		updateUiStatus();
	}

	private void updateUiStatus() {
		if (mEtName.isValidName() && mEtEmail.isValidEmail() && mEtPassword.isValidPassword()
		        && NetworkUtility.isNetworkAvailable(mContext)
		        && RegistrationHelper.getInstance().isJanrainIntialized()) {
			mBtnCreateAccount.setEnabled(true);
			mRegError.hideError();
		} else {
			mBtnCreateAccount.setEnabled(false);
		}
	}

	@Override
	public void onEventReceived(String event) {
		if (RegConstants.JANRAIN_INIT_SUCCESS.equals(event)) {
			updateUiStatus();
		}
	}

	@Override
	public void onNetWorkStateReceived(boolean isOnline) {
		handleUiState();
		updateUiStatus();
	}

}
