
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.customviews;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.apptagging.AppTagging;
import com.philips.cdp.registration.apptagging.AppTagingConstants;
import com.philips.cdp.registration.ui.utils.FieldsValidator;
import com.philips.cdp.registration.ui.utils.FontLoader;
import com.philips.cdp.registration.ui.utils.RegConstants;

public class XVerifyNumber extends RelativeLayout implements TextWatcher, OnFocusChangeListener,
        OnClickListener {


	private Button  mbtuResend;

	private Context mContext;

	private EditText mEtEnterCode;

	private boolean mValidPhoneNumber;

	private onUpdateListener mUpdateStatusListener;

	private RelativeLayout mRlEtName;

	private TextView mTvErrDescriptionView;

	private FrameLayout mFlInvaliFielddAlert;

	private TextView mTvCloseIcon;


	private ProgressBar mProgressBar;

	public XVerifyNumber(Context context) {
		super(context);
		this.mContext = context;
		initUi(R.layout.x_verify_mobile);
	}

	public XVerifyNumber(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		initUi(R.layout.x_verify_mobile);
	}

	public final void initUi(int resourceId) {

		/** inflate amount layout */
		LayoutInflater li = LayoutInflater.from(mContext);
		li.inflate(resourceId, this, true);

		mbtuResend= (Button) findViewById(R.id.btn_reg_resend);
		mbtuResend.setOnClickListener(this);
		mEtEnterCode = (EditText) findViewById(R.id.et_reg_enter_code);
		mEtEnterCode.setOnFocusChangeListener(this);
		mEtEnterCode.addTextChangedListener(this);
		mProgressBar= (ProgressBar) findViewById(R.id.pb_reg_verify_spinner);
		mRlEtName = (RelativeLayout) findViewById(R.id.rl_reg_parent_verified_field);

		mTvErrDescriptionView = (TextView) findViewById(R.id.tv_reg_email_err);
		mFlInvaliFielddAlert = (FrameLayout)findViewById(R.id.fl_reg_invalid_alert);
		mTvCloseIcon = (TextView) findViewById(R.id.iv_reg_close);
		FontLoader.getInstance().setTypeface(mTvCloseIcon, RegConstants.PUIICON_TTF);

	}

	public void setCountertimer (String mTimer){
		mbtuResend.setText(mTimer);
		mbtuResend.setEnabled(false);
	}
	public void setCounterFinish (){
		mbtuResend.setText(mContext.getString(R.string.Mobile_Resend_btntxt));
		mbtuResend.setEnabled(true);
	}

	private void showResendCodeSpinner() {
		mProgressBar.setVisibility(View.VISIBLE);
		mbtuResend.setEnabled(false);
	}

	private void hideResendcodeSpinner() {
		mProgressBar.setVisibility(View.INVISIBLE);
		mbtuResend.setEnabled(true);
	}


	@Override
	public void onClick(View v) {
		showResendCodeSpinner();
	}

	public void setOnUpdateListener(onUpdateListener updateStatusListener) {
		mUpdateStatusListener = updateStatusListener;
	}

	private void raiseUpdateUIEvent() {
		if (null != mUpdateStatusListener) {
			mUpdateStatusListener.onUpadte();
		}
	}

	private void showInvalidPhoneNumberAlert() {
		mEtEnterCode.setTextColor(mContext.getResources().getColor(R.color.reg_error_box_color));
		mRlEtName.setBackgroundResource(R.drawable.reg_et_focus_error);
		mFlInvaliFielddAlert.setVisibility(View.VISIBLE);
		mTvErrDescriptionView.setVisibility(VISIBLE);
	}

	private void showValidPhoneNumberAlert() {
		mFlInvaliFielddAlert.setVisibility(GONE);
		mTvErrDescriptionView.setVisibility(GONE);
	}

	private boolean validatePhoneNumber() {
		if (!FieldsValidator.isValidName(mEtEnterCode.getText().toString().trim())) {
			setValidPhoneNumber(false);
			return false;
		}
		setValidPhoneNumber(true);
		return true;
	}

	public String getNumber() {
		return mEtEnterCode.getText().toString().trim();
	}

	public void setValidPhoneNumber(boolean mValidPhoneNumber) {
		this.mValidPhoneNumber = mValidPhoneNumber;
	}

	private void handlePhoneNumber(boolean hasFocus) {
		if (!hasFocus) {
			showPhoneNumberEtFocusDisable();
			mEtEnterCode.setFocusable(true);
		} else {
			showEtPhoneNumberFocusEnable();
		}
	}

	public void setErrDescription(String mErrDescription) {
		mTvErrDescriptionView.setText(mErrDescription);
	}

	public void showEtPhoneNumberFocusEnable() {
		mRlEtName.setBackgroundResource(R.drawable.reg_et_focus_enable);
	}

	public void showPhoneNumberEtFocusDisable() {
		mRlEtName.setBackgroundResource(R.drawable.reg_et_focus_disable);
	}

	public void showInvalidAlert() {
		mEtEnterCode.setTextColor(mContext.getResources().getColor(R.color.reg_error_box_color));
		mRlEtName.setBackgroundResource(R.drawable.reg_et_focus_error);
		mFlInvaliFielddAlert.setVisibility(VISIBLE);
	}

	public void showErrPopUp() {
		mTvErrDescriptionView.setVisibility(View.VISIBLE);
	}

	public void setClickableTrue(boolean isClickable) {
		if (mEtEnterCode != null) {
			mEtEnterCode.setClickable(isClickable);
			mEtEnterCode.setEnabled(isClickable);
		}
	}
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		mEtEnterCode.setTextColor(mContext.getResources().getColor(R.color.reg_edt_text_feild_color));
		if (v.getId() == R.id.et_reg_phone_number) {
			handlePhoneNumber(hasFocus);
			raiseUpdateUIEvent();
			if(!hasFocus){
				handleOnFocusChanges();}
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (validatePhoneNumber()) {
			showValidPhoneNumberAlert();
		} else {
			if (mEtEnterCode.getText().toString().trim().length() == 0) {
				setErrDescription(getResources().getString(R.string.EmptyField_ErrorMsg));
			}
		}
	}

	private void handleOnFocusChanges() {
		if (validatePhoneNumber()) {
			showValidPhoneNumberAlert();
		} else {
			if (mEtEnterCode.getText().toString().trim().length() == 0) {
				AppTagging.trackAction(AppTagingConstants.SEND_DATA,AppTagingConstants.USER_ALERT,AppTagingConstants.FIELD_CANNOT_EMPTY_NAME);
				setErrDescription(getResources().getString(R.string.EmptyField_ErrorMsg));
				showInvalidPhoneNumberAlert();
			}

		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		if (validatePhoneNumber()) {
			mTvErrDescriptionView.setVisibility(View.GONE);
			mFlInvaliFielddAlert.setVisibility(GONE);
		}
		raiseUpdateUIEvent();
	}
}
