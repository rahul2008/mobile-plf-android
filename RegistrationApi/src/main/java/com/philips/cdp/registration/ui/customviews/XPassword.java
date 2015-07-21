
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.ui.utils.EmailValidator;

public class XPassword extends RelativeLayout implements TextWatcher, OnClickListener,
        OnFocusChangeListener {

	private Context mContext;

	private ImageView mIvPasswordErrAlert;

	private ImageView mIvValidPasswordAlert;

	private ImageView mIvArrowUpView;

	private TextView mTvErrDescriptionView;

	private EditText mEtPassword;

	private boolean mValidPassword;

	private onUpdateListener mUpdateStatusListener;

	private RelativeLayout mRlEtPassword;

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
		mIvValidPasswordAlert = (ImageView) findViewById(R.id.iv_reg_valid_password_alert);
		mIvArrowUpView = (ImageView) findViewById(R.id.iv_reg_up_arrow);
		mTvErrDescriptionView = (TextView) findViewById(R.id.tv_reg_password_err);
		mEtPassword = (EditText) findViewById(R.id.et_reg_password);
		mEtPassword.setOnClickListener(this);
		mEtPassword.setOnFocusChangeListener(this);
		mEtPassword.addTextChangedListener(this);
		mRlEtPassword = (RelativeLayout) findViewById(R.id.rl_reg_password_field_id);
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

	public void showInvalidAlert() {
		mIvValidPasswordAlert.setVisibility(View.GONE);
		mIvPasswordErrAlert.setVisibility(View.VISIBLE);
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

	public void showJanarainError() {
		mIvValidPasswordAlert.setVisibility(View.GONE);
		mIvPasswordErrAlert.setVisibility(View.VISIBLE);
	}

	private boolean validatePassword() {
		if (!EmailValidator.isValidPassword(mEtPassword.getText().toString().trim())) {
			setValidPassword(false);
			return false;
		}
		setValidPassword(true);
		return true;
	}

	private boolean validatePasswordWithoutPattern() {
		if (!EmailValidator.isValidName(mEtPassword.getText().toString().trim())) {
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
			mIvValidPasswordAlert.setVisibility(View.VISIBLE);
		} else {
			if (mEtPassword.getText().toString().trim().length() == 0) {
				setErrDescription(getResources().getString(R.string.EmptyField_ErrorMsg));
			} else {
				setErrDescription(getResources().getString(R.string.InValid_PwdErrorMsg));
			}
			mIvPasswordErrAlert.setVisibility(View.VISIBLE);
			mIvValidPasswordAlert.setVisibility(View.GONE);
		}
	}

	private void handleValidPasswordWithoutPattern() {
		if (validatePasswordWithoutPattern()) {
			mIvValidPasswordAlert.setVisibility(View.VISIBLE);
			mIvPasswordErrAlert.setVisibility(View.GONE);
		} else {
			if (mEtPassword.getText().toString().trim().length() == 0) {
				setErrDescription(getResources().getString(R.string.EmptyField_ErrorMsg));
			}
			mIvPasswordErrAlert.setVisibility(View.VISIBLE);
			mIvValidPasswordAlert.setVisibility(View.GONE);
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		fireUpdateStatusEvent();
		if (isValidatePassword && validatePassword()) {
			mIvValidPasswordAlert.setVisibility(View.VISIBLE);
			mIvArrowUpView.setVisibility(View.GONE);
			mTvErrDescriptionView.setVisibility(View.GONE);
		} else if (validatePasswordWithoutPattern() && !isValidatePassword) {
			mIvValidPasswordAlert.setVisibility(View.VISIBLE);
			mIvArrowUpView.setVisibility(View.GONE);
			mTvErrDescriptionView.setVisibility(View.GONE);
		}
	}

	public void isValidatePassword(boolean isValidatePassword) {
		this.isValidatePassword = isValidatePassword;
	}

	public void hideValidAlertError(){
		mIvValidPasswordAlert.setVisibility(View.GONE);
	}
}
