package com.philips.cl.di.dev.pa.ews;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.Html;
import android.text.Selection;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.philips.cl.di.common.ssdp.contants.ConnectionLibContants;
import com.philips.cl.di.common.ssdp.contants.MessageID;
import com.philips.cl.di.common.ssdp.controller.InternalMessage;
import com.philips.cl.di.common.ssdp.lib.SsdpService;
import com.philips.cl.di.common.ssdp.models.DeviceModel;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constants.AppConstants;
import com.philips.cl.di.dev.pa.customviews.CustomTextView;
import com.philips.cl.di.dev.pa.detail.utils.GraphConst;
import com.philips.cl.di.dev.pa.dto.SessionDto;
import com.philips.cl.di.dev.pa.pureairui.MainActivity;

public class EwsActivity extends ActionBarActivity implements OnClickListener, EWSListener, Callback {

	private int step = -1;
	/**
	 * Action bar variable
	 */
	private ActionBar mActionBar;
	private CustomTextView actionbarTitle;
	private Button actionbarBtn;
	/**
	 * Step1 variable declare
	 */
	private Button ewsStart;
	private View viewStart;
	/**
	 * Step1 variable declare
	 */
	private CustomTextView wifiNetworkNameStep1;
	private Button yesBtnStep1, noBtnStep1;
	private View viewStep1;
	/**
	 * Step2 variable declare
	 */
	private CustomTextView step2Message1, step2Message2;
	private Button yesBtnStep2, noBtnStep2;
	private View viewStep2;
	/**
	 * Step4 variable declare
	 */
	private CustomTextView passwordLabelStep3, wifiNetworkAddStep3;
	private EditText passwordStep3, deviceNameStep3, 
	ipAddStep3, subnetMaskStep3, routerAddStep3;
	private ImageView showPasswordImgStep3, showAdvSettingStep3;
	private Button nextBtnStep3, editSavePlaceNameBtnStep3;
	private RelativeLayout advSettingLayoutStep3;
	private LinearLayout advSettingBtnLayoutStep3;
	private boolean isPasswordVisibelStep3;
	private String placeBtnTxtStep3;
	private View viewStep3;
	/**
	 * Congratulation variable declare
	 */
	private Button congratulationBtn;
	private View viewCongratulation;
	/**
	 * Congratulation variable declare
	 */
	private Button errorPurifierNotDectBtn;
	private View viewErrorPurifierNotDect;
	private CustomTextView errorPurifierNotDectNetwork;
	/**
	 * Error connect to your network variable declare
	 */
	private View viewErrorConnect2Network;
	/**
	 * Error SSID variable declare
	 */
	private View viewErrorSSID;
	private CustomTextView errorSSIDNetwork;
	/**
	 * Contact Philips support variable declare
	 */
	private View viewContactPhilipsSupport;
	private CustomTextView contactPhilipsMessage1, contactPhilipsPhone, 
	contactPhilipsEmail, contactPhilipsWeb;
	private RelativeLayout contactPhilipsPhoneLayout, contactPhilipsEmailLayout, 
	contactPhilipsWebLayout;

	private LayoutInflater inflater;

	private WifiManager wifiManager ;
	private String networkSSID ;
	private String password ;
	private String purifierName ;

