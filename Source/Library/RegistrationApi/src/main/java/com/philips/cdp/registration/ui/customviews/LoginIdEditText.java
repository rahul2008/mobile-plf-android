/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.customviews;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputType;
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
import com.philips.cdp.registration.R2;
import com.philips.cdp.registration.app.tagging.AppTagging;
import com.philips.cdp.registration.app.tagging.AppTagingConstants;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.FieldsValidator;
import com.philips.cdp.registration.ui.utils.FontLoader;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginIdEditText extends RelativeLayout implements TextWatcher, OnClickListener,
        OnFocusChangeListener {

    private Context mContext;

    @BindView(R2.id.et_reg_email)
    EditText mEtEmail;

    @BindView(R2.id.tv_reg_email_err)
    TextView mTvErrDescriptionView;

    @BindView(R2.id.rl_reg_parent_verified_field)
    RelativeLayout mRlEtEmail;

    @BindView(R2.id.iv_reg_close)
    TextView mTvCloseIcon;

    @BindView(R2.id.fl_reg_email_field_err)
    FrameLayout mFlInvalidFieldAlert;

    private boolean mValidEmail;

    private OnUpdateListener mUpdateStatusListener;

    private String mSavedEmailError;

    public LoginIdEditText(Context context) {
        super(context);
        this.mContext = context;
        initUi(R.layout.reg_email);
    }

    public LoginIdEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initUi(R.layout.reg_email);
        checkingEmailorMobile();
    }

    public final void initUi(int resourceId) {
        RLog.d(RLog.SERVICE_DISCOVERY,"China Flow : "+ RegistrationHelper.getInstance().isMobileFlow());
        LayoutInflater li = LayoutInflater.from(mContext);
        View view = li.inflate(resourceId, this, true);
        ButterKnife.bind(this);
        mEtEmail.setOnClickListener(this);
        mEtEmail.setOnFocusChangeListener(this);
        mEtEmail.addTextChangedListener(this);
        FontLoader.getInstance().setTypeface(mTvCloseIcon, RegConstants.PUIICON_TTF);
    }

    private void checkingEmailorMobile() {
        if (RegistrationHelper.getInstance().isMobileFlow()) {
            mEtEmail.setHint(getResources().getString(R.string.reg_CreateAccount_PhoneNumber));
            mEtEmail.setInputType(InputType.TYPE_CLASS_NUMBER);
        }else {
            mEtEmail.setHint(getResources().getString(R.string.reg_EmailAddPlaceHolder_txtField));
        }
    }

    public void checkingEmailorMobileSignIn() {
        if (RegistrationHelper.getInstance().isMobileFlow()) {
            mEtEmail.setHint(getResources().getString(R.string.reg_CreateAccount_Email_PhoneNumber));
            mEtEmail.setInputType(InputType.TYPE_CLASS_TEXT);
        }else {
            mEtEmail.setHint(getResources().getString(R.string.reg_EmailAddPlaceHolder_txtField));
        }
    }

    public String getEmailId() {
        return mEtEmail.getText().toString().trim();
    }

    public boolean isValidEmail() {
        return mValidEmail;
    }

    public void setValidEmail(boolean mValidEmail) {
        this.mValidEmail = mValidEmail;
    }

    private boolean validateEmail() {
        if (mEtEmail != null) {
            //need to change by service discover
            if (RegistrationHelper.getInstance().isMobileFlow()) {
                if (isEmailandMobile()) return true;
            } else {
                if (isEmail()) return true;
            }
            setValidEmail(false);
            return false;
        }
        return false;
    }

    private boolean isEmail() {
        if (FieldsValidator.isValidEmail(mEtEmail.getText().toString().trim())) {
            setValidEmail(true);
            return true;
        }
        return false;
    }

    private boolean isEmailandMobile() {
        if (FieldsValidator.isValidEmail(mEtEmail.getText().toString().trim())){
            setValidEmail(true);
            return true;
        }else if(FieldsValidator.isValidMobileNumber(mEtEmail.getText().toString().trim())){
            setValidEmail(true);
            return true;
        }
        return false;
    }

    public void setErrDescription(String mErrDescription) {
        mTvErrDescriptionView.setText(mErrDescription);
        mSavedEmailError = mErrDescription;
    }

    public String getSavedEmailErrDescription(){
        return mSavedEmailError;
    }

    private void handleEmail(boolean hasFocus) {
        if (!hasFocus) {
            showEtEmailFocusDisable();
            mEtEmail.setFocusable(true);
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

    private void showEmailIsInvalidAlert() {
        mRlEtEmail.setBackgroundResource(R.drawable.reg_et_focus_error);
        mEtEmail.setTextColor(ContextCompat.getColor(mContext,R.color.reg_error_box_color));
        mFlInvalidFieldAlert.setVisibility(VISIBLE);
        mTvErrDescriptionView.setVisibility(VISIBLE);
    }

    public void showValidEmailAlert() {
        mRlEtEmail.setBackgroundResource(R.drawable.reg_et_focus_disable);
        mEtEmail.setTextColor(ContextCompat.getColor(mContext,R.color.reg_edit_text_field_color));
        mFlInvalidFieldAlert.setVisibility(GONE);
        mTvErrDescriptionView.setVisibility(GONE);
    }

    public void showInvalidAlert() {
        mEtEmail.setTextColor( ContextCompat.getColor(mContext,R.color.reg_error_box_color));
        mRlEtEmail.setBackgroundResource(R.drawable.reg_et_focus_error);
        mFlInvalidFieldAlert.setVisibility(VISIBLE);
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
        mEtEmail.setTextColor(ContextCompat.getColor(mContext,R.color.reg_edit_text_field_color));
        if (v.getId() == R.id.et_reg_email) {
            handleEmail(hasFocus);
            raiseUpdateUIEvent();
            if (!hasFocus) {
                handleOnFocusChanges();
            }
        }
    }

    @Override
    public void onClick(View v) {
    }

    public void showErrPopUp() {
        mTvErrDescriptionView.setVisibility(View.VISIBLE);
    }

    public boolean isEmailErrorVisible(){
        if(mTvErrDescriptionView.getVisibility() == View.VISIBLE){
            return true;
        }
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (validateEmail()) {
            showValidEmailAlert();
        } else {
            if (mEtEmail.getText().toString().trim().length() == 0) {
                setErrDescription(getResources().getString(R.string.reg_EmptyField_ErrorMsg));
            } else {
                if (RegistrationHelper.getInstance().isMobileFlow()) {
                    setErrDescription(getResources().getString(R.string.reg_Invalid_PhoneNumber_ErrorMsg));
                }else {
                    setErrDescription(getResources().getString(R.string.reg_InvalidEmailAdddress_ErrorMsg));
                }
            }
        }
    }

    private void handleOnFocusChanges() {

        if (validateEmail()) {
            showValidEmailAlert();
            mValidEmail = true;
        } else {
            if (mEtEmail.getText().toString().trim().length() == 0) {
                AppTagging.trackAction(AppTagingConstants.SEND_DATA,
                        AppTagingConstants.USER_ALERT,
                        AppTagingConstants.FIELD_CANNOT_EMPTY_EMAIL);
                setErrDescription(getResources().getString(R.string.reg_EmptyField_ErrorMsg));
            } else {
                if (RegistrationHelper.getInstance().isMobileFlow()){
                    AppTagging.trackAction(AppTagingConstants.SEND_DATA,
                            AppTagingConstants.USER_ALERT, AppTagingConstants.INVALID_MOBILE);
                    setErrDescription(getResources().getString(R.string.reg_Invalid_PhoneNumber_ErrorMsg));
                }else {

                    AppTagging.trackAction(AppTagingConstants.SEND_DATA,
                            AppTagingConstants.USER_ALERT, AppTagingConstants.INVALID_EMAIL);
                    setErrDescription(getResources().getString(R.string.reg_InvalidEmailAdddress_ErrorMsg));
                }


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
        if (mEtEmail != null) {
            mEtEmail.setHint(hintText);
        }
    }
    public void setInputType(int inputType){
        mEtEmail.setInputType(inputType);
    }

    public void setClickableTrue(boolean isClickable) {
        if (mEtEmail != null) {
            mEtEmail.setClickable(isClickable);
            mEtEmail.setEnabled(isClickable);
        }
    }

    public boolean isShown() {
        if (mEtEmail != null && mEtEmail.isShown()) {
            return true;
        } else {
            return false;
        }
    }

    public EditText getLoginIdEditText() {
        return mEtEmail;
    }

    public void setImeOptions(int option) {
        mEtEmail.setImeOptions(option);
    }


}
