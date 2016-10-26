
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
import com.philips.cdp.registration.ui.utils.RegConstants;

public class XVerifyNumber extends RelativeLayout implements TextWatcher,
        OnFocusChangeListener {

    private Context mContext;

    private EditText mEtVerify;

    private Button mBtResend;

    private TextView mTvErrDescriptionView;

    private onUpdateListener mUpdateStatusListener;

    private RelativeLayout mRlEtEmail;

    private ProgressBar mProgressBar;

    private FrameLayout mFlInvalidFieldAlert;

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
        LayoutInflater li = LayoutInflater.from(mContext);
        li.inflate(resourceId, this, true);
        mRlEtEmail = (RelativeLayout) findViewById(R.id.rl_reg_parent_verified_field);
        mEtVerify = (EditText) findViewById(R.id.et_reg_verify);
        mBtResend = (Button) findViewById(R.id.btn_reg_resend);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_reg_verify_spinner);

        mEtVerify.setOnFocusChangeListener(this);
        mEtVerify.addTextChangedListener(this);
        mEtVerify.setFocusable(true);
        mTvErrDescriptionView = (TextView) findViewById(R.id.tv_reg_verify_err);
        mFlInvalidFieldAlert = (FrameLayout) findViewById(R.id.fl_reg_verify_field_err);
    }

    public void setCountertimer(String mTimer) {
        mBtResend.setText(mTimer);
        mBtResend.setEnabled(false);
    }

    public void setCounterFinish() {
        mBtResend.setText(mContext.getString(R.string.Account_ActivationCode_resendtxt));
        mBtResend.setEnabled(true);
    }

    public String getNumber() {
        return mEtVerify.getText().toString().trim();
    }

    private boolean validateEmail() {
        if (mEtVerify != null) {
            if (mEtVerify.getText().toString().length() >= RegConstants.VERIFY_CODE_ENTER) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public void setErrDescription(String mErrDescription) {
        mTvErrDescriptionView.setText(mErrDescription);
    }

    private void handleEmail(boolean hasFocus) {
        if (!hasFocus) {
            showEtEmailFocusDisable();
            mEtVerify.setFocusable(true);
        } else {
            showEtEmailFocusEnable();
        }
    }

    public void showEtEmailFocusEnable() {
        mRlEtEmail.setBackgroundResource(R.drawable.reg_et_focus_enable);
    }

    public void showEtEmailFocusDisable() {
        mRlEtEmail.setBackgroundResource(R.drawable.reg_et_focus_disable);
    }

    public void showEmailIsInvalidAlert() {
        mRlEtEmail.setBackgroundResource(R.drawable.reg_et_focus_error);
        mEtVerify.setTextColor(mContext.getResources().getColor(R.color.reg_error_box_color));
        mFlInvalidFieldAlert.setVisibility(VISIBLE);
        mTvErrDescriptionView.setVisibility(VISIBLE);
    }

    public void showValidEmailAlert() {
        mRlEtEmail.setBackgroundResource(R.drawable.reg_et_focus_disable);
        mEtVerify.setTextColor(mContext.getResources().getColor(R.color.reg_edt_text_feild_color));
        mFlInvalidFieldAlert.setVisibility(GONE);
        mTvErrDescriptionView.setVisibility(GONE);
    }

    public void setOnUpdateListener(onUpdateListener updateStatusListener) {
        mUpdateStatusListener = updateStatusListener;
    }

    private void raiseUpdateUIEvent() {
        if (null != mUpdateStatusListener) {
            mUpdateStatusListener.onUpadte();
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        mEtVerify.setTextColor(mContext.getResources().getColor(R.color.reg_edt_text_feild_color));
        if (v.getId() == R.id.et_reg_verify) {
            handleEmail(hasFocus);
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
        if (validateEmail()) {
            showValidEmailAlert();
        } else {
            if (mEtVerify.getText().toString().trim().length() == 0) {
                setErrDescription(getResources().getString(R.string.reg_EmptyField_ErrorMsg));
            } else {
                setErrDescription(getResources().getString(R.string.Account_ActivationCode_ErrorTxt));
            }
        }
    }

    private void handleOnFocusChanges() {

        if (validateEmail()) {
            showValidEmailAlert();
        } else {
            if (mEtVerify.getText().toString().trim().length() == 0) {
                setErrDescription(getResources().getString(R.string.reg_EmptyField_ErrorMsg));
            } else {
                setErrDescription(getResources().getString(R.string.Account_ActivationCode_ErrorTxt));
            }
            showEmailIsInvalidAlert();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        raiseUpdateUIEvent();
        if (validateEmail()) {
            if (mTvErrDescriptionView != null && mFlInvalidFieldAlert != null) {
                mTvErrDescriptionView.setVisibility(GONE);
                mFlInvalidFieldAlert.setVisibility(GONE);
            }
        }
    }

    public void setHint(String hintText) {
        if (mEtVerify != null) {
            mEtVerify.setHint(hintText);
        }
    }

    public boolean isShown() {
        if (mEtVerify != null && mEtVerify.isShown()) {
            return true;
        } else {
            return false;
        }
    }

    public void setImeOptions(int option) {
        mEtVerify.setImeOptions(option);
    }

    public void showResendSpinner(){
        mBtResend.setEnabled(false);
        mEtVerify.setEnabled(false);
        mProgressBar.setVisibility(VISIBLE);
    }

    public void hideResendSpinner(){
        mEtVerify.setEnabled(true);
        mProgressBar.setVisibility(GONE);
    }

    public void disableResendSpinner(){
        mBtResend.setEnabled(false);
        mEtVerify.setEnabled(false);
        mProgressBar.setVisibility(GONE);
    }

    public void setOnClickListener(View.OnClickListener resendBtnClickListener){
        mBtResend.setOnClickListener(resendBtnClickListener);
    }
}
