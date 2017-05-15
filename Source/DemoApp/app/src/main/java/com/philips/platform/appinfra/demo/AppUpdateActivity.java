package com.philips.platform.appinfra.demo;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

public class AppUpdateActivity extends AppCompatActivity {

	private Button appUpdateRefresh;
	TextView appUpdateStatus;


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appupdate);
		appUpdateRefresh = (Button) findViewById(R.id.appUpdateRefresh);
		appUpdateStatus = (TextView) findViewById(R.id.appUpdateStatus);

	}
}
