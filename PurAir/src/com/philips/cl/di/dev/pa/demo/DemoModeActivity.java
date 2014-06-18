package com.philips.cl.di.dev.pa.demo;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.BaseActivity;
import com.philips.cl.di.dev.pa.ews.AlertDialogAutoNetworkSwitchOn;
import com.philips.cl.di.dev.pa.ews.EWSWifiManager;
import com.philips.cl.di.dev.pa.ews.SetupCancelDialogFragment;
import com.philips.cl.di.dev.pa.ews.SetupDialogFactory;
import com.philips.cl.di.dev.pa.ews.SetupDialogFragment;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.Fonts;
import com.philips.cl.di.dev.pa.util.networkutils.NetworkReceiver;
import com.philips.cl.di.dev.pa.util.networkutils.NetworkStateListener;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class DemoModeActivity extends BaseActivity implements OnClickListener, DemoModeListener {
	
	/**
	 * Action bar variable
	 */
	private FontTextView actionbarTitle;
	private ImageView actionbarBackImg;
	private Button actionbarCancelBtn;
	/**
	 * 
	 */
	private int demoStep = DemoModeConstant.DEMO_MODE_STEP_INTRO;
	private int prevStep = -1;
	private int apModeFailCounter;
	private int step1FailCounter;
	private DemoModeBroadcastReceiver broadcastReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup_main);
        
        getSupportFragmentManager().beginTransaction()
		.add(R.id.setup_fragment_container, new DemoModeStartFragement(), "demo_mode_start")
		.commit();
        
        //Some time action bar getting ClassCastException
		try {
			initActionBar();
		} catch (ClassCastException e) {
			ALog.e(ALog.DEMO_MODE, "Action bar cast exception: " + e.getMessage());
		}
	}
	
	/*Initialize action bar */
	private void initActionBar() throws ClassCastException {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setIcon(null);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		Drawable d=getResources().getDrawable(R.drawable.ews_nav_bar_2x);  
		actionBar.setBackgroundDrawable(d);
		View view  = getLayoutInflater().inflate(R.layout.setup_actionbar, null);
		actionbarTitle = (FontTextView) view.findViewById(R.id.setup_actionbar_title);
		actionbarTitle.setText(getString(R.string.demo_mode_title));
		actionbarCancelBtn = (Button) view.findViewById(R.id.setup_actionbar_cancel_btn);
		actionbarCancelBtn.setTypeface(Fonts.getGillsansLight(this));
		actionbarCancelBtn.setVisibility(View.VISIBLE);
		actionbarBackImg = (ImageView) view.findViewById(R.id.setup_actionbar_back_img);
		actionbarBackImg.setVisibility(View.INVISIBLE);
		actionbarCancelBtn.setOnClickListener(this);
		actionBar.setCustomView(view);	
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.setup_actionbar_cancel_btn) {
			try {
				FragmentManager fragMan = getSupportFragmentManager();
				fragMan.beginTransaction().add(
						SetupCancelDialogFragment.newInstance(), "demo_cancel").commitAllowingStateLoss();
			} catch (Exception e) {
				ALog.e(ALog.DEMO_MODE, e.getMessage());
			}
		}
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			ALog.i(ALog.DEMO_MODE, "On back fagment step: " + demoStep);
			replaceFragmentOnBackPress();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	public void showIntroScreen() {
		demoStep = DemoModeConstant.DEMO_MODE_STEP_INTRO;
		showDemoModeFragment(new DemoModeStartFragement());
	}
	
	public void gotoStepOneScreen() {
		/**
		 * Checking Wifi is enabled
		 */
		ConnectivityManager connManager = 
				(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		if (networkInfo != null && !networkInfo.isConnected()) {
			showDialogFragment(DemoModeWifiEnableDialogFragment.newInstance());
		} 
		/**
		 * Checking auto network switch enabled
		 * If enabled show alert dialog to user disable
		 */
		else if (EWSWifiManager.isPoorNetworkAvoidanceEnabled(this)) {
			showDialogFragment(AlertDialogAutoNetworkSwitchOn.newInstance());
		} else {
			showStepOneScreen();
		}
		
	}
	
	public void showStepOneScreen() {
		demoStep = DemoModeConstant.DEMO_MODE_STEP_ONE;
		showDemoModeFragment(new DemoModeStepOneFragment());
	}
	
	public void showSupportScreen() {
		prevStep = demoStep;
		demoStep = DemoModeConstant.DEMO_MODE_STEP_SUPPORT;
		showDemoModeFragment(new DemoModeSupportFragment());
	}
	
	public void showFinalScreen() {
		demoStep = DemoModeConstant.DEMO_MODE_STEP_FINAL;
		showDemoModeFragment(new DemoModeFinalFragment());
	}
	
	public void showStepToSetupAPMode() {
		apModeFailCounter++;
		if (apModeFailCounter > 2) {
			showSupportScreen();
		} else {
			SetupDialogFactory.getInstance(this).getDialog(SetupDialogFactory.ERROR_TS01_01).show() ;
		}
	}
	
	public void connectToAirPurifier() {
		dismissConnectingDialog();
		SetupDialogFactory.getInstance(this).getDialog(SetupDialogFactory.CHECK_SIGNAL_STRENGTH).show();
		if ( broadcastReceiver == null) {
			broadcastReceiver = new DemoModeBroadcastReceiver(this) ;
		}
		step1FailCounter++;
		broadcastReceiver.connectToDeviceAP() ;
	}
	
	public void showHomeScreen() {
		finish();
	}
	
	private void showDemoModeFragment(Fragment fragment) {
		try {
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.replace(R.id.setup_fragment_container, fragment, "demo_mode_fragment");
			fragmentTransaction.commit();
		} catch (IllegalStateException e) {
			ALog.e(ALog.DEMO_MODE, e.getMessage());
		} catch (Exception e) {
			ALog.e(ALog.DEMO_MODE, e.getMessage());
		}
	}
	
	public void setActionbarTitle(int step) {
		switch (step) {
		case DemoModeConstant.DEMO_MODE_STEP_INTRO:
		case DemoModeConstant.DEMO_MODE_STEP_ONE:
			actionbarCancelBtn.setVisibility(View.VISIBLE);
			actionbarTitle.setText(getString(R.string.demo_mode_title));
			break;
		case DemoModeConstant.DEMO_MODE_STEP_SUPPORT:
			actionbarCancelBtn.setVisibility(View.VISIBLE);
			actionbarTitle.setText(getString(R.string.demo_mode_support_title));
			break;
		case DemoModeConstant.DEMO_MODE_STEP_FINAL:
			actionbarCancelBtn.setVisibility(View.INVISIBLE);
			actionbarTitle.setText(getString(R.string.congratulations));
			break;
		default:
			break;
		}
	}
	
	private boolean replaceFragmentOnBackPress() {
		ALog.i(ALog.DEMO_MODE, "replaceFragmentOnBackPress: " + demoStep);
		switch (demoStep) {
		case DemoModeConstant.DEMO_MODE_STEP_INTRO:
			showDialogFragment(SetupCancelDialogFragment.newInstance());
			return true;
		case DemoModeConstant.DEMO_MODE_STEP_ONE:
			showIntroScreen();
			return true;
		case DemoModeConstant.DEMO_MODE_STEP_SUPPORT:
			if (prevStep == DemoModeConstant.DEMO_MODE_STEP_INTRO) {
				showIntroScreen();
			} else if (prevStep == DemoModeConstant.DEMO_MODE_STEP_ONE) {
				apModeFailCounter = 0;
				step1FailCounter = 0;
				showStepOneScreen();
			}
			return true;
		case DemoModeConstant.DEMO_MODE_STEP_FINAL:
			return true;
		default:
			return false;
		}
	}

	@Override
	public void onErrorOccur(final int errorCode) {
		ALog.i(ALog.DEMO_MODE, "onErrorOccurred: "+errorCode) ;
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				dismissConnectingDialog();
				
				switch (errorCode) {
				case DemoModeConstant.DEMO_MODE_ERROR_NOT_IN_PHILIPS_SETUP:
					if (step1FailCounter > 2) {
						showSupportScreen();
					} else {
						showErrorDialog(getString(R.string.error_ts01_01_title), 
								getString(R.string.error_ts01_01_message), 
								getString(R.string.next));
					}
					break;
				case DemoModeConstant.DEMO_MODE_ERROR_DATA_RECIEVED_FAILED:
					if (step1FailCounter > 2) {
						showSupportScreen();
					} else {
						try {
							showErrorDialog(getString(R.string.error_ts01_04_title), 
									getString(R.string.error_ts01_01_message),
									getString(R.string.error_purifier_not_detect_btn_txt));
						} catch (Exception e) {
							ALog.e(ALog.DEMO_MODE, e.getMessage());
						}
					}
					break;
				default:
					break;
				}
			}
		});
	}
	
	private void showDialogFragment(Fragment fragment) {
		try {
			FragmentManager fragMan = getSupportFragmentManager();
			fragMan.beginTransaction().add(
					fragment, "dialogfragment").commitAllowingStateLoss();
		} catch (Exception e) {
			ALog.e(ALog.DEMO_MODE, e.getMessage());
		}
	}
	
	private void showErrorDialog(String title,String msg, String btnTxt) {
		try {
			FragmentManager fragMan = getSupportFragmentManager();
			fragMan.beginTransaction()
			.add(SetupDialogFragment.newInstance(title, msg, btnTxt), "demo_error")
					.commitAllowingStateLoss();
		} catch (Exception e) {
			ALog.e(ALog.DEMO_MODE, e.getMessage());
		}
	}
	
	public void dismissConnectingDialog() {
		if (SetupDialogFactory.getInstance(
				DemoModeActivity.this).getDialog(SetupDialogFactory.CHECK_SIGNAL_STRENGTH).isShowing()) {
			SetupDialogFactory.getInstance(
					DemoModeActivity.this).getDialog(SetupDialogFactory.CHECK_SIGNAL_STRENGTH).dismiss();
		}
	}
	
	public int getApModeFailCounter() {
		return apModeFailCounter;
	}
	
	public int getStep1FailCounter() {
		return step1FailCounter;
	}

	@Override
	public void onHandShakeWithDevice() {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				showFinalScreen();
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent arg2) {
		if (requestCode == DemoModeConstant.REQUEST_CODE) {
			gotoStepOneScreen();
		}
	}

}