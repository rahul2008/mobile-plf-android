
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
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.R2;
import com.philips.cdp.registration.ui.utils.RegConstants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OtpEditTextWithResendButton extends RelativeLayout implements TextWatcher,
        OnFocusChangeListener {

    @BindView(R2.id.et_reg_verify)
    EditText mEtVerify;

    @BindView(R2.id.btn_reg_resend)
    Button mBtResend;

    @BindView(R2.id.tv_reg_verify_err)
    TextView mTvErrDescriptionView;

    @BindView(R2.id.rl_reg_parent_verified_field)
    RelativeLayout mRlEtEmail;

    @BindView(R2.id.pb_reg_verify_spinner)
    ProgressBar mProgressBar;

    @BindView(R2.id.fl_reg_verify_field_err)
    FrameLayout mFlInvalidFieldAlert;

    private Context mContext;

    private OnUpdateListener mUpdateStatusListener;

    private String mTimer;

    public OtpEditTextWithResendButton(Context context) {
        super(context);
        this.mContext = context;
        initUi(R.layout.x_verify_mobile);
    }

    public OtpEditTextWithResendButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initUi(R.layout.x_verify_mobile);
    }

    public final void initUi(int resourceId) {
        LayoutInflater li = LayoutInflater.from(mContext);
        View view = li.inflate(resourceId, this, true);
        ButterKnife.bind(this, view);
        mEtVerify.setOnFocusChangeListener(this);
        mEtVerify.addTextChangedListener(this);
        mEtVerify.setFocusable(true);
    }

    public void setCountertimer(String timer) {
        mTimer = timer;
        mBtResend.setText(timer);
        mBtResend.setEnabled(false);
    }

    public void enableResend() {
        if (mBtResend.getText().equals(mContext.getString(R.string.reg_Account_ActivationCode_resendtxt))){
            mBtResend.setEnabled(true);
        }
    }

    public void disableResend() {
        mBtResend.setEnabled(false);
    }


    public String getTimer(){
        return mTimer;
    }

    public void setCounterFinish() {
        mBtResend.setText(mContext.getString(R.string.reg_Account_ActivationCode_resendtxt));
        mBtResend.setEnabled(true);
    }

    public String getNumber() {
        return mEtVerify.getText().toString().trim();
    }

    private boolean validateEmail() {
        if (mEtVerify != null) {
            return mEtVerify.getText().toString().length() >= RegConstants.VERIFY_CODE_MINIMUM_LENGTH;
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
        mEtVerify.setTextColor(mContext.getResources().getColor(R.color.reg_edit_text_field_color));
        mFlInvalidFieldAlert.setVisibility(GONE);
        mTvErrDescriptionView.setVisibility(GONE);
    }

    public void setOnUpdateListener(OnUpdateListener updateStatusListener) {
        mUpdateStatusListener = updateStatusListener;
    }

    private void raiseUpdateUIEvent() {
        if (null != mUpdateStatusListener) {
            mUpdateStatusListener.onUpdate();
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        mEtVerify.setTextColor(mContext.getResources().getColor(R.color.reg_edit_text_field_color));
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
    //To do
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (validateEmail()) {
            showValidEmailAlert();
        } else {
            if (mEtVerify.getText().toString().trim().length() == 0) {
                setErrDescription(getResources().getString(R.string.reg_EmptyField_ErrorMsg));
            } else {
                setErrDescription(getResources().getString(R.string.reg_Account_ActivationCode_ErrorTxt));
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
                setErrDescription(getResources().getString(R.string.reg_Account_ActivationCode_ErrorTxt));
            }
            showEmailIsInvalidAlert();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        raiseUpdateUIEvent();
        if (validateEmail()) {
            if (mTvErrDescriptionView == null || mFlInvalidFieldAlert == null) {
                return;
            }
            mTvErrDescriptionView.setVisibility(GONE);
            mFlInvalidFieldAlert.setVisibility(GONE);
        }
    }

    public void setHint(String hintText) {
        if (mEtVerify != null) {
            mEtVerify.setHint(hintText);
        }
    }

    public boolean isShown() {
        return mEtVerify != null && mEtVerify.isShown();
    }

    public void setImeOptions(int option) {
        mEtVerify.setImeOptions(option);
    }

    public void showResendSpinnerAndDisableResendButton(){
        mBtResend.setEnabled(false);
        mEtVerify.setEnabled(false);
        mProgressBar.setVisibility(VISIBLE);
    }

    public void hideResendSpinnerAndEnableResendButton(){
        mProgressBar.setVisibility(GONE);
        mEtVerify.setEnabled(true);
        mBtResend.setEnabled(true);
    }

    public void disableResendSpinner(){
        mProgressBar.setVisibility(GONE);
    }

    public void setOnClickListener(View.OnClickListener resendBtnClickListener){
        mBtResend.setOnClickListener(resendBtnClickListener);
    }
}
