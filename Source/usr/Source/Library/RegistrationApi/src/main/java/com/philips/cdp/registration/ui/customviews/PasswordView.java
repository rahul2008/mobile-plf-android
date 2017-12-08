
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
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
import com.philips.cdp.registration.ui.utils.FieldsValidator;
import com.philips.cdp.registration.ui.utils.FontLoader;
import com.philips.cdp.registration.ui.utils.RegConstants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PasswordView extends RelativeLayout implements TextWatcher, OnClickListener,
        OnFocusChangeListener {

    private Context mContext;

    @BindView(R2.id.tv_reg_password_err)
    TextView mTvErrDescriptionView;

    @BindView(R2.id.et_reg_password)
    EditText mEtPassword;

    @BindView(R2.id.rl_reg_parent_verified_field)
    RelativeLayout mRlEtPassword;

    @BindView(R2.id.tv_password_mask)
    TextView mTvMaskPassword;

    @BindView(R2.id.iv_reg_close)
    TextView mTvCloseIcon;

    @BindView(R2.id.fl_reg_password_field_err)
    FrameLayout mFlInvalidFieldAlert;

    private boolean mValidPassword;

    private OnUpdateListener mUpdateStatusListener;

    private boolean isValidatePassword = true;

    private String mSavedPasswordError;

    public PasswordView(Context context) {
        super(context);
        this.mContext = context;
        initUi(R.layout.reg_password);
    }

    public PasswordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initUi(R.layout.reg_password);
    }

    public final void initUi(int resourceId) {
        LayoutInflater li = LayoutInflater.from(mContext);
        View view = li.inflate(resourceId, this, true);
        ButterKnife.bind(this, view);
        mEtPassword.setOnClickListener(this);
        mEtPassword.setOnFocusChangeListener(this);
        mEtPassword.addTextChangedListener(this);
        FontLoader.getInstance().setTypeface(mTvMaskPassword, RegConstants.PUIICON_TTF);
        FontLoader.getInstance().setTypeface(mTvCloseIcon, RegConstants.PUIICON_TTF);
        disableMaskPassoword();

        mEtPassword.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });
}


    public void setOnUpdateListener(OnUpdateListener updateStatusListener) {
        mUpdateStatusListener = updateStatusListener;
    }

    private void fireUpdateStatusEvent() {
        if (null != mUpdateStatusListener) {
            mUpdateStatusListener.onUpdate();
        }
    }

    private void handlePassword(boolean hasFocus) {
        if (!hasFocus) {
            showPasswordEtFocusDisable();
            mEtPassword.setFocusable(true);
        } else {
            showEtPasswordFocusEnable();
        }
    }



    public void showEtPasswordFocusEnable() {
        mRlEtPassword.setBackgroundResource(R.drawable.reg_et_focus_enable);
    }

    public void showPasswordEtFocusDisable() {
        mRlEtPassword.setBackgroundResource(R.drawable.reg_et_focus_disable);
    }

    public void setErrDescription(String mErrDescription) {
        mSavedPasswordError = mErrDescription;
        mTvErrDescriptionView.setText(mErrDescription);
    }

    public String getmSavedPasswordErrDescription(){
        return mSavedPasswordError;
    }

    private boolean validatePassword() {
        if (!FieldsValidator.isValidPassword(mEtPassword.getText().toString().trim())) {
            setValidPassword(false);
            return false;
        }
        setValidPassword(true);
        return true;
    }

    private boolean validatePasswordWithoutPattern() {
        if (!FieldsValidator.isValidName(mEtPassword.getText().toString().trim())) {
            setValidPassword(false);
            return false;
        }
        setValidPassword(true);
        return true;
    }

    public String getPassword() {
        return mEtPassword.getText().toString().trim();
    }

    public boolean isValidPassword() {
        return mValidPassword;
    }

    public void setValidPassword(boolean mValidPassword) {
        this.mValidPassword = mValidPassword;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        mEtPassword.setTextColor( ContextCompat.getColor(mContext,R.color.reg_edit_text_field_color));
        if (v.getId() == R.id.et_reg_password) {
            handlePassword(hasFocus);
            fireUpdateStatusEvent();
           if (!hasFocus){
                handleOnFocusChanges();
            }
        }
    }

    @Override
    public void onClick(View v) {

    }

    public boolean isPasswordErrorVisible(){
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
        handleOnTextChanged();
    }

    private void handleOnFocusChanges() {
        if (isValidatePassword) {
            handleValidPasswordWithPattern();
        } else {
            handleValidPasswordWithoutPattern();
        }
    }


    private void handleOnTextChanged() {
        if (isValidatePassword) {
            handleValidPasswordWithPatternTextChanage();
        } else {
            handleValidPasswordWithoutPatternTextChange();
        }
    }

    private void handleValidPasswordWithPatternTextChanage() {
        if (validatePassword()) {
        } else {
            if (mEtPassword.getText().toString().trim().length() == 0) {
                setErrDescription(getResources().getString(R.string.reg_EmptyField_ErrorMsg));
            } else {
                setErrDescription(getResources().getString(R.string.reg_InValid_PwdErrorMsg));
            }
        }
    }

    private void handleValidPasswordWithoutPatternTextChange() {
        if (validatePasswordWithoutPattern()) {
        } else {
            if (mEtPassword.getText().toString().trim().length() == 0) {
                setErrDescription(getResources().getString(R.string.reg_EmptyField_ErrorMsg));
            }
        }
    }


    private void handleValidPasswordWithPattern() {
        if (validatePassword()) {
            showValidPasswordAlert();
        } else {
            if (mEtPassword.getText().toString().trim().length() == 0) {
                AppTagging.trackAction(AppTagingConstants.SEND_DATA,AppTagingConstants.USER_ALERT,AppTagingConstants.FIELD_CANNOT_EMPTY_PASSWORD);
                setErrDescription(getResources().getString(R.string.reg_EmptyField_ErrorMsg));
            } else {
                AppTagging.trackAction(AppTagingConstants.SEND_DATA,AppTagingConstants.USER_ALERT,AppTagingConstants.WRONG_PASSWORD);
                setErrDescription(getResources().getString(R.string.reg_InValid_PwdErrorMsg));
            }
            showInvalidPasswordAlert();
        }
    }

    private void handleValidPasswordWithoutPattern() {
        if (validatePasswordWithoutPattern()) {
            showValidPasswordAlert();
        } else {
            if (mEtPassword.getText().toString().trim().length() == 0) {
                AppTagging.trackAction(AppTagingConstants.SEND_DATA,AppTagingConstants.USER_ALERT,AppTagingConstants.FIELD_CANNOT_EMPTY_PASSWORD);
                setErrDescription(getResources().getString(R.string.reg_EmptyField_ErrorMsg));
            }
            showInvalidPasswordAlert();
        }
    }

    public void showInvalidPasswordAlert() {
        mEtPassword.setTextColor( ContextCompat.getColor(mContext,R.color.reg_error_box_color));
        mRlEtPassword.setBackgroundResource(R.drawable.reg_et_focus_error);
        mFlInvalidFieldAlert.setVisibility(VISIBLE);
        mTvErrDescriptionView.setVisibility(VISIBLE);
    }

    private void showValidPasswordAlert() {
        mRlEtPassword.setBackgroundResource(R.drawable.reg_et_focus_disable);
        mEtPassword.setTextColor( ContextCompat.getColor(mContext,R.color.reg_edit_text_field_color));
        mFlInvalidFieldAlert.setVisibility(GONE);
        mTvErrDescriptionView.setVisibility(GONE);
    }

    @Override
    public void afterTextChanged(Editable s) {

        fireUpdateStatusEvent();
        if (isValidatePassword && validatePassword()) {
            mFlInvalidFieldAlert.setVisibility(View.GONE);
            mTvErrDescriptionView.setVisibility(View.GONE);
        } else if (validatePasswordWithoutPattern() && !isValidatePassword) {
            mFlInvalidFieldAlert.setVisibility(View.GONE);
            mTvErrDescriptionView.setVisibility(View.GONE);
        }

        handleMaskPasswordUi();
    }

    private void handleMaskPasswordUi() {
        if (getPassword().length() >= 1) {
            enableMaskPassword();
        } else if(getPassword().length() == 0){
            disableMaskPassoword();
        }
    }

    public void isValidatePassword(boolean isValidatePassword) {
        this.isValidatePassword = isValidatePassword;
    }


    public void enableMaskPassword() {
        mTvMaskPassword.setTextColor( ContextCompat.getColor(mContext,R.color.reg_password_mask_enable_ic_color));
        mTvMaskPassword.setOnClickListener(mMaskPasswordOnclickListener);
    }

    public void disableMaskPassoword() {
        mTvMaskPassword.setTextColor( ContextCompat.getColor(mContext,R.color.reg_password_mask_disable_ic_color));
        mTvMaskPassword.setOnClickListener(null);
    }

    private OnClickListener mMaskPasswordOnclickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            togglePasswordMask();
        }
    };

    private void togglePasswordMask() {
        if(mEtPassword.getInputType()!=(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)){
            AppTagging.trackAction(AppTagingConstants.SEND_DATA,AppTagingConstants.SHOW_PASSWORD,"true");
            mEtPassword.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

            mEtPassword.setSelection(mEtPassword.getText().length());
        }else{
            AppTagging.trackAction(AppTagingConstants.SEND_DATA,AppTagingConstants.SHOW_PASSWORD,"false");
            mEtPassword.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_PASSWORD);
            mEtPassword.setSelection(mEtPassword.getText().length());
        }
    }

    public void setHint(String hintText){
        mEtPassword.setHint(hintText);
    }

    public void setClicableTrue(boolean isClickable){
        mEtPassword.setClickable(isClickable);
        mEtPassword.setEnabled(isClickable);
    }
}
