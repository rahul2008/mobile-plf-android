
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

	private TextView mTvWelcome;
	private LinearLayout mLlEmailDetailsContainer;
	private LinearLayout mLlContinueBtnContainer;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "UserWelcomeFragment : onCreateView");
		View view = inflater.inflate(R.layout.fragment_welcome, null);
		init(view);
		return view;
	}

	private void init(View view) {
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "UserWelcomeFragment : onActivityCreated");
		mTvWelcome = (TextView) view.findViewById(R.id.tv_welcome);
		mLlEmailDetailsContainer = (LinearLayout) view.findViewById(R.id.email_details);
		mLlContinueBtnContainer = (LinearLayout) view.findViewById(R.id.continue_id);
		setViewParams(getResources().getConfiguration());
	}

	@Override
	public void onConfigurationChanged(Configuration config) {
		super.onConfigurationChanged(config);
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "UserWelcomeFragment : onConfigurationChanged");
		setViewParams(config);
	}

	@Override
	public void setViewParams(Configuration config) {
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "UserWelcomeFragment : setViewParams");

		LinearLayout.LayoutParams params = (LayoutParams) mTvWelcome.getLayoutParams();
		if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
			params.leftMargin = params.rightMargin = mLeftRightMarginPort;
		} else {
			params.leftMargin = params.rightMargin = mLeftRightMarginLand;
		}
		mTvWelcome.setLayoutParams(params);
		mLlEmailDetailsContainer.setLayoutParams(params);
		mLlContinueBtnContainer.setLayoutParams(params);

	}

	@Override
	public String getActionbarTitle() {
		return getResources().getString(R.string.create_account);
	}
}
