
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.apptagging.AppTagging;
import com.philips.cdp.registration.apptagging.AppTagingConstants;
import com.philips.cdp.registration.ui.utils.FieldsValidator;

public class XEmail extends RelativeLayout implements TextWatcher, OnClickListener,
        OnFocusChangeListener {

    private Context mContext;

    private TextView mIvEmailErrAlert;

    private EditText mEtEmail;

    private TextView mTvErrDescriptionView;

    private boolean mValidEmail;

    private ImageView mIvArrowUpView;

    private onUpdateListener mUpdateStatusListener;

    private RelativeLayout mRlEtEmail;

    private FrameLayout mFlInvalidAlert;

    public XEmail(Context context) {
        super(context);
        this.mContext = context;
        initUi(R.layout.x_email);
    }

    public XEmail(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initUi(R.layout.x_email);
    }

    public final void initUi(int resourceId) {
        LayoutInflater li = LayoutInflater.from(mContext);
        li.inflate(resourceId, this, true);
        mRlEtEmail = (RelativeLayout) findViewById(R.id.rl_reg_parent_verified_field);
        mEtEmail = (EditText) findViewById(R.id.et_reg_email);
        mEtEmail.setOnClickListener(this);
        mEtEmail.setOnFocusChangeListener(this);
        mEtEmail.addTextChangedListener(this);
        mTvErrDescriptionView = (TextView) findViewById(R.id.tv_reg_name_err);
        mIvEmailErrAlert = (TextView) findViewById(R.id.iv_reg_email_error_alert);
        mIvEmailErrAlert.setOnClickListener(this);
        mIvArrowUpView = (ImageView) findViewById(R.id.iv_reg_up_arrow);
        mFlInvalidAlert = (FrameLayout) findViewById(R.id.fl_reg_invalid_alert);

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
            if (!FieldsValidator.isValidEmail(mEtEmail.getText().toString().trim())) {
                setValidEmail(false);
                return false;
            }
            setValidEmail(true);
            return true;
        }
        return false;
    }

    public void setErrDescription(String mErrDescription) {
        mTvErrDescriptionView.setText(mErrDescription);
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

    public void showEmailInvalidAlert() {
        mIvEmailErrAlert.setVisibility(VISIBLE);
    }

    public void hideEmailInvalidAlert() {
        mIvEmailErrAlert.setVisibility(GONE);
    }

    private void showEmailIsInvalidAlert() {
        mIvEmailErrAlert.setVisibility(VISIBLE);
        mFlInvalidAlert.setVisibility(VISIBLE);
        mIvArrowUpView.setVisibility(VISIBLE);
        mTvErrDescriptionView.setVisibility(VISIBLE);
    }

    private void showValidEmailAlert() {
        mIvEmailErrAlert.setVisibility(GONE);
        mFlInvalidAlert.setVisibility(GONE);
        mIvArrowUpView.setVisibility(GONE);
        mTvErrDescriptionView.setVisibility(GONE);
    }

    public void showInvalidAlert() {
        mIvEmailErrAlert.setVisibility(View.VISIBLE);
        mFlInvalidAlert.setVisibility(VISIBLE);
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
        if (v.getId() == R.id.iv_reg_email_error_alert) {
            toggleErrorUi();
        }

    }

    private void toggleErrorUi() {
        if (mTvErrDescriptionView.isShown()) {
            hideErrPopUp();
        } else {
            showErrPopUp();
        }
    }

    public void showErrPopUp() {
        mTvErrDescriptionView.setVisibility(View.VISIBLE);
        mIvArrowUpView.setVisibility(View.VISIBLE);
    }

    public void hideErrPopUp() {
        mTvErrDescriptionView.setVisibility(View.GONE);
        mIvArrowUpView.setVisibility(View.GONE);
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
                setErrDescription(getResources().getString(R.string.EmptyField_ErrorMsg));
            } else {
                setErrDescription(getResources().getString(R.string.InvalidEmailAdddress_ErrorMsg));
            }
        }
    }

    private void handleOnFocusChanges() {
        if (validateEmail()) {
            showValidEmailAlert();
            mValidEmail = true;
        } else {
            if (mEtEmail.getText().toString().trim().length() == 0) {
                AppTagging.trackAction(AppTagingConstants.SEND_DATA, AppTagingConstants.USER_ALERT, AppTagingConstants.FIELD_CANNOT_EMPTY_EMAIL);
                setErrDescription(getResources().getString(R.string.EmptyField_ErrorMsg));
            } else {
                AppTagging.trackAction(AppTagingConstants.SEND_DATA, AppTagingConstants.USER_ALERT, AppTagingConstants.INVALID_EMAIL);
                setErrDescription(getResources().getString(R.string.InvalidEmailAdddress_ErrorMsg));
            }
            showEmailIsInvalidAlert();
        }
    }


    @Override
    public void afterTextChanged(Editable s) {
        raiseUpdateUIEvent();
        if (validateEmail()) {
            if (mTvErrDescriptionView != null & mIvArrowUpView != null && mIvEmailErrAlert != null && mFlInvalidAlert != null) {
                mTvErrDescriptionView.setVisibility(GONE);
                mIvArrowUpView.setVisibility(GONE);
                mIvEmailErrAlert.setVisibility(GONE);
                mFlInvalidAlert.setVisibility(GONE);
            }
        }
    }

    public void setHint(String hintText) {
        if (mEtEmail != null) {
            mEtEmail.setHint(hintText);
        }
    }

    public void clearEmailFieldData() {
        if (mEtEmail != null) {
            mEtEmail.getText().clear();
        }
    }

    public void setClicableTrue(boolean isClickable) {
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

    public void setImeOptions(int option) {
        mEtEmail.setImeOptions(option);
    }


}
