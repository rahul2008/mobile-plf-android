package com.philips.cdp.sampledigitalcareapp;

import android.app.Activity;
import android.os.Bundle;

import com.philips.cl.di.sampledigitalcareapp.R;

public class DummyScreen extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dummy_view);
	}
}
