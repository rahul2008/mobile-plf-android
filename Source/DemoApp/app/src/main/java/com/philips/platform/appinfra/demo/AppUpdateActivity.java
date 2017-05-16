package com.philips.platform.appinfra.demo;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.platform.appinfra.appupdate.AppupdateInterface;

public class AppUpdateActivity extends AppCompatActivity {

	private Button appUpdateRefresh;
	private TextView appUpdateStatus;
	private AppupdateInterface appupdateInterface;


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appupdate);
		appUpdateRefresh = (Button) findViewById(R.id.appUpdateRefresh);
		appUpdateStatus = (TextView) findViewById(R.id.appUpdateStatus);

		appupdateInterface = AppInfraApplication.gAppInfra.getAppupdate();

		appUpdateRefresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				appupdateInterface.refresh(new AppupdateInterface.OnRefreshListener() {
					@Override
					public void onError(AIAppUpdateRefreshResult error, String message) {
						Toast.makeText(AppUpdateActivity.this ,error.toString() ,Toast.LENGTH_LONG).show();
					}

					@Override
					public void onSuccess(AIAppUpdateRefreshResult result) {
						Toast.makeText(AppUpdateActivity.this ,result.toString() ,Toast.LENGTH_LONG).show();
					}
				});
			}
		});

	}
}
