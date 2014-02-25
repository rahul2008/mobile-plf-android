package com.philips.cl.di.dev.pa.ews;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.customviews.CustomTextView;
import com.philips.cl.di.dev.pa.dto.SessionDto;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class EwsActivity extends ActionBarActivity implements OnClickListener, EWSListener {

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
	private Button yesBtnStep2, noBtnStep2;
	private View viewStep2;
	/**
	 * Step4 variable declare
	 */
	private CustomTextView passwordLabelStep3, wifiNetworkAddStep3, deviceNameStep3;
	private EditText passwordStep3, passwordOnStep3, placeNameEditTxtStep3, 
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

	private LayoutInflater inflater;

	private WifiManager wifiManager ;
	private String networkSSID ;
	private String password ;
	private String purifierName ;

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

		//checkWifiConnectivity() ;

		setContentView(viewStart);

		//actionbarBtn.setVisibility(View.INVISIBLE);
		//actionbarTitle.setText(getString(R.string.error_purifier_not_detect_head));
	}


	private void checkWifiConnectivity() {
		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

		if(!wifiManager.isWifiEnabled()) {

		}

		else {
			setContentView(viewStep1) ;
			if( wifiManager.getConnectionInfo() != null ) {
				networkSSID = wifiManager.getConnectionInfo().getSSID() ;
				networkSSID = networkSSID.replace("\"", "") ;
			}
			wifiNetworkNameStep1.setText(networkSSID) ;
		}
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

		yesBtnStep2 = (Button) viewStep2.findViewById(R.id.ews_step2_yes_btn);
		noBtnStep2 = (Button) viewStep2.findViewById(R.id.ews_step2_no_btn);

		yesBtnStep2.setOnClickListener(this);
		noBtnStep2.setOnClickListener(this);

	}

	private void initializeStep4Variable() {
		viewStep3 = inflater.inflate(R.layout.ews_step3, null);

		passwordLabelStep3 = (CustomTextView) viewStep3.findViewById(R.id.ews_step4_password_lb);
		wifiNetworkAddStep3 = (CustomTextView) viewStep3.findViewById(R.id.ews_step4_wifi_add);
		deviceNameStep3 = (CustomTextView) viewStep3.findViewById(R.id.ews_step4_place_name_txt);

		passwordStep3 = (EditText) viewStep3.findViewById(R.id.ews_step4_password);
		passwordOnStep3 = (EditText) viewStep3.findViewById(R.id.ews_step4_password_visible_on);
		placeNameEditTxtStep3 = (EditText) viewStep3.findViewById(R.id.ews_step4_place_name_edittxt); 
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


	private void connectToAirPurifier() {
		changeNetworkToAPMode() ;
	}

	private EWSService ewsService ;
	private void changeNetworkToAPMode() {
		ewsService = new EWSService(this, this, networkSSID, password) ;
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
			setContentView(viewStart);
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
				passwordStep3.setText(passwordOnStep3.getText().toString());
				Editable editable = passwordStep3.getText();
				Selection.setSelection(editable, passwordStep3.length());
				passwordStep3.setVisibility(View.VISIBLE);
				passwordOnStep3.setVisibility(View.INVISIBLE);
			} else {
				isPasswordVisibelStep3 = true;
				showPasswordImgStep3.setImageResource(R.drawable.ews_password_on_2x);
				passwordOnStep3.setText(passwordStep3.getText().toString());
				Editable editable = passwordOnStep3.getText();
				Selection.setSelection(editable, passwordOnStep3.length());
				passwordStep3.setVisibility(View.INVISIBLE);
				passwordOnStep3.setVisibility(View.VISIBLE);

			}
			break;
		case R.id.ews_adv_config_img:
			advSettingLayoutStep3.setVisibility(View.VISIBLE);
			advSettingBtnLayoutStep3.setVisibility(View.INVISIBLE);
			break;
		case R.id.ews_step4_edit_name_btn:
			if (editSavePlaceNameBtnStep3.getText().toString().equals(
					getResources().getString(R.string.edit))) {
				deviceNameStep3.setVisibility(View.INVISIBLE);
				placeNameEditTxtStep3.setVisibility(View.VISIBLE);
				placeNameEditTxtStep3.setText(deviceNameStep3.getText().toString());
				Editable editable = placeNameEditTxtStep3.getText();
				Selection.setSelection(editable, placeNameEditTxtStep3.length());
				editSavePlaceNameBtnStep3.setText(getResources().getString(R.string.save));
			} else {
				deviceNameStep3.setVisibility(View.VISIBLE);
				placeNameEditTxtStep3.setVisibility(View.INVISIBLE);
				deviceNameStep3.setText(placeNameEditTxtStep3.getText().toString());
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
			
			break;
		case R.id.ews_actionbar_cancel_btn:
			break;
		default:
			break;
		}
	}

	
	private void sendNetworkDetails() {
		String enteredPassword = passwordOnStep3.getText().toString() ;
		
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
	}


	@Override
	public void onDeviceConnectToHomeNetwork() {
		setContentView(viewCongratulation) ;
		
	}

}
