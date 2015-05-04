package com.philips.cl.di.reg.ui.customviews;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.philips.cl.di.reg.R;
import com.philips.cl.di.reg.ui.utils.EmailValidator;

public class XUserName extends RelativeLayout implements TextWatcher,OnFocusChangeListener{
	
	private Context mContext;
	private ImageView mIvNameErrAlert;
	private ImageView mIvValidNameAlert;
	private EditText mEtUserName;
	private boolean mValidName;
	private onUpdateListener mUpdateStatusListener;
	private RelativeLayout mRlEtName;
	
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
		
		mIvNameErrAlert = (ImageView) findViewById(R.id.iv_reg_name_error_alert);
		mIvValidNameAlert = (ImageView) findViewById(R.id.iv_reg_valid_name_alert);
		mRlEtName =  (RelativeLayout) findViewById(R.id.rl_reg_name_verified_field);
	}
	
	public void setOnUpdateListener(onUpdateListener updateStatusListener) {
		mUpdateStatusListener = updateStatusListener;
	}
	
	private void fireUpdateStatusEvent() {

		if (null != mUpdateStatusListener) {
			mUpdateStatusListener.onUpadte();
		}
	}
	
	private boolean validateName() {
		if (!EmailValidator.isValidName(mEtUserName.getText().toString().trim())) {
			setmValidName(false);
			return false;
		}
		setmValidName(true);
		return true;
	}
	
	public String getName() {
		return mEtUserName.getText().toString().trim();
	}

	public boolean ismValidName() {
		return mValidName;
	}

	public void setmValidName(boolean mValidName) {
		this.mValidName = mValidName;
	}

	    
	private void handleName(boolean hasFocus) {
		if (!hasFocus) {
			showNameEtFocusDisable();
			mEtUserName.setFocusable(true);
			if(mEtUserName.getText().toString().trim().length()==0){
				mIvNameErrAlert.setVisibility(View.VISIBLE);
			}
		}else{
			showEtNameFocusEnable();
		}
	}
	
	
	public void showEtNameFocusEnable(){
		mRlEtName.setBackgroundResource(R.drawable.reg_et_focus_enable);
	}
	
	public void showNameEtFocusDisable(){
		mRlEtName.setBackgroundResource(R.drawable.reg_et_focus_disable);
	}
	
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (v.getId() == R.id.et_reg_fname) {
			handleName(hasFocus);
			fireUpdateStatusEvent();
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,int after) {
		if (validateName()) {
			mIvValidNameAlert.setVisibility(View.VISIBLE);
		}
	}

	
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (validateName()) {
			mIvValidNameAlert.setVisibility(View.VISIBLE);
			mIvNameErrAlert.setVisibility(View.GONE);
		} else {
			mIvNameErrAlert.setVisibility(View.VISIBLE);
			mIvValidNameAlert.setVisibility(View.GONE);
		}
		
	}

	@Override
	public void afterTextChanged(Editable s) {
		fireUpdateStatusEvent();
	}

}
