package com.philips.cl.di.dev.pa.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constant.AppConstants;

public class PrivacyPolicyActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int activity=getIntent().getIntExtra(AppConstants.ACTIVITY, 0);
		setContentView(R.layout.privacy_policy);
		
		this.getSupportActionBar().hide();
		
		TextView policyText= (TextView) findViewById(R.id.tv_privacy_policy_full);
		if(activity== AppConstants.PRIVACY_POLICY_SCREEN){
			policyText.setText(R.string.privacy_policy_text);
		}else if(activity== AppConstants.EULA_SCREEN){
			policyText.setText(R.string.eula_text);
		}else if(activity== AppConstants.TERMS_AND_CONDITIONS_SCREEN){
			policyText.setText(R.string.terms_and_conditions_text);
		}
		
		ImageButton closePrivacyPolicy= (ImageButton) findViewById(R.id.close_privacy_policy);
		closePrivacyPolicy.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}
}
