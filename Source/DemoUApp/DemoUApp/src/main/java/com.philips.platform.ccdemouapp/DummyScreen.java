package com.philips.platform.ccdemouapp;

import android.app.Activity;
import android.os.Bundle;

import com.philips.platform.ccdemouapplibrary.R;

public class DummyScreen extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dummy_view);
	}
}
