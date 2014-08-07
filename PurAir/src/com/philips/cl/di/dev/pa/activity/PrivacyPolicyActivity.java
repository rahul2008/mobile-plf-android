package com.philips.cl.di.dev.pa.activity;

import com.philips.cl.di.dev.pa.R;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class PrivacyPolicyActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.privacy_policy);
		
		this.getSupportActionBar().hide();
		ImageButton closePrivacyPolicy= (ImageButton) findViewById(R.id.close_privacy_policy);
		closePrivacyPolicy.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}
}
