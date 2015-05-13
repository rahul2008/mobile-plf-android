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
import com.philips.cl.di.reg.dao.SignInTraditionalFailuerInfo;
import com.philips.cl.di.reg.events.EventHelper;
import com.philips.cl.di.reg.events.EventListener;
import com.philips.cl.di.reg.handlers.TraditionalLoginHandler;
import com.philips.cl.di.reg.settings.RegistrationHelper;
import com.philips.cl.di.reg.ui.customviews.XButton;
import com.philips.cl.di.reg.ui.customviews.XEmail;
import com.philips.cl.di.reg.ui.customviews.XPassword;
import com.philips.cl.di.reg.ui.customviews.XRegError;
import com.philips.cl.di.reg.ui.customviews.onUpdateListener;
import com.philips.cl.di.reg.ui.traditional.RegistrationBaseFragment;
import com.philips.cl.di.reg.ui.utils.NetworkUtility;
import com.philips.cl.di.reg.ui.utils.RLog;
import com.philips.cl.di.reg.ui.utils.RegConstants;

public class MergeAccountFragment extends RegistrationBaseFragment implements EventListener, onUpdateListener,TraditionalLoginHandler, OnClickListener{

	private TextView mTvAccountMergeSignIn;
	private LinearLayout mLlUsedEMailAddressContainer;
	private LinearLayout mLlCreateAccountFields;
	private RelativeLayout mRlSingInOptions;
	private XRegError mRegError;
	private XButton mBtnMerge;
	private XButton mBtnForgotPassword;
	private XEmail mEtEmail;
	private XPassword mEtPassword;
	private ProgressBar mPbSpinner;
	private String mMergeToken;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		RLog.d(RLog.FRAGMENT_LIFECYCLE,
				"SocialMergeAccountFragment : onCreateView");
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
				"SocialMergeAccountFragment : onDestroy");
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
		
		mPbSpinner = (ProgressBar) view
				.findViewById(R.id.pb_reg_merge_sign_in_spinner);
		mPbSpinner.setClickable(false);
		mPbSpinner.setEnabled(true);
		mMergeToken = bundle.getString(RegConstants.REGISTER_MERGE_TOKEN);
		setViewParams(getResources().getConfiguration());
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
		}	
	}
	
	private void mergeAccount() {
		User user = new User(getRegistrationMainActivity().getApplicationContext());
		if (NetworkUtility.getInstance().isOnline()) {
			showSpinner();
			user.mergeToTraditionalAccount(mEtEmail.getEmailId(), mEtPassword.getPassword(), mMergeToken, this);
		} else {
			mRegError.setError(getString(R.string.JanRain_Error_Check_Internet));
		}
	}
	
	private void showSpinner() {
		mPbSpinner.setVisibility(View.VISIBLE);
		mBtnMerge.setEnabled(false);
	}

	private void hideSpinner() {
		mPbSpinner.setVisibility(View.INVISIBLE);
		mBtnMerge.setEnabled(true);
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
			mBtnForgotPassword.setEnabled(false);
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
		hideSpinner();
		getRegistrationMainActivity().addWelcomeFragmentOnVerification();
	}

	@Override
	public void onLoginFailedWithError(SignInTraditionalFailuerInfo signInTraditionalFailuerInfo) {
		 hideSpinner();
		Toast.makeText(getActivity(), "Login Failure", Toast.LENGTH_LONG).show();
		
	}

}
