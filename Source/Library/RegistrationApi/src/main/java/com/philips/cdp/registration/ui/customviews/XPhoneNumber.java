
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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.ui.utils.FontLoader;
import com.philips.cdp.registration.ui.utils.RegConstants;

public class XPhoneNumber extends RelativeLayout implements TextWatcher, OnFocusChangeListener,
        OnClickListener {

    private Context mContext;

    private EditText mEtPhoneNumber;

    private boolean mValidPhoneNumber;

    private onUpdateListener mUpdateStatusListener;

    private RelativeLayout mRlEtPhoneNumber;

    private TextView mTvErrDescriptionView;

    private FrameLayout mFlInvaliFielddAlert;

    private TextView mTvCloseIcon;

    public XPhoneNumber(Context context) {
        super(context);
        this.mContext = context;
        initUi(R.layout.x_phone_number);
    }

    public XPhoneNumber(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initUi(R.layout.x_phone_number);
    }

    public final void initUi(int resourceId) {

        /** inflate amount layout */
        LayoutInflater li = LayoutInflater.from(mContext);
        li.inflate(resourceId, this, true);

        mEtPhoneNumber = (EditText) findViewById(R.id.et_reg_phone_number);
        mEtPhoneNumber.setOnFocusChangeListener(this);
        mEtPhoneNumber.addTextChangedListener(this);
        mRlEtPhoneNumber = (RelativeLayout) findViewById(R.id.rl_reg_parent_verified_field);

        mTvErrDescriptionView = (TextView) findViewById(R.id.tv_reg_phone_number_err);
        mFlInvaliFielddAlert = (FrameLayout) findViewById(R.id.fl_reg_phone_field_err);
        mTvCloseIcon = (TextView) findViewById(R.id.iv_reg_close);
        FontLoader.getInstance().setTypeface(mTvCloseIcon, RegConstants.PUIICON_TTF);
    }

    @Override
    public void onClick(View v) {
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
        mEtPhoneNumber.setTextColor(mContext.getResources().getColor(R.color.reg_error_box_color));
        mRlEtPhoneNumber.setBackgroundResource(R.drawable.reg_et_focus_error);
        mFlInvaliFielddAlert.setVisibility(View.VISIBLE);
        mTvErrDescriptionView.setVisibility(VISIBLE);
    }

    private void showValidPhoneNumberAlert() {
        mFlInvaliFielddAlert.setVisibility(GONE);
        mTvErrDescriptionView.setVisibility(GONE);
    }

    private boolean validatePhoneNumber() {

        if (mEtPhoneNumber != null) {
            if (mEtPhoneNumber.getText().toString().length() >= 10) {
                setValidPhoneNumber(true);

                return true;
            } else {
                setValidPhoneNumber(false);
                return false;
            }
        }
        return false;
        /*if (!FieldsValidator.isValidName(mEtPhoneNumber.getText().toString().trim())) {
			setValidPhoneNumber(false);
			return false;
		}
		 mEtPhoneNumber.getText().toString().trim().length() >=12
		setValidPhoneNumber(true);
		return true;*/
    }

    public String getPhoneNumber() {
        return mEtPhoneNumber.getText().toString().trim();
    }

    public boolean isValidPhoneNumber() {
        return mValidPhoneNumber;
    }

    public void setValidPhoneNumber(boolean mValidPhoneNumber) {
        this.mValidPhoneNumber = mValidPhoneNumber;
    }

    private void handlePhoneNumber(boolean hasFocus) {
        if (!hasFocus) {
            showPhoneNumberEtFocusDisable();
            mEtPhoneNumber.setFocusable(true);
        } else {
            showEtPhoneNumberFocusEnable();
        }
    }

    public void setErrDescription(String mErrDescription) {
        mTvErrDescriptionView.setText(mErrDescription);
    }

    public void showEtPhoneNumberFocusEnable() {
        mRlEtPhoneNumber.setBackgroundResource(R.drawable.reg_et_focus_enable);
    }

    public void showPhoneNumberEtFocusDisable() {
        mRlEtPhoneNumber.setBackgroundResource(R.drawable.reg_et_focus_disable);
    }

    public void showInvalidAlert() {
        mEtPhoneNumber.setTextColor(mContext.getResources().getColor(R.color.reg_error_box_color));
        mRlEtPhoneNumber.setBackgroundResource(R.drawable.reg_et_focus_error);
        mFlInvaliFielddAlert.setVisibility(VISIBLE);
    }

    public void showErrPopUp() {
        mTvErrDescriptionView.setVisibility(View.VISIBLE);
    }

    public void setClickableTrue(boolean isClickable) {
        if (mEtPhoneNumber != null) {
            mEtPhoneNumber.setClickable(isClickable);
            mEtPhoneNumber.setEnabled(isClickable);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        mEtPhoneNumber.setTextColor(mContext.getResources().getColor(R.color.reg_edt_text_feild_color));
        if (v.getId() == R.id.et_reg_phone_number) {
            handlePhoneNumber(hasFocus);
            raiseUpdateUIEvent();
            if (!hasFocus) {
                handleOnFocusChanges();
            }
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
            if (mEtPhoneNumber.getText().toString().trim().length() == 0) {
                setErrDescription(getResources().getString(R.string.EmptyField_ErrorMsg));
            } else {
                setErrDescription(getResources().getString(R.string.Invalid_PhonenumberMsg));
            }
        }
    }

    private void handleOnFocusChanges() {
        if (validatePhoneNumber()) {
            showValidPhoneNumberAlert();
        } else {
            if (mEtPhoneNumber.getText().toString().trim().length() == 0) {
                setErrDescription(getResources().getString(R.string.EmptyField_ErrorMsg));
            } else {
                setErrDescription(getResources().getString(R.string.Invalid_PhonenumberMsg));
            }
            showInvalidPhoneNumberAlert();
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
