package com.philips.cl.di.dev.pa.ews;

import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.dashboard.OutdoorController;
import com.philips.cl.di.dev.pa.datamodel.SessionDto;
import com.philips.cl.di.dev.pa.fragment.StartFlowDialogFragment.StartFlowListener;
import com.philips.cl.di.dev.pa.newpurifier.ConnectionState;
import com.philips.cl.di.dev.pa.newpurifier.DiscoveryEventListener;
import com.philips.cl.di.dev.pa.newpurifier.DiscoveryManager;
import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager.EWS_STATE;
import com.philips.cl.di.dev.pa.purifier.PurifierDatabase;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.Fonts;
import com.philips.cl.di.dev.pa.util.LanguageUtils;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.util.Utils;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class EWSActivity extends ActionBarActivity implements 
	OnClickListener, EWSListener, DiscoveryEventListener, StartFlowListener {

	private EWSStartFragment ewsIntroductionScreenFragment;
	
	private int mStep = EWSConstant.EWS_STEP_START;
	private int prevStep;
	/**
	 * Action bar variable
	 */
	private FontTextView actionbarTitle;
	private ImageView actionbarBackImg;
	private Button actionbarCancelBtn;

	private WifiManager wifiManager ;
	private ConnectivityManager connManager;
	private NetworkInfo mWifi;
	private String networkSSID ;
	private String password ;
	private boolean advSetting = false ;
	
	private EWSBroadcastReceiver ewsService ;
	private String cppId;
	private int apModeFailCounter;
	private int step2FailCounter;
	private int powerOnFailCounter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup_main);
        
        try {
			getSupportFragmentManager().beginTransaction()
			.add(R.id.setup_fragment_container, getIntroFragment(), EWSConstant.EWS_INTRO_SCREEN_FRAGMENT_TAG)
			.commit();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
        
        //Some time action bar getting ClassCastException
		try {
			initActionBar();
		} catch (ClassCastException e) {
			ALog.e(ALog.EWS, "Action bar cast exception: " + "Error: " + e.getMessage());
		}
		
		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		
		PurifierManager.getInstance().setEwsSate(EWS_STATE.EWS);
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
		actionbarTitle.setText(getString(R.string.wifi_setup));
		actionbarCancelBtn = (Button) view.findViewById(R.id.setup_actionbar_cancel_btn);
		actionbarCancelBtn.setTypeface(Fonts.getGillsansLight(this));
		actionbarCancelBtn.setVisibility(View.INVISIBLE);
		actionbarBackImg = (ImageView) view.findViewById(R.id.setup_actionbar_back_img);
		actionbarBackImg.setOnClickListener(this);
		actionbarCancelBtn.setOnClickListener(this);
		actionBar.setCustomView(view);	
	}
	
	public void setActionBarHeading (int index) {
		switch (index) {
		case EWSConstant.EWS_STEP_START:
		case EWSConstant.EWS_STEP_CHANGE_NETWORK:	
		case EWSConstant.EWS_STEP_ONE:
		case EWSConstant.EWS_STEP_TWO_POWER_ON:
		case EWSConstant.EWS_STEP_TWO:
		case EWSConstant.EWS_STEP_THREE:
			//start, step1, step2 and step3 screen
			actionbarCancelBtn.setVisibility(View.INVISIBLE);
			actionbarBackImg.setVisibility(View.VISIBLE);
			actionbarTitle.setText(getString(R.string.wifi_setup));
			break;
		case EWSConstant.EWS_STEP_FINAL:
			//Congratulation screen
			actionbarCancelBtn.setVisibility(View.INVISIBLE);
			actionbarBackImg.setVisibility(View.INVISIBLE);
			actionbarTitle.setText(getString(R.string.congratulations));
			break;
		case EWSConstant.EWS_STEP_SUPPORT:
			//support screen
			actionbarCancelBtn.setVisibility(View.VISIBLE);
			actionbarBackImg.setVisibility(View.VISIBLE);
			actionbarTitle.setText(getString(R.string.support));
			break;
		case EWSConstant.EWS_STEP_ERROR_SSID:
			actionbarCancelBtn.setVisibility(View.VISIBLE);
			actionbarBackImg.setVisibility(View.GONE);
			actionbarTitle.setText(getString(R.string.error_purifier_not_detect_head));
			break;
		case EWSConstant.EWS_STEP_ERROR_DISCOVERY:
			actionbarCancelBtn.setVisibility(View.GONE);
			actionbarBackImg.setVisibility(View.GONE);
			actionbarTitle.setText(getString(R.string.error_purifier_not_detect_head));
			break;
		default:
			break;
		}
	}
		
	@Override
	protected void onStop() {
		super.onStop();
		unRegisterListener() ;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if( ewsService != null ) {
			ewsService.stopSSDPCountDownTimer() ;
		}
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		ALog.i(ALog.EWS, "onWindowFocused") ;
		super.onWindowFocusChanged(hasFocus);
		if ( hasFocus ) {
			registerNetworkListener() ;
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.setup_actionbar_back_img:
			replaceFragmentOnBackPress();
			break;
		case R.id.setup_actionbar_cancel_btn:
			try {
				FragmentManager fragMan = getSupportFragmentManager();
				fragMan.beginTransaction().add(
						SetupCancelDialogFragment.newInstance(), "ews_cancel").commitAllowingStateLoss();
			} catch (IllegalStateException e) {
				ALog.e(ALog.EWS, "Error: " + e.getMessage());
			}
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			ALog.i(ALog.EWS, "On back fagment step: " + mStep);
			replaceFragmentOnBackPress();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private boolean replaceFragmentOnBackPress() {
		ALog.i(ALog.EWS, "replaceFragmentOnBackPress: " + mStep);
		switch (mStep) {
		case EWSConstant.EWS_STEP_START:
			try {
				FragmentManager fragMan = getSupportFragmentManager();
				fragMan.beginTransaction().add(
						SetupCancelDialogFragment.newInstance(), "ews_cancel").commitAllowingStateLoss();
			} catch (IllegalStateException e) {
				ALog.e(ALog.EWS, "Error: " + e.getMessage());
			}
			return true;
		case EWSConstant.EWS_STEP_CHANGE_NETWORK:
			if (!isHomeNeworkSelected()) {
				showEwsStartFragment();
			} else {
				showStepOneFragment();
			}
			return true;
		case EWSConstant.EWS_STEP_ONE:
		case EWSConstant.EWS_STEP_ERROR_SSID:
			showEwsStartFragment();
			return true;
		case EWSConstant.EWS_STEP_TWO_POWER_ON:
			showStepOneFragment();
			return true;
		case EWSConstant.EWS_STEP_TWO:
			showStepTwoPowerOnPurifier();
			return true;
		case EWSConstant.EWS_STEP_THREE:
		case EWSConstant.EWS_STEP_ERROR_DISCOVERY:
		case EWSConstant.EWS_STEP_SUPPORT:
			if (prevStep == EWSConstant.EWS_STEP_TWO_POWER_ON) {
				powerOnFailCounter = 0;
				showStepTwoPowerOnPurifier();
			} else {
				apModeFailCounter = 0;
				step2FailCounter = 0;
				showStepTwo();
			}
			return true;
		case EWSConstant.EWS_STEP_FINAL:
			return true;
		default:
			return false;
		}
	}
	
	private void showEwsStartFragment() {
		this.mStep = EWSConstant.EWS_STEP_START;
		showFragment(new EWSStartFragment(), EWSConstant.EWS_INTRO_SCREEN_FRAGMENT_TAG);
	}
	
	private void showNetworkChangeFragment() {
		mStep = EWSConstant.EWS_STEP_CHANGE_NETWORK ;
		showFragment(new EWSNetworkChangeFragment(), EWSConstant.EWS_NETWORK_FRAGMENT_TAG);
	}
	
	public void showStepOneFragment() {
		mStep = EWSConstant.EWS_STEP_ONE ;
		showFragment(new EWSStepOneFragment(), EWSConstant.EWS_STEP_ONE_FRAGMENT_TAG);
	}
	
	private void showErrorSSIDFragement() {
		mStep = EWSConstant.EWS_STEP_ERROR_SSID ;
    	showFragment(new EWSErrorSSIDFragment(), EWSConstant.EWS_ERROR_SSID_FRAGMENT_TAG);
	}
	
	private void showErrorDiscoveryFragement() {
		mStep = EWSConstant.EWS_STEP_ERROR_DISCOVERY ;
		showFragment(new EWSErrorPurifierDiscoverFragment(), EWSConstant.EWS_ERROR_DISCOVER_FRAGMENT_TAG);
	}
	
	public void showSupportFragment() {
		prevStep  = mStep;
		mStep = EWSConstant.EWS_STEP_SUPPORT ;
		showFragment(new EWSSupportFragment(), EWSConstant.EWS_SUPPORT_FRAGMENT_TAG);
	}
	
	private void showStepOne() {
		ALog.i(ALog.EWS, "showStepOne== " +networkSSID + "  step: " + mStep);
		if( wifiManager.getConnectionInfo() != null ) {
			networkSSID = wifiManager.getConnectionInfo().getSSID() ;
			if ( networkSSID != null ) {
				ALog.i(ALog.EWS, networkSSID) ;
				networkSSID = networkSSID.replace("\"", "") ;
				if( ! networkSSID.contains(EWSWifiManager.DEVICE_SSID)) {
					if( mStep != EWSConstant.EWS_STEP_ONE ) {
						showStepOneFragment();
					}					
				}
				else {
					networkSSID = null ;
					showNetworkChangeFragment();
				}			
			}else {
				networkSSID = null ;
				showNetworkChangeFragment();
			}
		}
		else {
			networkSSID = null ;
			showNetworkChangeFragment();
		}		
	}
	
	public void showHomeScreen() {
		mStep = EWSConstant.EWS_START_MAIN;
		
		PurifierManager.getInstance().setEwsSate(EWS_STATE.EWS);
		Utils.saveAppFirstUse(false);
		
		Location location = OutdoorController.getInstance().getCurrentLocation();
		
		PurAirDevice current = PurifierManager.getInstance().getCurrentPurifier();
		if (current != null) {
			ALog.i(ALog.PAIRING, "EWS-setting paring to false");
			current.setPairing(NetworkNode.PAIRED_STATUS.NOT_PAIRED) ;
			//Save current location latitude and longitude
			if (location != null) {
				ALog.i(ALog.INDOOR_DETAILS, 
						"EWS Purifier Current city lat: " + location.getLatitude() + "; long:" + location.getLongitude());
				current.setLatitude(String.valueOf(location.getLatitude()));
				current.setLongitude(String.valueOf(location.getLongitude()));
			}
			new PurifierDatabase().insertPurAirDevice(current);
			List<PurAirDevice> purifiers = DiscoveryManager.getInstance().updateStoreDevices();
			PurifierManager.getInstance().setCurrentIndoorViewPagerPosition(purifiers.size() - 1);
		}
		
		// STOP move code
		finish() ;
	}
	
	public void changeWifiNetwork() {
		if( ewsService == null ) {
			ewsService = new EWSBroadcastReceiver(this, null) ;
		}
		ewsService.setSSID(null) ;
		showNetworkChangeFragment();
	}
	
	public void showStepTwoPowerOnPurifier() {
		mStep = EWSConstant.EWS_STEP_TWO_POWER_ON ;
		if( ewsService != null ) {
			ewsService.setSSID(networkSSID) ;
		}
		showFragment(new EWSPurifierSwitchOnFragment(), EWSConstant.EWS_STEP_TWO_POWER_ON_FRAGMENT_TAG);
	}
	
	public void showStepTwo() {
		mStep = EWSConstant.EWS_STEP_TWO ;
		if( ewsService != null ) {
			ewsService.setSSID(networkSSID) ;
		}
		showFragment(new EWSStepTwoFragment(), EWSConstant.EWS_STEP_TWO_FRAGMENT_TAG);
	}
	
	// This method will send the Device name to the AirPurifier when user selects Save
	public void sendDeviceNameToPurifier(String deviceName) {
		
		if (deviceName.equals("") || SessionDto.getInstance().getDeviceDto() == null) {
			return;
		}
		if( ! deviceName.equals("") && !deviceName.equals(SessionDto.getInstance().getDeviceDto().getName())) {
			ewsService.setDeviceName(deviceName) ;
			ewsService.putDeviceDetails() ;
		}
	}
	
	public void airPurifierInSetupMode() {
		mStep = EWSConstant.EWS_STEP_TWO ;
		showFragment(new EWSStepTwoFragment(), EWSConstant.EWS_STEP_TWO_FRAGMENT_TAG);
 	}
	
	public void checkWifiConnectivity() {
		/**
		 * Checking auto network switch enabled
		 * If enabled show alert dialog to user disable
		 */
		if (EWSWifiManager.isPoorNetworkAvoidanceEnabled(this)) {
			try {
				FragmentManager fragMan = getSupportFragmentManager();
				fragMan.beginTransaction().add(
						AlertDialogAutoNetworkSwitchOn.newInstance(), "auto_networ_switch").commitAllowingStateLoss();
			} catch (IllegalStateException e) {
				ALog.e(ALog.EWS, "Error: " + e.getMessage());
			}
		} else {
			mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			ALog.i(ALog.EWS, "mWifi.isConnected()== " +mWifi.isConnected() + ", step: " + mStep);
			if(!mWifi.isConnected()) {
				showNetworkChangeFragment();
				ewsService = new EWSBroadcastReceiver(this, networkSSID) ;
			}
			else {
				showStepOne() ;
			}
		}
	}
	
	private void registerNetworkListener() {
		ALog.i(ALog.EWS, "registerNetworkListener") ;
		
		if ( mStep == EWSConstant.EWS_STEP_CHANGE_NETWORK || mStep == EWSConstant.EWS_STEP_ONE) {
			if( ewsService == null ) {
				ewsService = new EWSBroadcastReceiver(this, null) ;
			}
			ewsService.setSSID(null) ;
			ewsService.registerListener() ;
		} else if (mStep == EWSConstant.EWS_STEP_ERROR_SSID) {
			if( ewsService == null ) {
				ewsService = new EWSBroadcastReceiver(this, networkSSID) ;
			}
			ewsService.setSSID(networkSSID) ;
			ewsService.registerListener() ;
		}
	}
	
	public void connectToAirPurifier() {
		SetupDialogFactory.getInstance(EWSActivity.this).dismissSignalStrength();
		SetupDialogFactory.getInstance(this).getDialog(SetupDialogFactory.CHECK_SIGNAL_STRENGTH).show();
		if ( ewsService == null) {
			ewsService = new EWSBroadcastReceiver(this, networkSSID) ;
		}
		step2FailCounter++;
		ewsService.setSSID(networkSSID) ;
		ewsService.connectToDeviceAP() ;
	}
	
	public void showEWSSetUpInstructionsDialog() {
		apModeFailCounter++;
		if (apModeFailCounter > 2) {
			showSupportFragment();
		} else {
			SetupDialogFactory.getInstance(this).getDialog(SetupDialogFactory.ERROR_TS01_01).show() ;
		}
	}	
	
	public void showPowerOnInstructionsDialog() {
		powerOnFailCounter++;
		if (powerOnFailCounter > 2) {
			showSupportFragment();
		} else {
			SetupDialogFactory.getInstance(this).getDialog(SetupDialogFactory.SUPPORT_PLUG_AND_POWER_ON).show() ;
		}
	}	
	
	public void sendNetworkDetails(String ssid, String password, String ipAdd, String subnetMask, String gateWay) {
		networkSSID = ssid;
		this.password = password;
		showConnectToPurifierDialog();
		ewsService.setSSID(networkSSID) ;
		ewsService.setPassword(password) ;
		ewsService.putWifiDetails(ipAdd, subnetMask, gateWay) ;
	}

	// Override methods - EWSListener - Start
	@Override
	public void onDeviceAPMode() {/**NOP*/}

	@Override
	public void onSelectHomeNetwork() {
		if (mStep == EWSConstant.EWS_STEP_ERROR_SSID) {
			DiscoveryManager.getInstance().start(this);
			showConnectToPurifierDialog();
			showStepThreeFragment();
			searchPurifierTimer.start();
		}
	}

	@Override
	public void onHandShakeWithDevice() {
		SetupDialogFactory.getInstance(EWSActivity.this).dismissSignalStrength();
		showStepThreeFragment();
	}
	
	private void showStepThreeFragment() {
		EWSStepThreeFragment stepThreeFragment = new EWSStepThreeFragment();
		
		//If mobile failed to connect home network
		if (mStep == EWSConstant.EWS_STEP_ERROR_SSID) {
			Bundle bundle = new Bundle();
			bundle.putString(EWSStepThreeFragment.EXTRA_PASSWORD, password);
			bundle.putBoolean(EWSStepThreeFragment.EXTRA_ADV_SETTING, advSetting);
			stepThreeFragment.setArguments(bundle);
		}
		
		mStep = EWSConstant.EWS_STEP_THREE ;
		showFragment(stepThreeFragment, EWSConstant.EWS_STEP_THREE_FRAGMENT_TAG);
		
		if (SessionDto.getInstance().getDeviceWifiDto() != null) {
			cppId = SessionDto.getInstance().getDeviceWifiDto().getCppid(); 
		}
	}

	@Override
	public void onDeviceConnectToHomeNetwork() {
		DiscoveryManager.getInstance().start(this);
	}
	
	@Override
	public void foundHomeNetwork() {
		/**
		 * Show step one screen when network change happen in Change network screen or error SSID screen
		 */
		if (mStep == EWSConstant.EWS_STEP_CHANGE_NETWORK) {
			showStepOne() ;
		}
	}
	
	@Override
	public void onWifiDisabled() {
		ALog.i(ALog.EWS, "onWifiDisabled");
	}

	@Override
	public void onErrorOccurred(int errorCode) {
		
		ALog.i(ALog.EWS, "onErrorOccurred: "+errorCode) ;
		SetupDialogFactory.getInstance(EWSActivity.this).dismissSignalStrength();
		
		if( SetupDialogFactory.getInstance(EWSActivity.this).getDialog(SetupDialogFactory.CONNECTING_TO_PRODUCT).isShowing()) {
			SetupDialogFactory.getInstance(EWSActivity.this).getDialog(SetupDialogFactory.CONNECTING_TO_PRODUCT).dismiss();
		}
		
		switch (errorCode) {
		case EWSListener.ERROR_CODE_PHILIPS_SETUP_NOT_FOUND:
			if (step2FailCounter > 2) {
				showSupportFragment();
			} else {
				showErrorDialog(getString(R.string.error_ts01_01_title), 
						getString(R.string.error_ts01_01_message), 
						getString(R.string.next));
			}
			MetricsTracker.trackPageTechnicalError("EWS", "Error : Philips Setup not found");
			break;
		case EWSListener.ERROR_CODE_COULDNOT_RECEIVE_DATA_FROM_DEVICE:
			if (step2FailCounter > 2) {
				showSupportFragment();
				MetricsTracker.trackPageTechnicalError("EWS", "Error : Unable to reveive data from purifier");
				break;
			}
		case EWSListener.ERROR_CODE_COULDNOT_SEND_DATA_TO_DEVICE:
			showErrorDialog(getString(R.string.error_ts01_04_title), 
					getString(R.string.error_ts01_01_message), 
					getString(R.string.error_purifier_not_detect_btn_txt));
			MetricsTracker.trackPageTechnicalError("EWS", "Error : Unable to send data to purifier");
			break;
		case EWSListener.ERROR_CODE_COULDNOT_CONNECT_HOME_NETWORK:	
			stopDiscovery();
			showErrorSSIDFragement();
			MetricsTracker.trackPageTechnicalError("EWS", "Error : Unable to connect to home network");
			break;
		case EWSListener.ERROR_CODE_COULDNOT_FIND_DEVICE:	
			stopDiscovery();
			showErrorDiscoveryFragement();
			MetricsTracker.trackPageTechnicalError("EWS", "Error : Unable to find purifier");
			break;
		case EWSListener.ERROR_CODE_INVALID_PASSWORD:
			Toast.makeText(this, getString(R.string.wrong_wifi_password), Toast.LENGTH_LONG).show() ;
			MetricsTracker.trackPageUserError("EWS", "Error : Invalid WiFi password");
			break;
		default:
			break;
		}
	}
	
	private void showConnectToPurifierDialog() {
		SetupDialogFactory.getInstance(this).setNetworkName(networkSSID);
		SetupDialogFactory.getInstance(this).getDialog(SetupDialogFactory.CONNECTING_TO_PRODUCT).show() ;
	}
	
	private void showErrorDialog(String title,String msg, String btnTxt) {
		try {
			FragmentManager fragMan = getSupportFragmentManager();
			fragMan.beginTransaction()
			.add(SetupDialogFragment.newInstance(title, msg, btnTxt), "ews_error")
					.commitAllowingStateLoss();
		} catch (IllegalStateException e) {
			ALog.e(ALog.EWS, "Error: " + e.getMessage());
		}
	}
	
	//SSDP Discovery
	private void deviceDiscoveryCompleted() {
		stopSearchPurifierTimer();
		stopDiscovery();
		SetupDialogFactory.getInstance(this).getDialog(SetupDialogFactory.CONNECTING_TO_PRODUCT).dismiss() ;
		if( ewsService != null ) {
			ewsService.stopSSDPCountDownTimer() ;
			ewsService.stopSSIDTimer();
		}
		
		mStep = EWSConstant.EWS_STEP_FINAL;
		showFragment(new EWSFinalStepFragment(), EWSConstant.EWS_DISCOVERED_FRAGMENT_TAG);
		unRegisterListener();
	}
	
	public void stopDiscovery() {
		DiscoveryManager.getInstance().stop();
	}
	
	private EWSStartFragment getIntroFragment() {
		if (ewsIntroductionScreenFragment == null) {
			ewsIntroductionScreenFragment = new EWSStartFragment();
		}
		return ewsIntroductionScreenFragment;
	}
	
	public void showFragment(Fragment fragment, String tag) {
		try {
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.replace(R.id.setup_fragment_container, fragment, tag);
			fragmentTransaction.commit();
		} catch (IllegalStateException e) {
			ALog.e(ALog.EWS, "Error: " + e.getMessage());
		}
	}
	
	public EWSBroadcastReceiver getEWSServiceObject() {
		return ewsService;
	}
	
	public String getNetworkSSID() {
		return networkSSID;
	}
	
	private boolean isHomeNeworkSelected() {
		mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (!mWifi.isConnected() || wifiManager.getConnectionInfo() == null 
			|| networkSSID == null || networkSSID.length() == 0 
			|| networkSSID.contains(EWSWifiManager.DEVICE_SSID)) {
			return false;
		} else {
			return true;
		}
	}
	
	public int getApModeFailCounter() {
		return apModeFailCounter;
	}
	
	public int getStep2FailCounter() {
		return step2FailCounter;
	}
	
	public int getPowerOnFailCounter() {
		return powerOnFailCounter;
	}
	
	private void unRegisterListener() {
		if(ewsService != null)	ewsService.unRegisterListener() ;
	}
	
	public void setAdvSettingViewVisibility(boolean advSetting) {
		ALog.i(ALog.EWS, "EWSActivity$setAdvSettingViewVisibility advSetting: " + advSetting);
		this.advSetting = advSetting;
	}
	
	@Override
	public void onDiscoveredDevicesListChanged() {
		ALog.d(ALog.EWS, "onDiscoveredDevicesListChanged: "+cppId) ;
		PurAirDevice ewsPurifier = DiscoveryManager.getInstance().getDeviceByEui64(cppId);
		if (ewsPurifier == null) return;
		if (ewsPurifier.getConnectionState() != ConnectionState.CONNECTED_LOCALLY) return;

		PurifierManager.getInstance().setCurrentPurifier(ewsPurifier);
		deviceDiscoveryCompleted();
	}
	
	//Start time if user connect home not manually
	private CountDownTimer searchPurifierTimer = new CountDownTimer(60000, 1000) {
		
		@Override
		public void onTick(long millisUntilFinished) {
			// NOP
		}

		@Override
		public void onFinish() {
			unRegisterListener() ;
			onErrorOccurred(ERROR_CODE_COULDNOT_FIND_DEVICE);
		}
	};
	
	private void stopSearchPurifierTimer() {
		if( searchPurifierTimer != null) searchPurifierTimer.cancel() ;
	}

	@Override
	public void noWifiTurnOnClicked(DialogFragment dialog) {/**NOP*/}

	@Override
	public void noInternetTurnOnClicked(DialogFragment dialog) {/**NOP*/}

	@Override
	public void locationServiceAllowClicked(DialogFragment dialog) {/**NOP*/}

	@Override
	public void locationServiceTurnOnClicked(DialogFragment dialog) {/**NOP*/}

	@Override
	public void dialogCancelClicked(DialogFragment dialog) {/**NOP*/}

	@Override
	public void onPurifierSelect(PurAirDevice purifier) {/**NOP*/}

}
