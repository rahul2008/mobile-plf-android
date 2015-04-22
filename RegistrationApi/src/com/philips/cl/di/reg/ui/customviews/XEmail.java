
package com.philips.cl.di.reg.ui.customviews;

import com.philips.cl.di.reg.R;
import com.philips.cl.di.reg.ui.utils.EmailValidator;

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

public class XEmail extends RelativeLayout implements TextWatcher, OnClickListener,
        OnFocusChangeListener {

	private Context mContext;

	private ImageView mIvEmailErrAlert;

	private ImageView mIvValidEmailAlert;

	private EditText mEtEmail;

	private TextView mTvErrDescriptionView;

	private boolean mValidEmail;

	private ImageView mIvArrowUpView;

	private onUpdateListener mUpdateStatusListener;

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

		mEtEmail = (EditText) findViewById(R.id.et_reg_email);
		mEtEmail.setOnClickListener(this);
		mEtEmail.setOnFocusChangeListener(this);
		mEtEmail.addTextChangedListener(this);
		mTvErrDescriptionView = (TextView) findViewById(R.id.tv_email_err);
		mIvEmailErrAlert = (ImageView) findViewById(R.id.iv_email_error_alert);
		mIvEmailErrAlert.setOnClickListener(this);
		mIvValidEmailAlert = (ImageView) findViewById(R.id.iv_valid_email_alert);
		mIvArrowUpView = (ImageView) findViewById(R.id.iv_up_arrow);
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
		if (!EmailValidator.isValidEmail(mEtEmail.getText().toString().trim())) {
			setValidEmail(false);
			return false;
		}
		setValidEmail(true);
		return true;
	}
	
	public void setErrDescription(String mErrDescription) {
		mTvErrDescriptionView.setText(mErrDescription);
	}

	private void handleEmail(boolean hasFocus) {
		if (!hasFocus) {
			mEtEmail.setFocusable(true);
			if (mEtEmail.getText().toString().trim().length() == 0) {
				showEmailInvalidAlert();
			}
		}
	}
	 
	public void showEmailInvalidAlert(){
		mIvEmailErrAlert.setVisibility(View.VISIBLE);
	}
	
	public void showInvalidEmailAlert() {
		mIvValidEmailAlert.setVisibility(View.GONE);
		mIvEmailErrAlert.setVisibility(View.VISIBLE);
	}

	public void setOnUpdateListener(onUpdateListener updateStatusListener) {
		mUpdateStatusListener = updateStatusListener;
	}

	private void fireUpdateStatusEvent() {

		if (null != mUpdateStatusListener) {
			mUpdateStatusListener.onUpadte();
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (v.getId() == R.id.et_reg_email) {
			handleEmail(hasFocus);
			fireUpdateStatusEvent();
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.iv_email_error_alert && mEtEmail.getText().toString().trim().length() >0) {
			mTvErrDescriptionView.setVisibility(View.VISIBLE);
			mIvArrowUpView.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (validateEmail()) {
			mIvValidEmailAlert.setVisibility(View.VISIBLE);
			mIvEmailErrAlert.setVisibility(View.GONE);
			mValidEmail = true;
		} else {
			if(mEtEmail.getText().toString().trim().length() == 0){
				mTvErrDescriptionView.setVisibility(View.GONE);
				mIvArrowUpView.setVisibility(View.GONE);
			}
			mTvErrDescriptionView.setText(getResources().getString(R.string.invalid_email));
			mIvEmailErrAlert.setVisibility(View.VISIBLE);
			mIvValidEmailAlert.setVisibility(View.GONE);
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		fireUpdateStatusEvent();
		if (validateEmail()) {
			mIvValidEmailAlert.setVisibility(View.VISIBLE);
			mTvErrDescriptionView.setVisibility(View.GONE);
			mIvArrowUpView.setVisibility(View.GONE);
			
		}
	}

}
