
package com.philips.cl.di.regsample.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.adobe.mobile.Config;
import com.philips.cl.di.reg.listener.UserRegistrationListener;
import com.philips.cl.di.reg.settings.RegistrationHelper;
import com.philips.cl.di.reg.ui.traditional.RegistrationActivity;
import com.philips.cl.di.reg.ui.utils.RLog;

public class RegistrationSampleActivity extends Activity implements OnClickListener,
        UserRegistrationListener {

	private Button mTestBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		RegistrationHelper.getInstance().registerUserRegistrationListener(this);
		mTestBtn = (Button) findViewById(R.id.button1);
		mTestBtn.setOnClickListener(this);

	}
	
	@Override
	protected void onStart() {
		RLog.d(RLog.ACTIVITY_LIFECYCLE, "RegistrationSampleActivity : onStart");
	    super.onStart();
	}
	
	@Override
	protected void onResume() {
		Config.collectLifecycleData();
		RLog.d(RLog.ACTIVITY_LIFECYCLE, "RegistrationSampleActivity : onResume");
	    super.onResume();
	}
	
	@Override
	protected void onPause() {
		Config.pauseCollectingLifecycleData();
		RLog.d(RLog.ACTIVITY_LIFECYCLE, "RegistrationSampleActivity : onPause");
	    super.onPause();
	}
	
	@Override
	protected void onStop() {
		RLog.d(RLog.ACTIVITY_LIFECYCLE, "RegistrationSampleActivity : onStop");
	    super.onStop();
	}

	@Override
	protected void onDestroy() {
		RegistrationHelper.getInstance().unRegisterUserRegistrationListener(this);
		RLog.d(RLog.EVENT_LISTENERS, "RegistrationSampleActivity unregister : RegisterUserRegistrationListener");
		super.onDestroy();
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.button1:
				startActivity(new Intent(this, RegistrationActivity.class));
				break;

			default:
				break;
		}

	}

	@Override
	public void onUserRegistrationComplete() {
		Intent intent = new Intent(this, RegistrationSampleActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
}
