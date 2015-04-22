
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
import com.philips.cl.di.reg.events.EventHelper;
import com.philips.cl.di.reg.events.EventListener;
import com.philips.cl.di.reg.handlers.RefreshUserHandler;
import com.philips.cl.di.reg.handlers.ResendVerificationEmailHandler;
import com.philips.cl.di.reg.settings.RegistrationSettings;
import com.philips.cl.di.reg.ui.customviews.XRegError;
import com.philips.cl.di.reg.ui.utils.NetworkUtility;
import com.philips.cl.di.reg.ui.utils.RLog;
import com.philips.cl.di.reg.ui.utils.RegConstants;

public class ActivateAccountFragment extends RegistrationBaseFragment implements OnClickListener,
        RefreshUserHandler, ResendVerificationEmailHandler, EventListener {

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

	private String mEmailId;

	private XRegError mRegError;

	private XRegError mEMailVerifiedError;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "ActivateAccountFragment : onCreateView");
		EventHelper.getInstance().registerEventNotification(RegConstants.IS_ONLINE, this);
		EventHelper.getInstance()
		        .registerEventNotification(RegConstants.JANRAIN_INIT_SUCCESS, this);
		EventHelper.getInstance()
		        .registerEventNotification(RegConstants.JANRAIN_INIT_FAILURE, this);
		Bundle bundle = getArguments();
		if (null != bundle) {
			mEmailId = bundle.getString(RegConstants.EMAIL);
		}
		mContext = getRegistrationMainActivity().getApplicationContext();
		mUser = new User(mContext);
		View view = inflater.inflate(R.layout.fragment_activate_account, null);
		initUI(view);
		return view;
	}

	@Override
	public void onConfigurationChanged(Configuration config) {
		super.onConfigurationChanged(config);
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "UserSignInFragment : onConfigurationChanged");
		setViewParams(config);
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
		if (id == R.id.activate_acct_btn) {
			handleActivate();
		} else if (id == R.id.resend_btn) {
			handleResend();
		}
	}

	/**
     * 
     */
	private void handleResend() {
		showResendSpinner();
		mBtnActivate.setClickable(false);
		mBtnActivate.setEnabled(false);
		mBtnResend.setClickable(false);
		mBtnResend.setEnabled(false);

		mUser.resendVerificationMail(mEmailId, this);
	}

	private void handleActivate() {
		showActivateSpinner();
		mBtnActivate.setClickable(false);
		mBtnActivate.setEnabled(false);
		mBtnResend.setClickable(false);
		mBtnResend.setEnabled(false);
		mUser.refreshUser(mContext, this);
	}

	private void initUI(View view) {
		mTvVerifyEmail = (TextView) view.findViewById(R.id.tv_veify_email);
		mLlWelcomeContainer = (LinearLayout) view.findViewById(R.id.ll_welcome_container);
		mTvResendDetails = (TextView) view.findViewById(R.id.tv_resend_details);
		mRlSingInOptions = (RelativeLayout) view.findViewById(R.id.ll_singin_options);
		mBtnActivate = (Button) view.findViewById(R.id.activate_acct_btn);
		mBtnResend = (Button) view.findViewById(R.id.resend_btn);
		mBtnActivate.setOnClickListener(this);
		mBtnResend.setOnClickListener(this);

		mPbActivateSpinner = (ProgressBar) view.findViewById(R.id.pb_activate_spinner);
		mPbResendSpinner = (ProgressBar) view.findViewById(R.id.pb_resend_spinner);

		TextView tvEmail = (TextView) view.findViewById(R.id.tv_email);
		tvEmail.setText(getString(R.string.mail_Sent_to) + mEmailId);
		mRegError = (XRegError) view.findViewById(R.id.reg_error_msg);
		mEMailVerifiedError = (XRegError) view.findViewById(R.id.reg_email_verified_error);
		setViewParams(getResources().getConfiguration());
		handleUiState();
	}

	private void handleUiState() {
		if (NetworkUtility.getInstance().isOnline()) {
			if (RegistrationSettings.isJanrainIntialized()) {
				mRegError.hideError();
			} else {
				mRegError.setError(getString(R.string.No_Internet_Connection));
			}
		} else {
			mRegError.setError(getString(R.string.No_Internet_Connection));
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
		mBtnActivate.setClickable(true);
		mBtnActivate.setEnabled(true);
		mBtnResend.setClickable(true);
		mBtnResend.setEnabled(true);
		if (mUser.getEmailVerificationStatus(mContext)) {
			Toast.makeText(getActivity(), "Verification email Success", Toast.LENGTH_LONG).show();
			mEMailVerifiedError.hideError();
			mRegError.hideError();
		} else {

			mEMailVerifiedError.setVisibility(View.VISIBLE);
			mEMailVerifiedError.setError(getResources().getString(
			        R.string.Janrain_Error_Need_Email_Verification));
		}
	}

	@Override
	public void setViewParams(Configuration config) {
		LinearLayout.LayoutParams params = (LayoutParams) mTvVerifyEmail.getLayoutParams();
		if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
			params.leftMargin = params.rightMargin = mLeftRightMarginPort;
		} else {
			params.leftMargin = params.rightMargin = mLeftRightMarginLand;
		}
		mTvVerifyEmail.setLayoutParams(params);
		mLlWelcomeContainer.setLayoutParams(params);
		mTvResendDetails.setLayoutParams(params);
		mRlSingInOptions.setLayoutParams(params);
		mRegError.setLayoutParams(params);

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
		Toast.makeText(getActivity(), "Resend Mail Successfully ", Toast.LENGTH_LONG).show();
	}

	private void updateResendUIState() {
		mBtnActivate.setClickable(true);
		mBtnActivate.setEnabled(true);
		mBtnResend.setClickable(true);
		mBtnResend.setEnabled(true);
		hideResendSpinner();
	}

	@Override
	public void onResendVerificationEmailFailedWithError(int error) {
		updateResendUIState();
		mRegError.setError(getResources().getString(R.string.resend_email_faild));
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
