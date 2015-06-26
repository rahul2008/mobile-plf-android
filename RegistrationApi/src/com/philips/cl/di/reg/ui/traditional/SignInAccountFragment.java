
package com.philips.cl.di.reg.ui.traditional;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.philips.cl.di.reg.R;
import com.philips.cl.di.reg.User;
import com.philips.cl.di.reg.adobe.analytics.AnalyticsConstants;
import com.philips.cl.di.reg.adobe.analytics.AnalyticsPages;
import com.philips.cl.di.reg.dao.UserRegistrationFailureInfo;
import com.philips.cl.di.reg.events.EventHelper;
import com.philips.cl.di.reg.events.EventListener;
import com.philips.cl.di.reg.events.NetworStateListener;
import com.philips.cl.di.reg.handlers.ForgotPasswordHandler;
import com.philips.cl.di.reg.handlers.TraditionalLoginHandler;
import com.philips.cl.di.reg.settings.RegistrationHelper;
import com.philips.cl.di.reg.ui.customviews.XEmail;
import com.philips.cl.di.reg.ui.customviews.XPassword;
import com.philips.cl.di.reg.ui.customviews.XRegError;
import com.philips.cl.di.reg.ui.customviews.onUpdateListener;
import com.philips.cl.di.reg.ui.utils.EmailValidator;
import com.philips.cl.di.reg.ui.utils.NetworkUtility;
import com.philips.cl.di.reg.ui.utils.RLog;
import com.philips.cl.di.reg.ui.utils.RegAlertDialog;
import com.philips.cl.di.reg.ui.utils.RegConstants;

