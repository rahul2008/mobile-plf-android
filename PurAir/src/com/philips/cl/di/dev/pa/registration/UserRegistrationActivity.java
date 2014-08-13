package com.philips.cl.di.dev.pa.registration;

import java.util.ArrayList;
import java.util.Locale;

import android.app.ProgressDialog;
import android.graphics.Typeface;
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
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager.EWS_STATE;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.Fonts;
import com.philips.cl.di.dev.pa.util.LanguageUtils;
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
	private Button mDeclineBtn;
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
			showUsageAgreementFragment();
		}
	}
	
	private void initUsageAgreementActionBar() {
		ActionBar actionBar;
		actionBar = getSupportActionBar();
		actionBar.setIcon(null);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		Drawable d=getResources().getDrawable(R.drawable.ews_nav_bar_2x);  
		actionBar.setBackgroundDrawable(d);
		View view  = getLayoutInflater().inflate(R.layout.user_registration_actionbar, null);
		
		mActionbarTitle = (FontTextView) view.findViewById(R.id.user_reg_actionbar_title);
		mActionbarTitle.setText(getString(R.string.create_account));
		//If Chinese language selected set font-type-face normal
		if( LanguageUtils.getLanguageForLocale(Locale.getDefault()).contains("ZH-HANS")
				|| LanguageUtils.getLanguageForLocale(Locale.getDefault()).contains("ZH-HANT")) {
			mActionbarTitle.setTypeface(Typeface.DEFAULT);
		}
		
		mActionBarCancelBtn = (Button) view.findViewById(R.id.user_reg_actionbar_cancel_btn);
		mActionBarCancelBtn.setText(R.string.i_accept);
		mActionBarCancelBtn.setTypeface(Fonts.getGillsansLight(getApplicationContext()));
		mActionBarCancelBtn.setOnClickListener(this);
		mActionBarCancelBtn.setVisibility(View.GONE);
		
		mDeclineBtn = (Button) view.findViewById(R.id.user_reg_actionbar_decline_btn);
		mDeclineBtn.setText(R.string.decline);
		mDeclineBtn.setTypeface(Fonts.getGillsansLight(getApplicationContext()));
		mDeclineBtn.setOnClickListener(this);
		mDeclineBtn.setVisibility(View.GONE);
		
		mActionBarBackBtn = (ImageView) view.findViewById(R.id.user_reg_actionbar_back_img);
		mActionBarBackBtn.setOnClickListener(this);
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
		try {
			getSupportFragmentManager().beginTransaction()
			.add(R.id.fl_simple_fragment_container, new CreateAccountFragment(), CreateAccountFragment.class.getSimpleName())
			.addToBackStack(null)
			.commit();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}	
		
		setActionBar(R.string.create_account, View.GONE);
		mActionBarBackBtn.setVisibility(View.VISIBLE);
	}
	
	protected void showUsageAgreementFragment() {
		try {
			getSupportFragmentManager().beginTransaction()
			.replace(R.id.fl_simple_fragment_container, new UsageAgreementFragment(), UsageAgreementFragment.class.getSimpleName())
			.addToBackStack(null)
			.commit();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}	
		
		setActionBar(R.string.usage_agreement, View.VISIBLE);
		mActionBarBackBtn.setVisibility(View.GONE);
	}
	
	public void showSuccessFragment() {
		try {
			getSupportFragmentManager().beginTransaction()
			.replace(R.id.fl_simple_fragment_container, new SignedInFragment(), SignedInFragment.class.getSimpleName())
			.addToBackStack(null)
			.commit();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}	
		
		setActionBar(R.string.create_account, View.GONE);
		mActionBarBackBtn.setVisibility(View.GONE);
	}
	
	private void setActionBar(int textId, int cancelButtonVisisbility) {
		mActionbarTitle.setText(textId);
		mActionBarCancelBtn.setVisibility(cancelButtonVisisbility);
		mDeclineBtn.setVisibility(cancelButtonVisisbility);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.user_reg_actionbar_cancel_btn:
			if(ConnectionState.DISCONNECTED == NetworkReceiver.getInstance().getLastKnownNetworkState()) {
				showErrorDialog(Error.NO_NETWORK_CONNECTION); //TODO : Change error type to "Connect to internet"
				break;
			}
			showCreateAccountFragment() ;
			break;
		case R.id.user_reg_actionbar_decline_btn:
		case R.id.user_reg_actionbar_back_img:
			onBackPressed();
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
		ALog.i(ALog.APP_START_UP, "UserRegistrationActivity$closeUserRegistration listener " + mListener + " firstUse " + firstUse);
		if(mListener != null) {
			mListener.userRegistrationClosed(firstUse);
			PurifierManager.getInstance().setEwsSate(EWS_STATE.REGISTRATION);
		}
		ALog.i(ALog.USER_REGISTRATION, "Before calling finish") ;
		finish();
	}
	
	public static void setUserRegistrationChangedListener(UserRegistrationChanged listener) {
		mListener = listener;
	}
	
	public interface UserRegistrationChanged {
		public void userRegistrationClosed(boolean firstUse);
	}
	
	@Override
	public void onBackPressed() {
		FragmentManager manager = getSupportFragmentManager();
		Fragment fragment = manager.findFragmentById(R.id.fl_simple_fragment_container);
		
		if (fragment instanceof CreateAccountFragment) {
			setActionBar(R.string.usage_agreement, View.VISIBLE);
			mActionBarBackBtn.setVisibility(View.GONE);
			manager.popBackStack();
		}else if(fragment instanceof UsageAgreementFragment){
			finish();
		}
	}
}
