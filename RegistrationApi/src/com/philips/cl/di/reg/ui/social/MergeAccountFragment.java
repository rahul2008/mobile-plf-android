
package com.philips.cl.di.reg.ui.social;

import android.app.Activity;
import android.content.Context;
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
import com.philips.cl.di.reg.ui.customviews.XButton;
import com.philips.cl.di.reg.ui.customviews.XEmail;
import com.philips.cl.di.reg.ui.customviews.XPassword;
import com.philips.cl.di.reg.ui.customviews.XRegError;
import com.philips.cl.di.reg.ui.customviews.onUpdateListener;
import com.philips.cl.di.reg.ui.traditional.RegistrationBaseFragment;
import com.philips.cl.di.reg.ui.utils.EmailValidator;
import com.philips.cl.di.reg.ui.utils.NetworkUtility;
import com.philips.cl.di.reg.ui.utils.RLog;
import com.philips.cl.di.reg.ui.utils.RegAlertDialog;
import com.philips.cl.di.reg.ui.utils.RegConstants;

public class MergeAccountFragment extends RegistrationBaseFragment implements EventListener,
        onUpdateListener, TraditionalLoginHandler, ForgotPasswordHandler, NetworStateListener,
        OnClickListener {

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

	private Context mContext;

	@Override
	public void onAttach(Activity activity) {
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "MergeAccountFragment : onAttach");
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "MergeAccountFragment : onCreate");
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "MergeAccountFragment : onCreateView");
		RegistrationHelper.getInstance().registerNetworkStateListener(this);
		EventHelper.getInstance()
		        .registerEventNotification(RegConstants.JANRAIN_INIT_SUCCESS, this);
		View view = inflater.inflate(R.layout.fragment_social_merge_account, container, false);
		RLog.i(RLog.EVENT_LISTENERS,
		        "MergeAccountFragment register: NetworStateListener,JANRAIN_INIT_SUCCESS");
		initUI(view);
		handleUiErrorState();
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "MergeAccountFragment : onActivityCreated");
	}

	@Override
	public void onStart() {
		super.onStart();
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "MergeAccountFragment : onStart");
	}

	@Override
	public void onResume() {
		super.onResume();
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "MergeAccountFragment : onResume");
	}

	@Override
	public void onPause() {
		super.onPause();
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "MergeAccountFragment : onPause");
	}

	@Override
	public void onStop() {
		super.onStop();
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "MergeAccountFragment : onStop");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "MergeAccountFragment : onDestroyView");
	}

	@Override
	public void onDestroy() {
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "MergeAccountFragment : onDestroy");
		RegistrationHelper.getInstance().unRegisterNetworkListener(this);
		EventHelper.getInstance().unregisterEventNotification(RegConstants.JANRAIN_INIT_SUCCESS,
		        this);
		RLog.i(RLog.EVENT_LISTENERS,
		        "MergeAccountFragment unregister: JANRAIN_INIT_SUCCESS,NetworStateListener");
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		super.onDetach();
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "MergeAccountFragment : onDetach");
	}

	@Override
	public void onConfigurationChanged(Configuration config) {
		super.onConfigurationChanged(config);
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "MergeAccountFragment : onConfigurationChanged");
		setViewParams(config);
	}

	private void initUI(View view) {
		consumeTouch(view);
		Bundle bundle = this.getArguments();
		mBtnMerge = (XButton) view.findViewById(R.id.btn_reg_merg);
		mBtnMerge.setOnClickListener(this);

		mBtnForgotPassword = (XButton) view.findViewById(R.id.btn_reg_forgot_password);
		mBtnForgotPassword.setOnClickListener(this);
		mTvAccountMergeSignIn = (TextView) view.findViewById(R.id.tv_reg_account_merge_sign_in);
		mLlUsedEMailAddressContainer = (LinearLayout) view
		        .findViewById(R.id.ll_reg_used_email_address_container);

		mLlCreateAccountFields = (LinearLayout) view
		        .findViewById(R.id.ll_reg_create_account_fields);

		mRlSingInOptions = (RelativeLayout) view.findViewById(R.id.rl_reg_btn_container);
		mRegError = (XRegError) view.findViewById(R.id.reg_error_msg);
		mEtEmail = (XEmail) view.findViewById(R.id.rl_reg_email_field);
		mEtEmail.setOnUpdateListener(this);
		mEtPassword = (XPassword) view.findViewById(R.id.rl_reg_password_field);
		mEtPassword.setOnUpdateListener(this);

		mPbMergeSpinner = (ProgressBar) view.findViewById(R.id.pb_reg_merge_sign_in_spinner);
		mPbMergeSpinner.setClickable(false);
		mPbMergeSpinner.setEnabled(true);

		mPbForgotPaswwordSpinner = (ProgressBar) view.findViewById(R.id.pb_reg_forgot_spinner);
		mPbForgotPaswwordSpinner.setClickable(false);
		mPbForgotPaswwordSpinner.setEnabled(true);

		mMergeToken = bundle.getString(RegConstants.SOCIAL_MERGE_TOKEN);
		setViewParams(getResources().getConfiguration());
		mContext = getRegistrationFragment().getParentActivity().getApplicationContext();
		mUser = new User(mContext);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_reg_merg) {
			RLog.d(RLog.ONCLICK, "MergeAccountFragment : Merge");
			if (mEtEmail.hasFocus()) {
				mEtEmail.clearFocus();
			} else if (mEtPassword.hasFocus()) {
				mEtPassword.clearFocus();
			}
			getView().requestFocus();
			mergeAccount();

		} else if (v.getId() == R.id.btn_reg_forgot_password) {
			RLog.d(RLog.ONCLICK, "MergeAccountFragment : Forgot Password");
			resetPassword();
		}
	}

	private void mergeAccount() {
		if (NetworkUtility.isNetworkAvailable(mContext)) {
			trackActionStatus(AnalyticsConstants.SEND_DATA,
					AnalyticsConstants.SPECIAL_EVENTS, AnalyticsConstants.START_SOCIAL_MERGE);
			mUser.mergeToTraditionalAccount(mEtEmail.getEmailId(), mEtPassword.getPassword(),
			        mMergeToken, this);
			showMergeSpinner();
		} else {
			mRegError.setError(getString(R.string.JanRain_Error_Check_Internet));
		}
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
		if (NetworkUtility.isNetworkAvailable(mContext)) {
			if (RegistrationHelper.getInstance().isJanrainIntialized()) {
				mRegError.hideError();
			} else {
				mRegError.setError(getString(R.string.NoNetworkConnection));
			}
		} else {
			mRegError.setError(getString(R.string.NoNetworkConnection));
			trackActionLoginError(AnalyticsConstants.NETWORK_ERROR_CODE);
		}
	}

	private void updateUiStatus() {
		RLog.i("MergeAccountFragment", "updateUiStatus");
		if (mEtEmail.isValidEmail() && mEtPassword.isValidPassword()
		        && NetworkUtility.isNetworkAvailable(mContext)
		        && RegistrationHelper.getInstance().isJanrainIntialized()) {
			mBtnMerge.setEnabled(true);
			mBtnForgotPassword.setEnabled(true);
			mRegError.hideError();
		} else {
			mBtnMerge.setEnabled(false);
		}
	}

	@Override
	public void onEventReceived(String event) {
		RLog.i(RLog.EVENT_LISTENERS, "MergeAccountFragment :onEventReceived is : " + event);
		if (RegConstants.JANRAIN_INIT_SUCCESS.equals(event)) {
			updateUiStatus();
		}
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
	public int getTitleResourceId() {
		return R.string.SigIn_TitleTxt;
	}

	@Override
	public void onLoginSuccess() {
		RLog.i(RLog.CALLBACK, "MergeAccountFragment : onLoginSuccess");
		hideMergeSpinner();
		launchWelcomeFragment();
	}

	private void launchWelcomeFragment() {
		getRegistrationFragment().addWelcomeFragmentOnVerification();
		trackPage(AnalyticsPages.MERGE_ACCOUNT, AnalyticsPages.WELCOME);
	}

	@Override
	public void onLoginFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo) {
		RLog.i(RLog.CALLBACK, "MergeAccountFragment : onLoginFailedWithError");
		hideMergeSpinner();
		if (null != userRegistrationFailureInfo.getEmailErrorMessage()) {
			mEtEmail.setErrDescription(userRegistrationFailureInfo.getEmailErrorMessage());
			mEtEmail.showInvalidAlert();
		}

		if (null != userRegistrationFailureInfo.getPasswordErrorMessage()) {

			mEtPassword.setErrDescription(userRegistrationFailureInfo.getPasswordErrorMessage());
			mEtPassword.showInvalidAlert();
		}

		mRegError.setError(userRegistrationFailureInfo.getErrorDescription());
		trackActionLoginError(userRegistrationFailureInfo.getError().code);
	}

	@Override
	public void onSendForgotPasswordSuccess() {
		RLog.i(RLog.CALLBACK, "MergeAccountFragment : onSendForgotPasswordSuccess");
		RegAlertDialog.showResetPasswordDialog(getRegistrationFragment().getParentActivity(),AnalyticsConstants.MERGE_ACCOUNT);
		hideForgotPasswordSpinner();
		mRegError.hideError();
	}

	@Override
	public void onSendForgotPasswordFailedWithError(
	        UserRegistrationFailureInfo userRegistrationFailureInfo) {
		RLog.i(RLog.CALLBACK, "MergeAccountFragment : onSendForgotPasswordFailedWithError");
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
	}

	@Override
	public void onNetWorkStateReceived(boolean isOnline) {
		RLog.i(RLog.NETWORK_STATE, "MergeAccountFragment :onNetWorkStateReceived state :"
		        + isOnline);
		handleUiErrorState();
		updateUiStatus();
	}
}
