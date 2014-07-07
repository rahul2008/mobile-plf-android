package com.philips.cl.di.dev.pa.registration;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.BaseActivity;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.Fonts;
import com.philips.cl.di.dev.pa.util.networkutils.NetworkReceiver;
import com.philips.cl.di.dev.pa.util.networkutils.NetworkReceiver.ConnectionState;
import com.philips.cl.di.dev.pa.view.FontTextView;
import com.philips.cl.di.reg.User;
import com.philips.cl.di.reg.dao.DIUserProfile;
import com.philips.cl.di.reg.errormapping.Error;
import com.philips.cl.di.reg.errormapping.ErrorMessage;
import com.philips.cl.di.reg.handlers.TraditionalRegistrationHandler;

public class UserRegistrationActivity extends BaseActivity implements OnClickListener, TraditionalRegistrationHandler {

	private Button mActionBarCancelBtn;
	private ImageView mActionBarBackBtn;
	private FontTextView mActionbarTitle;
	public DIUserProfile mDIUserProfile;
	private User mUser;
	private ProgressDialog mProgressDialog;
	private static UserRegistrationChanged mListener = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simple_fragment_container);
		
		initUsageAgreementActionBar();

		// User registration needs the call to UserRegistrationController.getInstance() to make Janrain work
		if(UserRegistrationController.getInstance().isUserLoggedIn()) {
			showSuccessFragment();
		} else {
			showCreateAccountFragment();
		}
	}
	
	@Override
	public void onBackPressed() {
		showPreviousFragment();
	}
	
	private void initUsageAgreementActionBar() {
		ActionBar actionBar;
		actionBar = getSupportActionBar();
		actionBar.setIcon(null);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		Drawable d=getResources().getDrawable(R.drawable.ews_nav_bar_2x);  
		actionBar.setBackgroundDrawable(d);
		View view  = getLayoutInflater().inflate(R.layout.setup_actionbar, null);
		
		mActionbarTitle = (FontTextView) view.findViewById(R.id.setup_actionbar_title);
		mActionbarTitle.setText(getString(R.string.create_account));
		
		mActionBarCancelBtn = (Button) view.findViewById(R.id.setup_actionbar_cancel_btn);
		mActionBarCancelBtn.setText(R.string.i_accept);
		mActionBarCancelBtn.setTypeface(Fonts.getGillsansLight(getApplicationContext()));
		mActionBarCancelBtn.setOnClickListener(this);
		mActionBarCancelBtn.setVisibility(View.GONE);
		
		mActionBarBackBtn = (ImageView) view.findViewById(R.id.setup_actionbar_back_img);
		mActionBarBackBtn.setVisibility(View.GONE);
		actionBar.setCustomView(view);
	}
	
	public void createAccount() {
		mUser = new User(this);
				
		ArrayList<DIUserProfile> profileList = new ArrayList<DIUserProfile>();
		profileList.add(mDIUserProfile);
		
		mUser.registerNewUserUsingTraditional(profileList, this, this);
	}
	
	private void showCreateAccountFragment() {
		getSupportFragmentManager().beginTransaction()
		.add(R.id.fl_simple_fragment_container, new CreateAccountFragment(), CreateAccountFragment.class.getSimpleName())
		.commit();
		
		setActionBar(R.string.create_account, View.GONE);
	}
	
	protected void showUsageAgreementFragment() {
		getSupportFragmentManager().beginTransaction()
		.replace(R.id.fl_simple_fragment_container, new UsageAgreementFragment(), UsageAgreementFragment.class.getSimpleName())
		.commit();
		
		setActionBar(R.string.usage_agreement, View.VISIBLE);
	}
	
	public void showSuccessFragment() {
		getSupportFragmentManager().beginTransaction()
		.replace(R.id.fl_simple_fragment_container, new SignedInFragment(), SignedInFragment.class.getSimpleName())
		.commit();
		
		setActionBar(R.string.create_account, View.GONE);
	}
	
	private void setActionBar(int textId, int cancelButtonVisisbility) {
		mActionbarTitle.setText(textId);
		mActionBarCancelBtn.setVisibility(cancelButtonVisisbility);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.setup_actionbar_cancel_btn:
			if(ConnectionState.DISCONNECTED == NetworkReceiver.getInstance().getLastKnownNetworkState()) {
				showErrorDialog(Error.NO_NETWORK_CONNECTION); //TODO : Change error type to "Connect to internet"
				break;
			}
			
			try {
				createAccount();
				showProgressDialog();
			} catch (Exception e) {
				ALog.e(ALog.USER_REGISTRATION, "Create account error " + e.getMessage());
				showErrorDialog(Error.GENERIC_ERROR);
			}
			break;
		default:
			break;
		}
	}
	
	private void showErrorDialog(Error type) {
		try {
			RegistrationErrorDialogFragment dialog = RegistrationErrorDialogFragment.newInstance(type);
			FragmentManager fragMan = getSupportFragmentManager();
			dialog.show(fragMan, null);
		} catch (IllegalStateException e) {
			ALog.e(ALog.USER_REGISTRATION, e.getMessage());
		}
	}
	
	public void showProgressDialog() {
		try {
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setMessage(getString(R.string.please_wait));
			mProgressDialog.setCancelable(false);
			mProgressDialog.show();
		} catch (IllegalStateException e) {
			ALog.e(ALog.USER_REGISTRATION, e.getMessage());
		}
	}
	
	private void showPreviousFragment() {
		FragmentManager manager = getSupportFragmentManager();
		Fragment fragment = manager.findFragmentById(R.id.fl_simple_fragment_container);
		
		if (fragment instanceof CreateAccountFragment) {
			finish();
		} else if (fragment instanceof SignedInFragment) {
			finish();
		} else if (fragment instanceof UsageAgreementFragment) {
			getSupportFragmentManager().beginTransaction()
			.replace(R.id.fl_simple_fragment_container, new CreateAccountFragment(), CreateAccountFragment.class.getSimpleName())
			.commit();
			
			setActionBar(R.string.create_account, View.GONE);
		} 
	}
	
	private void cancelProgressDialog() {
		if(mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.cancel() ;
		}
	}
	
	@Override
	public void onRegisterSuccess() {
		ALog.i(ALog.USER_REGISTRATION, "onRegisterSuccess");
		cancelProgressDialog() ;
		showSuccessFragment();
	}

	@Override
	public void onRegisterFailedWithFailure(int error) {
		ALog.i(ALog.USER_REGISTRATION, "onRegisterFailedWithFailure error " + new ErrorMessage().getError(error));
		cancelProgressDialog() ;
		showErrorDialog(UserRegistrationController.getInstance().getErrorEnum(error));
	}
	
	public DIUserProfile getDIUserProfile() {
		return mDIUserProfile;
	}
	
	public void setDIUserProfile(DIUserProfile profile) {
		mDIUserProfile = profile;
	}
	
	public void closeUserRegistration(boolean firstUse) {
		if(mListener != null) {
			mListener.userRegistrationClosed(firstUse);
		}
		finish();
	}
	
	public static void setUserRegistrationChangedListener(UserRegistrationChanged listener) {
		mListener = listener;
	}
	
	public interface UserRegistrationChanged {
		public void userRegistrationClosed(boolean firstUse);
	}
}
