
package com.philips.cl.di.regsample.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.philips.cl.di.reg.listener.UserRegistrationListener;
import com.philips.cl.di.reg.settings.RegistrationHelper;
import com.philips.cl.di.reg.ui.traditional.RegistrationActivity;

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
	protected void onDestroy() {
		RegistrationHelper.getInstance().unRegisterUserRegistrationListener(this);
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
		System.out.println("**************************** FIRE USER EVENT ");
		Intent intent = new Intent(this, RegistrationSampleActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);

	}
}
