package com.philips.cl.di.reg.ui.social;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cl.di.reg.R;
import com.philips.cl.di.reg.User;
import com.philips.cl.di.reg.dao.ForgotPasswordFailureInfo;
import com.philips.cl.di.reg.dao.SignInTraditionalFailuerInfo;
import com.philips.cl.di.reg.events.EventHelper;
import com.philips.cl.di.reg.events.EventListener;
import com.philips.cl.di.reg.handlers.ForgotPasswordHandler;
import com.philips.cl.di.reg.handlers.TraditionalLoginHandler;
import com.philips.cl.di.reg.settings.RegistrationHelper;
import com.philips.cl.di.reg.ui.customviews.XButton;
import com.philips.cl.di.reg.ui.customviews.XEmail;
import com.philips.cl.di.reg.ui.customviews.XPassword;
import com.philips.cl.di.reg.ui.customviews.XRegError;
import com.philips.cl.di.reg.ui.customviews.onUpdateListener;
import com.philips.cl.di.reg.ui.traditional.RegistrationBaseFragment;
import com.philips.cl.di.reg.ui.utils.EmailValidator;
import com.philips.cl.di.reg.ui.utils.JanrainErrorMessage;
import com.philips.cl.di.reg.ui.utils.NetworkUtility;
import com.philips.cl.di.reg.ui.utils.RLog;
import com.philips.cl.di.reg.ui.utils.RegAlertDialog;
import com.philips.cl.di.reg.ui.utils.RegConstants;

