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

	private View mView;
	private Button mCreateAcctBtn;
	private Button mPhilipsAcctBtn;
	private LinearLayout mFirstLayout = null;
	private TextView mSecondLayout = null;
	private LinearLayout mThirdLayout = null;
	private LinearLayout mFourthLayout = null;
	private LinearLayout.LayoutParams mParams = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "UserSignInFragment : onCreateView");
		mView = inflater.inflate(R.layout.home_fragment, null);
		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		RLog.d(RLog.FRAGMENT_LIFECYCLE,
				"UserSignInFragment : onActivityCreated");
		mFirstLayout = (LinearLayout) getActivity().findViewById(
				R.id.first_part);

		mSecondLayout = (TextView) getActivity().findViewById(
				R.id.account_details);

		mThirdLayout = (LinearLayout) getActivity().findViewById(
				R.id.third_part);

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
		mCreateAcctBtn = (Button) mView.findViewById(R.id.create_account_id);
		mPhilipsAcctBtn = (Button) mView.findViewById(R.id.philips_acct_id);
		mCreateAcctBtn.setOnClickListener(this);
		mPhilipsAcctBtn.setOnClickListener(this);
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
