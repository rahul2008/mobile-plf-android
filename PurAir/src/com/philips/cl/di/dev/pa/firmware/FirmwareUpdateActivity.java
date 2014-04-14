package com.philips.cl.di.dev.pa.firmware;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.BaseActivity;
import com.philips.cl.di.dev.pa.constant.AppConstants;
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
		if(upgradeVersion == null || upgradeVersion.equals("")) {
			getSupportFragmentManager().beginTransaction()
			.add(R.id.firmware_container, new NewFirmwareUpdateFragment(), "NewFirmwareUpdateFragment")
			.commit();
		} else {
			getSupportFragmentManager().beginTransaction()
			.add(R.id.firmware_container, new NewFirmware(), "NewFirmware")
			.commit();
		}
		
	}
	
	@Override
	public void onBackPressed() {
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		setCancelled(false);
		FirmwareDownloadFragment.setCounter(0);
		FirmwareInstallFragment.setCounter(0);
	}

	/*Initialize action bar */
	private void initActionBar() {
		ActionBar actionBar;
		FontTextView actionbarTitle;
		Button actionBarCancelBtn;
		actionBar = getSupportActionBar();
		actionBar.setIcon(null);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
		Drawable d=getResources().getDrawable(R.drawable.ews_nav_bar_2x);  
		actionBar.setBackgroundDrawable(d);
		View view  = getLayoutInflater().inflate(R.layout.ews_actionbar, null);
		actionbarTitle = (FontTextView) view.findViewById(R.id.ews_actionbar_title);
		actionbarTitle.setText(getString(R.string.firmware));
		actionBarCancelBtn = (Button) view.findViewById(R.id.ews_actionbar_cancel_btn);
		actionBarCancelBtn.setTypeface(Fonts.getGillsansLight(this));
		actionBarCancelBtn.setOnClickListener(this);
		actionBar.setCustomView(view);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ews_actionbar_cancel_btn:
			setCancelled(true);
			Toast.makeText(this, "Cancel button clicked", Toast.LENGTH_SHORT).show();
			setDeviceDetailsLocally("state", "cancel");
			finish();
			break;

		default:
			break;
		}
	}

	public void setDeviceDetailsLocally(String key, String value )
	{
		String dataToUpload = JSONBuilder.getDICommBuilder(key,value) ;
		startServerTask(dataToUpload) ;
	}
	
	private void startServerTask(String dataToUpload) {
		FirmwarePutPropsTask statusUpdateTask = new FirmwarePutPropsTask(dataToUpload, String.format(AppConstants.URL_FIRMWARE_PORT, Utils.getIPAddress()),this) ;
		Thread statusUpdateTaskThread = new Thread(statusUpdateTask) ;
		statusUpdateTaskThread.start() ;
	}

	/**
	 * We are not doing anything with the response object.
	 */
	@Override
	public void receiveServerResponse(int responseCode, String responseData) {
		ALog.i(ALog.FIRMWARE, "FUActivity$receiveServerResponse resp code " + responseCode + " resp data " + responseData);
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
		String firmwareUrl = String.format(AppConstants.URL_FIRMWARE_PORT, Utils.getIPAddress());
		return firmwareUrl;
	}
	
	public static void setCancelled(boolean cancelled) {
		FirmwareUpdateActivity.cancelled = cancelled;
	}
	
	public static boolean isCancelled() {
		return cancelled;
	}
}
