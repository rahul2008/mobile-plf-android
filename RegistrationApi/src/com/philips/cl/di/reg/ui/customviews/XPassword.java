package com.philips.cl.di.reg.ui.customviews;

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

import com.philips.cl.di.reg.R;
import com.philips.cl.di.reg.ui.utils.EmailValidater;

public class XPassword extends RelativeLayout implements TextWatcher,OnClickListener,OnFocusChangeListener {
	
	private Context mContext;
	private ImageView mIvPasswordErrAlert;
	private ImageView mIvValidPasswordAlert;
	private ImageView mIvArrowUpView;
	private TextView mTvErrDescriptionView;
	private EditText mEtPassword;
	private boolean mValidPassword;
	private onUpdateListener mUpdateStatusListener;
	
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
		
		mIvPasswordErrAlert = (ImageView) findViewById(R.id.iv_password_error_alert);
		mIvPasswordErrAlert.setOnClickListener(this);
		mIvValidPasswordAlert = (ImageView) findViewById(R.id.iv_valid_password_alert);
		mIvArrowUpView = (ImageView) findViewById(R.id.iv_up_arrow);
		mTvErrDescriptionView = (TextView) findViewById(R.id.tv_password_err);
		mEtPassword = (EditText) findViewById(R.id.et_reg_password);
		mEtPassword.setOnClickListener(this);
		mEtPassword.setOnFocusChangeListener(this);
		mEtPassword.addTextChangedListener(this);
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
			mEtPassword.setFocusable(true);
			if(mEtPassword.getText().toString().trim().length()==0){
				mIvPasswordErrAlert.setVisibility(View.VISIBLE);
			}
		}
	}
	
	public void setErrDescription(String mErrDescription) {
		mTvErrDescriptionView.setText(mErrDescription);
	}
	
	public void showJanarainError(){
		mIvValidPasswordAlert.setVisibility(View.GONE);
		mIvPasswordErrAlert.setVisibility(View.VISIBLE);
	}
	
	private boolean validatePassword() {
		if (!EmailValidater.isValidPassword(mEtPassword.getText().toString().trim())) {
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
		if (v.getId() == R.id.iv_password_error_alert && mEtPassword.getText().toString().trim().length() > 0) {
			mTvErrDescriptionView.setVisibility(View.VISIBLE);
			mIvArrowUpView.setVisibility(View.VISIBLE);
		}	
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,int after) {
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (validatePassword()) {
			mIvPasswordErrAlert.setVisibility(View.GONE);
			mIvValidPasswordAlert.setVisibility(View.VISIBLE);
		} else {
			if(mEtPassword.getText().toString().trim().length() == 0){
				mTvErrDescriptionView.setVisibility(View.GONE);
				mIvArrowUpView.setVisibility(View.GONE);
			}
			mIvPasswordErrAlert.setVisibility(View.VISIBLE);
			mIvValidPasswordAlert.setVisibility(View.GONE);
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		fireUpdateStatusEvent();
		if (validatePassword()) {
			mIvValidPasswordAlert.setVisibility(View.VISIBLE);
			mIvArrowUpView.setVisibility(View.GONE);
			mTvErrDescriptionView.setVisibility(View.GONE);
			
		}
	}

}
