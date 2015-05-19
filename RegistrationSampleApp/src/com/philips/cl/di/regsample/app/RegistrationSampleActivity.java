
package com.philips.cl.di.regsample.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.philips.cl.di.reg.events.EventHelper;
import com.philips.cl.di.reg.events.EventListener;
import com.philips.cl.di.reg.ui.traditional.RegistrationActivity;
import com.philips.cl.di.reg.ui.utils.RegConstants;

public class RegistrationSampleActivity extends Activity implements OnClickListener, EventListener {

	private Button mTestBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		EventHelper.getInstance().registerEventNotification(RegConstants.USER_LOGIN_SUCCESS, this);
		mTestBtn = (Button) findViewById(R.id.button1);
		mTestBtn.setOnClickListener(this);
	}

	/*
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		EventHelper.getInstance()
		        .unregisterEventNotification(RegConstants.USER_LOGIN_SUCCESS, this);
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
	public void onEventReceived(String event) {
		if (RegConstants.USER_LOGIN_SUCCESS.equals(event)) {
			Log.i("User logged in ", "User logged in");
		}
	}

}
