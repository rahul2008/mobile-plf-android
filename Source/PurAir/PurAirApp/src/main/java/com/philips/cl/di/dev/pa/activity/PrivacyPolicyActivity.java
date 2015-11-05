package com.philips.cl.di.dev.pa.activity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.util.TrackPageConstants;
 
public class PrivacyPolicyActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int activity=getIntent().getIntExtra(AppConstants.ACTIVITY, 0);
		setContentView(R.layout.privacy_policy);
		
		TextView heading_name_tv=(TextView) findViewById(R.id.heading_name_tv);
	
		TextView policyText= (TextView) findViewById(R.id.tv_privacy_policy_full);
		
		heading_name_tv.setVisibility(View.VISIBLE);
		
		if(activity== AppConstants.PRIVACY_POLICY_SCREEN){
			
			policyText.setText(Html.fromHtml(getString(R.string.privacy_policy_text)));
			heading_name_tv.setText(this.getResources().getString(R.string.privacy_policy));
			
		}else if(activity== AppConstants.EULA_SCREEN){
			policyText.setText(Html.fromHtml(getString(R.string.eula_text)));
			heading_name_tv.setText(this.getResources().getString(R.string.eula));
		}
		
		else if(activity== AppConstants.TERMS_AND_CONDITIONS_SCREEN){
			policyText.setText(Html.fromHtml(getString(R.string.terms_and_conditions_text)));
			heading_name_tv.setText(this.getResources().getString(R.string.terms_and_conditions));
		}
		
		ImageButton backButton= (ImageButton) findViewById(R.id.heading_back_imgbtn);
		backButton.setVisibility(View.VISIBLE);
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MetricsTracker.trackPage(TrackPageConstants.PRIVACY_POLICY);
	}
	
}
