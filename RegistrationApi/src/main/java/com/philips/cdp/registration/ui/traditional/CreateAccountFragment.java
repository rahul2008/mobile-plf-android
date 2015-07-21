
package com.philips.cdp.registration.ui.traditional;

import android.app.Activity;
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

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.analytics.AnalyticsConstants;
import com.philips.cdp.registration.analytics.AnalyticsPages;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.events.EventHelper;
import com.philips.cdp.registration.events.EventListener;
import com.philips.cdp.registration.events.NetworStateListener;
import com.philips.cdp.registration.handlers.TraditionalRegistrationHandler;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.customviews.XEmail;
import com.philips.cdp.registration.ui.customviews.XPassword;
import com.philips.cdp.registration.ui.customviews.XRegError;
import com.philips.cdp.registration.ui.customviews.XUserName;
import com.philips.cdp.registration.ui.customviews.onUpdateListener;
import com.philips.cdp.registration.ui.utils.FontLoader;
import com.philips.cdp.registration.ui.utils.NetworkUtility;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;

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
	public void onAttach(Activity activity) {
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "CreateAccountFragment : onAttach");
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "CreateAccountFragment : onCreate");
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "CreateAccountFragment : onCreateView");
		RLog.d(RLog.EVENT_LISTENERS,
		        "CreateAccountFragment register: NetworStateListener,JANRAIN_INIT_SUCCESS");
		mContext = getRegistrationFragment().getActivity().getApplicationContext();

		RegistrationHelper.getInstance().registerNetworkStateListener(this);
		EventHelper.getInstance()
		        .registerEventNotification(RegConstants.JANRAIN_INIT_SUCCESS, this);
		View view = inflater.inflate(R.layout.fragment_create_account, container, false);
		initUI(view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "CreateAccountFragment : onActivityCreated");
	}

	@Override
	public void onStart() {
		super.onStart();
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "CreateAccountFragment : onStart");
	}

	@Override
	public void onResume() {
		super.onResume();
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "CreateAccountFragment : onResume");
	}

	@Override
	public void onPause() {
		super.onPause();
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "CreateAccountFragment : onPause");
	}

	@Override
	public void onStop() {
		super.onStop();
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "CreateAccountFragment : onStop");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "CreateAccountFragment : onDestroyView");
	}

	@Override
	public void onDestroy() {
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "CreateAccountFragment : onDestroy");
		RegistrationHelper.getInstance().unRegisterNetworkListener(this);
		EventHelper.getInstance().unregisterEventNotification(RegConstants.JANRAIN_INIT_SUCCESS,
		        this);
		EventHelper.getInstance().unregisterEventNotification(RegConstants.JANRAIN_INIT_FAILURE,
		        this);
		RLog.d(RLog.EVENT_LISTENERS,
		        "CreateAccountFragment unregister: NetworStateListener,JANRAIN_INIT_SUCCESS");
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		super.onDetach();
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "CreateAccountFragment : onDetach");
	}

	@Override
	public void onConfigurationChanged(Configuration config) {
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "CreateAccountFragment : onConfigurationChanged");
		super.onConfigurationChanged(config);
		setViewParams(config);
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
			RLog.d(RLog.ONCLICK, "CreateAccountFragment : Register Account");
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

		//trackPage(AnalyticsPages.CREATE_ACCOUNT);
		handleUiState();
		mUser = new User(mContext);
	}

	private void register() {
		showSpinner();
		mEtName.clearFocus();
		mEtEmail.clearFocus();
		mEtPassword.clearFocus();
		mEtName.hideValidAlertError();
		mEtEmail.hideValidAlertError();
		mEtPassword.hideValidAlertError();
		mUser.registerUserInfoForTraditional(mEtName.getName().toString(), mEtEmail.getEmailId()
		        .toString(), mEtPassword.getPassword().toString(), true, mCbTerms.isChecked(), this);
	}

	private void trackCheckMarketing() {
		if (mCbTerms.isChecked()) {
			trackActionForRemarkettingOption(AnalyticsConstants.REMARKETING_OPTION_IN);
		} else {
			trackActionForRemarkettingOption(AnalyticsConstants.REMARKETING_OPTION_OUT);
		}
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
			trackActionRegisterError(AnalyticsConstants.NETWORK_ERROR_CODE);
		}
	}

	@Override
	public void onRegisterSuccess() {
		RLog.i(RLog.CALLBACK, "CreateAccountFragment : onRegisterSuccess");
		hideSpinner();
		trackCheckMarketing();
		trackActionStatus(AnalyticsConstants.SEND_DATA, AnalyticsConstants.SPECIAL_EVENTS,
				AnalyticsConstants.SUCCESS_USER_CREATION);
		if (RegistrationConfiguration.getInstance().getFlow().isEmailVerificationRequired()) {
			launchAccountActivateFragment();
		} else {
			launchWelcomeFragment();
		}
	}

	private void launchAccountActivateFragment() {
		getRegistrationFragment().addFragment(new AccountActivationFragment());
		trackPage(AnalyticsPages.ACCOUNT_ACTIVATION);
	}

	private void launchWelcomeFragment() {
		getRegistrationFragment().addFragment(new WelcomeFragment());
		trackPage(AnalyticsPages.WELCOME);
	}

	@Override
	public void onRegisterFailedWithFailure(UserRegistrationFailureInfo userRegistrationFailureInfo) {
		RLog.i(RLog.CALLBACK, "CreateAccountFragment : onRegisterFailedWithFailure");
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
		trackActionRegisterError(userRegistrationFailureInfo.getError().code);
		hideSpinner();
	}

	@Override
	public int getTitleResourceId() {
		return R.string.RegCreateAccount_NavTitle;
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
		RLog.i(RLog.EVENT_LISTENERS, "CreateAccoutFragment :onEventReceived : " + event);
		if (RegConstants.JANRAIN_INIT_SUCCESS.equals(event)) {
			updateUiStatus();
		}
	}

	@Override
	public void onNetWorkStateReceived(boolean isOnline) {
		RLog.i(RLog.NETWORK_STATE, "CreateAccoutFragment :onNetWorkStateReceived : " + isOnline);
		handleUiState();
		updateUiStatus();
	}

}
