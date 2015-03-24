package com.philips.cl.di.dev.pa.demo;

import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.ews.AlertDialogAutoNetworkSwitchOn;
import com.philips.cl.di.dev.pa.ews.EWSWifiManager;
import com.philips.cl.di.dev.pa.ews.SetupCancelDialogFragment;
import com.philips.cl.di.dev.pa.ews.SetupDialogFactory;
import com.philips.cl.di.dev.pa.ews.SetupDialogFragment;
import com.philips.cl.di.dev.pa.newpurifier.AirPurifierManager;
import com.philips.cl.di.dev.pa.newpurifier.AirPurifierManager.EWS_STATE;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.LanguageUtils;
import com.philips.cl.di.dev.pa.util.Utils;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class DemoModeActivity extends ActionBarActivity implements OnClickListener, DemoModeListener {
	
	/**
	 * Action bar variable
	 */
	private FontTextView actionbarTitle;
	private ImageView actionbarBackImg;
	private ImageButton actionbarCancelBtn;
	/**
	 * 
	 */
	private int demoStep = DemoModeConstant.DEMO_MODE_STEP_INTRO;
	private int prevStep = -1;
	private int apModeFailCounter;
	private int powerOnFailCounter;
	private int step1FailCounter;
	private DemoModeBroadcastReceiver broadcastReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup_main);
        
        try {
			getSupportFragmentManager().beginTransaction()
			.add(R.id.setup_fragment_container, new DemoModeStartFragement(), "demo_mode_start")
			.commit();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
        
        //Some time action bar getting ClassCastException
		try {
			initActionBar();
		} catch (ClassCastException e) {
			ALog.e(ALog.DEMO_MODE, "Action bar cast exception: " + "Error: " + e.getMessage());
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
		//If Chinese language selected set font-type-face normal
		if( LanguageUtils.getLanguageForLocale(Locale.getDefault()).contains("ZH-HANS")
				|| LanguageUtils.getLanguageForLocale(Locale.getDefault()).contains("ZH-HANT")) {
			actionbarTitle.setTypeface(Typeface.DEFAULT);
		}
		actionbarTitle.setText(getString(R.string.demo_mode_title));
		actionbarCancelBtn = (ImageButton) view.findViewById(R.id.setup_actionbar_cancel_btn);
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
			} catch (IllegalStateException e) {
				ALog.e(ALog.DEMO_MODE, "Error: " + e.getMessage());
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
		WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

		if (wifiManager != null && !wifiManager.isWifiEnabled()) {
			showDialogFragment(DemoModeWifiEnableDialogFragment.newInstance());
		} 
		/**
		 * Checking auto network switch enabled
		 * If enabled show alert dialog to user disable
		 */
		else if (EWSWifiManager.isPoorNetworkAvoidanceEnabled(this)) {
			showDialogFragment(AlertDialogAutoNetworkSwitchOn.newInstance());
		} else {
			showStepSwitchOnScreen();
		}
		
	}
	
	public void showStepSwitchOnScreen() {
		demoStep = DemoModeConstant.DEMO_MODE_STEP_SWITCHON;
		showDemoModeFragment(new DemoModePurifierSwitchOnFragment());
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
	
	public void showStepToSwitchAirPurOn() {
		powerOnFailCounter++;
		if (powerOnFailCounter > 2) {
			showSupportScreen();
		} else {
			SetupDialogFactory.getInstance(this).getDialog(SetupDialogFactory.SUPPORT_PLUG_AND_POWER_ON).show() ;
		}
	}
	
	public void connectToAirPurifier() {
		SetupDialogFactory.getInstance(DemoModeActivity.this).dismissSignalStrength();
		SetupDialogFactory.getInstance(this).getDialog(SetupDialogFactory.CHECK_SIGNAL_STRENGTH).show();
		if ( broadcastReceiver == null) {
			broadcastReceiver = new DemoModeBroadcastReceiver(this) ;
		}
		step1FailCounter++;
		broadcastReceiver.connectToDeviceAP() ;
	}
	
	public void showHomeScreen() {
		AirPurifierManager.getInstance().setEwsSate(EWS_STATE.EWS);
		AirPurifierManager.getInstance().setCurrentIndoorViewPagerPosition(0);
		setStaticIpAddress() ;
		finish();
	}
	
	/**
	 * Set the static ipAddress for the 2.3 version and below
	 */
	@SuppressWarnings("deprecation")
	private void setStaticIpAddress() {
		if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB ) {
			android.provider.Settings.System.putString(getContentResolver(), android.provider.Settings.System.WIFI_USE_STATIC_IP, "1");        
			android.provider.Settings.System.putString(getContentResolver(), android.provider.Settings.System.WIFI_STATIC_IP, Utils.getStaticIpAddress());
			android.provider.Settings.System.putString(getContentResolver(), android.provider.Settings.System.WIFI_STATIC_NETMASK, "255.255.255.0");
			android.provider.Settings.System.putString(getContentResolver(), android.provider.Settings.System.WIFI_STATIC_GATEWAY, "192.168.1.1");
			android.provider.Settings.System.putString(getContentResolver(), android.provider.Settings.System.WIFI_STATIC_DNS1, "0.0.0.0");
		}
	}
	
	private void showDemoModeFragment(Fragment fragment) {
		try {
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.replace(R.id.setup_fragment_container, fragment, "demo_mode_fragment");
			fragmentTransaction.commit();
		} catch (IllegalStateException e) {
			ALog.e(ALog.DEMO_MODE, "Error: " + e.getMessage());
		}
	}
	
	public void setActionbarTitle(int step) {
		switch (step) {
		case DemoModeConstant.DEMO_MODE_STEP_INTRO:
		case DemoModeConstant.DEMO_MODE_STEP_SWITCHON:
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
		case DemoModeConstant.DEMO_MODE_STEP_SWITCHON:
			showIntroScreen();
			return true;
		case DemoModeConstant.DEMO_MODE_STEP_ONE:
			showStepSwitchOnScreen();
			return true;
		case DemoModeConstant.DEMO_MODE_STEP_SUPPORT:
			if (prevStep == DemoModeConstant.DEMO_MODE_STEP_INTRO) {
				showIntroScreen();
			} else if (prevStep == DemoModeConstant.DEMO_MODE_STEP_SWITCHON) {
				powerOnFailCounter = 0;
				showStepSwitchOnScreen();
			} else {
				step1FailCounter = 0;
				apModeFailCounter = 0;
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
				SetupDialogFactory.getInstance(DemoModeActivity.this).dismissSignalStrength();
				
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
							ALog.e(ALog.DEMO_MODE, "Error: " + e.getMessage());
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
		} catch (IllegalStateException e) {
			ALog.e(ALog.DEMO_MODE, "Error: " + e.getMessage());
		}
	}
	
	private void showErrorDialog(String title,String msg, String btnTxt) {
		try {
			FragmentManager fragMan = getSupportFragmentManager();
			fragMan.beginTransaction()
			.add(SetupDialogFragment.newInstance(title, msg, btnTxt), "demo_error")
					.commitAllowingStateLoss();
		} catch (IllegalStateException e) {
			ALog.e(ALog.DEMO_MODE, "Error: " + e.getMessage());
		}
	}
	
	public int getApModeFailCounter() {
		return apModeFailCounter;
	}
	
	public int getStep1FailCounter() {
		return step1FailCounter;
	}
	
	public int getPowerOnFailCounter() {
		return powerOnFailCounter;
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