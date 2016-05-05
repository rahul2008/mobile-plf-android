
package com.philips.cdp.registration.ui.customviews;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.apptagging.AppTagging;
import com.philips.cdp.registration.apptagging.AppTagingConstants;
import com.philips.cdp.registration.ui.utils.FieldsValidator;
import com.philips.cdp.registration.ui.utils.FontLoader;
import com.philips.cdp.registration.ui.utils.RegConstants;

public class XPassword extends RelativeLayout implements TextWatcher, OnClickListener,
        OnFocusChangeListener {

    private Context mContext;

    private TextView mIvPasswordErrAlert;

    private ImageView mIvArrowUpView;

    private TextView mTvErrDescriptionView;

    private EditText mEtPassword;

    private boolean mValidPassword;

    private onUpdateListener mUpdateStatusListener;

    private RelativeLayout mRlEtPassword;

    private TextView mTvMaskPassword;

    private boolean isValidatePassword = true;


    public XPassword(Context context) {
        super(context);
        this.mContext = context;
        initUi(R.layout.x_password);
    }

    public XPassword(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initUi(R.layout.x_password);
    }

    public final void initUi(int resourceId) {
        LayoutInflater li = LayoutInflater.from(mContext);
        li.inflate(resourceId, this, true);
        mIvPasswordErrAlert = (TextView) findViewById(R.id.iv_reg_password_error_alert);
        mIvPasswordErrAlert.setOnClickListener(this);
        mIvArrowUpView = (ImageView) findViewById(R.id.iv_reg_up_arrow);
        mTvErrDescriptionView = (TextView) findViewById(R.id.tv_reg_password_err);
        mEtPassword = (EditText) findViewById(R.id.et_reg_password);
        mEtPassword.setOnClickListener(this);
        mEtPassword.setOnFocusChangeListener(this);
        mEtPassword.addTextChangedListener(this);
        mRlEtPassword = (RelativeLayout) findViewById(R.id.rl_reg_parent_verified_field);
        mTvMaskPassword = (TextView) findViewById(R.id.tv_password_mask);
        FontLoader.getInstance().setTypeface(mTvMaskPassword, RegConstants.PUIICON_TTF);
        disableMaskPassoword();
    }


    public void setOnUpdateListener(onUpdateListener updateStatusListener) {
        mUpdateStatusListener = updateStatusListener;
    }

    private void fireUpdateStatusEvent() {
        if (null != mUpdateStatusListener) {
            mUpdateStatusListener.onUpadte();
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
        mTvErrDescriptionView.setText(mErrDescription);
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
        if (v.getId() == R.id.iv_reg_password_error_alert) {
            handleErrorUi();
        }

    }

    private void handleErrorUi() {
        if (mTvErrDescriptionView.isShown()) {
            hideErrPopUp();
        } else {
            showErrPopUp();
        }
    }

    private void showErrPopUp() {
        mTvErrDescriptionView.setVisibility(View.VISIBLE);
        mIvArrowUpView.setVisibility(View.VISIBLE);
    }

    private void hideErrPopUp() {
        mTvErrDescriptionView.setVisibility(View.GONE);
        mIvArrowUpView.setVisibility(View.GONE);
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
                setErrDescription(getResources().getString(R.string.EmptyField_ErrorMsg));
            } else {
                setErrDescription(getResources().getString(R.string.InValid_PwdErrorMsg));
            }
        }
    }

    private void handleValidPasswordWithoutPatternTextChange() {
        if (validatePasswordWithoutPattern()) {
        } else {
            if (mEtPassword.getText().toString().trim().length() == 0) {
                setErrDescription(getResources().getString(R.string.EmptyField_ErrorMsg));
            }
        }
    }


    private void handleValidPasswordWithPattern() {
        if (validatePassword()) {
            showValidPasswordAlert();
        } else {
            if (mEtPassword.getText().toString().trim().length() == 0) {
                AppTagging.trackAction(AppTagingConstants.SEND_DATA,AppTagingConstants.USER_ALERT,AppTagingConstants.FIELD_CANNOT_EMPTY_PASSWORD);
                setErrDescription(getResources().getString(R.string.EmptyField_ErrorMsg));
            } else {
                AppTagging.trackAction(AppTagingConstants.SEND_DATA,AppTagingConstants.USER_ALERT,AppTagingConstants.WRONG_PASSWORD);
                setErrDescription(getResources().getString(R.string.InValid_PwdErrorMsg));
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
                setErrDescription(getResources().getString(R.string.EmptyField_ErrorMsg));
            }
            showInvalidPasswordAlert();
        }
    }

    private void showInvalidPasswordAlert() {
        mIvPasswordErrAlert.setVisibility(VISIBLE);
        mIvArrowUpView.setVisibility(VISIBLE);
        mTvErrDescriptionView.setVisibility(VISIBLE);
    }

    private void showValidPasswordAlert() {
        mIvPasswordErrAlert.setVisibility(GONE);
        mIvArrowUpView.setVisibility(GONE);
        mTvErrDescriptionView.setVisibility(GONE);
    }

    @Override
    public void afterTextChanged(Editable s) {

        fireUpdateStatusEvent();
        if (isValidatePassword && validatePassword()) {
            mIvArrowUpView.setVisibility(View.GONE);
            mTvErrDescriptionView.setVisibility(View.GONE);
            mIvPasswordErrAlert.setVisibility(View.GONE);
        } else if (validatePasswordWithoutPattern() && !isValidatePassword) {
            mIvArrowUpView.setVisibility(View.GONE);
            mTvErrDescriptionView.setVisibility(View.GONE);
            mIvPasswordErrAlert.setVisibility(View.GONE);
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
        mTvMaskPassword.setTextColor(mContext.getResources().getColor(R.color.reg_password_mask_enable_ic_color));
        mTvMaskPassword.setOnClickListener(mMaskPasswordOnclickListener);
    }

    public void disableMaskPassoword() {
        mTvMaskPassword.setTextColor(mContext.getResources().getColor(R.color.reg_password_mask_disable_ic_color));
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
            AppTagging.trackAction(AppTagingConstants.SEND_DATA,AppTagingConstants.SHOW_PASSWORD,true);
            mEtPassword.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

            mEtPassword.setSelection(mEtPassword.getText().length());
        }else{
            AppTagging.trackAction(AppTagingConstants.SEND_DATA,AppTagingConstants.SHOW_PASSWORD,false);
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
