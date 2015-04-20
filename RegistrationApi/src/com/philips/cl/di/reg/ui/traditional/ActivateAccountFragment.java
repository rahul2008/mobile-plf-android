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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cl.di.reg.R;
import com.philips.cl.di.reg.User;
import com.philips.cl.di.reg.handlers.TraditionalLoginHandler;
import com.philips.cl.di.reg.handlers.TraditionalRegistrationHandler;
import com.philips.cl.di.reg.ui.utils.RLog;

public class ActivateAccountFragment extends RegistrationBaseFragment implements
		OnClickListener, TraditionalLoginHandler,TraditionalRegistrationHandler {

	private Button mActivateBtn;
	private Button mResendBtn;
	private TextView mTvVerifyEmail;
	private LinearLayout mSecondLayout;
	private TextView mTvResendDetails;
	private RelativeLayout mFourthLayout;
	private User mUser;
	private ProgressBar mPbSpinner;

	public ActivateAccountFragment(User mUser) {
		this.mUser = mUser;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		RLog.d(RLog.FRAGMENT_LIFECYCLE,
				"ActivateAccountFragment : onCreateView");
		View view = inflater.inflate(R.layout.fragment_activate_account, null);
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
	public void onClick(View v) {
		// TODO: converted into switch after the proper solution
		int id = v.getId();
		if (id == R.id.activate_acct_btn) {

			// System.out.println("***** email  : "+mUser.mEmail +
			// " pasword : "+mUser.mPassword + " name : "+mUser.mGivenName);
			if (mUser.getEmailVerificationStatus(getActivity())) {
				Toast.makeText(getActivity(), "Verification email Success",Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getActivity(),R.string.Janrain_Error_Need_Email_Verification,Toast.LENGTH_LONG).show();
			}
		} else if (id == R.id.resend_btn) {
			showSpinner();
			mUser.registerUserInfoForTraditional(mUser.mGivenName,
					mUser.mEmail, mUser.mPassword, true, true, this);
		}

	}

	private void initUI(View view) {
		mTvVerifyEmail = (TextView) view.findViewById(R.id.tv_veify_email);
		mSecondLayout = (LinearLayout) view.findViewById(R.id.second_part);
		mTvResendDetails = (TextView) view.findViewById(R.id.tv_resend_details);
		mFourthLayout = (RelativeLayout) view.findViewById(R.id.fourth_part);
		
		
		mActivateBtn = (Button) view.findViewById(R.id.activate_acct_btn);
		mResendBtn = (Button) view.findViewById(R.id.resend_btn);
		mActivateBtn.setOnClickListener(this);
		mResendBtn.setOnClickListener(this);
		setViewParams(getResources().getConfiguration());
		mPbSpinner = (ProgressBar) view.findViewById(R.id.pb_spinner);
	}

	private void showSpinner() {
		mPbSpinner.setVisibility(View.VISIBLE);
	}

	private void hideSpinner() {
		mPbSpinner.setVisibility(View.INVISIBLE);
	}

	@Override
	public void setViewParams(Configuration config) {
		LinearLayout.LayoutParams params = (LayoutParams) mTvVerifyEmail.getLayoutParams();
		if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
			params.leftMargin = params.rightMargin = mLeftRightMarginPort;
		} else {
			params.leftMargin = params.rightMargin = mLeftRightMarginLand;
		}
		mTvVerifyEmail.setLayoutParams(params);
		mSecondLayout.setLayoutParams(params);
		mTvResendDetails.setLayoutParams(params);
		mFourthLayout.setLayoutParams(params);

	}

	@Override
	public String getActionbarTitle() {
		return getResources().getString(R.string.sign_in);
	}

	@Override
	public void onLoginSuccess() {

	}

	@Override
	public void onLoginFailedWithError(int error) {

	}

	@Override
	public void onRegisterSuccess() {
		hideSpinner();
	}

	@Override
	public void onRegisterFailedWithFailure(int error) {
		hideSpinner();
	}

}
