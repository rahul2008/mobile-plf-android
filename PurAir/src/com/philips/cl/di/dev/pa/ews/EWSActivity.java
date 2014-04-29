package com.philips.cl.di.dev.pa.ews;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.philips.cl.di.common.ssdp.contants.DiscoveryMessageID;
import com.philips.cl.di.common.ssdp.controller.InternalMessage;
import com.philips.cl.di.common.ssdp.lib.SsdpService;
import com.philips.cl.di.common.ssdp.models.DeviceModel;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.BaseActivity;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.datamodel.PurifierDetailDto;
import com.philips.cl.di.dev.pa.datamodel.SessionDto;
import com.philips.cl.di.dev.pa.newpurifier.ConnectionState;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager;
import com.philips.cl.di.dev.pa.purifier.PurifierDatabase;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.Fonts;
import com.philips.cl.di.dev.pa.util.Utils;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class EWSActivity extends BaseActivity implements OnClickListener, EWSListener, Callback {

	private EWSStartFragment ewsIntroductionScreenFragment;
	
	private int mStep = EWSConstant.EWS_STEP_START;
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
	
	private EWSBroadcastReceiver ewsService ;
	
	private String cppId;
	
	private PurifierDetailDto deviceInfoDto;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ews_main);
        
        getSupportFragmentManager().beginTransaction()
		.add(R.id.ews_fragment_container, getIntroFragment(), EWSConstant.EWS_INTRO_SCREEN_FRAGMENT_TAG)
		.commit();
        
		initActionBar();
		
		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	}

	/*Initialize action bar */
	private void initActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setIcon(null);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		Drawable d=getResources().getDrawable(R.drawable.ews_nav_bar_2x);  
		actionBar.setBackgroundDrawable(d);
		View view  = getLayoutInflater().inflate(R.layout.ews_actionbar, null);
		actionbarTitle = (FontTextView) view.findViewById(R.id.ews_actionbar_title);
		actionbarTitle.setText(getString(R.string.wifi_setup));
		actionbarCancelBtn = (Button) view.findViewById(R.id.ews_actionbar_cancel_btn);
		actionbarCancelBtn.setTypeface(Fonts.getGillsansLight(this));
		actionbarCancelBtn.setVisibility(View.INVISIBLE);
		actionbarBackImg = (ImageView) view.findViewById(R.id.ews_actionbar_back_img);
		actionbarBackImg.setOnClickListener(this);
		actionbarCancelBtn.setOnClickListener(this);
		actionBar.setCustomView(view);	
	}
	
	public void setActionBarHeading (int index) {
		switch (index) {
		case EWSConstant.EWS_STEP_START:
		case EWSConstant.EWS_STEP_CHANGE_NETWORK:	
		case EWSConstant.EWS_STEP_ONE:
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
		case EWSConstant.EWS_STEP_ERROR_DISCOVERY:
			//Error mobile device connect home network screen
			//Error Airpurifier not discovered in home network screen
			actionbarCancelBtn.setVisibility(View.INVISIBLE);
			actionbarBackImg.setVisibility(View.INVISIBLE);
			actionbarTitle.setText(getString(R.string.error_purifier_not_detect_head));
			break;
		default:
			break;
		}
	}
	//Activity Override methods - Start
	@Override
	protected void onStop() {
		if(ewsService != null)
			ewsService.unRegisterListener() ;
		
		stopDiscovery();
		super.onStop();
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
		case R.id.ews_actionbar_back_img:
			replaceFragmentOnBackPress();
			break;
		case R.id.ews_actionbar_cancel_btn:
			try {
				FragmentManager fragMan = getSupportFragmentManager();
				fragMan.beginTransaction().add(
						EWSCancelDialogFragment.newInstance(), "ews_cancel").commitAllowingStateLoss();
			} catch (Exception e) {
				ALog.e(ALog.EWS, e.getMessage());
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
						EWSCancelDialogFragment.newInstance(), "ews_cancel").commitAllowingStateLoss();
			} catch (Exception e) {
				ALog.e(ALog.EWS, e.getMessage());
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
		case EWSConstant.EWS_STEP_TWO:
			showStepOneFragment();
			return true;
		case EWSConstant.EWS_STEP_THREE:
		case EWSConstant.EWS_STEP_ERROR_DISCOVERY:
		case EWSConstant.EWS_STEP_SUPPORT:
			showStepTwo();
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
	
	private void showStepOneFragment() {
		mStep = EWSConstant.EWS_STEP_ONE ;
		showFragment(new EWSStepOneFragment(), EWSConstant.EWS_STEP_ONE_FRAGMENT_TAG);
	}
	
	private void showErrorSSIDFragement() {
		mStep = EWSConstant.EWS_STEP_ERROR_SSID ;
    	showFragment(new EWSErrorSSIDFragment(), EWSConstant.EWS_ERROR_SSID_FRAGMENT_TAG);
	}
	
	public void showSupportFragment() {
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
			}
		}
		else {
			networkSSID = null ;
			showNetworkChangeFragment();
		}		
	}

	private void showErrorScreen() {
		stopDiscovery();
		ALog.e(ALog.EWS, "showErrorScreen: wifiManager.isWifiEnabled(): " + wifiManager.isWifiEnabled()
				+ ", wifiManager.getConnectionInfo().getSSID(): " + wifiManager.getConnectionInfo().getSSID() 
				+ ", networkSSID: " + networkSSID);
        if(!wifiManager.isWifiEnabled()) {
        	showErrorSSIDFragement();
        }
        else {
             if( wifiManager.getConnectionInfo() != null &&
                       wifiManager.getConnectionInfo().getSSID() != null) {
            	 String ssid =  wifiManager.getConnectionInfo().getSSID().replace("\"", "") ;
            	 if(ssid.equals(networkSSID)) {
	            	 mStep = EWSConstant.EWS_STEP_ERROR_DISCOVERY ;
	            	 showFragment(new EWSErrorPurifierDiscoverFragment(), EWSConstant.EWS_ERROR_DISCOVER_FRAGMENT_TAG);
            	 }
            	 else {
                	 showErrorSSIDFragement();
                 }
             } else {
            	 showErrorSSIDFragement();
             }
            
        }
	}
	
	public void showHomeScreen() {
		mStep = EWSConstant.EWS_START_MAIN;
		stopDiscovery();
		
		if (deviceInfoDto != null) {
			new PurifierDatabase().insertPurifierDetail(deviceInfoDto);
			getSharedPreferences(
					"cpp_preferences01", 0).edit().putString("airpurifierid", deviceInfoDto.getCppId()).commit();
		}
		Intent intent = new Intent(this,MainActivity.class) ;
		setResult(RESULT_OK,intent) ;
		finish() ;
	}
	
	public void changeWifiNetwork() {
		if( ewsService == null ) {
			ewsService = new EWSBroadcastReceiver(this, null, null) ;
		}
		ewsService.setSSID(null) ;
		showNetworkChangeFragment();
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
		System.out.println("deviceName:"+deviceName+" SessionDto.getInstance().getDeviceDto(): "+ SessionDto.getInstance().getDeviceDto());
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
	//
	public void checkWifiConnectivity() {
		mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		ALog.i(ALog.EWS, "mWifi.isConnected()== " +mWifi.isConnected() + ", step: " + mStep);
		if(!mWifi.isConnected()) {
			showNetworkChangeFragment();
			ewsService = new EWSBroadcastReceiver(this, networkSSID, password) ;
		}
		else {
			showStepOne() ;
		}
	}
	
	private void registerNetworkListener() {
		ALog.i(ALog.EWS, "registerNetworkListener") ;
		if( mStep == EWSConstant.EWS_STEP_CHANGE_NETWORK 
				|| mStep == EWSConstant.EWS_STEP_ONE 
				|| mStep == EWSConstant.EWS_STEP_ERROR_SSID) {
			if( ewsService == null ) {
				ewsService = new EWSBroadcastReceiver(this, null, null) ;
			}
			ewsService.setSSID(null) ;
			ewsService.registerListener() ;
		}
	}
	
	public void connectToAirPurifier() {
		dismissCheckSingnalStrengthDialog();
		EWSDialogFactory.getInstance(this).getDialog(EWSDialogFactory.CHECK_SIGNAL_STRENGTH).show();
		if ( ewsService == null)
			ewsService = new EWSBroadcastReceiver(this, networkSSID, password) ;
		ewsService.setSSID(networkSSID) ;
		ewsService.connectToDeviceAP() ;
	}
	
	public void showEWSSetUpInstructionsDialog() {
		EWSDialogFactory.getInstance(this).getDialog(EWSDialogFactory.ERROR_TS01_01).show() ;
	}	
	
	public void sendNetworkDetails(String ssid, String password) {
		networkSSID = ssid;
		EWSDialogFactory.getInstance(this).setNetworkName(networkSSID);
		EWSDialogFactory.getInstance(this).getDialog(EWSDialogFactory.CONNECTING_TO_PRODUCT).show() ;
		ewsService.setSSID(networkSSID) ;
		ewsService.setPassword(password) ;
		ewsService.putWifiDetails() ;
		
	}

	// Override methods - EWSListener - Start
	@Override
	public void onDeviceAPMode() {

	}

	@Override
	public void onSelectHomeNetwork() {

	}

	@Override
	public void onHandShakeWithDevice() {
		dismissCheckSingnalStrengthDialog();
		mStep = EWSConstant.EWS_STEP_THREE ;
		showFragment(new EWSStepThreeFragment(), EWSConstant.EWS_STEP_THREE_FRAGMENT_TAG);
		
		
		if (SessionDto.getInstance().getDeviceWifiDto() != null) {
			cppId = SessionDto.getInstance().getDeviceWifiDto().getCppid(); 
		}
	}

	@Override
	public void onDeviceConnectToHomeNetwork() {
		SsdpService.getInstance().startDeviceDiscovery(this) ;
	}
	
	@Override
	public void foundHomeNetwork() {
		showStepOne() ;
	}
	
	@Override
	public void onWifiDisabled() {
		ALog.i(ALog.EWS, "onWifiDisabled");
	}

	@Override
	public void onErrorOccurred(int errorCode) {
		
		ALog.i(ALog.EWS, "onErrorOccurred: "+errorCode) ;
		if (EWSDialogFactory.getInstance(this).getDialog(EWSDialogFactory.CHECK_SIGNAL_STRENGTH).isShowing()) {
			EWSDialogFactory.getInstance(this).getDialog(EWSDialogFactory.CHECK_SIGNAL_STRENGTH).dismiss();
		}
		else if( EWSDialogFactory.getInstance(this).getDialog(EWSDialogFactory.CONNECTING_TO_PRODUCT).isShowing()) {
			EWSDialogFactory.getInstance(this).getDialog(EWSDialogFactory.CONNECTING_TO_PRODUCT).dismiss();
		}
		
		switch (errorCode) {
		case EWSListener.ERROR_CODE_PHILIPS_SETUP_NOT_FOUND:				
			EWSDialogFactory.getInstance(this).getDialog(EWSDialogFactory.ERROR_TS01_01).show() ;
			break;
		case EWSListener.ERROR_CODE_COULDNOT_RECEIVE_DATA_FROM_DEVICE:
//			EWSDialogFactory.getInstance(this).getDialog(EWSDialogFactory.ERROR_TS01_03).show() ;
//			break;
		case EWSListener.ERROR_CODE_COULDNOT_SEND_DATA_TO_DEVICE:
//			EWSDialogFactory.getInstance(this).getDialog(EWSDialogFactory.ERROR_TS01_04).show() ;
			try {
				FragmentManager fragMan = getSupportFragmentManager();
				fragMan.beginTransaction().add(
						EWSDialogFragment.newInstance(), "ews_error").commitAllowingStateLoss();
			} catch (Exception e) {
				ALog.e(ALog.EWS, e.getMessage());
			}
			break;
		case EWSListener.ERROR_CODE_COULDNOT_FIND_DEVICE:				
			showErrorScreen();
			break;
		case EWSListener.ERROR_CODE_INVALID_PASSWORD:
			Toast.makeText(this, getString(R.string.wrong_wifi_password), Toast.LENGTH_LONG).show() ;
			break;
		default:
			break;
		}
	}
	
	//SSDP Discovery
	private void deviceDiscoveryCompleted() {
		EWSDialogFactory.getInstance(this).getDialog(EWSDialogFactory.CONNECTING_TO_PRODUCT).dismiss() ;
		if( ewsService != null ) {
			ewsService.stopNetworkDetailsTimer() ;
		}
		mStep = EWSConstant.EWS_STEP_FINAL;
		showFragment(new EWSFinalStepFragment(), EWSConstant.EWS_DISCOVERED_FRAGMENT_TAG);
	}
	
	// SSDPListener Callback handler
	public boolean handleMessage(Message msg) {
		DeviceModel device = null ;
		if (null != msg) {
			final DiscoveryMessageID message = DiscoveryMessageID.getID(msg.what);
			final InternalMessage internalMessage = (InternalMessage) msg.obj;
			if (null != internalMessage && internalMessage.obj instanceof DeviceModel) {
				device = (DeviceModel) internalMessage.obj;
			}
			
			if (device == null ) {
				return false;
			}
			
			switch (message) {
			case DEVICE_DISCOVERED:
				
				if (device.getSsdpDevice() == null 
					|| device.getSsdpDevice().getCppId() == null
					|| device.getSsdpDevice().getModelName() == null
					|| device.getSsdpDevice().getFriendlyName() == null) {
					return false    ;
				}
				
				if (cppId == null || cppId.length() <= 0) {
					return  false ;
				}
								
				deviceDiscovered(device);
				break;
			case DEVICE_LOST:
				break;
			default:
				break;
			}
			return false;
		}
		return false;
	}	
	
	private boolean deviceDiscovered(DeviceModel device) {
		String ssdpCppId = device.getSsdpDevice().getCppId();
		ALog.i(ALog.EWS, "SSDP CppId: " + ssdpCppId +", ews cppid: " + cppId);
		if (ssdpCppId == null || ssdpCppId.length() <= 0) {
			return false;
		}
		
		if (device.getSsdpDevice().getModelName().contains(AppConstants.MODEL_NAME) 
				&& cppId.equalsIgnoreCase(ssdpCppId)) {
			
			
			deviceInfoDto = new PurifierDetailDto();
			deviceInfoDto.setUsn(device.getUsn());
			deviceInfoDto.setCppId(device.getSsdpDevice().getCppId());
			deviceInfoDto.setDeviceName(device.getSsdpDevice().getFriendlyName());
			
			if (device.getBootID() != null && device.getBootID().length() > 0) {
				deviceInfoDto.setBootId(Long.parseLong(device.getBootID()));
			}
			if ( ewsService != null) {
				deviceInfoDto.setDeviceKey(ewsService.getDevKey());
			}

			// START NEW CODE
			PurAirDevice purifier = new PurAirDevice(device.getSsdpDevice().getCppId(), device.getUsn(), device.getIpAddress(), device.getSsdpDevice().getFriendlyName(), "" + deviceInfoDto.getBootId(), ConnectionState.CONNECTED_LOCALLY);
			PurifierManager.getInstance().setCurrentPurifier(purifier);
			// STOP NEW CODE

			deviceDiscoveryCompleted();
		}
		return true;
	}
	
	public void stopDiscovery() {
		SsdpService.getInstance().stopDeviceDiscovery() ;
		
	}
	
	private EWSStartFragment getIntroFragment() {
		if (ewsIntroductionScreenFragment == null) {
			ewsIntroductionScreenFragment = new EWSStartFragment();
		}
		return ewsIntroductionScreenFragment;
	}
	
	public void showFragment(Fragment fragment, String tag) {
//		setActionBarHeading(mStep) ;
		try {
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.replace(R.id.ews_fragment_container, fragment, tag);
			fragmentTransaction.commit();
		} catch (IllegalStateException e) {
			ALog.e(ALog.EWS, e.getMessage());
		} catch (Exception e) {
			ALog.e(ALog.EWS, e.getMessage());
		}
	}
	
	public EWSBroadcastReceiver getEWSServiceObject() {
		return ewsService;
	}
	
	public String getNetworkSSID() {
		return networkSSID;
	}
	
	private void dismissCheckSingnalStrengthDialog() {
		if (EWSDialogFactory.getInstance(this).getDialog(EWSDialogFactory.CHECK_SIGNAL_STRENGTH).isShowing()) {
			EWSDialogFactory.getInstance(this).getDialog(EWSDialogFactory.CHECK_SIGNAL_STRENGTH).dismiss();
		}
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

}
