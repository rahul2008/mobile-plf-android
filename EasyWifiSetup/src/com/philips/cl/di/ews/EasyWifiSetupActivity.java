package com.philips.cl.di.ews;

import com.philips.cl.di.fragments.DiSectionsPagerAdapter;
import com.philips.cl.disecurity.DISecurity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

public class EasyWifiSetupActivity extends FragmentActivity implements EasyWifiSetupListener {

	/**
	 * The list of constants are listed here
	 */
	private static final String TAG = EasyWifiSetupActivity.class.getName();
	public static final String EXTRA_TARGET_WIFI = "targetString";
	public static final String EXTRA_END_POINT = "endPoint";
	public static final String EXTRA_BASE_URL = "baseUrl";
	public static final int SETUP_WIFI = 324;
	public static final int SETUP_WIFI_DONE = 200;
	public static final int SETUP_WIFI_FAILED = 404;
	
	private DISecurity diSecurity;
	
	public String mUrl_base=null;

	/**
	 * BroadcastReceiver handles the wifi setting related event and send event
	 * to the activity
	 */
	private EasyWifiSetupService mService;

	/**
	 * contains the cunnent enabled wifi ap info
	 */

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	DiSectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	
	private View ews_start_button = null;
	
	private String password;
	private String deviceName;

	private Context context;

	public Context getAppContext() {
		return context;
	}

	static public void start(Activity aActivity, String aTarget,
			String aEndPoint) {
		Intent intent = new Intent(aActivity, EasyWifiSetupActivity.class);
		intent.putExtra(EXTRA_TARGET_WIFI, aTarget);
		intent.putExtra(EXTRA_END_POINT, aEndPoint);
		aActivity.startActivityForResult(intent, SETUP_WIFI);
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getApplicationContext();
		setContentView(R.layout.ews_activity);
		diSecurity = new DISecurity(null);
		mSectionsPagerAdapter = new DiSectionsPagerAdapter(getSupportFragmentManager(), getBaseContext(),"ews_page_",7);
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

	}
	
	public void goToPage(int page) {
		if (mViewPager.getCurrentItem() == (page - 1) ){
			mViewPager.setCurrentItem(page);
		}

		Log.i(TAG, "PAGE IN goToPage= " + page);
		
		switch (page) {
		case 0: // go to explaining page
			break;
		case 1: // go to welcome page
			initService();
			break;
		case 2: // go to scanning page
			startBlinking(R.id.orange_led);
			getService().startScanForDeviceAp();
			break;
		case 3: // go to ssid page
			if(mService.mInfo.isOpen()){
				goToPage(page+1);
			}
			break;
		case 4: // go to sending info to device page
			password = ((EditText) findViewById(R.id.password)).getText().toString();   
			break;
		case 5: // go to final page
			startBlinking(R.id.blue_led);
			EditText et = (EditText) findViewById(R.id.device_name);
			deviceName = et.getText().toString(); 
			sendNetworkInfoToDevieceAp();
			break;
		case 6:
			changeResultonPage(mUrl_base);
			break;
		case 7:
			finishWifiSetup();
			break;
		

		}

	}

	private void changeResultonPage(String url) {
		if(url==null){
			
		}
	}

	private void initService() {
		Intent intent = getIntent();
		String anEndPoint = intent.getStringExtra(EXTRA_END_POINT);
		String aTargetString = intent.getStringExtra(EXTRA_TARGET_WIFI);
		setService(new EasyWifiSetupService(this, this, aTargetString,anEndPoint));
	}

	
	@Override
	protected void onResume() {
		super.onResume();
		ews_start_button = findViewById(R.id.ews_button0);
		if(ews_start_button!=null){
			ews_start_button.setEnabled(wifiEnabled());
		}
	}
	@Override
	protected void onDestroy() {
		try {
			// TODO: solve it more elegantly later please
			unregisterReceiver(getService());
		} catch (Exception e) {
			// can not be sure that it is registered
		}
		super.onDestroy();
	}


	public void goToSetting(View v) {
		startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
	}
	public void goBack(View v) {
		int current = mViewPager.getCurrentItem();
		if (current > 0) {
			mViewPager.setCurrentItem(current - 1);
		}
	}
	public void goToPage(View v) {
		int page = Integer.parseInt(v.getTag().toString());
		goToPage(page);
	}
	public void changeTransform(View v) {
		if(((CheckBox)v).isChecked()){
			((EditText) findViewById(R.id.password)).setTransformationMethod(null);
		}else{
			((EditText) findViewById(R.id.password)).setTransformationMethod(new PasswordTransformationMethod());
		}
	}


	private void startBlinking(int resId) {
		 ImageView img = (ImageView)findViewById(resId);
		 AnimationDrawable animation = (AnimationDrawable) img.getDrawable();
		 animation.start();
	}

	private void stopBlinking(int resId) {
		ImageView img = (ImageView)findViewById(resId);
		 AnimationDrawable animation = (AnimationDrawable) img.getDrawable();
		 animation.stop();
	}

	private boolean wifiEnabled() {
		final WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
		return wifiManager.isWifiEnabled();
	}

	boolean found = false;
	private boolean sendToDevice=false;

	@Override
	public void foundDeviceAp(String target) {
		Log.i(TAG, "Target IN foundDeviceAp= " + target);
		if (!found) {
			stopBlinking(R.id.orange_led);
			EasyWifiSetupService.connectTo( this,target,"");
			found = true;
		}
	}

	@Override
	public void connectedToDeviceAp(String ssid) {
		Log.i(TAG, "SSID IN connectedToDeviceAp= " + ssid);
		goToPage(3);
	}

	public void sendNetworkInfoToDevieceAp() {
		if (!sendToDevice){
			getService().setPassowrd(password);
			getService().setDeviceName(deviceName);
			getService().sendSSidToDevice();
			sendToDevice = true;
		}
	}
	

	@Override
	public void sentSsidToDevice(String result) {
		Log.i(TAG, "Result IN Encrypted sentSsidToDevice= " + result);
		String response = diSecurity.decryptData(result, "devId01");
		Log.i(TAG, "Result IN Decryted sentSsidToDevice= " + response);
		getService().verifyConnectedToHomeNetwork();
	}


	public void finishWifiSetup() {
		startBlinking(R.id.error_led);

		Intent intent = getIntent();
		intent.putExtra(EXTRA_BASE_URL, mUrl_base);
		setResult(SETUP_WIFI_DONE,intent);
		finish();
	}

	public EasyWifiSetupService getService() {
		return mService;
	}

	public void setService(EasyWifiSetupService aService) {
		mService = aService;
	}
	

	
	@Override
	public void connectedToHomeNetwork(String url_base) {
		Log.i(TAG, "url_base IN connectedToHomeNetwork= " + url_base);
		mUrl_base = url_base;
		goToPage(6);
	}
}
