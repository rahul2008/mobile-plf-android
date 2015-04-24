
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
import android.widget.TextView;

import com.philips.cl.di.reg.R;
import com.philips.cl.di.reg.User;
import com.philips.cl.di.reg.dao.DIUserProfile;
import com.philips.cl.di.reg.ui.utils.RLog;

public class WelcomeFragment extends RegistrationBaseFragment implements OnClickListener {

	private TextView mTvWelcome;
	private TextView mTvSignInEmail;
	private LinearLayout mLlEmailDetailsContainer;
	private LinearLayout mLlContinueBtnContainer;
	private User mUser;
	private Context mContext;
	private Button mBtnSignOut;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "UserWelcomeFragment : onCreateView");
		View view = inflater.inflate(R.layout.fragment_welcome, null);
		mContext = getRegistrationMainActivity().getApplicationContext();
		mUser = new User(mContext);
		init(view);
		return view;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		getRegistrationMainActivity().clearBackStack();
	}

	private void init(View view) {
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "UserWelcomeFragment : onActivityCreated");
		mTvWelcome = (TextView) view.findViewById(R.id.tv_welcome);
		mLlEmailDetailsContainer = (LinearLayout) view.findViewById(R.id.email_details);
		mLlContinueBtnContainer = (LinearLayout) view.findViewById(R.id.continue_id);
		setViewParams(getResources().getConfiguration());
		mBtnSignOut = (Button) view.findViewById(R.id.sign_out_btn);
		mBtnSignOut.setOnClickListener(this);
		mTvSignInEmail = (TextView) view.findViewById(R.id.tv_sign_in_email);
		
		DIUserProfile userProfile = mUser.getUserInstance(mContext);
		mTvWelcome.setText(getString(R.string.welcome) + " ["+userProfile.getGivenName() +"]");
		mTvSignInEmail.setText(getString(R.string.we_sent_email) +userProfile.getEmail());
	}
	
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.sign_out_btn) {
            mUser.logout();
		}
		
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
