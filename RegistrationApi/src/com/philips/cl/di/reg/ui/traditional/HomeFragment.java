package com.philips.cl.di.reg.ui.traditional;

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
import com.philips.cl.di.reg.ui.utils.RLog;

public class HomeFragment extends RegistrationBaseFragment implements
		OnClickListener {

	private Button mBtnCreateAccount;
	private Button mBtnMyPhilips;
	private TextView mTvWelcome;
	private TextView mTvWelcomeDesc;
	private LinearLayout mLlCreateBtnContainer;
	private LinearLayout mLlLoginBtnContainer;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
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
		setViewParams(getResources().getConfiguration());
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

}
