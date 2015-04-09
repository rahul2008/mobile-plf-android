package com.philips.cl.di.sampledigitalcareapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.philips.cl.di.digitalcare.DigitalCareActivity;

public class LaunchDigitalCare extends Activity {

	private Button mLaunchDigitalCare = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_digital_care);
		mLaunchDigitalCare = (Button) findViewById(R.id.launchDigitalCare);
		mLaunchDigitalCare.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LaunchDigitalCare.this,
						DigitalCareActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});
	}
}
