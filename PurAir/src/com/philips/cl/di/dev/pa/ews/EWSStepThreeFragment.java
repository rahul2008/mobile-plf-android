package com.philips.cl.di.dev.pa.ews;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.Selection;
import android.text.Spanned;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.datamodel.SessionDto;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.Fonts;
import com.philips.cl.di.dev.pa.util.GraphConst;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class EWSStepThreeFragment extends Fragment {
	
	private FontTextView passwordLabelStep3, wifiNetworkAddStep3;
	private EditText passwordStep3, deviceNameStep3, 
					ipAddStep3, subnetMaskStep3, routerAddStep3;
	private ImageView showPasswordImgStep3, showAdvanceConfigImg;
	private Button editSavePlaceNameBtnStep3, nextBtn;
	private RelativeLayout advSettingLayoutStep3;
	private LinearLayout advSettingBtnLayoutStep3;
	private boolean isPasswordVisibelStep3 = true;
	private OnFocusChangeListener focusListener;
	private ButtonClickListener buttonClickListener;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.ews_step3, null);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		focusListener = new EditTextFocusChangeListener();
		buttonClickListener = new ButtonClickListener();
		initializeXmlVariable();
		setFontStyle();
		initializeListener();
		deviceNameStep3.setFilters(new InputFilter[] { purifierNamefilter });
		enablePasswordFild();
		setPurifierDetils();
	}
	
	private void setPurifierDetils() {
		String passwordLabel = getString(R.string.step3_msg1) +
				" <font color=#3285FF>"+((EWSActivity) getActivity()).getNetworkSSID()+"</font>";
		passwordLabelStep3.setText(Html.fromHtml(passwordLabel));
		if (SessionDto.getInstance().getDeviceDto() != null) {
			deviceNameStep3.setText(SessionDto.getInstance().getDeviceDto().getName()) ;
		}
		
		if (SessionDto.getInstance().getDeviceWifiDto() != null) {
			ipAddStep3.setText(SessionDto.getInstance().getDeviceWifiDto().getIpaddress()) ;
			subnetMaskStep3.setText(SessionDto.getInstance().getDeviceWifiDto().getNetmask()) ; 
			routerAddStep3.setText(SessionDto.getInstance().getDeviceWifiDto().getGateway()) ;
			wifiNetworkAddStep3.setText(SessionDto.getInstance().getDeviceWifiDto().getMacaddress());
		}
		
	}

	private void enablePasswordFild() {
		if (((EWSActivity) getActivity()).getEWSServiceObject().isNoPasswordSSID()) {
			passwordStep3.setEnabled(false);
			passwordStep3.setBackgroundResource(R.drawable.ews_edit_txt_2_bg_gray);
		} else {
			passwordStep3.setEnabled(true);
			passwordStep3.setBackgroundResource(R.drawable.ews_edit_txt_2_bg);
		}
		
	}

	private void initializeXmlVariable() {
		passwordLabelStep3 = (FontTextView) getView().findViewById(R.id.ews_step3_password_lb);
		wifiNetworkAddStep3 = (FontTextView) getView().findViewById(R.id.ews_step3_wifi_add);

		passwordStep3 = (EditText) getView().findViewById(R.id.ews_step3_password);
		passwordStep3.setOnFocusChangeListener(focusListener);
		deviceNameStep3 = (EditText) getView().findViewById(R.id.ews_step3_place_name_edittxt); 
		deviceNameStep3.setOnFocusChangeListener(focusListener);
		ipAddStep3 = (EditText) getView().findViewById(R.id.ews_step3_ip_edittxt); 
		ipAddStep3.setOnFocusChangeListener(focusListener);
		subnetMaskStep3 = (EditText) getView().findViewById(R.id.ews_step3_subnet_edittxt); 
		subnetMaskStep3.setOnFocusChangeListener(focusListener);
		routerAddStep3 = (EditText) getView().findViewById(R.id.ews_step3_router_edittxt);
		routerAddStep3.setOnFocusChangeListener(focusListener);

		showPasswordImgStep3 = (ImageView) getView().findViewById(R.id.ews_password_enable_img);
		showAdvanceConfigImg = (ImageView) getView().findViewById(R.id.ews_adv_config_img);

		nextBtn = (Button) getView().findViewById(R.id.ews_step3_next_btn);
		editSavePlaceNameBtnStep3 = (Button) getView().findViewById(R.id.ews_step3_edit_name_btn);

		advSettingLayoutStep3 = (RelativeLayout) getView().findViewById(R.id.ews_step3_adv_config_layout);
		advSettingBtnLayoutStep3 = (LinearLayout) getView().findViewById(R.id.ews_adv_config_layout);

	}
	
	private void setFontStyle() {
		passwordStep3.setTypeface(Fonts.getGillsansLight(getActivity()));
		deviceNameStep3.setTypeface(Fonts.getGillsansLight(getActivity()));
		ipAddStep3.setTypeface(Fonts.getGillsansLight(getActivity()));
		subnetMaskStep3.setTypeface(Fonts.getGillsansLight(getActivity()));
		routerAddStep3.setTypeface(Fonts.getGillsansLight(getActivity()));
		nextBtn.setTypeface(Fonts.getGillsansLight(getActivity()));
		editSavePlaceNameBtnStep3.setTypeface(Fonts.getGillsansLight(getActivity()));
	}
	
	private void initializeListener() {
		passwordStep3.setOnFocusChangeListener(focusListener);
		deviceNameStep3.setOnFocusChangeListener(focusListener);
		ipAddStep3.setOnFocusChangeListener(focusListener);
		subnetMaskStep3.setOnFocusChangeListener(focusListener);
		routerAddStep3.setOnFocusChangeListener(focusListener);
		showAdvanceConfigImg.setOnClickListener(buttonClickListener);
		nextBtn.setOnClickListener(buttonClickListener);
		showPasswordImgStep3.setOnClickListener(buttonClickListener);
		editSavePlaceNameBtnStep3.setOnClickListener(buttonClickListener);
	}
	
	private InputFilter purifierNamefilter = new InputFilter() {

		@Override
		public CharSequence filter(CharSequence source, int start, int end,
				Spanned dest, int dstart, int dend) {
			if (source.equals(" ")) { 
				return source;
			}
			if (source.toString().matches("[%^<>;&+*():'\"`~!#{}|=?, ]")) {
				return source.subSequence(0, source.length() - 1);
			} else {
				return source;
			}
		}
	};
	
	private class ButtonClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ews_password_enable_img:
				passwordFieldEnableClickEvent();
				break;
			case R.id.ews_adv_config_img:
				advSettingLayoutStep3.setVisibility(View.VISIBLE);
				advSettingBtnLayoutStep3.setVisibility(View.INVISIBLE);
				break;
			case R.id.ews_step3_edit_name_btn:
				editPurifierNameClickEvent();
				break;
			case R.id.ews_step3_next_btn:
				ALog.i(ALog.EWS, "step3 next button click");
				((EWSActivity) getActivity()).sendNetworkDetails(passwordStep3.getText().toString()) ;
				break;
			default:
				ALog.i(ALog.EWS, "Default...");
				break;
			}
		}
	}
	
	private void passwordFieldEnableClickEvent() {
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
	}
	
	private void editPurifierNameClickEvent() {
		if (editSavePlaceNameBtnStep3.getText().toString().equals(
				getResources().getString(R.string.edit))) {
			deviceNameStep3.setBackgroundResource(R.drawable.ews_edit_txt_2_bg);
			deviceNameStep3.setEnabled(true);
			deviceNameStep3.setTextColor(GraphConst.COLOR_TITLE_GRAY);
			Editable editable = deviceNameStep3.getText();
			Selection.setSelection(editable, deviceNameStep3.length());
			editSavePlaceNameBtnStep3.setText(getResources().getString(R.string.save));
		} else {
			ALog.i(ALog.EWS, "step3 save name button click");
			deviceNameStep3.setBackgroundColor(Color.WHITE);
			deviceNameStep3.setEnabled(false);
			deviceNameStep3.setTextColor(GraphConst.COLOR_PHILIPS_BLUE);
			editSavePlaceNameBtnStep3.setText(getResources().getString(R.string.edit));
			String purifierName = deviceNameStep3.getText().toString();
			if (purifierName != null && purifierName.trim().length() > 0) {
				((EWSActivity) getActivity()).sendDeviceNameToPurifier(purifierName.trim()) ;
			} else {
				deviceNameStep3.setText(SessionDto.getInstance().getDeviceDto().getName());
			}
		}
	}
	
	private class EditTextFocusChangeListener implements OnFocusChangeListener {

	    public void onFocusChange(View v, boolean hasFocus){

	        if(!hasFocus) {
	            InputMethodManager imm =  
	            		(InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
	            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	        }
	    }
	}
}

