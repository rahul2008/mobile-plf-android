
package com.philips.cl.di.regsample.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.philips.cl.di.reg.ui.traditional.RegistrationActivity;

public class RegistrationSampleActivity extends Activity implements OnClickListener {

	private Button mTestBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mTestBtn = (Button) findViewById(R.id.button1);
		mTestBtn.setOnClickListener(this);
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
}
