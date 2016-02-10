
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
import com.philips.cdp.registration.ui.utils.FieldsValidator;

public class XUserName extends RelativeLayout implements TextWatcher, OnFocusChangeListener,
        OnClickListener {

	private Context mContext;

	private TextView mIvErrAlert;

	private EditText mEtUserName;

	private boolean mValidName;

	private onUpdateListener mUpdateStatusListener;

	private RelativeLayout mRlEtName;

	private TextView mTvErrDescriptionView;

	private ImageView mIvArrowUpView;

	private FrameLayout mFlInvalidAlert;

	public XUserName(Context context) {
		super(context);
		this.mContext = context;
		initUi(R.layout.x_user_name);
	}

	public XUserName(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		initUi(R.layout.x_user_name);
	}

	public final void initUi(int resourceId) {

		/** inflate amount layout */
		LayoutInflater li = LayoutInflater.from(mContext);
		li.inflate(resourceId, this, true);

		mEtUserName = (EditText) findViewById(R.id.et_reg_fname);
		mEtUserName.setOnFocusChangeListener(this);
		mEtUserName.addTextChangedListener(this);

		mIvErrAlert = (TextView) findViewById(R.id.iv_reg_name_error_alert);
		mIvErrAlert.setOnClickListener(this);
		mRlEtName = (RelativeLayout) findViewById(R.id.rl_reg_parent_verified_field);

		mTvErrDescriptionView = (TextView) findViewById(R.id.tv_reg_name_err);
		mIvArrowUpView = (ImageView) findViewById(R.id.iv_reg_up_arrow);

		mFlInvalidAlert = (FrameLayout)findViewById(R.id.fl_reg_invalid_alert);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.iv_reg_name_error_alert) {
			handleErrorUi();
		}
	}

	private void handleErrorUi() {
		if (mTvErrDescriptionView.isShown()) {
			hideErrorPopUp();
		} else {
			showErrorPopUp();
		}
	}

	private void showErrorPopUp() {
		mTvErrDescriptionView.setVisibility(View.VISIBLE);
		mIvArrowUpView.setVisibility(View.VISIBLE);
	}

	private void hideErrorPopUp() {
		mTvErrDescriptionView.setVisibility(View.GONE);
		mIvArrowUpView.setVisibility(View.GONE);
	}

	public void setOnUpdateListener(onUpdateListener updateStatusListener) {
		mUpdateStatusListener = updateStatusListener;
	}

	private void raiseUpdateUIEvent() {
		if (null != mUpdateStatusListener) {
			mUpdateStatusListener.onUpadte();
		}
	}

	private void showInvalidUserNameAlert() {
		mIvErrAlert.setVisibility(VISIBLE);
		mFlInvalidAlert.setVisibility(View.VISIBLE);
		mIvArrowUpView.setVisibility(VISIBLE);
		mTvErrDescriptionView.setVisibility(VISIBLE);
	}

	private void showValidUserNameAlert() {
		mFlInvalidAlert.setVisibility(GONE);
		mIvErrAlert.setVisibility(GONE);
		mIvArrowUpView.setVisibility(GONE);
		mTvErrDescriptionView.setVisibility(GONE);
	}

	private boolean validateName() {
		if (!FieldsValidator.isValidName(mEtUserName.getText().toString().trim())) {
			setValidName(false);
			return false;
		}
		setValidName(true);
		return true;
	}

	public String getName() {
		return mEtUserName.getText().toString().trim();
	}

	public boolean isValidName() {
		return mValidName;
	}

	public void setValidName(boolean mValidName) {
		this.mValidName = mValidName;
	}

	private void handleName(boolean hasFocus) {
		if (!hasFocus) {
			showNameEtFocusDisable();
			mEtUserName.setFocusable(true);
		} else {
			showEtNameFocusEnable();
		}
	}

	public void setErrDescription(String mErrDescription) {
		mTvErrDescriptionView.setText(mErrDescription);
	}

	public void showEtNameFocusEnable() {
		mRlEtName.setBackgroundResource(R.drawable.reg_et_focus_enable);
	}

	public void showNameEtFocusDisable() {
		mRlEtName.setBackgroundResource(R.drawable.reg_et_focus_disable);
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (v.getId() == R.id.et_reg_fname) {
			handleName(hasFocus);
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
		if (validateName()) {
			showValidUserNameAlert();
		} else {
			if (mEtUserName.getText().toString().trim().length() == 0) {
				setErrDescription(getResources().getString(R.string.EmptyField_ErrorMsg));
			}
		}
	}

	private void handleOnFocusChanges() {
		if (validateName()) {
			showValidUserNameAlert();
		} else {
			if (mEtUserName.getText().toString().trim().length() == 0) {
				setErrDescription(getResources().getString(R.string.EmptyField_ErrorMsg));
				showInvalidUserNameAlert();
			}

		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		if (validateName()) {
			mTvErrDescriptionView.setVisibility(View.GONE);
			mIvArrowUpView.setVisibility(View.GONE);
			mFlInvalidAlert.setVisibility(GONE);
		}
		raiseUpdateUIEvent();
	}
}
