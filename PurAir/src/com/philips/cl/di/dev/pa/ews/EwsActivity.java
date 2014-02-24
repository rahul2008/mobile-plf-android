package com.philips.cl.di.dev.pa.ews;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.customviews.CustomTextView;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
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

public class EwsActivity extends ActionBarActivity implements OnClickListener {
	
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
	private CustomTextView passwordLabelStep4, wifiNetworkAddStep4, placeNameTxtStep4;
	private EditText passwordStep4, passwordOnStep4, placeNameEditTxtStep4, 
					 ipAddStep4, subnetMaskStep4, routerAddStep4;
	private ImageView showPasswordImgStep4, showAdvSettingStep4;
	private Button nextBtnStep4, editSavePlaceNameBtnStep4;
	private RelativeLayout advSettingLayoutStep4;
	private LinearLayout advSettingBtnLayoutStep4;
	private boolean isPasswordVisibelStep4;
	private String placeBtnTxtStep4;
	private View viewStep4;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ews_error_purifier_not_detect);
		
		initActionBar();
		
		inflater = getLayoutInflater();
		
		initializeIntroVariable();
		initializeStep1Variable();
		initializeStep2Variable();
		initializeStep4Variable();
		initializeCongraltVariable();
		initializeErrorPurifierNotDetect();
		
		//setContentView(viewStart);
		
		//actionbarBtn.setVisibility(View.INVISIBLE);
		//actionbarTitle.setText(getString(R.string.error_purifier_not_detect_head));
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
		viewStep4 = inflater.inflate(R.layout.ews_step3, null);
		
		passwordLabelStep4 = (CustomTextView) viewStep4.findViewById(R.id.ews_step4_password_lb);
		wifiNetworkAddStep4 = (CustomTextView) viewStep4.findViewById(R.id.ews_step4_wifi_add);
		placeNameTxtStep4 = (CustomTextView) viewStep4.findViewById(R.id.ews_step4_place_name_txt);
		
		passwordStep4 = (EditText) viewStep4.findViewById(R.id.ews_step4_password);
		passwordOnStep4 = (EditText) viewStep4.findViewById(R.id.ews_step4_password_visible_on);
		placeNameEditTxtStep4 = (EditText) viewStep4.findViewById(R.id.ews_step4_place_name_edittxt); 
		ipAddStep4 = (EditText) viewStep4.findViewById(R.id.ews_step4_ip_edittxt); 
		subnetMaskStep4 = (EditText) viewStep4.findViewById(R.id.ews_step4_subnet_edittxt); 
		routerAddStep4 = (EditText) viewStep4.findViewById(R.id.ews_step4_router_edittxt);
		
		showPasswordImgStep4 = (ImageView) viewStep4.findViewById(R.id.ews_password_enable_img);
		showAdvSettingStep4 = (ImageView) viewStep4.findViewById(R.id.ews_adv_config_img);
		
		nextBtnStep4 = (Button) viewStep4.findViewById(R.id.ews_step4_next_btn);
		editSavePlaceNameBtnStep4 = (Button) viewStep4.findViewById(R.id.ews_step4_edit_name_btn);
		
		advSettingLayoutStep4 = (RelativeLayout) viewStep4.findViewById(R.id.ews_step4_adv_config_layout);
		advSettingBtnLayoutStep4 = (LinearLayout) viewStep4.findViewById(R.id.ews_adv_config_layout);
		
		showPasswordImgStep4.setOnClickListener(this);
		showAdvSettingStep4.setOnClickListener(this);
		nextBtnStep4.setOnClickListener(this);
		editSavePlaceNameBtnStep4.setOnClickListener(this);
		
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



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.ews_get_start_btn:
				setContentView(viewStep1);
				break;
			case R.id.ews_step1_yes_btn:
				setContentView(viewStep2);
				break;
			case R.id.ews_step1_no_btn:
				setContentView(viewStart);
				break;
			case R.id.ews_step2_yes_btn:
				setContentView(viewStep4);
				break;
			case R.id.ews_step2_no_btn:
				setContentView(viewStep1);
				break;	
			case R.id.ews_password_enable_img:
				if (isPasswordVisibelStep4) {
					isPasswordVisibelStep4 = false;
					showPasswordImgStep4.setImageResource(R.drawable.ews_password_off_3x);
					passwordStep4.setText(passwordOnStep4.getText().toString());
					Editable editable = passwordStep4.getText();
					Selection.setSelection(editable, passwordStep4.length());
					passwordStep4.setVisibility(View.VISIBLE);
					passwordOnStep4.setVisibility(View.INVISIBLE);
				} else {
					isPasswordVisibelStep4 = true;
					showPasswordImgStep4.setImageResource(R.drawable.ews_password_on_2x);
					passwordOnStep4.setText(passwordStep4.getText().toString());
					Editable editable = passwordOnStep4.getText();
					Selection.setSelection(editable, passwordOnStep4.length());
					passwordStep4.setVisibility(View.INVISIBLE);
					passwordOnStep4.setVisibility(View.VISIBLE);
					
				}
				break;
			case R.id.ews_adv_config_img:
				advSettingLayoutStep4.setVisibility(View.VISIBLE);
				advSettingBtnLayoutStep4.setVisibility(View.INVISIBLE);
				break;
			case R.id.ews_step4_edit_name_btn:
				if (editSavePlaceNameBtnStep4.getText().toString().equals(
						getResources().getString(R.string.edit))) {
					placeNameTxtStep4.setVisibility(View.INVISIBLE);
					placeNameEditTxtStep4.setVisibility(View.VISIBLE);
					placeNameEditTxtStep4.setText(placeNameTxtStep4.getText().toString());
					Editable editable = placeNameEditTxtStep4.getText();
					Selection.setSelection(editable, placeNameEditTxtStep4.length());
					editSavePlaceNameBtnStep4.setText(getResources().getString(R.string.save));
				} else {
					placeNameTxtStep4.setVisibility(View.VISIBLE);
					placeNameEditTxtStep4.setVisibility(View.INVISIBLE);
					placeNameTxtStep4.setText(placeNameEditTxtStep4.getText().toString());
					editSavePlaceNameBtnStep4.setText(getResources().getString(R.string.edit));
				}
				break;
			case R.id.ews_step4_next_btn:
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

}
