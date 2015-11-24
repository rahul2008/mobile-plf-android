
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
import com.philips.cdp.registration.ui.utils.FieldsValidator;

public class XPassword extends RelativeLayout implements TextWatcher, OnClickListener,
        OnFocusChangeListener {

    private Context mContext;

    private ImageView mIvPasswordErrAlert;

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
        mIvPasswordErrAlert = (ImageView) findViewById(R.id.iv_reg_password_error_alert);
        mIvPasswordErrAlert.setOnClickListener(this);
        mIvArrowUpView = (ImageView) findViewById(R.id.iv_reg_up_arrow);
        mTvErrDescriptionView = (TextView) findViewById(R.id.tv_reg_password_err);
        mEtPassword = (EditText) findViewById(R.id.et_reg_password);
        mEtPassword.setOnClickListener(this);
        mEtPassword.setOnFocusChangeListener(this);
        mEtPassword.addTextChangedListener(this);
        mRlEtPassword = (RelativeLayout) findViewById(R.id.rl_reg_parent_verified_field);
        mTvMaskPassword = (TextView) findViewById(R.id.tv_password_mask);
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
            if (mEtPassword.getText().toString().trim().length() == 0) {
                mIvPasswordErrAlert.setVisibility(View.VISIBLE);
            }
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
        if (isValidatePassword) {
            handleValidPasswordWithPattern();
        } else {
            handleValidPasswordWithoutPattern();
        }
    }

    private void handleValidPasswordWithPattern() {
        if (validatePassword()) {
            mIvPasswordErrAlert.setVisibility(View.GONE);
           // RegUtility.invalidalertvisibilitygone(mEtPassword);
        } else {

            if (mEtPassword.getText().toString().trim().length() == 0) {
                setErrDescription(getResources().getString(R.string.EmptyField_ErrorMsg));
            } else {
                setErrDescription(getResources().getString(R.string.InValid_PwdErrorMsg));
            }
            mIvPasswordErrAlert.setVisibility(View.VISIBLE);
           // RegUtility.invalidalertvisibilityview(mEtPassword);
        }
    }

    private void handleValidPasswordWithoutPattern() {
        if (validatePasswordWithoutPattern()) {
            mIvPasswordErrAlert.setVisibility(View.GONE);
        } else {
            if (mEtPassword.getText().toString().trim().length() == 0) {
                setErrDescription(getResources().getString(R.string.EmptyField_ErrorMsg));
            }
            mIvPasswordErrAlert.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

        fireUpdateStatusEvent();
        if (isValidatePassword && validatePassword()) {
         //   RegUtility.invalidalertvisibilitygone(mEtPassword);
            mIvArrowUpView.setVisibility(View.GONE);
            mTvErrDescriptionView.setVisibility(View.GONE);
        } else if (validatePasswordWithoutPattern() && !isValidatePassword) {
         //   RegUtility.invalidalertvisibilityview(mEtPassword);
            mIvArrowUpView.setVisibility(View.GONE);
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


    private void enableMaskPassword() {
        mTvMaskPassword.setTextColor(mContext.getResources().getColor(R.color.reg_password_mask_enable_ic_color));
        mTvMaskPassword.setOnClickListener(mMaskPasswordOnclickListener);
    }

    private void disableMaskPassoword() {
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
            mEtPassword.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

            mEtPassword.setSelection(mEtPassword.getText().length());

        }else{
            mEtPassword.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_PASSWORD);

            mEtPassword.setSelection(mEtPassword.getText().length());
        }
    }

    public void setHint(String hintText){
        mEtPassword.setHint(hintText);
    }

}
