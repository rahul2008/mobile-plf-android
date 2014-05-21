package com.philips.cl.di.dev.pa.demo;

import android.graphics.drawable.Drawable;
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
import com.philips.cl.di.dev.pa.ews.SetupCancelDialogFragment;
import com.philips.cl.di.dev.pa.ews.SetupDialogFactory;
import com.philips.cl.di.dev.pa.ews.EWSDialogFragment;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.Fonts;
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
	public int setAPModeCounter;
	public int step1FailedCounter;
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
	
	public void showStepOneScreen() {
		demoStep = DemoModeConstant.DEMO_MODE_STEP_ONE;
		showDemoModeFragment(new DemoModeStepOneFragment());
	}
	
	public void showSupportScreen() {
		demoStep = DemoModeConstant.DEMO_MODE_STEP_SUPPORT;
		showDemoModeFragment(new DemoModeSupportFragment());
	}
	
	public void showFinalScreen() {
		demoStep = DemoModeConstant.DEMO_MODE_STEP_FINAL;
		showDemoModeFragment(new DemoModeFinalFragment());
	}
	
	public void showStepToSetupAPMode() {
		setAPModeCounter++;
		if (setAPModeCounter > 2) {
			showSupportScreen();
		} else {
			SetupDialogFactory.getInstance(this).getDialog(SetupDialogFactory.ERROR_TS01_01).show() ;
		}
	}
	
	private boolean isTaskStarted;
	public void connectToAirPurifier() {
		if (isTaskStarted) return;
		isTaskStarted = true;
		dismissConnectingDialog();
		SetupDialogFactory.getInstance(this).getDialog(SetupDialogFactory.CHECK_SIGNAL_STRENGTH).show();
		if ( broadcastReceiver == null) {
			broadcastReceiver = new DemoModeBroadcastReceiver(this) ;
		}
		step1FailedCounter++;
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
			try {
				FragmentManager fragMan = getSupportFragmentManager();
				fragMan.beginTransaction().add(
						SetupCancelDialogFragment.newInstance(), "setup_cancel").commitAllowingStateLoss();
			} catch (Exception e) {
				ALog.e(ALog.DEMO_MODE, e.getMessage());
			}
			return true;
		case DemoModeConstant.DEMO_MODE_STEP_ONE:
			showIntroScreen();
			return true;
		case DemoModeConstant.DEMO_MODE_STEP_SUPPORT:
			setAPModeCounter = 0;
			step1FailedCounter = 0;
			showStepOneScreen();
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
		isTaskStarted = false;
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				dismissConnectingDialog();
				
				switch (errorCode) {
				case DemoModeConstant.DEMO_MODE_ERROR_NOT_IN_PHILIPS_SETUP:
					if (step1FailedCounter > 2) {
						showSupportScreen();
					} else {
						SetupDialogFactory.getInstance(
								DemoModeActivity.this).getDialog(SetupDialogFactory.ERROR_TS01_01).show() ;
					}
					break;
				case DemoModeConstant.DEMO_MODE_ERROR_DATA_RECIEVED_FAILED:
					if (step1FailedCounter > 2) {
						showSupportScreen();
					} else {
						try {
							FragmentManager fragMan = getSupportFragmentManager();
							fragMan.beginTransaction()
									.add(EWSDialogFragment.newInstance(), "demo_error")
									.commitAllowingStateLoss();
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

	@Override
	public void onHandShakeWithDevice() {
		isTaskStarted = false;
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				showFinalScreen();
			}
		});
	}
	
	public void dismissConnectingDialog() {
		if (SetupDialogFactory.getInstance(
				DemoModeActivity.this).getDialog(SetupDialogFactory.CHECK_SIGNAL_STRENGTH).isShowing()) {
			SetupDialogFactory.getInstance(
					DemoModeActivity.this).getDialog(SetupDialogFactory.CHECK_SIGNAL_STRENGTH).dismiss();
		}
	}
}
