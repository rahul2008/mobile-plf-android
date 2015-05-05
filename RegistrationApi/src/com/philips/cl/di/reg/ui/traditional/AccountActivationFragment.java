package com.philips.cl.di.reg.ui.traditional;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cl.di.reg.R;
import com.philips.cl.di.reg.User;
import com.philips.cl.di.reg.dao.DIUserProfile;
import com.philips.cl.di.reg.events.EventHelper;
import com.philips.cl.di.reg.events.EventListener;
import com.philips.cl.di.reg.handlers.RefreshUserHandler;
import com.philips.cl.di.reg.handlers.ResendVerificationEmailHandler;
import com.philips.cl.di.reg.settings.RegistrationHelper;
import com.philips.cl.di.reg.ui.customviews.XRegError;
import com.philips.cl.di.reg.ui.utils.NetworkUtility;
import com.philips.cl.di.reg.ui.utils.RLog;
import com.philips.cl.di.reg.ui.utils.RegConstants;

public class AccountActivationFragment extends RegistrationBaseFragment
		implements OnClickListener, RefreshUserHandler,
		ResendVerificationEmailHandler, EventListener {

	private Button mBtnActivate;
	private Button mBtnResend;
	private TextView mTvVerifyEmail;
	private LinearLayout mLlWelcomeContainer;
	private TextView mTvResendDetails;
	private RelativeLayout mRlSingInOptions;
	private ProgressBar mPbActivateSpinner;
	private ProgressBar mPbResendSpinner;
	private User mUser;
	private Context mContext;
	private XRegError mRegError;
	private XRegError mEMailVerifiedError;
	private String mEmailId;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		RLog.d(RLog.FRAGMENT_LIFECYCLE,
				"ActivateAccountFragment : onCreateView");
		EventHelper.getInstance().registerEventNotification(
				RegConstants.IS_ONLINE, this);
		EventHelper.getInstance().registerEventNotification(
				RegConstants.JANRAIN_INIT_SUCCESS, this);
		EventHelper.getInstance().registerEventNotification(
				RegConstants.JANRAIN_INIT_FAILURE, this);
		mContext = getRegistrationMainActivity().getApplicationContext();
		mUser = new User(mContext);
		View view = inflater
				.inflate(R.layout.fragment_account_activation, null);
		initUI(view);
		return view;
	}

	@Override
	public void onConfigurationChanged(Configuration config) {
		super.onConfigurationChanged(config);
		RLog.d(RLog.FRAGMENT_LIFECYCLE,
				"UserSignInFragment : onConfigurationChanged");
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
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.btn_reg_activate_acct) {
			handleActivate();
		} else if (id == R.id.btn_reg_resend) {
			handleResend();
		}
	}

	/**
     * 
     */
	private void handleResend() {
		showResendSpinner();
		mBtnActivate.setEnabled(false);
		mBtnActivate.setEnabled(false);
		mBtnResend.setEnabled(false);
		mBtnResend.setEnabled(false);

		mUser.resendVerificationMail(mEmailId, this);
	}

	private void handleActivate() {
		showActivateSpinner();
		mBtnActivate.setEnabled(false);
		mBtnActivate.setEnabled(false);
		mBtnResend.setEnabled(false);
		mBtnResend.setEnabled(false);
		mUser.refreshUser(mContext, this);
	}

	private void initUI(View view) {
		consumeTouch(view);
		mTvVerifyEmail = (TextView) view.findViewById(R.id.tv_reg_veify_email);
		mLlWelcomeContainer = (LinearLayout) view
				.findViewById(R.id.ll_reg_welcome_container);
		mTvResendDetails = (TextView) view.findViewById(R.id.tv_reg_resend_details);
		mRlSingInOptions = (RelativeLayout) view
				.findViewById(R.id.rl_reg_singin_options);
		mBtnActivate = (Button) view.findViewById(R.id.btn_reg_activate_acct);
		mBtnResend = (Button) view.findViewById(R.id.btn_reg_resend);
		mBtnActivate.setOnClickListener(this);
		mBtnResend.setOnClickListener(this);

		mPbActivateSpinner = (ProgressBar) view
				.findViewById(R.id.pb_reg_activate_spinner);
		mPbResendSpinner = (ProgressBar) view
				.findViewById(R.id.pb_reg_resend_spinner);

		TextView tvEmail = (TextView) view.findViewById(R.id.tv_reg_email);

		DIUserProfile userProfile = mUser.getUserInstance(mContext);
		mEmailId = userProfile.getEmail();
		tvEmail.setText(getString(R.string.mail_Sent_to) + mEmailId);
		mRegError = (XRegError) view.findViewById(R.id.reg_error_msg);
		mEMailVerifiedError = (XRegError) view
				.findViewById(R.id.reg_email_verified_error);
		setViewParams(getResources().getConfiguration());
		handleUiState();
	}

	private void handleUiState() {
		if (NetworkUtility.getInstance().isOnline()) {
			if (RegistrationHelper.isJanrainIntialized()) {
				mRegError.hideError();
				mBtnActivate.setEnabled(true);
				mBtnResend.setEnabled(true);
			} else {
				mBtnActivate.setEnabled(false);
				mBtnResend.setEnabled(false);
				mRegError.setError(getString(R.string.No_Internet_Connection));
			}
		} else {
			mRegError.setError(getString(R.string.No_Internet_Connection));
			mBtnActivate.setEnabled(false);
			mBtnResend.setEnabled(false);
		}
	}

	private void showActivateSpinner() {
		mPbActivateSpinner.setVisibility(View.VISIBLE);
	}

	private void hideActivateSpinner() {
		mPbActivateSpinner.setVisibility(View.GONE);
	}

	private void showResendSpinner() {
		mPbResendSpinner.setVisibility(View.VISIBLE);
	}

	private void hideResendSpinner() {
		mPbResendSpinner.setVisibility(View.GONE);
	}

	/**
     * 
     */
	private void updateActivationUIState() {
		hideActivateSpinner();
		mBtnActivate.setEnabled(true);
		mBtnActivate.setEnabled(true);
		mBtnResend.setEnabled(true);
		mBtnResend.setEnabled(true);
		if (mUser.getEmailVerificationStatus(mContext)) {
			mBtnResend.setVisibility(View.GONE);
			Toast.makeText(getActivity(), "Verification email Success",
					Toast.LENGTH_LONG).show();
			mEMailVerifiedError.hideError();
			mRegError.hideError();
			getRegistrationMainActivity().addWelcomeFragmentOnVerification();

		} else {

			mEMailVerifiedError.setVisibility(View.VISIBLE);
			mEMailVerifiedError.setError(getResources().getString(
					R.string.Janrain_Error_Need_Email_Verification));
		}
	}

	@Override
	public void setViewParams(Configuration config) {
		applyParams(config, mTvVerifyEmail);
		applyParams(config, mLlWelcomeContainer);
		applyParams(config, mTvResendDetails);
		applyParams(config, mRlSingInOptions);
		applyParams(config, mRegError);
	}

	@Override
	public String getActionbarTitle() {
		return getResources().getString(R.string.sign_in);
	}

	@Override
	public void onRefreshUserSuccess() {
		updateActivationUIState();
	}

	@Override
	public void onRefreshUserFailed(int error) {
		updateActivationUIState();
	}

	@Override
	public void onResendVerificationEmailSuccess() {
		// Navigate to signin
		updateResendUIState();
		Toast.makeText(getActivity(), "Resend Mail Successfully ",
				Toast.LENGTH_LONG).show();
	}

	private void updateResendUIState() {
		mBtnActivate.setEnabled(true);
		mBtnActivate.setEnabled(true);
		mBtnResend.setEnabled(true);
		mBtnResend.setEnabled(true);
		hideResendSpinner();
	}

	@Override
	public void onResendVerificationEmailFailedWithError(int error) {
		updateResendUIState();
		mRegError.setError(getResources()
				.getString(R.string.resend_email_faild));
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
