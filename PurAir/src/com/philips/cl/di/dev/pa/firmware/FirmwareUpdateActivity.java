package com.philips.cl.di.dev.pa.firmware;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.BaseActivity;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.constant.AppConstants.Port;
import com.philips.cl.di.dev.pa.firmware.FirmwareConstants.FragmentID;
import com.philips.cl.di.dev.pa.purifier.AirPurifierController;
import com.philips.cl.di.dev.pa.purifier.SubscriptionManager;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.Fonts;
import com.philips.cl.di.dev.pa.util.JSONBuilder;
import com.philips.cl.di.dev.pa.util.ServerResponseListener;
import com.philips.cl.di.dev.pa.util.Utils;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class FirmwareUpdateActivity extends BaseActivity implements OnClickListener, ServerResponseListener{
	
	private String purifierName;
	private String upgradeVersion;
	private String currentVersion;
	private int downloadFailedCount;
	private static boolean cancelled;
	private Button actionBarCancelBtn;
	private ImageView actionBarBackBtn;
	private FontTextView actionbarTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.firmware_container);
		initActionBar();
		Intent intent = getIntent();  
		purifierName = intent.getStringExtra(AppConstants.PURIFIER_NAME);
		upgradeVersion = intent.getStringExtra(AppConstants.UPGRADE_VERSION);
		currentVersion = intent.getStringExtra(AppConstants.CURRENT_VERSION);
		ALog.i(ALog.FIRMWARE, "Intent params purifierName " + purifierName + " upgradeVersion " + upgradeVersion + " currentVersion " + currentVersion);
		showFragment(upgradeVersion);
	}
			
	private void showFragment(String upgradeVersion) {
		if(upgradeVersion == null || upgradeVersion.isEmpty()) {
			getSupportFragmentManager().beginTransaction()
			.add(R.id.firmware_container, new FirmwareInstalledFragment(), FirmwareInstalledFragment.class.getSimpleName())
			.commit();
		} else {
			getSupportFragmentManager().beginTransaction()
			.add(R.id.firmware_container, new FirmwareUpdateFragment(), FirmwareUpdateFragment.class.getSimpleName())
			.commit();
		}
	}
	
	@Override
	public void onBackPressed() {
		showPreviousFragment();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		setCancelled(true);
		
		// TODO add bootid
		AirPurifierController.getInstance().unSubscribeFromAllEvents("",Utils.getIPAddress(), true);
		SubscriptionManager.getInstance().disableLocalSubscription();
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		setCancelled(false);
		
		// TODO add bootid
		AirPurifierController.getInstance().subscribeToAllEvents("", Utils.getIPAddress(), true);
		SubscriptionManager.getInstance().enableLocalSubscription();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		FirmwareDownloadFragment.setCounter(0);
		FirmwareInstallFragment.setCounter(0);
	}

	/*Initialize action bar */
	private void initActionBar() {
		ActionBar actionBar;
		actionBar = getSupportActionBar();
		actionBar.setIcon(null);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
		Drawable d=getResources().getDrawable(R.drawable.ews_nav_bar_2x);  
		actionBar.setBackgroundDrawable(d);
		View view  = getLayoutInflater().inflate(R.layout.ews_actionbar, null);
		actionbarTitle = (FontTextView) view.findViewById(R.id.ews_actionbar_title);
		actionbarTitle.setText(getString(R.string.firmware));
		actionBarCancelBtn = (Button) view.findViewById(R.id.ews_actionbar_cancel_btn);
		actionBarCancelBtn.setTypeface(Fonts.getGillsansLight(getApplicationContext()));
		actionBarCancelBtn.setOnClickListener(this);
		actionBarBackBtn = (ImageView) view.findViewById(R.id.ews_actionbar_back_img);
		actionBarBackBtn.setOnClickListener(this);
		actionBar.setCustomView(view);
	}

	public void setActionBar(FragmentID id) {
		switch (id) {
		case FIRMWARE_INSTALLED:
		case FIRMWARE_UPDATE:
			setActionBar(R.string.firmware, View.INVISIBLE, View.VISIBLE);
			break;
		case FIRMWARE_DOWNLOAD:
			setActionBar(R.string.firmware_update, View.VISIBLE, View.INVISIBLE);
			break;
		case FIRMWARE_INSTALL:
		case FIRMWARE_INSTALL_SUCCESS:
			setActionBar(R.string.firmware_update, View.INVISIBLE, View.INVISIBLE);
			break;
		case FIRMWARE_DOWNLOAD_FAILED:
			setActionBar(R.string.firmware_update, View.VISIBLE, View.INVISIBLE);
			break;
		case FIRMWARE_CONTACT_SUPPORT:
			setActionBar(R.string.contact_support, View.INVISIBLE, View.VISIBLE);
			break;
		case FIRMWARE_FAILED_SUPPORT:
			setActionBar(R.string.firmware_update, View.VISIBLE, View.INVISIBLE);
			break;
		default:
			break;
		}
	}
	
	private void setActionBar(int textId, int cancelButton, int backButton) {
		actionbarTitle.setText(textId);
		actionBarCancelBtn.setVisibility(cancelButton);
		actionBarBackBtn.setVisibility(backButton);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ews_actionbar_cancel_btn:
			setCancelled(true);
			Toast.makeText(this, "Cancel button clicked", Toast.LENGTH_SHORT).show();
			setDeviceDetailsLocally(FirmwareConstants.STATE, FirmwareConstants.CANCEL);
			finish();
			break;
		case R.id.ews_actionbar_back_img:
			showPreviousFragment();
			break;
		default:
			break;
		}
	}
	
	private void showPreviousFragment() {
		FragmentManager manager = getSupportFragmentManager();
		Fragment fragment = manager.findFragmentById(R.id.firmware_container);
		ALog.i(ALog.FIRMWARE, " NewFirmwareUpdateFragment " + (fragment instanceof FirmwareUpdateFragment));
		
		if(fragment instanceof FirmwareUpdateFragment || fragment instanceof FirmwareInstalledFragment) {
			finish();
		} else if (fragment instanceof FirmwareContactSupportFragment) {
			getSupportFragmentManager().beginTransaction()
			.replace(R.id.firmware_container, new FirmwareFailedSupportFragment(), FirmwareFailedSupportFragment.class.getSimpleName())
			.commit();
		}
	}

	public void setDeviceDetailsLocally(String key, String value )
	{
		String dataToUpload = JSONBuilder.getDICommBuilder(key,value) ;
		startServerTask(dataToUpload) ;
	}
	
	private void startServerTask(String dataToUpload) {
		FirmwarePutPropsTask statusUpdateTask = new FirmwarePutPropsTask(dataToUpload, Utils.getPortUrl(Port.FIRMWARE, Utils.getIPAddress()),this) ;
		Thread statusUpdateTaskThread = new Thread(statusUpdateTask) ;
		statusUpdateTaskThread.start() ;
	}

	/**
	 * We are not doing anything with the response object.
	 */
	@Override
	public void receiveServerResponse(int responseCode, String responseData) {
//		ALog.i(ALog.FIRMWARE, "FUActivity$receiveServerResponse resp code " + responseCode + " resp data " + responseData);
	}
	
	public String getUpgradeVersion() {
		return upgradeVersion;
	}
	
	public String getCurrentVersion() {
		return currentVersion;
	}
	
	public String getPurifierName() {
		return purifierName;
	}
	
	public int getDownloadFailedCount() {
		return downloadFailedCount;
	}

	public void setDownloadFailedCount(int downloadFailedCount) {
		this.downloadFailedCount = downloadFailedCount;
	}
	
	public String getFirmwareURL() {
		String firmwareUrl = Utils.getPortUrl(Port.FIRMWARE, Utils.getIPAddress());
		return firmwareUrl;
	}
	
	public static void setCancelled(boolean cancelled) {
		FirmwareUpdateActivity.cancelled = cancelled;
	}
	
	public static boolean isCancelled() {
		return cancelled;
	}
}
