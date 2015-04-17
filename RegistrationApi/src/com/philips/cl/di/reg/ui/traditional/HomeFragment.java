package com.philips.cl.di.reg.ui.traditional;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.philips.cl.di.reg.R;
import com.philips.cl.di.reg.events.EventHelper;
import com.philips.cl.di.reg.events.EventListener;
import com.philips.cl.di.reg.ui.customviews.XRegError;
import com.philips.cl.di.reg.ui.utils.NetworkUtility;
import com.philips.cl.di.reg.ui.utils.RLog;
import com.philips.cl.di.reg.ui.utils.RegConstants;

public class HomeFragment extends RegistrationBaseFragment implements
		OnClickListener, EventListener {

	private Button mBtnCreateAccount;
	private Button mBtnMyPhilips;
	private TextView mTvWelcome;
	private TextView mTvWelcomeDesc;
	private LinearLayout mLlCreateBtnContainer;
	private LinearLayout mLlLoginBtnContainer;
	private XRegError mRegError;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		EventHelper.getInstance().registerEventNotification(
				RegConstants.IS_ONLINE, this);
		EventHelper.getInstance().registerEventNotification(
				RegConstants.JANRAIN_INIT_SUCCESS, this);
		EventHelper.getInstance().registerEventNotification(
				RegConstants.JANRAIN_INIT_FAILURE, this);
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "UserSignInFragment : onCreateView");
		View view = inflater.inflate(R.layout.fragment_home, container, false);
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
		EventHelper.getInstance().registerEventNotification(
				RegConstants.JANRAIN_INIT_FAILURE, this);
		super.onDestroy();
	}

	private void initUI(View view) {
		mTvWelcome = (TextView) view.findViewById(R.id.tv_welcome);
		mTvWelcomeDesc = (TextView) view.findViewById(R.id.tv_welcome_desc);
		mLlCreateBtnContainer = (LinearLayout) view
				.findViewById(R.id.third_part);
		mLlLoginBtnContainer = (LinearLayout) view
				.findViewById(R.id.fourth_part);
		mBtnCreateAccount = (Button) view.findViewById(R.id.create_account_id);
		mBtnCreateAccount.setOnClickListener(this);
		mBtnMyPhilips = (Button) view.findViewById(R.id.philips_acct_id);
		mBtnMyPhilips.setOnClickListener(this);

		mRegError = (XRegError) view.findViewById(R.id.reg_error_msg);

		setViewParams(getResources().getConfiguration());
		handleUiState();
	}

	@Override
	public void onClick(View v) {
		// Library does not include resource constants after ADT 14
		// Link :http://tools.android.com/tips/non-constant-fields
		if (v.getId() == R.id.create_account_id) {
			((RegistrationActivity) getActivity())
					.addFragment(new CreateAccountFragment());
		} else if (v.getId() == R.id.philips_acct_id) {
			((RegistrationActivity) getActivity())
					.addFragment(new CreateAccountFragment());
		}
	}

	@Override
	public void setViewParams(Configuration config) {
		LinearLayout.LayoutParams mParams = (LayoutParams) mTvWelcome
				.getLayoutParams();
		if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
			mParams.leftMargin = mParams.rightMargin = mLeftRightMarginPort;
		} else {
			mParams.leftMargin = mParams.rightMargin = mLeftRightMarginLand;
		}
		mTvWelcome.setLayoutParams(mParams);
		mTvWelcomeDesc.setLayoutParams(mParams);
		mLlCreateBtnContainer.setLayoutParams(mParams);
		mLlLoginBtnContainer.setLayoutParams(mParams);
	}

	@Override
	public String getActionbarTitle() {
		return getResources().getString(R.string.sign_in);
	}

	@Override
	public void onEventReceived(String event) {
		if (RegConstants.IS_ONLINE.equals(event)) {
			handleUiState();
		} else if (RegConstants.JANRAIN_INIT_SUCCESS.equals(event)) {
			enableControls(true);
		} else if (RegConstants.JANRAIN_INIT_FAILURE.equals(event)) {
			enableControls(false);
		}
	}

	private void handleUiState() {
		if (NetworkUtility.getInstance().isOnline()) {
			if (isJanrainIntialized()) {
				mRegError.hideError();
				enableControls(true);
			} else {
				// Show janran offline error
				System.out.println("ffff");
				mRegError.setError(getString(R.string.No_Internet_Connection));
			}
		} else {
			// Show network error
			mRegError.setError(getString(R.string.No_Internet_Connection));
			enableControls(false);
		}
	}

	@SuppressLint("NewApi")
	private void enableControls(boolean state) {

		mBtnCreateAccount.setEnabled(state);
		mBtnMyPhilips.setEnabled(state);

		// btnCreateAccount.setEnabled(state);
		// mLlMyPhilips.setEnabled(state);
		// mLlFacebook.setEnabled(state);
		// mLlTwitter.setEnabled(state);
		// mLlGooglePlus.setEnabled(state);
		//
		if (state) {
			mBtnCreateAccount.setAlpha(1);
			mBtnMyPhilips.setAlpha(1);
			// mLlFacebook.setAlpha(1);
			// mLlTwitter.setAlpha(1);
			// mLlGooglePlus.setAlpha(1);
			// mLlRegisterError.setVisibility(View.INVISIBLE);
		} else {
			// setErrorMsg(SaecoAvantiApplication.getInstance().getJanrainErrorMsg());
			mBtnCreateAccount.setAlpha(0.75f);
			mBtnCreateAccount.setAlpha(0.75f);

			// mLlFacebook.setAlpha(0.75f);
			// mLlTwitter.setAlpha(0.75f);
			// mLlGooglePlus.setAlpha(0.75f);
		}
	}

}
