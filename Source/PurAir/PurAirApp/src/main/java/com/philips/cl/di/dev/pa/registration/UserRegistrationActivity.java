package com.philips.cl.di.dev.pa.registration;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.BaseActivity;
import com.philips.cl.di.dev.pa.newpurifier.AirPurifierManager;
import com.philips.cl.di.dev.pa.newpurifier.AirPurifierManager.EWS_STATE;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.util.networkutils.NetworkReceiver;
import com.philips.cl.di.dev.pa.util.networkutils.NetworkReceiver.ConnectionState;
import com.philips.cl.di.reg.User;
import com.philips.cl.di.reg.dao.DIUserProfile;
import com.philips.cl.di.reg.errormapping.Error;
import com.philips.cl.di.reg.errormapping.ErrorMessage;
import com.philips.cl.di.reg.handlers.TraditionalRegistrationHandler;

public class UserRegistrationActivity extends BaseActivity implements
		OnClickListener, TraditionalRegistrationHandler {

	public DIUserProfile mDIUserProfile;
	private User mUser;
	private ProgressDialog mProgressDialog;
	private static UserRegistrationChanged mListener = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simple_fragment_container);

		if (UserRegistrationController.getInstance().isUserLoggedIn()) {
			showFragment(new SignedInFragment());
		} else {
			showFragment(new UsageAgreementFragment());
		}
	}

	public void createAccount() {
		mUser = new User(this);

		ArrayList<DIUserProfile> profileList = new ArrayList<DIUserProfile>();
		profileList.add(mDIUserProfile);

		mUser.registerNewUserUsingTraditional(profileList, this);
	}
	
	public void showFragment(Fragment fragment) {
		try {
			FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
			fragmentTransaction.replace(R.id.fl_simple_fragment_container, fragment, fragment.getTag());
			fragmentTransaction.addToBackStack(fragment.getTag()) ;
			fragmentTransaction.commit();
		} catch (IllegalStateException e) {
			ALog.e(ALog.USER_REGISTRATION, "Error: " + e.getMessage());
		}

		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (getWindow() != null && getWindow().getCurrentFocus() != null) {
			imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
		}		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.user_reg_actionbar_cancel_btn:
			if (ConnectionState.DISCONNECTED == NetworkReceiver.getInstance().getLastKnownNetworkState()) {
				showErrorDialog(Error.NO_NETWORK_CONNECTION);
				break;
			}
			showFragment(new CreateAccountFragment());
			break;
		case R.id.user_reg_actionbar_decline_btn:
		case R.id.user_reg_actionbar_back_img:
			onBackPressed();
			break;
		default:
			break;
		}
	}

	public void showErrorDialog(Error type) {
		try {
			RegistrationErrorDialogFragment dialog = RegistrationErrorDialogFragment
					.newInstance(type);
			FragmentManager fragMan = getSupportFragmentManager();
			dialog.show(fragMan, null);
		} catch (IllegalStateException e) {
			ALog.e(ALog.USER_REGISTRATION, "Error: " + e.getMessage());
		}
	}

	public void showProgressDialog() {
		try {
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setMessage(getString(R.string.please_wait));
			mProgressDialog.setCancelable(false);
			mProgressDialog.show();
		} catch (IllegalStateException e) {
			ALog.e(ALog.USER_REGISTRATION, "Error: " + e.getMessage());
		}
	}

	private void cancelProgressDialog() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.cancel();
		}
	}

	@Override
	public void onRegisterSuccess() {
		ALog.i(ALog.USER_REGISTRATION, "onRegisterSuccess");
		MetricsTracker.trackPageFinishedUserRegistration();
		cancelProgressDialog();
		showFragment(new SignedInFragment());
	}

	@Override
	public void onRegisterFailedWithFailure(int error) {
		ALog.i(ALog.USER_REGISTRATION, "onRegisterFailedWithFailure error "	+ new ErrorMessage().getError(error));
		cancelProgressDialog();
		MetricsTracker.trackPageTechnicalError("UserRegistration", "Error : " + new ErrorMessage().getError(error));
		showErrorDialog(UserRegistrationController.getInstance().getErrorEnum(error));
	}

	public DIUserProfile getDIUserProfile() {
		return mDIUserProfile;
	}

	public void setDIUserProfile(DIUserProfile profile) {
		mDIUserProfile = profile;
	}

	public void closeUserRegistration(boolean firstUse) {
		ALog.i(ALog.APP_START_UP, "UserRegistrationActivity$closeUserRegistration listener " + mListener + " firstUse " + firstUse);
		if (mListener != null) {
			mListener.userRegistrationClosed(firstUse);
			AirPurifierManager.getInstance().setEwsSate(EWS_STATE.REGISTRATION);
		}
		ALog.i(ALog.USER_REGISTRATION, "Before calling finish");
		mListener = null;
		finish();
	}

	public static void setUserRegistrationChangedListener(UserRegistrationChanged listener) {
		mListener = listener;
	}

	public interface UserRegistrationChanged {
		void userRegistrationClosed(boolean firstUse);
	}

	@Override
	public void onBackPressed() {
		FragmentManager manager = getSupportFragmentManager();
		Fragment fragment = manager.findFragmentById(R.id.fl_simple_fragment_container);

		if (fragment instanceof CreateAccountFragment) {
			manager.popBackStack();
		} else if (fragment instanceof UsageAgreementFragment) {
			finish();
		}
	}
}
