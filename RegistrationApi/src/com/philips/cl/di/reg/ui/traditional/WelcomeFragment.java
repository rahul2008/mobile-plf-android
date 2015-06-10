
package com.philips.cl.di.reg.ui.traditional;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.philips.cl.di.reg.R;
import com.philips.cl.di.reg.User;
import com.philips.cl.di.reg.adobe.analytics.AnalyticsPages;
import com.philips.cl.di.reg.dao.DIUserProfile;
import com.philips.cl.di.reg.settings.RegistrationHelper;
import com.philips.cl.di.reg.ui.utils.RLog;
import com.philips.cl.di.reg.ui.utils.RegConstants;

public class WelcomeFragment extends RegistrationBaseFragment implements OnClickListener {

	private TextView mTvWelcome;

	private TextView mTvSignInEmail;

	private LinearLayout mLlEmailDetailsContainer;

	private LinearLayout mLlContinueBtnContainer;

	private User mUser;

	private Context mContext;

	private LinearLayout mLlEmailDetails;

	private boolean isfromVerification;

	@Override
	public void onAttach(Activity activity) {
		RLog.d(RLog.FRAGMENT_LIFECYCLE, " WelcomeFragment : onAttach");
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		RLog.d(RLog.FRAGMENT_LIFECYCLE, " WelcomeFragment : onCreate");
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "UserWelcomeFragment : onCreateView");
		Bundle bundle = getArguments();
		if (null != bundle) {
			isfromVerification = bundle.getBoolean(RegConstants.VERIFICATIN_SUCCESS);
		}

		View view = inflater.inflate(R.layout.fragment_welcome, null);
		mContext = getRegistrationMainActivity().getApplicationContext();
		mUser = new User(mContext);
		init(view);
		trackCurrentPage(AnalyticsPages.WELCOME);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		RLog.d(RLog.FRAGMENT_LIFECYCLE, " WelcomeFragment : onActivityCreated");
	}

	@Override
	public void onStart() {
		super.onStart();
		RLog.d(RLog.FRAGMENT_LIFECYCLE, " WelcomeFragment : onStart");
	}

	@Override
	public void onResume() {
		super.onResume();
		RLog.d(RLog.FRAGMENT_LIFECYCLE, " WelcomeFragment : onResume");
	}

	@Override
	public void onPause() {
		super.onPause();
		RLog.d(RLog.FRAGMENT_LIFECYCLE, " WelcomeFragment : onPause");
	}

	@Override
	public void onStop() {
		super.onStop();
		RLog.d(RLog.FRAGMENT_LIFECYCLE, " WelcomeFragment : onStop");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		RLog.d(RLog.FRAGMENT_LIFECYCLE, " WelcomeFragment : onDestroyView");
	}

	@Override
	public void onDestroy() {
		RLog.d(RLog.FRAGMENT_LIFECYCLE, " WelcomeFragment : onDestroy");
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		super.onDetach();
		RLog.d(RLog.FRAGMENT_LIFECYCLE, " WelcomeFragment : onDetach");
	}

	private void init(View view) {
		consumeTouch(view);
		mTvWelcome = (TextView) view.findViewById(R.id.tv_reg_welcome);
		mLlEmailDetailsContainer = (LinearLayout) view.findViewById(R.id.ll_reg_email_details);
		mLlContinueBtnContainer = (LinearLayout) view.findViewById(R.id.ll_reg_continue_id);
		setViewParams(getResources().getConfiguration());
		Button btnSignOut = (Button) view.findViewById(R.id.btn_reg_sign_out);
		btnSignOut.setOnClickListener(this);
		Button btnContinue = (Button) view.findViewById(R.id.btn_reg_continue);
		btnContinue.setOnClickListener(this);
		mTvSignInEmail = (TextView) view.findViewById(R.id.tv_reg_sign_in_using);
		mLlEmailDetails = (LinearLayout) view.findViewById(R.id.email_details);
		if (isfromVerification) {
			mLlEmailDetails.setVisibility(View.GONE);
		}
		DIUserProfile userProfile = mUser.getUserInstance(mContext);
		mTvWelcome.setText(getString(R.string.RegWelcomeText) + " " + userProfile.getGivenName());

		String email = getString(R.string.InitialSignedIn_SigninEmailText);
		email = String.format(email, userProfile.getEmail());
		mTvSignInEmail.setText(email);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.btn_reg_sign_out) {
			RLog.d(RLog.ONCLICK, ": WelcomeFragment : Sign Out");
			mUser.logout();
			getRegistrationMainActivity().navigateToHome();
		} else if (id == R.id.btn_reg_continue) {
			RLog.d(RLog.ONCLICK, ": WelcomeFragment : Continue");
			RegistrationHelper.getInstance().getUserRegistrationListener().notifyEventOccurred();
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
		applyParams(config, mTvWelcome);
		applyParams(config, mLlEmailDetailsContainer);
		applyParams(config, mLlContinueBtnContainer);
	}

	@Override
	public int getTitleResourceId() {
		return R.string.SigIn_TitleTxt;
	}

}