public class SignInAccountFragment extends RegistrationBaseFragment implements OnClickListener,
        TraditionalLoginHandler, ForgotPasswordHandler, onUpdateListener, EventListener,
        NetworStateListener {

	private LinearLayout mLlCreateAccountFields;

	private RelativeLayout mRlSignInBtnContainer;

	private Button mBtnSignInAccount;

	private Button mBtnForgot;

	private Button mBtnResend;

	private XEmail mEtEmail;

	private XPassword mEtPassword;

	private User mUser;

	private ProgressBar mPbSignInSpinner;

	private ProgressBar mPbForgotPasswdSpinner;

	private XRegError mRegError;

	private Context mContext;

	@Override
	public void onAttach(Activity activity) {
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "HomeFragment : onAttach");
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "SignInAccountFragment : onCreate");
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "SignInAccountFragment : onCreateView");
		mContext = getRegistrationFragment().getParentActivity().getApplicationContext();
		RegistrationHelper.getInstance().registerNetworkStateListener(this);
		EventHelper.getInstance()
		        .registerEventNotification(RegConstants.JANRAIN_INIT_SUCCESS, this);
		View view = inflater.inflate(R.layout.fragment_sign_in_account, null);
		RLog.i(RLog.EVENT_LISTENERS,
		        "SignInAccountFragment register: NetworStateListener,JANRAIN_INIT_SUCCESS");
		initUI(view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "SignInAccountFragment : onActivityCreated");
	}

	@Override
	public void onStart() {
		super.onStart();
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "SignInAccountFragment : onStart");
	}

	@Override
	public void onResume() {
		super.onResume();
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "SignInAccountFragment : onResume");
	}

	@Override
	public void onPause() {
		super.onPause();
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "SignInAccountFragment : onPause");
	}

	@Override
	public void onStop() {
		super.onStop();
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "SignInAccountFragment : onStop");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "SignInAccountFragment : onDestroyView");
	}

	@Override
	public void onDestroy() {
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "SignInAccountFragment : onDestroy");
		RegistrationHelper.getInstance().unRegisterNetworkListener(this);
		EventHelper.getInstance().unregisterEventNotification(RegConstants.JANRAIN_INIT_SUCCESS,
		        this);
		RLog.i(RLog.EVENT_LISTENERS,
		        "SignInAccountFragment unregister: NetworStateListener,JANRAIN_INIT_SUCCESS");
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		super.onDetach();
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "SignInAccountFragment : onDetach");
	}

	@Override
	public void onConfigurationChanged(Configuration config) {
		super.onConfigurationChanged(config);
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "SignInAccountFragment : onConfigurationChanged");
		setViewParams(config);
	}

	@Override
	public void setViewParams(Configuration config) {
		applyParams(config, mLlCreateAccountFields);
		applyParams(config, mRlSignInBtnContainer);
		applyParams(config, mRegError);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.btn_reg_sign_in) {
			RLog.d(RLog.ONCLICK, "SignInAccountFragment : SignIn");
			signIn();
		} else if (id == R.id.btn_reg_forgot_password) {
			RLog.d(RLog.ONCLICK, "SignInAccountFragment : Forgot Password");
			resetPassword();

		} else if (id == R.id.btn_reg_resend) {
			RLog.d(RLog.ONCLICK, "SignInAccountFragment : Resend");
			mEtEmail.clearFocus();
			mEtPassword.clearFocus();
			lauchAccountActivationFragment();
		}
	}

	private void lauchAccountActivationFragment() {
		getRegistrationFragment().addFragment(new AccountActivationFragment());
		trackPage(AnalyticsPages.SIGN_IN_ACCOUNT, AnalyticsPages.ACCOUNT_ACTIVATION);
	}

	private void initUI(View view) {
		consumeTouch(view);
		mBtnSignInAccount = (Button) view.findViewById(R.id.btn_reg_sign_in);
		mBtnSignInAccount.setOnClickListener(this);
		mBtnForgot = (Button) view.findViewById(R.id.btn_reg_forgot_password);
		mBtnForgot.setOnClickListener(this);
		mBtnResend = (Button) view.findViewById(R.id.btn_reg_resend);
		mBtnResend.setOnClickListener(this);
		mLlCreateAccountFields = (LinearLayout) view
		        .findViewById(R.id.ll_reg_create_account_fields);
		mRlSignInBtnContainer = (RelativeLayout) view.findViewById(R.id.rl_reg_welcome_container);

		mEtEmail = (XEmail) view.findViewById(R.id.rl_reg_email_field);
		mEtEmail.setOnClickListener(this);
		mEtEmail.setOnUpdateListener(this);
		mEtEmail.setFocusable(true);
		mEtPassword = (XPassword) view.findViewById(R.id.rl_reg_password_field);
		mEtPassword.setOnClickListener(this);
		mEtPassword.setOnUpdateListener(this);
		mEtPassword.isValidatePassword(false);
		mRegError = (XRegError) view.findViewById(R.id.reg_error_msg);
		setViewParams(getResources().getConfiguration());
		handleUiState();

		mUser = new User(mContext);
		mPbSignInSpinner = (ProgressBar) view.findViewById(R.id.pb_reg_sign_in_spinner);
		mPbForgotPasswdSpinner = (ProgressBar) view.findViewById(R.id.pb_reg_forgot_spinner);

	}

	@Override
	public int getTitleResourceId() {
		return R.string.SigIn_TitleTxt;
	}

	private void signIn() {
		((RegistrationFragment) getParentFragment()).hideKeyBoard();
		trackActionStatus(AnalyticsConstants.SEND_DATA, AnalyticsConstants.SPECIAL_EVENTS,
		        AnalyticsConstants.LOGIN_STARTS);
		if (mUser != null) {
			showSignInSpinner();
		}
		mEtEmail.clearFocus();
		mEtPassword.clearFocus();
		mBtnForgot.setEnabled(false);
		mBtnResend.setEnabled(false);
		mUser.loginUsingTraditional(mEtEmail.getEmailId().toString(), mEtPassword.getPassword()
		        .toString(), this);
	}

	private void handleUiState() {
		if (NetworkUtility.isNetworkAvailable(mContext)) {
			if (RegistrationHelper.getInstance().isJanrainIntialized()) {
				mRegError.hideError();
			} else {
				mRegError.setError(getString(R.string.NoNetworkConnection));
			}
		} else {
			trackActionLoginError(AnalyticsConstants.NETWORK_ERROR_CODE);
			mRegError.setError(getString(R.string.NoNetworkConnection));
		}
	}

	@Override
	public void onLoginSuccess() {
		RLog.i(RLog.CALLBACK, "SignInAccountFragment : onLoginSuccess");
		trackActionStatus(AnalyticsConstants.SEND_DATA, AnalyticsConstants.SPECIAL_EVENTS,
		        AnalyticsConstants.SUCCESS_LOGIN);
		hideSignInSpinner();
		mBtnForgot.setEnabled(true);
		mBtnResend.setEnabled(true);
		mRegError.hideError();
		if (mUser.getEmailVerificationStatus(getActivity())) {
			launchWelcomeFragment();
		} else {
			mRegError.setError(getString(R.string.Janrain_Error_Need_Email_Verification));
			mBtnResend.setVisibility(View.VISIBLE);
		}
	}

	private void launchWelcomeFragment() {
		getRegistrationFragment().addWelcomeFragmentOnVerification();
		trackPage(AnalyticsPages.SIGN_IN_ACCOUNT, AnalyticsPages.WELCOME);
	}

	@Override
	public void onLoginFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo) {
		RLog.i(RLog.CALLBACK, "SignInAccountFragment : onLoginFailedWithError");
		mBtnForgot.setEnabled(true);
		mBtnResend.setEnabled(true);
		hideSignInSpinner();

		if (null != userRegistrationFailureInfo.getEmailErrorMessage()) {
			mEtEmail.setErrDescription(userRegistrationFailureInfo.getEmailErrorMessage());
			mEtEmail.showInvalidAlert();
		}

		if (null != userRegistrationFailureInfo.getPasswordErrorMessage()) {
			mEtPassword.setErrDescription(userRegistrationFailureInfo.getPasswordErrorMessage());
			mEtPassword.showInvalidAlert();
		}
		trackActionLoginError(userRegistrationFailureInfo.getError().code);
		mRegError.setError(userRegistrationFailureInfo.getErrorDescription());
	}

	@Override
	public void onSendForgotPasswordSuccess() {
		RLog.i(RLog.CALLBACK, "SignInAccountFragment : onSendForgotPasswordSuccess");
		trackActionStatus(AnalyticsConstants.SEND_DATA, AnalyticsConstants.STATUS_NOTIFICATION,
		        AnalyticsConstants.RESET_PASSWORD_SUCCESS);
		hideForgotPasswordSpinner();
		RegAlertDialog.showResetPasswordDialog(getRegistrationFragment().getParentActivity(),
		        AnalyticsConstants.SIGN_IN);
		hideForgotPasswordSpinner();
		mBtnResend.setEnabled(true);
		mRegError.hideError();
	}

	@Override
	public void onSendForgotPasswordFailedWithError(
	        UserRegistrationFailureInfo userRegistrationFailureInfo) {
		RLog.i(RLog.CALLBACK, "SignInAccountFragment : onSendForgotPasswordFailedWithError");
		mBtnResend.setEnabled(true);
		hideForgotPasswordSpinner();

		if (null != userRegistrationFailureInfo.getSocialOnlyError()) {
			mEtEmail.setErrDescription(userRegistrationFailureInfo.getSocialOnlyError());
			mEtEmail.showInvalidAlert();
			mRegError.setError(userRegistrationFailureInfo.getSocialOnlyError());
			return;
		}

		if (null != userRegistrationFailureInfo.getEmailErrorMessage()) {
			mEtEmail.setErrDescription(userRegistrationFailureInfo.getEmailErrorMessage());
			mEtEmail.showInvalidAlert();
		}

		mRegError.setError(userRegistrationFailureInfo.getErrorDescription());
		trackActionLoginError(userRegistrationFailureInfo.getError().code);
	}

	private void showSignInSpinner() {
		mPbSignInSpinner.setVisibility(View.VISIBLE);
		mBtnSignInAccount.setEnabled(false);
	}

	private void hideSignInSpinner() {
		mPbSignInSpinner.setVisibility(View.INVISIBLE);
		mBtnSignInAccount.setEnabled(true);
	}

	private void showForgotPasswordSpinner() {
		mPbForgotPasswdSpinner.setVisibility(View.VISIBLE);
		mBtnForgot.setEnabled(false);
	}

	private void hideForgotPasswordSpinner() {
		mPbForgotPasswdSpinner.setVisibility(View.INVISIBLE);
		mBtnForgot.setEnabled(true);
	}

	private void resetPassword() {
		boolean validatorResult = EmailValidator.isValidEmail(mEtEmail.getEmailId().toString());
		if (!validatorResult) {
			mEtEmail.showInvalidAlert();
		} else {
			if (NetworkUtility.isNetworkAvailable(mContext)) {
				if (mUser != null) {
					showForgotPasswordSpinner();
					mEtEmail.clearFocus();
					mEtPassword.clearFocus();
					mBtnSignInAccount.setEnabled(false);
					mBtnResend.setEnabled(false);
					mUser.forgotPassword(mEtEmail.getEmailId(), this);
				}

			} else {
				mRegError.setError(getString(R.string.NoNetworkConnection));
			}
		}
	}

	private void updateUiStatus() {
		if (mEtEmail.isValidEmail() && mEtPassword.isValidPassword()
		        && NetworkUtility.isNetworkAvailable(mContext)
		        && RegistrationHelper.getInstance().isJanrainIntialized()) {
			mBtnSignInAccount.setEnabled(true);
			mRegError.hideError();
		} else {
			mBtnSignInAccount.setEnabled(false);
		}
	}

	@Override
	public void onUpadte() {
		updateUiStatus();
	}

	@Override
	public void onEventReceived(String event) {
		RLog.i(RLog.EVENT_LISTENERS, "SignInAccountFragment :onEventReceived is : " + event);
		if (RegConstants.JANRAIN_INIT_SUCCESS.equals(event)) {
			updateUiStatus();
		}
	}

	@Override
	public void onNetWorkStateReceived(boolean isOnline) {
		RLog.i(RLog.NETWORK_STATE, "SignInAccountFragment : onNetWorkStateReceived state :"
		        + isOnline);
		handleUiState();
		updateUiStatus();
	}

}
