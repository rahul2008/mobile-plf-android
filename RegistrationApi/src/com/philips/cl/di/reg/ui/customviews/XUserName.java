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

import com.philips.cl.di.reg.R;
import com.philips.cl.di.reg.ui.utils.EmailValidater;

public class XUserName extends RelativeLayout implements TextWatcher,OnClickListener,OnFocusChangeListener{
	
	private Context mContext;
	private ImageView mIvNameErrAlert;
	private ImageView mIvValidNameAlert;
	private EditText mEtUserName;
	private boolean mValidName;
	
	
	public XUserName(Context context) {
		super(context);
		this.mContext = context;
		initUi(R.layout.name_field);
	}
	
	public XUserName(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		initUi(R.layout.name_field);
	}
	
	public final void initUi(int resourceId) {

		/** inflate amount layout */
		LayoutInflater li = LayoutInflater.from(mContext);
		li.inflate(resourceId, this, true);
		
		mEtUserName = (EditText) findViewById(R.id.et_reg_fname);
		mEtUserName.setOnClickListener(this);
		mEtUserName.setOnFocusChangeListener(this);
		mEtUserName.addTextChangedListener(this);
		
		mIvNameErrAlert = (ImageView) findViewById(R.id.iv_name_error_alert);
		mIvNameErrAlert.setOnClickListener(this);
		mIvValidNameAlert = (ImageView) findViewById(R.id.iv_valid_name_alert);

	}
	
	private boolean validateName() {
		if (!EmailValidater.isValidName(mEtUserName.getText().toString().trim())) {
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
			mEtUserName.setFocusable(true);
			if(mEtUserName.getText().toString().trim().length()==0){
				mIvNameErrAlert.setVisibility(View.VISIBLE);
			}
		}
	}
	
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (v.getId() == R.id.et_reg_fname) {
			handleName(hasFocus);
		}
	}

	@Override
	public void onClick(View v) {
		
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
	
		
	}

}
