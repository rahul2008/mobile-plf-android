package com.philips.cl.di.reg.ui.traditional;

import android.app.AlertDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.philips.cl.di.reg.R;
import com.philips.cl.di.reg.User;
import com.philips.cl.di.reg.events.EventHelper;
import com.philips.cl.di.reg.events.EventListener;
import com.philips.cl.di.reg.handlers.ForgotPasswordHandler;
import com.philips.cl.di.reg.handlers.TraditionalLoginHandler;
import com.philips.cl.di.reg.settings.RegistrationHelper;
import com.philips.cl.di.reg.ui.customviews.XEmail;
import com.philips.cl.di.reg.ui.customviews.XPassword;
import com.philips.cl.di.reg.ui.customviews.XRegError;
import com.philips.cl.di.reg.ui.customviews.onUpdateListener;
import com.philips.cl.di.reg.ui.utils.EmailValidator;
import com.philips.cl.di.reg.ui.utils.JanrainErrorMessage;
import com.philips.cl.di.reg.ui.utils.NetworkUtility;
import com.philips.cl.di.reg.ui.utils.RLog;
import com.philips.cl.di.reg.ui.utils.RegConstants;

public class SignInAccountFragment extends RegistrationBaseFragment implements
		OnClickListener, TraditionalLoginHandler, ForgotPasswordHandler,
		onUpdateListener, EventListener {

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
	private final int INVALID_CREDENTIAL = 10;
	private XRegError mRegError;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		RLog.d(RLog.FRAGMENT_LIFECYCLE,
				"UserPhilipsAccountSignInFragment : onCreateView");
		EventHelper.getInstance().registerEventNotification(
				RegConstants.IS_ONLINE, this);
		EventHelper.getInstance().registerEventNotification(
				RegConstants.JANRAIN_INIT_SUCCESS, this);
		EventHelper.getInstance().registerEventNotification(
				RegConstants.JANRAIN_INIT_FAILURE, this);
		View view = inflater.inflate(R.layout.fragment_sign_in_account, null);
		initUI(view);
		return view;
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
		LinearLayout.LayoutParams params = (LayoutParams) mLlCreateAccountFields
				.getLayoutParams();
		if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
			params.leftMargin = params.rightMargin = mLeftRightMarginPort;
		} else {
			params.leftMargin = params.rightMargin = mLeftRightMarginLand;
		}
		mLlCreateAccountFields.setLayoutParams(params);
		mRlSignInBtnContainer.setLayoutParams(params);
		mRegError.setLayoutParams(params);

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
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.sign_in_btn) {
			signIn();
		} else if (id == R.id.forgot_password_btn) {
			resetPassword();
			
		}else if (id == R.id.resend_btn) {
		   getRegistrationMainActivity().addFragment(new AccountActivationFragment());
		}
	}

	private void initUI(View view) {
		consumeTouch(view);
		mBtnSignInAccount = (Button) view.findViewById(R.id.sign_in_btn);
		mBtnSignInAccount.setOnClickListener(this);
		mBtnForgot = (Button) view.findViewById(R.id.forgot_password_btn);
		mBtnForgot.setOnClickListener(this);
		mBtnResend = (Button) view.findViewById(R.id.resend_btn);
		mBtnResend.setOnClickListener(this);
		mLlCreateAccountFields = (LinearLayout) view
				.findViewById(R.id.ll_create_account_fields);
		mRlSignInBtnContainer = (RelativeLayout) view
				.findViewById(R.id.rl_welcome_container);

		mEtEmail = (XEmail) view.findViewById(R.id.rl_email_field);
		mEtEmail.setOnClickListener(this);
		mEtEmail.setOnUpdateListener(this);
		mEtEmail.setFocusable(true);
		mEtPassword = (XPassword) view.findViewById(R.id.rl_password_field);
		mEtPassword.setOnClickListener(this);
		mEtPassword.setOnUpdateListener(this);
		mRegError = (XRegError) view.findViewById(R.id.reg_error_msg);
		setViewParams(getResources().getConfiguration());
		handleUiState();
		mUser = new User(getActivity().getApplicationContext());
		mPbSignInSpinner = (ProgressBar) view
				.findViewById(R.id.pb_sign_in_spinner);
		mPbForgotPasswdSpinner = (ProgressBar) view
				.findViewById(R.id.pb_forgot_spinner);

	}

	@Override
	public String getActionbarTitle() {
		return getResources().getString(R.string.sign_in);
	}

	private void signIn() {
		if (mUser != null)
			showSignInSpinner();
		    mBtnForgot.setEnabled(false);
		    mBtnResend.setEnabled(false);
		mUser.loginUsingTraditional(mEtEmail.getEmailId().toString(),
				mEtPassword.getPassword().toString(), this);
	}

	private void handleUiState() {
		if (NetworkUtility.getInstance().isOnline()) {
			if (RegistrationHelper.isJanrainIntialized()) {
				mRegError.hideError();
			} else {
				mRegError.setError(getString(R.string.No_Internet_Connection));
			}
		} else {
			// Show network error
			mRegError.setError(getString(R.string.No_Internet_Connection));
		}
	}

	@Override
	public void onLoginSuccess() {
		hideSignInSpinner();
		mBtnForgot.setEnabled(true);
		mBtnResend.setEnabled(true);
		mRegError.hideError();
		if (mUser.getEmailVerificationStatus(getActivity())) {
			getRegistrationMainActivity().addWelcomeFragmentOnVerification();
	   } else {
            mRegError.setError(getString(R.string.Janrain_Error_Need_Email_Verification));
            mBtnResend.setVisibility(View.VISIBLE);
       }
	}

	@Override
	public void onLoginFailedWithError(int errorType) {
		mBtnForgot.setEnabled(true);
		mBtnResend.setEnabled(true);
		hideSignInSpinner();
		if (errorType == INVALID_CREDENTIAL) {

			mRegError.setError(getResources().getString(
					R.string.Janrain_invalid_credentials));
			mEtEmail.setErrDescription(getResources().getString(
					R.string.Janrain_invalid_credentials));
			mEtEmail.showInvalidEmailAlert();
			mEtPassword.setErrDescription(getResources().getString(
					R.string.Janrain_invalid_credentials));
			mEtPassword.showJanarainError();

		} else {
			JanrainErrorMessage errorMessage = new JanrainErrorMessage(
					getActivity());
			String message = errorMessage.getError(errorType);
			updateUiStatus();
			mEtPassword.setErrDescription(message);
			mEtEmail.setErrDescription(message);
			mEtEmail.showInvalidEmailAlert();
			mEtPassword.showJanarainError();

		}
	}

	private void forgotpassword() {
		final AlertDialog myBuilder = new AlertDialog.Builder(getActivity())
				.create();
		myBuilder.requestWindowFeature(Window.FEATURE_NO_TITLE);
		myBuilder.setCancelable(true);
		LayoutInflater myLayoutInflater = getActivity().getLayoutInflater();
		View myView = myLayoutInflater.inflate(R.layout.dialog_reset_password, null);
		Button continueBtn = (Button) myView.findViewById(R.id.continue_btn);

		continueBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				myBuilder.dismiss();
				hideForgotPasswordSpinner();
			}
		});

		myBuilder.setView(myView);
		myBuilder.show();
	}

	@Override
	public void onSendForgotPasswordSuccess() {
		hideForgotPasswordSpinner();
		forgotpassword();
		mBtnResend.setEnabled(true);
	}

	@Override
	public void onSendForgotPasswordFailedWithError(int error) {
		mBtnResend.setEnabled(true);
		hideForgotPasswordSpinner();
		JanrainErrorMessage errorMessage = new JanrainErrorMessage(
				getActivity());
		String message = errorMessage.getError(error);
		mRegError.setError(message);
		updateUiStatus();
		mEtPassword.setErrDescription(message);
		mEtEmail.setErrDescription(message);
		mEtEmail.showInvalidEmailAlert();
		mEtPassword.showJanarainError();
	}

	private void showSignInSpinner() {
		mPbSignInSpinner.setVisibility(View.VISIBLE);
		mBtnSignInAccount.setBackgroundResource(R.drawable.disable_btn);
		mBtnSignInAccount.setEnabled(false);
	}

	private void hideSignInSpinner() {
		mPbSignInSpinner.setVisibility(View.INVISIBLE);
		mBtnSignInAccount.setBackgroundResource(R.drawable.navigation_bar);
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
		boolean validatorResult = EmailValidator.isValidEmail(mEtEmail
				.getEmailId().toString());
		if (!validatorResult) {
			mEtEmail.showInvalidEmailAlert();
		} else {
			if (NetworkUtility.getInstance().isOnline()) {
				if (mUser != null) {
					showForgotPasswordSpinner();
					mBtnSignInAccount.setBackgroundResource(R.drawable.disable_btn);
					mBtnSignInAccount.setEnabled(false);
					mBtnSignInAccount.setTextColor(getResources().getColor(R.color.btn_disable_text_color));
					mBtnResend.setEnabled(false);
					mUser.forgotPassword(mEtEmail.getEmailId().toString(), this);
				}

			} else {
				mRegError.setError(getString(R.string.No_Internet_Connection));
			}
		}
	}

	private void updateUiStatus() {
		if (mEtEmail.isValidEmail() && mEtPassword.isValidPassword()
				&& NetworkUtility.getInstance().isOnline()
				&& RegistrationHelper.isJanrainIntialized()) {
			mBtnSignInAccount.setBackgroundResource(R.drawable.navigation_bar);
			mBtnSignInAccount.setEnabled(true);
			mBtnSignInAccount.setTextColor(getResources().getColor(R.color.btn_enable_text_color));
			mRegError.hideError();
		} else {
			mBtnSignInAccount.setEnabled(false);
			mBtnSignInAccount.setBackgroundResource(R.drawable.disable_btn);
			mBtnSignInAccount.setTextColor(getResources().getColor(R.color.btn_disable_text_color));
		}
	}

	@Override
	public void onUpadte() {
		updateUiStatus();
	}

	@Override
	public void onEventReceived(String event) {
		if (RegConstants.IS_ONLINE.equals(event)) {
			handleUiState();
			updateUiStatus();
		} else if (RegConstants.JANRAIN_INIT_SUCCESS.equals(event)) {
			System.out.println("reint");
			updateUiStatus();
		}
	}
}
