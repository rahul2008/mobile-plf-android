package com.philips.cl.di.reg.ui.traditional;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.philips.cl.di.reg.R;
import com.philips.cl.di.reg.ui.utils.RLog;

public class WelcomeFragment extends RegistrationBaseFragment {

	private TextView mWelcomeText;
	private LinearLayout mEmailDetails;
	private LinearLayout mContinueLayout;
	private LinearLayout.LayoutParams mParams = null;
	private View mView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "UserWelcomeFragment : onCreateView");
		mView = inflater.inflate(R.layout.fragment_home, null);
		mView = inflater.inflate(R.layout.fragment_welcome, null);
		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		RLog.d(RLog.FRAGMENT_LIFECYCLE,
				"UserWelcomeFragment : onActivityCreated");
		mWelcomeText = (TextView) getActivity().findViewById(R.id.tv_welcome);

		mEmailDetails = (LinearLayout) getActivity().findViewById(
				R.id.email_details);

		mContinueLayout = (LinearLayout) getActivity().findViewById(
				R.id.continue_id);

		mParams = (LayoutParams) mWelcomeText.getLayoutParams();
		Configuration config = getResources().getConfiguration();
		setViewParams(config);

	}

	@Override
	public void onConfigurationChanged(Configuration config) {
		super.onConfigurationChanged(config);
		RLog.d(RLog.FRAGMENT_LIFECYCLE,
				"UserWelcomeFragment : onConfigurationChanged");
		setViewParams(config);
	}

	@Override
	public void setViewParams(Configuration config) {
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "UserWelcomeFragment : setViewParams");
		if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
			mParams.leftMargin = mParams.rightMargin = mLeftRightMarginPort;
		} else {
			mParams.leftMargin = mParams.rightMargin = mLeftRightMarginLand;
		}
		mWelcomeText.setLayoutParams(mParams);
		mEmailDetails.setLayoutParams(mParams);
		mContinueLayout.setLayoutParams(mParams);

	}

	@Override
	public String getActionbarTitle() {
		return getResources().getString(R.string.create_account);
	}
}
