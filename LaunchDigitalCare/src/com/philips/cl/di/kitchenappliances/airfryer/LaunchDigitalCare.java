package com.philips.cl.di.kitchenappliances.airfryer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.philips.cl.di.digitalcare.DigitalCareActivity;
import com.philips.di.cl.launchdigitalcare.R;

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
				// ComponentName component = new ComponentName(
				// "com.philips.cl.di.digitalcare",
				// "com.philips.cl.di.digitalcare.DigitalCareActivity");
				// intent.setComponent(component);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});
	}
}
