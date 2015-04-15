package com.philips.cl.di.reg.ui.traditional;

import com.philips.cl.di.reg.R;
import com.philips.cl.di.reg.ui.utils.RLog;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class ActivateAccountFragment extends RegistrationBaseFragment implements
		OnClickListener {

	private View mView;
	private Button mActivateBtn;
	private Button mResendBtn;
	private TextView mFirstLayout = null;
	private LinearLayout mSecondLayout = null;
	private TextView mThirdLayout = null;
	private LinearLayout mFourthLayout = null;
	private LinearLayout.LayoutParams mParams = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "ActivateAccountFragment : onCreateView");
		mView = inflater.inflate(R.layout.fragment_activate_account, null);
		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		RLog.d(RLog.FRAGMENT_LIFECYCLE,
				"ActivateAccountFragment : onActivityCreated");
		mFirstLayout = (TextView) getActivity().findViewById(
				R.id.first_part);

		mSecondLayout = (LinearLayout) getActivity().findViewById(
				R.id.second_part);

		mThirdLayout = (TextView) getActivity().findViewById(
				R.id.resend_details);

		mFourthLayout = (LinearLayout) getActivity().findViewById(
				R.id.fourth_part);

		mParams = (LayoutParams) mFirstLayout.getLayoutParams();
		Configuration config = getResources().getConfiguration();
		setViewParams(config);

		initialize();

	}

	@Override
	public void onConfigurationChanged(Configuration config) {
		super.onConfigurationChanged(config);
		RLog.d(RLog.FRAGMENT_LIFECYCLE,
				"UserSignInFragment : onConfigurationChanged");
		setViewParams(config);
	}

	@Override
	public void onClick(View v) {
		// TODO: converted into switch after the proper solution
		int id = v.getId();
		if (id == R.id.create_account_id) {
			((RegistrationActivity) getActivity())
					.addFragment(new CreateAccountFragment());
		} else if (id == R.id.philips_acct_id) {
			((RegistrationActivity) getActivity())
					.addFragment(new SignInAccountFragment());
		} else {
		}

	}

	private void initialize() {
		mActivateBtn = (Button) mView.findViewById(R.id.activate_acct_btn);
		mResendBtn = (Button) mView.findViewById(R.id.resend_btn);
		mActivateBtn.setOnClickListener(this);
		mResendBtn.setOnClickListener(this);
	}

	@Override
	public void setViewParams(Configuration config) {

		if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
			mParams.leftMargin = mParams.rightMargin = mLeftRightMarginPort;
		} else {
			mParams.leftMargin = mParams.rightMargin = mLeftRightMarginLand;
		}
		mFirstLayout.setLayoutParams(mParams);
		mSecondLayout.setLayoutParams(mParams);
		mThirdLayout.setLayoutParams(mParams);
		mFourthLayout.setLayoutParams(mParams);

	}

	@Override
	public String getActionbarTitle() {
		return getResources().getString(R.string.sign_in);
	}

}
