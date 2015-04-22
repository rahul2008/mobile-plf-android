
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
import com.philips.cl.di.reg.settings.RegistrationSettings;
import com.philips.cl.di.reg.ui.customviews.XEmail;
import com.philips.cl.di.reg.ui.customviews.XPassword;
import com.philips.cl.di.reg.ui.customviews.XRegError;
import com.philips.cl.di.reg.ui.customviews.onUpdateListener;
import com.philips.cl.di.reg.ui.utils.EmailValidator;
import com.philips.cl.di.reg.ui.utils.JanrainErrorMessage;
import com.philips.cl.di.reg.ui.utils.NetworkUtility;
import com.philips.cl.di.reg.ui.utils.RLog;
import com.philips.cl.di.reg.ui.utils.RegConstants;

public class SignInAccountFragment extends RegistrationBaseFragment implements OnClickListener,
        TraditionalLoginHandler, ForgotPasswordHandler, onUpdateListener, EventListener {

	private LinearLayout mLlCreateAccountFields;

	private RelativeLayout mRlSignInBtnContainer;

	private Button mBtnSignInAccount;

	private Button mBtnForgot;

	private XEmail mEtEmail;

	private XPassword mEtPassword;

	private User mUser;

	private ProgressBar mPbSpinner;

	private final int INVALID_CREDENTIAL = 10;

	private XRegError mRegError;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "UserPhilipsAccountSignInFragment : onCreateView");
		EventHelper.getInstance().registerEventNotification(RegConstants.IS_ONLINE, this);
		EventHelper.getInstance()
		        .registerEventNotification(RegConstants.JANRAIN_INIT_SUCCESS, this);
		EventHelper.getInstance()
		        .registerEventNotification(RegConstants.JANRAIN_INIT_FAILURE, this);
		View view = inflater.inflate(R.layout.fragment_sign_in_account, null);
		initUI(view);
		return view;
	}

	@Override
	public void onConfigurationChanged(Configuration config) {
		super.onConfigurationChanged(config);
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "UserPhilipsAccountSignInFragment : onConfigurationChanged");
		setViewParams(config);
	}

	@Override
	public void setViewParams(Configuration config) {
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "UserPhilipsAccountSignInFragment : setViewParams");
		LinearLayout.LayoutParams params = (LayoutParams) mLlCreateAccountFields.getLayoutParams();
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
		EventHelper.getInstance().unregisterEventNotification(RegConstants.IS_ONLINE, this);
		EventHelper.getInstance().unregisterEventNotification(RegConstants.JANRAIN_INIT_SUCCESS,
		        this);
		EventHelper.getInstance().unregisterEventNotification(RegConstants.JANRAIN_INIT_FAILURE,
		        this);
		super.onDestroy();
	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.sign_in_btn) {
			signIn();
		} else if (id == R.id.forgot_password_btn) {
			resetPassword();
		}
	}

	private void initUI(View view) {
		mBtnSignInAccount = (Button) view.findViewById(R.id.sign_in_btn);
		mBtnSignInAccount.setOnClickListener(this);
		mBtnForgot = (Button) view.findViewById(R.id.forgot_password_btn);
		mBtnForgot.setOnClickListener(this);
		mLlCreateAccountFields = (LinearLayout) view.findViewById(R.id.ll_create_account_fields);
		mRlSignInBtnContainer = (RelativeLayout) view.findViewById(R.id.rl_welcome_container);

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
		mPbSpinner = (ProgressBar) view.findViewById(R.id.pb_activate_spinner);
	}

	@Override
	public String getActionbarTitle() {
		return getResources().getString(R.string.sign_in);
	}

	private void signIn() {
		if (mUser != null)
			showSpinner();
		mUser.loginUsingTraditional(mEtEmail.getEmailId().toString(), mEtPassword.getPassword()
		        .toString(), this);
	}

	private void handleUiState() {
		if (NetworkUtility.getInstance().isOnline()) {
			if (RegistrationSettings.isJanrainIntialized()) {
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
		hideSpinner();
		mRegError.hideError();
		((RegistrationActivity) getActivity()).addFragment(new WelcomeFragment());

	}

	@Override
	public void onLoginFailedWithError(int errorType) {

		hideSpinner();
		if (errorType == INVALID_CREDENTIAL) {

			mRegError.setError(getResources().getString(R.string.Janrain_invalid_credentials));
			mEtEmail.setErrDescription(getResources().getString(
			        R.string.Janrain_invalid_credentials));
			mEtEmail.showInvalidEmailAlert();
			mEtPassword.setErrDescription(getResources().getString(
			        R.string.Janrain_invalid_credentials));
			mEtPassword.showJanarainError();

		} else {
			JanrainErrorMessage errorMessage = new JanrainErrorMessage(getActivity());
			String message = errorMessage.getError(errorType);
			signInBtnUIControle();
			mEtPassword.setErrDescription(message);
			mEtEmail.setErrDescription(message);
			mEtEmail.showInvalidEmailAlert();
			mEtPassword.showJanarainError();

		}
	}

	private void forgotpassword() {
		final AlertDialog myBuilder = new AlertDialog.Builder(getActivity()).create();
		myBuilder.requestWindowFeature(Window.FEATURE_NO_TITLE);
		myBuilder.setCancelable(true);
		LayoutInflater myLayoutInflater = getActivity().getLayoutInflater();
		View myView = myLayoutInflater.inflate(R.layout.reset_password, null);
		Button continueBtn = (Button) myView.findViewById(R.id.continue_btn);

		continueBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				myBuilder.dismiss();
				hideSpinner();
			}
		});

		myBuilder.setView(myView);
		myBuilder.show();
	}

	@Override
	public void onSendForgotPasswordSuccess() {
		hideSpinner();
		forgotpassword();
	}

	@Override
	public void onSendForgotPasswordFailedWithError(int error) {
		hideSpinner();
		JanrainErrorMessage errorMessage = new JanrainErrorMessage(getActivity());
		String message = errorMessage.getError(error);
		mRegError.setError(message);
		signInBtnUIControle();
		mEtPassword.setErrDescription(message);
		mEtEmail.setErrDescription(message);
		mEtEmail.showInvalidEmailAlert();
		mEtPassword.showJanarainError();
	}

	private void showSpinner() {
		mPbSpinner.setVisibility(View.VISIBLE);
		mBtnSignInAccount.setBackgroundResource(R.drawable.disable_btn);
		mBtnSignInAccount.setEnabled(false);
	}

	private void hideSpinner() {
		mPbSpinner.setVisibility(View.INVISIBLE);
		mBtnSignInAccount.setBackgroundResource(R.drawable.navigation_bar);
		mBtnSignInAccount.setEnabled(true);
	}

	private void resetPassword() {
		boolean validatorResult = EmailValidator.isValidEmail(mEtEmail.getEmailId().toString());
		if (!validatorResult) {
			mEtEmail.showInvalidEmailAlert();
		} else {
			if (NetworkUtility.getInstance().isOnline()) {
				if (mUser != null) {
					showSpinner();
					mUser.forgotPassword(mEtEmail.getEmailId().toString(), this);
				}

			} else {
				mRegError.setError(getString(R.string.No_Internet_Connection));
			}
		}
	}

	private void signInBtnUIControle() {
		if (mEtEmail.isValidEmail() && mEtPassword.isValidPassword()) {
			mBtnSignInAccount.setBackgroundResource(R.drawable.navigation_bar);
			mBtnSignInAccount.setEnabled(true);
			mRegError.hideError();
		} else {
			mBtnSignInAccount.setBackgroundResource(R.drawable.disable_btn);
			mBtnSignInAccount.setEnabled(false);
		}
	}

	@Override
	public void onUpadte() {
		signInBtnUIControle();
	}

	@Override
	public void onEventReceived(String event) {
		if (RegConstants.IS_ONLINE.equals(event)) {
			handleUiState();
		} else if (RegConstants.JANRAIN_INIT_SUCCESS.equals(event)) {
			System.out.println("reint");
		}
	}
}