public class MergeAccountFragment extends RegistrationBaseFragment implements EventListener, onUpdateListener
,TraditionalLoginHandler,ForgotPasswordHandler,OnClickListener{

	private TextView mTvAccountMergeSignIn;
	private LinearLayout mLlUsedEMailAddressContainer;
	private LinearLayout mLlCreateAccountFields;
	private RelativeLayout mRlSingInOptions;
	private XRegError mRegError;
	private XButton mBtnMerge;
	private XButton mBtnForgotPassword;
	private XEmail mEtEmail;
	private XPassword mEtPassword;
	private ProgressBar mPbMergeSpinner;
	private ProgressBar mPbForgotPaswwordSpinner;
	private String mMergeToken;
	private User mUser;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		RLog.d(RLog.FRAGMENT_LIFECYCLE,
				"MergeAccountFragment : onCreateView");
		EventHelper.getInstance().registerEventNotification(
				RegConstants.IS_ONLINE, this);
		EventHelper.getInstance().registerEventNotification(
				RegConstants.JANRAIN_INIT_SUCCESS, this);
		EventHelper.getInstance().registerEventNotification(
				RegConstants.JANRAIN_INIT_FAILURE, this);
		View view = inflater.inflate(R.layout.fragment_social_merge_account, container, false);
		initUI(view);
		handleUiErrorState();
		return view;
	}
	
	@Override
	public void onDestroy() {
		RLog.d(RLog.FRAGMENT_LIFECYCLE,
				"MergeAccountFragment : onDestroy");
		EventHelper.getInstance().unregisterEventNotification(
				RegConstants.IS_ONLINE, this);
		EventHelper.getInstance().unregisterEventNotification(
				RegConstants.JANRAIN_INIT_SUCCESS, this);
		EventHelper.getInstance().unregisterEventNotification(
				RegConstants.JANRAIN_INIT_FAILURE, this);
		super.onDestroy();
	}
	
	private void initUI(View view) {
		consumeTouch(view);
		Bundle bundle = this.getArguments();
		mBtnMerge = (XButton)view.findViewById(R.id.btn_reg_merg);
		mBtnMerge.setOnClickListener(this);
		
		mBtnForgotPassword = (XButton)view.findViewById(R.id.btn_reg_forgot_password);
		mBtnForgotPassword.setOnClickListener(this);
		mTvAccountMergeSignIn = (TextView) view
				.findViewById(R.id.tv_reg_account_merge_sign_in);
		mLlUsedEMailAddressContainer = (LinearLayout) view
				.findViewById(R.id.ll_reg_used_email_address_container);

		mLlCreateAccountFields = (LinearLayout) view
				.findViewById(R.id.ll_reg_create_account_fields);

		mRlSingInOptions = (RelativeLayout) view
				.findViewById(R.id.rl_reg_btn_container);
		mRegError = (XRegError) view.findViewById(R.id.reg_error_msg);
		mEtEmail = (XEmail) view.findViewById(R.id.rl_reg_email_field);
		mEtEmail.setOnUpdateListener(this);
		mEtPassword = (XPassword) view.findViewById(R.id.rl_reg_password_field);
		mEtPassword.setOnUpdateListener(this);
		
		mPbMergeSpinner = (ProgressBar) view
				.findViewById(R.id.pb_reg_merge_sign_in_spinner);
		mPbMergeSpinner.setClickable(false);
		mPbMergeSpinner.setEnabled(true);
		
		mPbForgotPaswwordSpinner = (ProgressBar) view
				.findViewById(R.id.pb_reg_forgot_spinner);
		mPbForgotPaswwordSpinner.setClickable(false);
		mPbForgotPaswwordSpinner.setEnabled(true);
		
		
		mMergeToken = bundle.getString(RegConstants.SOCIAL_MERGE_TOKEN);
		setViewParams(getResources().getConfiguration());
		
		 mUser = new User(getRegistrationMainActivity().getApplicationContext());
	}
	
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_reg_merg) {
			if (mEtEmail.hasFocus()) {
				mEtEmail.clearFocus();
			} else if (mEtPassword.hasFocus()) {
				mEtPassword.clearFocus();
			}
			getView().requestFocus();
			mergeAccount();
		}else if (v.getId() == R.id.btn_reg_forgot_password) {
			resetPassword();
		}
	}
	
	private void mergeAccount() {
		
		if (NetworkUtility.getInstance().isOnline()) {
			showMergeSpinner();
			mUser.mergeToTraditionalAccount(mEtEmail.getEmailId(), mEtPassword.getPassword(), mMergeToken, this);
		} else {
			mRegError.setError(getString(R.string.JanRain_Error_Check_Internet));
		}
	}
	
	private void resetPassword() {
		boolean validatorResult = EmailValidator.isValidEmail(mEtEmail
				.getEmailId().toString());
		if (!validatorResult) {
			mEtEmail.showInvalidAlert();
		} else {
			if (NetworkUtility.getInstance().isOnline()) {
				if (mUser != null) {
					showForgotPasswordSpinner();
					mEtEmail.clearFocus();
					mEtPassword.clearFocus();
					mBtnMerge.setEnabled(false);
					mUser.forgotPassword(mEtEmail.getEmailId().toString(), this);
				}

			} else {
				mRegError.setError(getString(R.string.NoNetworkConnection));
			}
		}
	}
	
	private void showMergeSpinner() {
		mPbMergeSpinner.setVisibility(View.VISIBLE);
		mBtnMerge.setEnabled(false);
	}

	private void hideMergeSpinner() {
		mPbMergeSpinner.setVisibility(View.INVISIBLE);
		mBtnMerge.setEnabled(true);
	}
	
	private void showForgotPasswordSpinner() {
		mPbForgotPaswwordSpinner.setVisibility(View.VISIBLE);
		mBtnForgotPassword.setEnabled(false);
	}

	private void hideForgotPasswordSpinner() {
		mPbForgotPaswwordSpinner.setVisibility(View.INVISIBLE);
		mBtnForgotPassword.setEnabled(true);
	}
	
	private void handleUiErrorState() {
		if (NetworkUtility.getInstance().isOnline()) {
			if (RegistrationHelper.isJanrainIntialized()) {
				mRegError.hideError();
			} else {
				mRegError.setError(getString(R.string.NoNetworkConnection));
			}
		} else {
			mRegError.setError(getString(R.string.NoNetworkConnection));
		}
	}
	
	private void updateUiStatus() {
		if (mEtEmail.isValidEmail()
				&& mEtPassword.isValidPassword()
				&& NetworkUtility.getInstance().isOnline()
				&& RegistrationHelper.isJanrainIntialized()) {
			mBtnMerge.setEnabled(true);
			mBtnForgotPassword.setEnabled(true);
			mRegError.hideError();
		} else {
			mBtnMerge.setEnabled(false);
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
	
	@Override
	public void onConfigurationChanged(Configuration config) {
		super.onConfigurationChanged(config);
		RLog.d(RLog.FRAGMENT_LIFECYCLE,
				"SocialMergeAccountFragment : onConfigurationChanged");
		setViewParams(config);
	}

	@Override
	public void setViewParams(Configuration config) {
		applyParams(config, mTvAccountMergeSignIn);
		applyParams(config, mLlUsedEMailAddressContainer);
		applyParams(config, mLlCreateAccountFields);
		applyParams(config, mRlSingInOptions);
		applyParams(config, mRegError);
	}
	
	@Override
	public void onUpadte() {
		updateUiStatus();
	}

	@Override
	public String getActionbarTitle() {
		return getResources().getString(R.string.SigIn_TitleTxt);
	}

	@Override
	public void onLoginSuccess() {
		hideMergeSpinner();
		getRegistrationMainActivity().addWelcomeFragmentOnVerification();
	}

	@Override
	public void onLoginFailedWithError(SignInTraditionalFailuerInfo signInTraditionalFailuerInfo) {
		 hideMergeSpinner();
		
		if (signInTraditionalFailuerInfo.getError().captureApiError.code == RegConstants.INVALID_CREDENTIALS_ERROR_CODE) {

			handleInvalidCredentials(signInTraditionalFailuerInfo);
			return ;
		}
		
		if (signInTraditionalFailuerInfo.getError().captureApiError.code == RegConstants.INVALID_FIELDS_ERROR_CODE) {

			handleInvalidCredentials(signInTraditionalFailuerInfo);
			return ;
		}
		
		mRegError.setError(signInTraditionalFailuerInfo.getErrorDescription());
	}

	private void handleInvalidCredentials(
			SignInTraditionalFailuerInfo signInTraditionalFailuerInfo) {
		if (null != signInTraditionalFailuerInfo.getEmailErrorMessage()) {
			mEtEmail.setErrDescription(signInTraditionalFailuerInfo
					.getEmailErrorMessage());
			mEtEmail.showInvalidAlert();
		}

		if (null != signInTraditionalFailuerInfo.getPasswordErrorMessage()) {

			mEtPassword.setErrDescription(signInTraditionalFailuerInfo
					.getPasswordErrorMessage());
			mEtPassword.showInvalidAlert();
		}

		mRegError.setError(signInTraditionalFailuerInfo
				.getErrorDescription());
	}

	@Override
	public void onSendForgotPasswordSuccess() {
		RegAlertDialog.showResetPasswordDialog(getRegistrationMainActivity());
		hideForgotPasswordSpinner();
		mRegError.hideError();
	}

	@Override
	public void onSendForgotPasswordFailedWithError(
			ForgotPasswordFailureInfo forgotPasswordFailureInfo) {
		hideForgotPasswordSpinner();
		
		if(forgotPasswordFailureInfo.getError().captureApiError.code == RegConstants.ONLY_SOCIAL_SIGN_IN_ERROR_CODE){
			mEtEmail.setErrDescription(forgotPasswordFailureInfo
					.getEmailErrorMessage());
			mEtEmail.showInvalidAlert();
			mRegError.setError(forgotPasswordFailureInfo
					.getEmailErrorMessage());
			return;
			
		}
		
		if (forgotPasswordFailureInfo.getError().captureApiError.code == RegConstants.NO_SUCH_ACCOUNT_ERROR_CODE) {

			if (null != forgotPasswordFailureInfo.getEmailErrorMessage()) {
				mEtEmail.setErrDescription(forgotPasswordFailureInfo
						.getEmailErrorMessage());
				mEtEmail.showInvalidAlert();
			}

			mRegError.setError(forgotPasswordFailureInfo.getErrorDescription());

		} else {

			JanrainErrorMessage errorMessage = new JanrainErrorMessage(
					getActivity());
			String message = errorMessage.getError(forgotPasswordFailureInfo
					.getErrorCode());
			mRegError.setError(message);
			updateUiStatus();
			mEtPassword.setErrDescription(message);
			mEtEmail.setErrDescription(message);
			mEtEmail.showInvalidAlert();
			mEtPassword.showJanarainError();

		}
		
	}

}