	private SsdpService ssdpService ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.ews_error_purifier_not_detect);

		initActionBar();

		inflater = getLayoutInflater();

		initializeIntroVariable();
		initializeStep1Variable();
		initializeStep2Variable();
		initializeStep4Variable();
		initializeCongraltVariable();
		initializeErrorPurifierNotDetect();
		initializeErrorConnect2Network();
		initializeErrorSSID();
		initializeContactPhilips();

		setContentView(viewStart);
	}


	private void checkWifiConnectivity() {
		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

		if(!wifiManager.isWifiEnabled()) {
			step = 0 ;
			setContentView(viewErrorConnect2Network) ;
			ewsService = new EWSService(this, this, networkSSID, password) ;
		}

		else {
			showStepOne() ;
		}
	}

	private void showStepOne() {
		step = 1 ;
		setContentView(viewStep1) ;
		if( wifiManager.getConnectionInfo() != null ) {
			networkSSID = wifiManager.getConnectionInfo().getSSID() ;
			networkSSID = networkSSID.replace("\"", "") ;
		}
		wifiNetworkNameStep1.setText(networkSSID) ;
	}

	private void initializeIntroVariable() {
		viewStart = inflater.inflate(R.layout.ews_intro_screen, null);
		ewsStart = (Button) viewStart.findViewById(R.id.ews_get_start_btn);
		ewsStart.setOnClickListener(this);

	}

	private void initializeStep1Variable() {
		viewStep1 = inflater.inflate(R.layout.ews_step1, null);

		wifiNetworkNameStep1 = (CustomTextView) viewStep1.findViewById(R.id.ews_step1_wifi_network);

		yesBtnStep1 = (Button) viewStep1.findViewById(R.id.ews_step1_yes_btn);
		noBtnStep1 = (Button) viewStep1.findViewById(R.id.ews_step1_no_btn);

		yesBtnStep1.setOnClickListener(this);
		noBtnStep1.setOnClickListener(this);

	}

	private void initializeStep2Variable() {
		viewStep2 = inflater.inflate(R.layout.ews_step2, null);

		step2Message1 = (CustomTextView) viewStep2.findViewById(R.id.ews_step2_message1);
		step2Message2 = (CustomTextView) viewStep2.findViewById(R.id.ews_step2_message2);

		String msg1 = getString(R.string.step2_msg1) + " <font color=#EF6921>"+getString(R.string.orange)+"</font>.";
		step2Message1.setText(Html.fromHtml(msg1));

		String msg2 = getString(R.string.step2_msg2) + " <font color=#EF6921>"+getString(R.string.orange)+"</font>?";
		step2Message2.setText(Html.fromHtml(msg2));


		yesBtnStep2 = (Button) viewStep2.findViewById(R.id.ews_step2_yes_btn);
		noBtnStep2 = (Button) viewStep2.findViewById(R.id.ews_step2_no_btn);

		yesBtnStep2.setOnClickListener(this);
		noBtnStep2.setOnClickListener(this);

	}

	private void initializeStep4Variable() {

		viewStep3 = inflater.inflate(R.layout.ews_step3, null);

		passwordLabelStep3 = (CustomTextView) viewStep3.findViewById(R.id.ews_step4_password_lb);
		wifiNetworkAddStep3 = (CustomTextView) viewStep3.findViewById(R.id.ews_step4_wifi_add);

		passwordStep3 = (EditText) viewStep3.findViewById(R.id.ews_step4_password);
		deviceNameStep3 = (EditText) viewStep3.findViewById(R.id.ews_step4_place_name_edittxt); 
		ipAddStep3 = (EditText) viewStep3.findViewById(R.id.ews_step4_ip_edittxt); 
		subnetMaskStep3 = (EditText) viewStep3.findViewById(R.id.ews_step4_subnet_edittxt); 
		routerAddStep3 = (EditText) viewStep3.findViewById(R.id.ews_step4_router_edittxt);

		showPasswordImgStep3 = (ImageView) viewStep3.findViewById(R.id.ews_password_enable_img);
		showAdvSettingStep3 = (ImageView) viewStep3.findViewById(R.id.ews_adv_config_img);

		nextBtnStep3 = (Button) viewStep3.findViewById(R.id.ews_step4_next_btn);
		editSavePlaceNameBtnStep3 = (Button) viewStep3.findViewById(R.id.ews_step4_edit_name_btn);

		advSettingLayoutStep3 = (RelativeLayout) viewStep3.findViewById(R.id.ews_step4_adv_config_layout);
		advSettingBtnLayoutStep3 = (LinearLayout) viewStep3.findViewById(R.id.ews_adv_config_layout);

		showPasswordImgStep3.setOnClickListener(this);
		showAdvSettingStep3.setOnClickListener(this);
		nextBtnStep3.setOnClickListener(this);
		editSavePlaceNameBtnStep3.setOnClickListener(this);

	}

	/*Initialize action bar */
	private void initActionBar() {
		mActionBar = getSupportActionBar();
		mActionBar.setIcon(null);
		mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
		Drawable d=getResources().getDrawable(R.drawable.ews_nav_bar_2x);  
		mActionBar.setBackgroundDrawable(d);
		View view  = getLayoutInflater().inflate(R.layout.ews_actionbar, null);
		actionbarTitle = (CustomTextView) view.findViewById(R.id.ews_actionbar_title);
		actionbarTitle.setText(getString(R.string.wifi_setup));
		actionbarBtn = (Button) view.findViewById(R.id.ews_actionbar_cancel_btn);
		actionbarBtn.setOnClickListener(this);
		mActionBar.setCustomView(view);	
	}

	private void initializeCongraltVariable() {
		viewCongratulation = inflater.inflate(R.layout.ews_congratulation, null);

		congratulationBtn = (Button) viewCongratulation.findViewById(R.id.ews_congratulation_btn);

		congratulationBtn.setOnClickListener(this);

	}

	private void initializeErrorPurifierNotDetect() {
		viewErrorPurifierNotDect = inflater.inflate(R.layout.ews_error_purifier_not_detect, null);

		errorPurifierNotDectNetwork = 
				(CustomTextView) viewErrorPurifierNotDect.findViewById(R.id.ews_purifier_not_dect_network);
		errorPurifierNotDectBtn = (Button) viewErrorPurifierNotDect.findViewById(R.id.ews_purifier_not_dect_btn);

		errorPurifierNotDectBtn.setOnClickListener(this);

	}


	private void initializeContactPhilips() {
		viewContactPhilipsSupport = inflater.inflate(R.layout.contact_philips_support, null);

		contactPhilipsMessage1 = (CustomTextView) 
				viewContactPhilipsSupport.findViewById(R.id.contact_philips_support_message1); 
		contactPhilipsPhone = (CustomTextView) 
				viewContactPhilipsSupport.findViewById(R.id.contact_support_phone); 
		contactPhilipsEmail = (CustomTextView) 
				viewContactPhilipsSupport.findViewById(R.id.contact_support_email); 
		contactPhilipsWeb = (CustomTextView) 
				viewContactPhilipsSupport.findViewById(R.id.contact_support_website);

		contactPhilipsPhoneLayout = (RelativeLayout) 
				viewContactPhilipsSupport.findViewById(R.id.contact_support_phone_layout);  
		contactPhilipsEmailLayout = (RelativeLayout) 
				viewContactPhilipsSupport.findViewById(R.id.contact_support_email_layout);  						
		contactPhilipsWebLayout = (RelativeLayout) 
				viewContactPhilipsSupport.findViewById(R.id.contact_support_website_layout); 

		contactPhilipsPhoneLayout.setOnClickListener(this);
		contactPhilipsEmailLayout.setOnClickListener(this);
		contactPhilipsWebLayout.setOnClickListener(this);
	}

	private void initializeErrorSSID() {

	}

	private void initializeErrorConnect2Network() {
		viewErrorConnect2Network = inflater.inflate(R.layout.ews_connect_2_your_network, null) ;
	}
	private void connectToAirPurifier() {
		changeNetworkToAPMode() ;
	}

	private EWSService ewsService ;
	private void changeNetworkToAPMode() {
		if ( ewsService == null)
			ewsService = new EWSService(this, this, networkSSID, password) ;
		ewsService.setSSID(networkSSID) ;
		ewsService.connectToDeviceAP() ;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ews_get_start_btn:
			checkWifiConnectivity() ;
			break;
		case R.id.ews_step1_yes_btn:
			setContentView(viewStep2);
			break;
		case R.id.ews_step1_no_btn:
			step = 0 ;
			setContentView(viewErrorConnect2Network) ;
			break;
		case R.id.ews_step2_yes_btn:
			connectToAirPurifier() ;
			break;
		case R.id.ews_step2_no_btn:
			setContentView(viewStep1);
			break;	
		case R.id.ews_password_enable_img:
			if (isPasswordVisibelStep3) {
				isPasswordVisibelStep3 = false;
				showPasswordImgStep3.setImageResource(R.drawable.ews_password_off_3x);
				passwordStep3.setTransformationMethod(new PasswordTransformationMethod());
				Editable editable = passwordStep3.getText();
				Selection.setSelection(editable, passwordStep3.length());
			} else {
				isPasswordVisibelStep3 = true;
				showPasswordImgStep3.setImageResource(R.drawable.ews_password_on_2x);
				passwordStep3.setTransformationMethod(null);
				Editable editable = passwordStep3.getText();
				Selection.setSelection(editable, passwordStep3.length());

			}
			break;
		case R.id.ews_adv_config_img:
			advSettingLayoutStep3.setVisibility(View.VISIBLE);
			advSettingBtnLayoutStep3.setVisibility(View.INVISIBLE);
			break;
		case R.id.ews_step4_edit_name_btn:

			if (editSavePlaceNameBtnStep3.getText().toString().equals(
					getResources().getString(R.string.edit))) {
				deviceNameStep3.setBackgroundResource(R.drawable.ews_edit_txt_2_bg);
				deviceNameStep3.setEnabled(true);
				deviceNameStep3.setTextColor(GraphConst.COLOR_TITLE_GRAY);
				Editable editable = deviceNameStep3.getText();
				Selection.setSelection(editable, deviceNameStep3.length());
				editSavePlaceNameBtnStep3.setText(getResources().getString(R.string.save));
			} else {
				deviceNameStep3.setBackgroundColor(Color.WHITE);
				deviceNameStep3.setEnabled(false);
				deviceNameStep3.setTextColor(GraphConst.COLOR_PHILIPS_BLUE);
				editSavePlaceNameBtnStep3.setText(getResources().getString(R.string.edit));

				sendDeviceNameToPurifier(deviceNameStep3.getText().toString()) ;
			}

			break;
		case R.id.ews_step4_next_btn:
			sendNetworkDetails() ;
			break;
		case R.id.ews_purifier_not_dect_btn:
			break;
		case R.id.ews_congratulation_btn:
			showHomeScreen() ;
			break;
		case R.id.contact_support_phone_layout:
			break;
		case R.id.contact_support_email_layout:
			break;
		case R.id.contact_support_website_layout:
			break;
		case R.id.ews_actionbar_cancel_btn:
			break;
		default:
			break;
		}
	}

	@Override
	protected void onStop() {
		if(ewsService != null)
			ewsService.unRegisterListener() ;
		if(ssdpService != null)
			ssdpService.stopDeviceDiscovery() ;
		super.onStop();
	}


	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if ( hasFocus ) {
			registerNetworkListener() ;
		}
	}

	private void registerNetworkListener() {
		if( step == 0 ) {
			if( ewsService == null ) {
				ewsService = new EWSService(this, this, networkSSID, password) ;
			}
			ewsService.registerListener() ;
		}
	}

	@Override
	protected void onResume() {
		registerNetworkListener() ;
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void showHomeScreen() {
		Intent intent = new Intent(this,MainActivity.class) ;
		intent.putExtra("ipaddress", ipAddress) ;
		setResult(RESULT_OK,intent) ;
		finish() ;
	}

	private void sendNetworkDetails() {
		String enteredPassword = passwordStep3.getText().toString() ;

		ewsService.setPassword(enteredPassword) ;
		ewsService.putWifiDetails() ;
	}

	private void sendDeviceNameToPurifier(String deviceName) {
		if( ! deviceName.equals("") && !deviceName.equals(SessionDto.getInstance().getDeviceDto().getName())) {
			ewsService.setDeviceName(deviceName) ;
			ewsService.putDeviceDetails() ;
		}

	}


	@Override
	public void onDeviceAPMode() {

	}


	@Override
	public void onSelectHomeNetwork() {

	}


	@Override
	public void onHandShakeWithDevice() {
		Log.i("EWS", "Handshake") ;
		setContentView(viewStep3);
		deviceNameStep3.setText(SessionDto.getInstance().getDeviceDto().getName()) ;
		ipAddStep3.setText(SessionDto.getInstance().getDeviceWifiDto().getIpaddress()) ;
		subnetMaskStep3.setText(SessionDto.getInstance().getDeviceWifiDto().getNetmask()) ; 
		routerAddStep3.setText(SessionDto.getInstance().getDeviceWifiDto().getGateway()) ;
		wifiNetworkAddStep3.setText(SessionDto.getInstance().getDeviceWifiDto().getMacaddress()) ;
	}


	@Override
	public void onDeviceConnectToHomeNetwork() {
		ssdpService = SsdpService.getInstance() ;
		ssdpService.startDeviceDiscovery(this) ;
	}

	private String ipAddress ;
	public boolean handleMessage(Message msg) {
		DeviceModel device = null ;
		if (null != msg) {
			final MessageID message = MessageID.getID(msg.what);
			final InternalMessage internalMessage = (InternalMessage) msg.obj;
			if (null != internalMessage && internalMessage.obj instanceof DeviceModel) {
				device = (DeviceModel) internalMessage.obj;
				//	Log.i(TAG, "Device Information " + device);

			}
			String ip = "";
			switch (message) {
			case DEVICE_DISCOVERED:
				final String xml = msg.getData().getString(ConnectionLibContants.XML_KEY);
				//ip = msg.getData().getString(ConnectionLibContants.IP_KEY);
				final int port = msg.getData().getInt(ConnectionLibContants.PORT_KEY);


				if (device != null && device.getSsdpDevice() != null &&
						device.getSsdpDevice().getModelName().contains(AppConstants.MODEL_NAME) &&
						device.getIpAddress() != null && ipAddress == null) {
					ipAddress = device.getIpAddress() ;
					com.philips.cl.di.dev.pa.utils.Utils.setIPAddress(device.getIpAddress(), this) ;
					deviceDiscoveryCompleted();
					//diSecurity.exchangeKey(String.format(AppConstants.URL_SECURITY, device.getIpAddress()), "dev01");
				}
				break;
			case DEVICE_LOST:
				ip = msg.getData().getString("ip");
				//Log.i(TAG, "DEVICE LOST USN  " + device.getUsn());
				if (device != null && device.getIpAddress() != null) {
					//deviceList.remove(device.getIpAddress());
				}
				ip = null;
				break;

			default:
				//Log.i(TAG, "default");
				break;
			}
			if (null != ip && (!ip.isEmpty())) {
				//deviceList.add(ip);
			} 

			return false;
		}
		return false;
	}

	private void deviceDiscoveryCompleted() {
		setContentView(viewCongratulation) ;
	}


	@Override
	public void foundHomeNetwork() {
		showStepOne() ;
	}


	@Override
	public void onErrorOccurred(int errorCode) {
		switch (errorCode) {
		case EWSListener.ERROR_CODE1:
			break;
		}
	}

}
